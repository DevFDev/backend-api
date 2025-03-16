package com.backend.devfordev.controller;

import com.backend.devfordev.apiPayload.ApiResponse;
import com.backend.devfordev.apiPayload.code.status.SuccessStatus;

import com.backend.devfordev.domain.enums.TeamType;
import com.backend.devfordev.dto.CommunityDto.CommunityResponse;
import com.backend.devfordev.dto.CustomPageResponse;
import com.backend.devfordev.dto.TeamDto.TeamCommentRequest;
import com.backend.devfordev.dto.TeamDto.TeamCommentResponse;
import com.backend.devfordev.dto.TeamDto.TeamRequest;
import com.backend.devfordev.dto.TeamDto.TeamResponse;
import com.backend.devfordev.service.TeamService.TeamCommentService;
import com.backend.devfordev.service.TeamService.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Tag(name = "팀 API")
@RequiredArgsConstructor
@RestController
@RequestMapping
@Slf4j
public class TeamController {
    private final TeamService teamService;
    private final TeamCommentService teamCommentService;

    @Operation(summary = "팀 모집글 등록", description = "팀 모집 공고를 등록하는 api입니다.")
    @PostMapping(value = "/v1/team")
    public ResponseEntity<ApiResponse<TeamResponse.TeamCreateResponse>> createTeam(@Valid @RequestBody TeamRequest.TeamCreateRequest request, @AuthenticationPrincipal User user){

        TeamResponse.TeamCreateResponse teamCreateResponse = teamService.createTeam(request, Long.parseLong(user.getUsername()));
        ApiResponse<TeamResponse.TeamCreateResponse> apiResponse = ApiResponse.onSuccess(teamCreateResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }


    @Operation(summary = "팀 모집글 전체 조회", description = "팀 모집 공고 전체 글 조회 API입니다. 검색, 필터링, 정렬, 페이징 적용.")
    @GetMapping(value = "/v1/team")
    public ResponseEntity<ApiResponse<CustomPageResponse<TeamResponse.TeamListResponse>>> getTeamList(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) TeamType teamType,
            @RequestParam(required = false) List<String> positions,
            @RequestParam(required = false) List<String> techStacks,
            @RequestParam(defaultValue = "recent") String sortBy,
            @RequestParam(required = false) Boolean teamIsActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size); // 정렬은 서비스에서 처리
        CustomPageResponse<TeamResponse.TeamListResponse> teamList = teamService.getTeamList(
                Optional.ofNullable(searchTerm),
                Optional.ofNullable(teamType),
                positions != null ? positions : Collections.emptyList(),
                techStacks != null ? techStacks : Collections.emptyList(),
                sortBy,
                Optional.ofNullable(teamIsActive),
                pageable
        );

