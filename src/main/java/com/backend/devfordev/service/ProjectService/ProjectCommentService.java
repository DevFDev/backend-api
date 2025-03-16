package com.backend.devfordev.service.ProjectService;

import com.backend.devfordev.converter.ProjectConverter;
import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.MemberEntity.MemberInfo;
import com.backend.devfordev.domain.ProjectEntity.Project;
import com.backend.devfordev.domain.ProjectEntity.ProjectComment;
import com.backend.devfordev.dto.CommunityDto.CommunityCommentResponse;
import com.backend.devfordev.dto.ProjectDto.ProjectCommentRequest;
import com.backend.devfordev.dto.ProjectDto.ProjectCommentResponse;
import com.backend.devfordev.repository.MemberRepository.MemberInfoRepository;
import com.backend.devfordev.repository.MemberRepository.MemberRepository;
import com.backend.devfordev.repository.ProjectRepository.ProjectCommentRepository;
import com.backend.devfordev.repository.ProjectRepository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProjectCommentService {
    private final ProjectCommentRepository commentRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;

    /**
     * ✅ 프로젝트 댓글 추가
     */
    @Transactional
    public ProjectCommentResponse addComment(Long projectId, Long userId, ProjectCommentRequest request) {
        // ✅ 프로젝트 존재 여부 확인
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        // ✅ 사용자 존재 여부 확인
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // ✅ 부모 댓글이 존재하는 경우 확인 (대댓글 기능)
        ProjectComment parentComment = null;
        if (request.getParentId() != null) {
            parentComment = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
        }

        MemberInfo memberInfoEntity = memberInfoRepository.findByMember(member);
        CommunityCommentResponse.MemberInfo memberInfo = new CommunityCommentResponse.MemberInfo(
                userId,
                memberInfoEntity.getImageUrl(), // MemberInfo의 imageUrl 사용
                memberInfoEntity.getNickname()  // MemberInfo의 nickname 사용
        );

        // ✅ 댓글 엔티티 생성
        ProjectComment comment = ProjectComment.builder()
                .project(project)
                .member(member)
                .commentContent(request.getContent())
                .parent(parentComment)
                .build();

        // ✅ 댓글 저장
        ProjectComment savedComment = commentRepository.save(comment);

        return ProjectConverter.toProjectCommentResponse(savedComment, memberInfo);
    }

    /**
     * ✅ 프로젝트 댓글 조회 (대댓글 포함)
     */
    @Transactional(readOnly = true)
    public List<ProjectCommentResponse> getCommentsByProjectId(Long projectId) {
        // ✅ 최상위 댓글만 조회
        List<ProjectComment> topLevelComments = commentRepository.findByProjectIdAndParentIsNull(projectId);

        return topLevelComments.stream()
                .map(this::convertWithReplies)  // 대댓글 포함 변환
                .collect(Collectors.toList());
    }

    /**
     * ✅ 대댓글 변환
     */
    private ProjectCommentResponse convertWithReplies(ProjectComment comment) {
        // Member와 연관된 MemberInfo 조회
        MemberInfo memberInfoEntity = memberInfoRepository.findByMember(comment.getMember());

        CommunityCommentResponse.MemberInfo memberInfo = new CommunityCommentResponse.MemberInfo(
                comment.getMember().getId(),
                memberInfoEntity.getImageUrl(), // MemberInfo의 imageUrl 사용
                memberInfoEntity.getNickname()  // MemberInfo의 nickname 사용
        );

        // ✅ 최상위 댓글 변환
        ProjectCommentResponse response = ProjectConverter.toProjectCommentResponse(comment, memberInfo);

        // ✅ 대댓글(Children) 처리
        List<ProjectCommentResponse> replies = comment.getChildren().stream()
                .map(this::convertWithReplies) // 재귀 호출
                .collect(Collectors.toList());

        response.setReplies(replies);
        return response;
    }
}
