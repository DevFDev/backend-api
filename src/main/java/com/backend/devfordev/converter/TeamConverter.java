package com.backend.devfordev.converter;

import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.TeamEntity.*;
import com.backend.devfordev.domain.enums.TeamType;
import com.backend.devfordev.dto.CommunityDto.CommunityResponse;
import com.backend.devfordev.dto.TeamDto.TeamRequest;
import com.backend.devfordev.dto.TeamDto.TeamResponse;

import java.util.List;
import java.util.stream.Collectors;


public class TeamConverter {
    public static Team toTeam(TeamRequest.TeamCreateRequest request, Member member, List<TeamTag> tags, TeamType teamType) {
        // 기본 팀 정보 설정
        Team team = Team.builder()
                .teamTitle(request.getTeamTitle())
                .teamContent(request.getTeamContent())
                .teamType(teamType)
                .teamPosition(request.getTeamPosition())
                .teamRecruitmentNum(String.valueOf(request.getTeamRecruitmentNum()))
                .teamIsActive(true) // 기본 값 설정
                .teamViews(0L) // 기본 값 설정
                .member(member)
                .build();

        // 기술 스택 변환
        List<TeamTechStack> techStacks = request.getTeamTechStack().stream()
                .map(stack -> TeamTechStack.builder()
                        .name(stack)
                        .team(team)
                        .build())
                .collect(Collectors.toList());

        team.setTeamTechStacks(techStacks); // 기술 스택 설정

        // 태그 매핑 설정
        List<TeamTagMap> tagMaps = tags.stream()
                .map(tag -> TeamTagMap.builder()
                        .team(team)
                        .tag(tag) // 매핑된 태그 엔티티
                        .build())
                .collect(Collectors.toList());

        team.setTeamTagMaps(tagMaps); // 태그 매핑 설정

        return team;
    }
    public static Team toUpdateTeam(TeamRequest.TeamUpdateRequest request,  List<TeamTag> tags, TeamType teamType) {
        // 기본 팀 정보 설정
        Team team = Team.builder()
                .teamTitle(request.getTeamTitle())
                .teamContent(request.getTeamContent())
                .teamType(teamType)
                .teamPosition(request.getTeamPosition())
                .teamRecruitmentNum(String.valueOf(request.getTeamRecruitmentNum()))
                .teamIsActive(true) // 기본 값 설정
                .build();

        // 기술 스택 변환
        List<TeamTechStack> techStacks = request.getTeamTechStack().stream()
                .map(stack -> TeamTechStack.builder()
                        .name(stack)
                        .team(team)
                        .build())
                .collect(Collectors.toList());

        team.setTeamTechStacks(techStacks); // 기술 스택 설정

        // 태그 매핑 설정
        List<TeamTagMap> tagMaps = tags.stream()
                .map(tag -> TeamTagMap.builder()
                        .team(team)
                        .tag(tag) // 매핑된 태그 엔티티
                        .build())
                .collect(Collectors.toList());

        team.setTeamTagMaps(tagMaps); // 태그 매핑 설정

        return team;
    }

    public static TeamResponse.TeamCreateResponse toTeamCreateResponse(Team team) {
        // 기술 스택 리스트와 태그 리스트 변환
        List<String> techStackNames = team.getTeamTechStacks().stream()
                .map(TeamTechStack::getName)
                .collect(Collectors.toList());

        List<String> tagNames = team.getTeamTagMaps().stream()
                .map(teamTagMap -> teamTagMap.getTag().getName())
                .collect(Collectors.toList());

        return TeamResponse.TeamCreateResponse.builder()
                .id(team.getId())
                .teamTitle(team.getTeamTitle())
                .teamContent(team.getTeamContent())
                .teamType(team.getTeamType())
                .teamPosition(team.getTeamPosition())
                .teamRecruitmentNum(Long.valueOf(team.getTeamRecruitmentNum()))
                .teamTechStack(techStackNames)
                .teamTags(tagNames)
                .createdAt(team.getCreatedAt())
                .build();
    }

    public static TeamResponse.TeamListResponse toTeamListResponse(
        Team team, CommunityResponse.MemberInfo member, Long likeCount, String shortenedContent) {

        List<String> techStackNames = team.getTeamTechStacks().stream()
                .map(TeamTechStack::getName)
                .collect(Collectors.toList());

        List<String> tagNames = team.getTeamTagMaps().stream()
                .map(teamTagMap -> teamTagMap.getTag().getName())
                .collect(Collectors.toList());

        return new TeamResponse.TeamListResponse(
                team.getId(),
                member,
                team.getTeamTitle(),
                shortenedContent,
                team.getTeamType(),
                team.getTeamPosition(),
                Long.valueOf(team.getTeamRecruitmentNum()),
                techStackNames,
                tagNames,
                team.getCreatedAt(),
                team.getTeamIsActive(),
                team.getTeamViews(),
                0L,
                likeCount

        );
    }

    public static TeamResponse.TeamDetailResponse toTeamDetailResponse(
            Team team, CommunityResponse.MemberInfo member, Long likeCount) {

        // 기술 스택과 태그를 문자열 리스트로 변환
        List<String> techStackNames = team.getTeamTechStacks().stream()
                .map(TeamTechStack::getName)
                .collect(Collectors.toList());

        List<String> tagNames = team.getTeamTagMaps().stream()
                .map(teamTagMap -> teamTagMap.getTag().getName())
                .collect(Collectors.toList());

        // TeamDetailResponse 객체 반환
        return new TeamResponse.TeamDetailResponse(
                team.getId(),
                member,
                team.getTeamTitle(),
                team.getTeamContent(),
                team.getTeamType(),
                team.getTeamPosition(),
                Long.valueOf(team.getTeamRecruitmentNum()),
                techStackNames,
                tagNames,
                team.getCreatedAt(),
                team.getTeamIsActive(),
                team.getTeamViews(),
                0L, // 필요한 경우 업데이트
                likeCount

        );
    }


    public static TeamResponse.TeamUpdateResponse toTeamUpdateResponse(
            Team team) {

        // 기술 스택과 태그를 문자열 리스트로 변환
        List<String> techStackNames = team.getTeamTechStacks().stream()
                .map(TeamTechStack::getName)
                .collect(Collectors.toList());

        List<String> tagNames = team.getTeamTagMaps().stream()
                .map(teamTagMap -> teamTagMap.getTag().getName())
                .collect(Collectors.toList());

        // TeamDetailResponse 객체 반환
        return new TeamResponse.TeamUpdateResponse(
                team.getId(),
                team.getTeamTitle(),
                team.getTeamContent(),
                team.getTeamType(),
                team.getTeamPosition(),
                Long.valueOf(team.getTeamRecruitmentNum()),
                techStackNames,
                tagNames,
                team.getCreatedAt()
        );
    }

    public static TeamMember toTeamMember(Team team, Member member) {
        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .member(member)
                .build();
        return teamMember;
    }

    public static TeamResponse.TeamAddMemberResponse toTeamMemberResponse(TeamMember teamMember, Team team, Member member) {
        return new TeamResponse.TeamAddMemberResponse(
                teamMember.getId(),
                team.getId(),
                member.getId()
        );
    }


    public static TeamResponse.TeamMemberListWithIdResponse toTeamMemberListResponse(Long teamId, List<TeamResponse.TeamMemberListResponse> memberResponses) {
        return new TeamResponse.TeamMemberListWithIdResponse(teamId, memberResponses);
    }

}
