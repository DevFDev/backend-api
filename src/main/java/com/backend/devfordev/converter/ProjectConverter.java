package com.backend.devfordev.converter;

import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.ProjectEntity.Project;
import com.backend.devfordev.domain.ProjectEntity.ProjectLink;
import com.backend.devfordev.dto.CommunityDto.CommunityResponse;
import com.backend.devfordev.dto.ProjectDto.ProjectRequest;
import com.backend.devfordev.dto.ProjectDto.ProjectResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectConverter {

    public static Project toProject(ProjectRequest.ProjectCreateRequest request, Member member, String imageUrl) {

        Project project = Project.builder()
                .projectTitle(request.getProjectTitle())
                .projectContent(request.getProjectContent())
                .projectSummary(request.getProjectSummary())
                .projectCategory(request.getProjectCategory())
                .projectImageUrl(imageUrl)
                .projectViews(0L)
                .member(member)
                .build();

        // techStacks 리스트를 쉼표로 구분된 문자열로 변환하여 저장
        project.setTags(Collections.singletonList(String.join(",", request.getTags())));
        project.setProTechStacks(Collections.singletonList(String.join(",", request.getProjectTechStacks())));
        return project;
    }

    public static List<ProjectLink> toProjectCreateLinks(List<ProjectRequest.ProjectCreateRequest.LinkRequest> linkRequests, Project project) {
        return linkRequests.stream()
                .map(linkRequest -> ProjectLink.builder()
                        .type(linkRequest.getType())
                        .url(linkRequest.getUrl())
                        .project(project)
                        .build())
                .collect(Collectors.toList());
    }

    public static List<ProjectLink> toProjectUdpateLinks(List<ProjectRequest.ProjectUpdateRequest.LinkRequest> linkRequests, Project project) {
        return linkRequests.stream()
                .map(linkRequest -> ProjectLink.builder()
                        .type(linkRequest.getType())
                        .url(linkRequest.getUrl())
                        .project(project)
                        .build())
                .collect(Collectors.toList());
    }

    public static ProjectResponse.ProjectCreateResponse toProjectResponse(
            Project project,
            List<ProjectLink> links
    ) {
        List<ProjectResponse.ProjectCreateResponse.LinkResponse> linkResponses = links.stream()
                .map(link -> new ProjectResponse.ProjectCreateResponse.LinkResponse(
                        link.getType(),
                        link.getUrl(),
                        link.getOrderIndex()
                ))
                .collect(Collectors.toList());

        List<String> projectTechStacks = project.getProTechStacks() != null ? project.getProTechStacks() : new ArrayList<>();
        List<String> tags = project.getTags() != null ? project.getTags() : new ArrayList<>();
        return new ProjectResponse.ProjectCreateResponse(
                project.getId(),
                project.getMember().getId(),
                project.getProjectTitle(),
                project.getProjectContent(),
                project.getProjectSummary(),
                project.getProjectCategory(),
                tags,
                projectTechStacks,
                project.getProjectImageUrl(),
                project.getCreatedAt(),
                linkResponses
        );
    }

    public static ProjectResponse.ProjectDetailResponse toProjectDetailResponse(
            Project project, CommunityResponse.MemberInfo member, Long likeCount, List<ProjectLink> links) {

        // 기술 스택과 태그를 문자열 리스트로 변환
        List<String> tags = project.getTags() != null ? project.getTags() : new ArrayList<>();

        List<String> techStackNames = project.getProTechStacks() != null ? project.getProTechStacks() : new ArrayList<>();

        List<ProjectResponse.ProjectCreateResponse.LinkResponse> linkResponses = links.stream()
                .map(link -> new ProjectResponse.ProjectCreateResponse.LinkResponse(
                        link.getType(),
                        link.getUrl(),
                        link.getOrderIndex()
                ))
                .collect(Collectors.toList());
        // TeamDetailResponse 객체 반환
        return new ProjectResponse.ProjectDetailResponse(
                project.getId(),
                member,
                project.getProjectTitle(),
                project.getProjectContent(),
                project.getProjectSummary(),
                project.getProjectCategory(),
                tags,
                techStackNames,
                project.getProjectImageUrl(),
                project.getCreatedAt(),
                linkResponses,
                project.getProjectViews(),
                0L,
                likeCount

        );
    }

    public static void toUpdateProject(Project project, ProjectRequest.ProjectUpdateRequest request, String imageUrl) {
        // 변경된 데이터만 업데이트
        project.setProjectTitle(request.getProjectTitle());
        project.setProjectContent(request.getProjectContent());
        project.setProjectSummary(request.getProjectSummary());
        project.setProjectCategory(request.getProjectCategory());

        // 이미지가 변경되었을 경우 업데이트
        if (imageUrl != null) {
            project.setProjectImageUrl(imageUrl);
        }

        // 기술 스택과 태그 리스트 업데이트 (쉼표로 구분된 문자열로 변환)
        project.setTags(Collections.singletonList(String.join(",", request.getTags())));
        project.setProTechStacks(Collections.singletonList(String.join(",", request.getProjectTechStacks())));
    }


    public static ProjectResponse.ProjectUpdateResponse toProjectUpdateResponse(
            Project project,
            List<ProjectLink> links
    ) {
        List<ProjectResponse.ProjectCreateResponse.LinkResponse> linkResponses = links.stream()
                .map(link -> new ProjectResponse.ProjectCreateResponse.LinkResponse(
                        link.getType(),
                        link.getUrl(),
                        link.getOrderIndex()
                ))
                .collect(Collectors.toList());

        List<String> projectTechStacks = project.getProTechStacks() != null ? project.getProTechStacks() : new ArrayList<>();
        List<String> tags = project.getTags() != null ? project.getTags() : new ArrayList<>();
        return new ProjectResponse.ProjectUpdateResponse(
                project.getId(),
                project.getProjectTitle(),
                project.getProjectContent(),
                project.getProjectSummary(),
                project.getProjectCategory(),
                tags,
                projectTechStacks,
                project.getProjectImageUrl(),
                project.getCreatedAt(),
                linkResponses
        );
    }

}
