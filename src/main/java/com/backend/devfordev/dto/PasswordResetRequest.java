package com.backend.devfordev.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record PasswordResetRequest(
        @NotBlank
        @Schema(description = "토큰(url에 있는 UUID값)", example = "abcd1234~")
        String token,


        @Pattern(
                regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,16}$",
                message = "비밀번호는 영문 대소문자, 숫자 2가지 이상으로 조합해 8자 이상 16자 이하로 입력해주세요."
        )
        @NotBlank(message = "This field must not be null.")
        String newPassword,
        @NotBlank
        String confirmPassword
) {
}
