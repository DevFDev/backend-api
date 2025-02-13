package com.backend.devfordev.converter;

import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.ProjectEntity.Project;
import com.backend.devfordev.domain.ProjectEntity.ProjectLink;
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

    public static List<ProjectLink> toProjectLinks(List<ProjectRequest.ProjectCreateRequest.LinkRequest> linkRequests, Project project) {
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
}
