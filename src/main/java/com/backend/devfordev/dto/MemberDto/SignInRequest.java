package com.backend.devfordev.dto.MemberDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignInRequest(
        @Schema(description = "회원 이메일", example = "xxx@naver.com")
        @NotBlank(message = "This field must not be null.")
        String email,
        @Schema(description = "회원 비밀번호", example = "bboggo1234!")
        @NotBlank(message = "This field must not be null.")
        String password
) {
}
