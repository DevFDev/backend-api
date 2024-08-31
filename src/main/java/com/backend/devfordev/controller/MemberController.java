package com.backend.devfordev.controller;

import com.backend.devfordev.apiPayload.ApiResponse;
import com.backend.devfordev.apiPayload.code.status.SuccessStatus;
import com.backend.devfordev.dto.SignUpRequest;
import com.backend.devfordev.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원 가입 및 로그인")
@RequiredArgsConstructor
@RestController
@RequestMapping
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "회원 가입")
    @PostMapping(value = "/v1/auth/sign-up")
    public ResponseEntity<ApiResponse> join(@RequestBody SignUpRequest request){
        try {
            ApiResponse apiResponse = ApiResponse.builder()
                    .result(memberService.registerMember(request))
                    .isSuccess(SuccessStatus._OK.getReason().getIsSuccess())
                    .code(SuccessStatus._OK.getCode())
                    .message(SuccessStatus._OK.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        SignUpResponse member = memberService.registerMember(request);
//        return ApiResponse.onSuccess(MemberConverter.toSignUpResponse(member));
    }

}