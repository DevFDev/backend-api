package com.backend.devfordev.service.TeamService;

import com.backend.devfordev.apiPayload.code.status.ErrorStatus;
import com.backend.devfordev.apiPayload.exception.handler.CommunityHandler;
import com.backend.devfordev.apiPayload.exception.handler.MemberHandler;
import com.backend.devfordev.apiPayload.exception.handler.TeamHandler;
import com.backend.devfordev.converter.TeamConverter;
import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.MemberEntity.MemberInfo;
import com.backend.devfordev.domain.TeamEntity.*;
import com.backend.devfordev.domain.enums.TeamType;
import com.backend.devfordev.dto.CommunityDto.CommunityResponse;
import com.backend.devfordev.dto.TeamDto.TeamRequest;
import com.backend.devfordev.dto.TeamDto.TeamResponse;
import com.backend.devfordev.repository.*;
import com.backend.devfordev.repository.MemberRepository.MemberInfoRepository;
import com.backend.devfordev.repository.MemberRepository.MemberRepository;
import com.backend.devfordev.repository.TeamRepository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final TeamTagRepository teamTagRepository;
    private final LikeRepository likeRepository;
    private final TeamTechStackRepository teamTechStackRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final TeamTagMapRepository teamTagMapRepository;
    @Override
    @Transactional
    public TeamResponse.TeamCreateResponse createTeam(TeamRequest.TeamCreateRequest request, Long userId) {
        // 작성자 확인
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.INVALID_MEMBER));


        TeamType teamType;
        try {
            teamType = request.getTeamType();
        } catch (IllegalArgumentException e) {
            throw new TeamHandler(ErrorStatus.INVALID_TEAM);
        }

        if (request.getTeamTechStack().size() > 5) {
            throw new TeamHandler(ErrorStatus.INVALID_TECH_STACK_COUNT);
        }
        if (request.getTeamTags().size() > 5) {
            throw new TeamHandler(ErrorStatus.INVALID_TAG_COUNT);
        }


        List<TeamTag> tags = request.getTeamTags().stream()
                .map(tagName -> teamTagRepository.findByName(tagName)
                        .orElseGet(() -> teamTagRepository.save(TeamTag.builder().name(tagName).build())))
                .collect(Collectors.toList());

        Team team = TeamConverter.toTeam(request, member, tags, teamType);

        // 작성자를 팀 멤버로 추가
        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .member(member)
                .build();
        teamMemberRepository.save(teamMember);

        teamRepository.save(team);

        return TeamConverter.toTeamCreateResponse(team);

    }

    @Transactional
    @Override
    public void closeRecruitment(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));

        if (!team.getMember().getId().equals(userId)) {
            throw new MemberHandler(ErrorStatus.UNAUTHORIZED_USER);
        }

        team.setTeamIsActive(false);  // 모집 상태를 마감으로 설정
        teamRepository.save(team);
    }


    @Override
    @Transactional
    public List<TeamResponse.TeamListResponse> getTeamList(Optional<String> searchTermOpt, Optional<TeamType> teamTypeOpt, List<String> positions, List<String> techStacks, String sortBy, Optional<Boolean> teamIsActive) {

        List<Object[]> results = teamRepository.findAllWithLikesAndMember();

        List<TeamResponse.TeamListResponse> teamList = results.stream()
                .map(result -> {
                    Team team = (Team) result[0];
                    Long likeCount = (Long) result[1];


                    // 모집 여부 필터링
                    if (teamIsActive.isPresent()) {
                        boolean isRecruiting = teamIsActive.get();
                        if ((isRecruiting && !team.getTeamIsActive()) ||
                                (!isRecruiting && team.getTeamIsActive())) {
                            return null;
                        }
                    }

                    if (teamTypeOpt.isPresent() && !team.getTeamType().equals(teamTypeOpt.get())) {
                        return null;
                    }


                    // `teamPosition` filter
                    if (!positions.isEmpty() && positions.stream().noneMatch(pos -> team.getTeamPosition().contains(pos))) {
                        return null;
                    }

                    // `teamTechStack` filter
                    List<String> teamStackNames = team.getTeamTechStacks().stream()
                            .map(TeamTechStack::getName)
                            .collect(Collectors.toList());
                    if (!techStacks.isEmpty() && techStacks.stream().noneMatch(teamStackNames::contains)) {
                        return null;
                    }

                    // Search term filtering
                    if (searchTermOpt.isPresent()) {
                        String searchTerm = searchTermOpt.get().toLowerCase();
                        boolean matches = team.getTeamTitle().toLowerCase().contains(searchTerm) ||
                                team.getMember().getName().toLowerCase().contains(searchTerm) ||
                                team.getTeamContent().toLowerCase().contains(searchTerm);
                        if (!matches) {
                            return null;
                        }
                    }
                    MemberInfo memberInfoEntity = memberInfoRepository.findByMember(team.getMember());


                    // Construct MemberInfo
                    CommunityResponse.MemberInfo memberInfo = new CommunityResponse.MemberInfo(
                            team.getMember().getId(),
                            memberInfoEntity.getImageUrl(),
                            memberInfoEntity.getNickname()
                    );

                    // Shortened content
                    String shortenedContent = team.getTeamContent();
                    if (shortenedContent.length() > 80) {
                        shortenedContent = shortenedContent.substring(0, 80) + "...";
                    }

                    return TeamConverter.toTeamListResponse(team, memberInfo, likeCount, shortenedContent);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Sorting
        switch (sortBy.toLowerCase()) {
            case "likes":
                teamList.sort(Comparator.comparingLong(TeamResponse.TeamListResponse::getLikes).reversed());
                break;
            case "views":
                teamList.sort(Comparator.comparingLong(TeamResponse.TeamListResponse::getViews).reversed());
                break;
            default:
                teamList.sort(Comparator.comparing(TeamResponse.TeamListResponse::getCreatedAt).reversed());
                break;
        }

        return teamList;
    }


    @Override
    @Transactional
    public TeamResponse.TeamDetailResponse getTeamDetail(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new CommunityHandler(ErrorStatus.TEAM_NOT_FOUND));

        if (team.getDeletedAt() != null) {
            throw new CommunityHandler(ErrorStatus.TEAM_DELETED);
        }

        Long Likecount = likeRepository.countByTeamId(id);
        MemberInfo memberInfoEntity = memberInfoRepository.findByMember(team.getMember());

        // Construct MemberInfo
        CommunityResponse.MemberInfo memberInfo = new CommunityResponse.MemberInfo(
                team.getMember().getId(),
                memberInfoEntity.getImageUrl(),
                memberInfoEntity.getNickname()
        );

        return TeamConverter.toTeamDetailResponse(team, memberInfo, Likecount);
    }

    @Override
    @Transactional
    public void deleteTeam(Long id, Long userId) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));

        if (team.getDeletedAt() != null) {
            throw new TeamHandler(ErrorStatus.TEAM_DELETED);
        }


        if (!team.getMember().getId().equals(userId)) {
            throw new CommunityHandler(ErrorStatus.UNAUTHORIZED_USER);
        }

        team.deleteSoftly();

        teamRepository.save(team);
    }


    @Override
    @Transactional
    public List<CommunityResponse.MemberInfo> searchMembersByNickname(String nickname, Long userId, Long teamId) {
        // 팀 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));

        // 작성자인지 확인
        if (!team.getMember().getId().equals(userId)) {
            throw new TeamHandler(ErrorStatus.UNAUTHORIZED_USER); // 권한 없음 예외 발생
        }

        List<MemberInfo> memberInfos;

        // nickname이 null 또는 빈 문자열인 경우, 전체 리스트 조회
        if (nickname == null || nickname.isBlank()) {
            memberInfos = memberInfoRepository.findAll(); // 모든 멤버 정보 조회
        } else {
            // nickname이 있는 경우, 검색 조건으로 조회
            memberInfos = memberInfoRepository.findByNicknameContainingIgnoreCase(nickname);
        }

        return memberInfos.stream()
                .map(memberInfo -> new CommunityResponse.MemberInfo(
                        memberInfo.getMember().getId(),
                        memberInfo.getImageUrl(),
                        memberInfo.getNickname()
                ))
                .collect(Collectors.toList());
    }



    @Override
    @Transactional
    public TeamResponse.TeamAddMemberResponse AddTeamMember(TeamRequest.TeamAddMemberRequest request, Long userId, Long teamId) {
        // 팀 모집 공고 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));

        // 작성자인지 확인
        if (!team.getMember().getId().equals(userId)) {
            throw new TeamHandler(ErrorStatus.UNAUTHORIZED_USER); // 권한 없음 예외 발생
        }

        Member memberToInvite = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 작성자가 스스로를 추가하려는 경우 예외 처리
        if (team.getMember().getId().equals(request.getMemberId())) {
            throw new TeamHandler(ErrorStatus.CANNOT_ADD_OWNER_AS_MEMBER); // 작성자 추가 불가 예외 발생
        }

        // 이미 팀에 추가된 멤버인지 확인
        boolean isAlreadyMember = teamMemberRepository.existsByTeamAndMember(team, memberToInvite);
        if (isAlreadyMember) {
            throw new TeamHandler(ErrorStatus.ALREADY_TEAM_MEMBER); // 이미 팀에 추가된 멤버 예외 발생
        }

        // 모집 인원을 초과하지 않았는지 확인
        long currentMemberCount = teamMemberRepository.countByTeam(team);
        int maxRecruitmentNum = Integer.parseInt(team.getTeamRecruitmentNum());

        if (currentMemberCount >= maxRecruitmentNum+1) {
            throw new TeamHandler(ErrorStatus.TEAM_RECRUITMENT_FULL); // 모집 인원 초과 예외 발생
        }

        // 팀에 멤버 추가
        TeamMember teamMember = TeamConverter.toTeamMember(team, memberToInvite);
        teamMemberRepository.save(teamMember);

        return TeamConverter.toTeamMemberResponse(teamMember, team, memberToInvite);
    }

    public TeamResponse.TeamMemberListWithIdResponse getTeamMemberList(Long teamId) {
        // 팀 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));

        // 팀에 속한 모든 팀 멤버 조회
        List<TeamMember> teamMembers = teamMemberRepository.findByTeamId(teamId);

        // 각 팀원의 MemberInfo 및 TeamMemberListResponse 생성
        List<TeamResponse.TeamMemberListResponse> memberResponses = teamMembers.stream()
                .map(teamMember -> {
                    Member member = memberRepository.findById(teamMember.getMember().getId())
                            .orElseThrow(() -> new TeamHandler(ErrorStatus.MEMBER_NOT_FOUND));

                    MemberInfo memberInfoEntity = memberInfoRepository.findByMember(member);
                    CommunityResponse.MemberInfo memberInfo = new CommunityResponse.MemberInfo(
                            member.getId(),
                            memberInfoEntity.getImageUrl(),
                            memberInfoEntity.getNickname()
                    );

                    return TeamResponse.TeamMemberListResponse.builder()
                            //.id(teamMember.getId())
                            .member(memberInfo)
                            .build();
                })
                .collect(Collectors.toList());

        // 컨버터 호출 (팀 ID와 멤버 리스트 전달)
        return TeamConverter.toTeamMemberListResponse(team.getId(), memberResponses);
    }


    @Override
    @Transactional
    public void deleteTeamMember(Long teamId, Long memberId, Long userId) {
        // 팀 조회 및 작성자인지 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));

        if (!team.getMember().getId().equals(userId)) {
            throw new TeamHandler(ErrorStatus.UNAUTHORIZED_USER); // 작성자가 아닌 경우 예외 발생
        }

        // 삭제할 팀 멤버 조회
        TeamMember teamMember = teamMemberRepository.findByTeamIdAndMemberId(teamId, memberId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 팀 멤버 삭제
        teamMemberRepository.delete(teamMember);
    }


    @Transactional
    public TeamResponse.TeamUpdateResponse updateTeam(Long teamId, TeamRequest.TeamUpdateRequest request, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));


        if (!team.getMember().getId().equals(userId)) {
            throw new CommunityHandler(ErrorStatus.UNAUTHORIZED_USER);
        }

        // 태그 처리
        List<TeamTag> tags = getTeamTagsByNamesOrCreate(request.getTeamTags());
        List<TeamTagMap> tagMaps = tags.stream()
                .map(tag -> TeamTagMap.builder()
                        .team(team)
                        .tag(tag)
                        .build())
                .toList();

        // 기술 스택 처리
        List<TeamTechStack> techStacks = request.getTeamTechStack().stream()
                .map(stack -> TeamTechStack.builder()
                        .name(stack)
                        .team(team)
                        .build())
                .toList();

        // 기존 컬렉션 초기화 후 새 값 설정
        team.getTeamTagMaps().clear();
        team.getTeamTagMaps().addAll(tagMaps);

        team.getTeamTechStacks().clear();
        team.getTeamTechStacks().addAll(techStacks);

        // 팀 정보 업데이트
        team.setTeamTitle(request.getTeamTitle());
        team.setTeamContent(request.getTeamContent());
        team.setTeamPosition(request.getTeamPosition());
        team.setTeamRecruitmentNum(String.valueOf(request.getTeamRecruitmentNum()));

        // 변경된 팀 저장
        return TeamConverter.toTeamUpdateResponse(team);
    }

    public List<TeamTag> getTeamTagsByNames(List<String> tagNames) {
        List<TeamTag> tags = teamTagRepository.findAllByNameIn(tagNames);

        // 태그가 누락된 경우 예외 처리
        if (tags.size() != tagNames.size()) {
            throw new IllegalArgumentException("Some tags are missing or invalid: " + tagNames);
        }
        return tags;
    }

    @Transactional
    public List<TeamTag> getTeamTagsByNamesOrCreate(List<String> tagNames) {
        // 1. 데이터베이스에서 기존 태그를 조회
        List<TeamTag> existingTags = teamTagRepository.findAllByNameIn(tagNames);

        // 2. 요청된 태그 중 없는 태그를 찾음
        List<String> missingTags = tagNames.stream()
                .filter(name -> existingTags.stream().noneMatch(tag -> tag.getName().equals(name)))
                .toList();

        // 3. 누락된 태그를 생성
        List<TeamTag> newTags = missingTags.stream()
                .map(name -> TeamTag.builder().name(name).build())
                .toList();

        // 4. 새 태그를 데이터베이스에 저장
        if (!newTags.isEmpty()) {
            teamTagRepository.saveAll(newTags);
        }

        // 5. 기존 태그와 새 태그를 합쳐 반환
        existingTags.addAll(newTags);
        return existingTags;
    }

}