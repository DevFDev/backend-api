package com.backend.devfordev.service.ProjectService;

import com.backend.devfordev.apiPayload.code.status.ErrorStatus;
import com.backend.devfordev.apiPayload.exception.handler.CommunityHandler;
import com.backend.devfordev.apiPayload.exception.handler.MemberHandler;
import com.backend.devfordev.apiPayload.exception.handler.ProjectHandler;
import com.backend.devfordev.apiPayload.exception.handler.TeamHandler;
import com.backend.devfordev.converter.ProjectConverter;
import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.MemberEntity.MemberInfo;
import com.backend.devfordev.domain.ProjectEntity.Project;
import com.backend.devfordev.domain.ProjectEntity.ProjectLink;
import com.backend.devfordev.domain.TeamEntity.Team;
import com.backend.devfordev.dto.CommunityDto.CommunityResponse;
import com.backend.devfordev.dto.ProjectDto.ProjectRequest;
import com.backend.devfordev.dto.ProjectDto.ProjectResponse;
import com.backend.devfordev.repository.LikeRepository;
import com.backend.devfordev.repository.MemberRepository.MemberInfoRepository;
import com.backend.devfordev.repository.MemberRepository.MemberRepository;
import com.backend.devfordev.repository.ProjectRepository.ProjectLinkRepository;
import com.backend.devfordev.repository.ProjectRepository.ProjectRepository;
import com.backend.devfordev.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService{
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectLinkRepository projectLinkRepository;
    private final S3Service s3Service;
    private final LikeRepository likeRepository;
    private final MemberInfoRepository memberInfoRepository;

    @Override
    @Transactional
    public ProjectResponse.ProjectCreateResponse createProject(ProjectRequest.ProjectCreateRequest request, Long userId, MultipartFile portImage) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.INVALID_MEMBER));

        String imageUrl;
        try {
            // 이미지 파일이 비어 있는지 확인
            if (portImage == null || portImage.isEmpty()) {
                // 기본 이미지 URL을 설정
                imageUrl = s3Service.saveDefaultProfileImage();
            } else {
                // 이미지 업로드 후 URL 반환
                imageUrl = s3Service.saveProfileImage(portImage);
            }
        } catch (IOException e) {
            throw new MemberHandler(ErrorStatus.IMAGE_UPLOAD_FAILED);
        }

        // 포트폴리오 생성
        Project project = ProjectConverter.toProject(request, member, imageUrl);
        projectRepository.save(project);

        // 링크 리스트 순서 자동 설정 후 변환 및 저장
        List<ProjectLink> links = ProjectConverter.toProjectCreateLinks(request.getLinks(), project);
        for (int i = 0; i < links.size(); i++) {
            links.get(i).setOrderIndex(i + 1); // 자동 순서 설정
        }
        projectLinkRepository.saveAll(links);

        // 포트폴리오 응답 변환
        return ProjectConverter.toProjectResponse(project, links);
    }

    @Override
    @Transactional
    public ProjectResponse.ProjectDetailResponse getProjectDetail(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectHandler(ErrorStatus.PROJECT_NOT_FOUND));

        if (project.getDeletedAt() != null) {
            throw new CommunityHandler(ErrorStatus.PROJECT_DELETED);
        }

        Long Likecount = likeRepository.countByTeamId(id);
        MemberInfo memberInfoEntity = memberInfoRepository.findByMember(project.getMember());

        // Construct MemberInfo
        CommunityResponse.MemberInfo memberInfo = new CommunityResponse.MemberInfo(
                project.getMember().getId(),
                memberInfoEntity.getImageUrl(),
                memberInfoEntity.getNickname()
        );

        // 프로젝트 링크 조회
        List<ProjectLink> links = projectLinkRepository.findByProject(project);

        // 순서 자동 설정
        for (int i = 0; i < links.size(); i++) {
            links.get(i).setOrderIndex(i + 1);
        }

        return ProjectConverter.toProjectDetailResponse(project, memberInfo, Likecount, links);
    }

    @Override
    @Transactional
    public ProjectResponse.ProjectUpdateResponse updateProject(Long projectId, Long userId, ProjectRequest.ProjectUpdateRequest request, MultipartFile projectImage){
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.INVALID_MEMBER));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectHandler(ErrorStatus.PROJECT_NOT_FOUND));

        if (!project.getMember().getId().equals(userId)) {
            throw new ProjectHandler(ErrorStatus.UNAUTHORIZED_USER);
        }

        String imageUrl = project.getProjectImageUrl();
        try {
            if (projectImage != null && !projectImage.isEmpty()) {
                imageUrl = s3Service.saveProfileImage(projectImage);
            }
        } catch (IOException e) {
            throw new ProjectHandler(ErrorStatus.IMAGE_UPLOAD_FAILED);
        }
        ProjectConverter.toUpdateProject(project, request, imageUrl);

        // 링크 삭제하고 새로 추가하기
        projectLinkRepository.deleteByProject(project);

        // 링크 리스트 순서 자동 설정 후 변환 및 저장
        List<ProjectLink> links = ProjectConverter.toProjectUdpateLinks(request.getLinks(), project);

        for (int i = 0; i < links.size(); i++) {
            links.get(i).setOrderIndex(i + 1); // 자동 순서 설정
        }
        projectLinkRepository.saveAll(links);
        //return ProjectConverter.toProjectDetailResponse(project);
        return ProjectConverter.toProjectUpdateResponse(project, links);
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectHandler(ErrorStatus.PROJECT_NOT_FOUND));

        if (project.getDeletedAt() != null) {
            throw new TeamHandler(ErrorStatus.PROJECT_DELETED);
        }


        if (!project.getMember().getId().equals(userId)) {
            throw new CommunityHandler(ErrorStatus.UNAUTHORIZED_USER);
        }

        project.deleteSoftly();

        projectRepository.save(project);
    }

    @Override
    @Transactional
    public List<ProjectResponse.OtherProjectResponse> getOtherProjects(Long currentProjectId) {
        // 현재 프로젝트를 찾음
        Project project = projectRepository.findById(currentProjectId)
                .orElseThrow(() -> new ProjectHandler(ErrorStatus.PROJECT_NOT_FOUND));

        // 작성자의 다른 프로젝트 조회
        List<Project> otherProjects = projectRepository.findOtherProjectsByMember(project.getMember().getId(), currentProjectId);

        // 변환 후 반환
        return otherProjects.stream()
                .map(ProjectConverter::toOtherProjectResponse)
                .collect(Collectors.toList());
    }
}
