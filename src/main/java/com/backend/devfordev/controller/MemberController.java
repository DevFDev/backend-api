package com.backend.devfordev.controller;

import com.backend.devfordev.apiPayload.ApiResponse;
import com.backend.devfordev.apiPayload.code.status.SuccessStatus;
import com.backend.devfordev.dto.EmailRequest;
import com.backend.devfordev.dto.EmailResponse;
import com.backend.devfordev.dto.MemberDto.*;
import com.backend.devfordev.dto.PasswordResetRequest;
import com.backend.devfordev.service.EmailService;
import com.backend.devfordev.service.MemberService.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 가입 및 로그인 API")
@RequiredArgsConstructor
@RestController
@RequestMapping
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final EmailService emailService;

    @Operation(summary = "회원 가입", description = "회원가입 api입니다. 중복 이메일로는 회원가입 불가")
    @PostMapping(value = "/v1/auth/sign-up")
    public ResponseEntity<ApiResponse<SignUpResponse>> createMember(@RequestBody @Valid SignUpRequest request){
        SignUpResponse signUpResponse = memberService.signUp(request);
        ApiResponse<SignUpResponse> apiResponse = ApiResponse.onSuccess(signUpResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);

//        SignUpResponse member = memberService.registerMember(request);
//        return ApiResponse.onSuccess(MemberConverter.toSignUpResponse(member));
    }

    @Operation(summary = "로그인", description = "로그인 api입니다.")
    @PostMapping(value = "/v1/auth/sign-in")
    public ResponseEntity<ApiResponse<SignInResponse>> signIn(@Valid @RequestBody SignInRequest request) {
        SignInResponse signInResponse = memberService.signIn(request);
        ApiResponse<SignInResponse> apiResponse = ApiResponse.onSuccess(signInResponse);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "로그인한 유저 조회", description = "로그인한 유저의 id, 이메일, 이름, 닉네임, 이미지를 확인할 수 있는 api입니다.")
    @GetMapping("/v1/auth")
    public ResponseEntity<ApiResponse<MemberResponse.MemberInfoResponse>> getMemberInfo(@AuthenticationPrincipal User user) {
        MemberResponse.MemberInfoResponse memberResponse = memberService.getMember(Long.parseLong(user.getUsername()));
        ApiResponse<MemberResponse.MemberInfoResponse> apiResponse =ApiResponse.onSuccess(memberResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "이메일 중복 체크", description = "입력된 이메일이 이미 존재하는 이메일인지 체크할 수 있는 api입니다.")
    @PostMapping("/v1/auth/check-email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmailDuplicate(@RequestBody MemberRequest.checkEmailRequest request) {
        boolean isDuplicate = memberService.checkEmailDuplicate(request.getEmail());
        return ResponseEntity.ok(ApiResponse.onSuccess(!isDuplicate));
    }

    @Operation(summary = "액세스 토큰 재발급", description = "액세스 토큰을 재발급하는 api입니다. access 토큰과 refresh 토큰을 요청합니다.")
    @PostMapping("/v1/auth/new-token")
    public ResponseEntity<ApiResponse<TokenResponse.AccessTokenResponse>> refreshAccessToken(
            @RequestBody TokenRequest.RefreshTokenRequest request) {
        String newAccessToken = memberService.refreshAccessToken(request.getRefreshToken(), request.getOldAccessToken());

        // 응답 데이터를 DTO에 담아 반환
        return ResponseEntity.ok(ApiResponse.onSuccess(
                new TokenResponse.AccessTokenResponse(newAccessToken)
        ));
    }


    // 비밀번호 찾기 이메일 관련

    @Operation(summary = "비밀번호 찾기 이메일 전송 api", description = "비밀번호 찾기용 이메일 발송 api 입니다.")
    @PostMapping("/v1/auth/email")
    public ResponseEntity<ApiResponse<EmailResponse>> sendEmail(@RequestBody @Valid EmailRequest request) {
        EmailResponse sendEmailResponse = emailService.sendMail(request);
        ApiResponse<EmailResponse> apiResponse = ApiResponse.onSuccess(sendEmailResponse);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "비밀번호 재설정 api", description = "비밀번호 재설정 페이지용 api입니다.")
    @PostMapping("/v1/auth/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody @Valid PasswordResetRequest request) {
        emailService.resetPassword(request);
        ApiResponse apiResponse = ApiResponse.builder()
                .isSuccess(SuccessStatus._OK.getReason().getIsSuccess())
                .code(SuccessStatus._OK.getCode())
                .message("비밀번호 재설정 성공!")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
