package com.backend.devfordev.dto.MemberDto;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignInResponse(
        @Schema(description = "회원 아이디", example = "1")
        Long id,
        @Schema(description = "회원 이메일", example = "xxx@naver.com")
        String email,

        @Schema(description = "회원 이름", example = "김민지")
        String name,

        @Schema(description = "회원 닉네임", example = "뽀꼬")
        String nickname,

        @Schema(description = "프로필 사진 url", example = "aaa.com")
        String imageUrl,

        @Schema(description = "엑세스 토큰", example = "accessToken")
        String accessToken,
        @Schema(description = "리프레시 토큰", example = "refreshToken")
        String refreshToken
) {
}