        return ResponseEntity.ok(ApiResponse.onSuccess(teamList));
    }


    @Operation(summary = "팀 모집글 상세 조회", description = "각 팀 모집 공고를 상세 조회할 수 있는 api입니다.")
    @GetMapping(value = "/v1/team/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponse.TeamDetailResponse>> getTeamDetail(@PathVariable Long teamId) {
        TeamResponse.TeamDetailResponse teamDetail = teamService.getTeamDetail(teamId);

        ApiResponse<TeamResponse.TeamDetailResponse> apiResponse = ApiResponse.onSuccess(teamDetail);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "팀 모집 마감", description = "팀 모집 공고를 마감하는 api입니다. 모집 상태가 '모집완료'로 변경됩니다. 작성자만 해당 기능을 사용할 수 있습니다.")
    @PatchMapping("v1/team/{teamId}/close")
    public ResponseEntity<ApiResponse> closeRecruitment(
            @PathVariable Long teamId,
            @AuthenticationPrincipal User user) {

        teamService.closeRecruitment(teamId, Long.parseLong(user.getUsername()));

        ApiResponse response = ApiResponse.builder()
                .isSuccess(true)
                .code(SuccessStatus._OK.getCode())
                .message("모집 상태가 마감되었습니다.")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(summary = "팀 모집글 삭제", description = "팀 모집글을 삭제하는 api입니다. 작성자만 해당 기능을 사용할 수 있습니다.")
    @DeleteMapping(value = "/v1/team/{teamId}")
    public ResponseEntity<ApiResponse> deleteTeam(@PathVariable Long teamId, @AuthenticationPrincipal User user) {
        teamService.deleteTeam(teamId, Long.parseLong(user.getUsername()));
        ApiResponse apiResponse = ApiResponse.builder()
                .isSuccess(SuccessStatus._OK.getReason().getIsSuccess())
                .code(SuccessStatus._OK.getCode())
                .message("팀 모집글이 성공적으로 삭제되었습니다.")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "멤버 리스트 검색", description = "전체 유저 리스트를 검색하는 api입니다. 닉네임으로 검색할 수 있습니다. 작성자만 해당 기능을 사용할 수 있습니다.")
    @GetMapping("/v1/team/{teamId}/search-members")
    public ResponseEntity<ApiResponse<List<CommunityResponse.MemberInfo>>> searchMembers(@RequestParam(required = false) String nickname,
                                                                                         @PathVariable Long teamId,
                                                                                         @AuthenticationPrincipal User user) {
        List<CommunityResponse.MemberInfo> members = teamService.searchMembersByNickname(nickname, Long.parseLong(user.getUsername()), teamId);
        ApiResponse<List<CommunityResponse.MemberInfo>> apiResponse = ApiResponse.onSuccess(members);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "팀 멤버 추가", description = "팀에 멤버를 추가할 수 있는 api입니다. 작성자만 해당 기능을 사용할 수 있습니다.")
    @PostMapping("/v1/team/{teamId}/add")
    public ResponseEntity<ApiResponse<TeamResponse.TeamAddMemberResponse>> addMemberToTeam(
            @Valid @RequestBody TeamRequest.TeamAddMemberRequest request,
            @PathVariable Long teamId,
            @AuthenticationPrincipal User user) {

        TeamResponse.TeamAddMemberResponse teamAddMemberResponse = teamService.AddTeamMember(request, Long.parseLong(user.getUsername()), teamId);
        ApiResponse<TeamResponse.TeamAddMemberResponse> apiResponse = ApiResponse.onSuccess(teamAddMemberResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @Operation(summary = "팀 멤버 전체 조회", description = "해당 팀의 전체 멤버를 조회할 수 있는 api입니다.")
    @GetMapping("v1/team/{teamId}/members")
    public ResponseEntity<ApiResponse<TeamResponse.TeamMemberListWithIdResponse>> getTeamMembers(@PathVariable Long teamId) {
        TeamResponse.TeamMemberListWithIdResponse members = teamService.getTeamMemberList(teamId);
        ApiResponse<TeamResponse.TeamMemberListWithIdResponse> apiResponse = ApiResponse.onSuccess(members);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "팀 멤버 삭제", description = "해당 팀에 속한 팀 멤버를 삭제할 수 있는 api입니다. 작성자만 해당 기능을 사용할 수 있습니다.")
    @DeleteMapping("v1/team/{teamId}/members/{memberId}")
    public ResponseEntity<ApiResponse> deleteTeamMember(@PathVariable Long teamId,
                                                        @PathVariable Long memberId,
                                                        @AuthenticationPrincipal User user) {
        teamService.deleteTeamMember(teamId, memberId, Long.parseLong(user.getUsername()));

        ApiResponse apiResponse = ApiResponse.builder()
                .isSuccess(SuccessStatus._OK.getReason().getIsSuccess())
                .code(SuccessStatus._OK.getCode())
                .message("팀 멤버가 성공적으로 삭제되었습니다.")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "팀 모집 공고 업데이트", description = "팀 모집 공고를 업데이트 할 수 있는 api입니다. 작성자만 해당 기능을 사용할 수 있습니다.")
    @PatchMapping("v1/team/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponse.TeamUpdateResponse>> updateTeam(
            @PathVariable Long teamId,
            @RequestBody @Valid TeamRequest.TeamUpdateRequest request,
            @AuthenticationPrincipal User user) {

        TeamResponse.TeamUpdateResponse response = teamService.updateTeam(teamId, request, Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }


    @Operation(summary = "팀 모집 댓글 등록",
            description = "팀 모집글에 댓글 작성하는 API입니다. 작성자만 해당 기능을 사용할 수 있습니다. " +
                    "최상위 댓글의 경우 parentId를 null로 보내주세요!")
    @PostMapping("v1/team/{teamId}/comments")
    public ResponseEntity<ApiResponse<TeamCommentResponse>> createComment(
            @PathVariable Long teamId,
            @RequestBody @Valid TeamCommentRequest request,
            @AuthenticationPrincipal User user) {

        TeamCommentResponse response = teamCommentService.addComment(teamId, Long.parseLong(user.getUsername()), request);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }


    @Operation(summary = "팀 모집 댓글 조회",
            description = "특정 팀 모집글의 댓글을 조회합니다. 댓글은 계층 구조로 반환됩니다.")
    @GetMapping("v1/team/{teamId}/comments")
    public ResponseEntity<ApiResponse<List<TeamCommentResponse>>> getComments(
            @PathVariable Long teamId) {

        List<TeamCommentResponse> responses = teamCommentService.getCommentsByTeamId(teamId);
        return ResponseEntity.ok(ApiResponse.onSuccess(responses));
    }
}
