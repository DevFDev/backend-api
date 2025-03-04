package com.backend.devfordev.dto.MemberDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class MemberRequest {
    @Getter
    @Setter
    public static class checkEmailRequest{
        @Schema(description = "회원 이메일", example = "xxx@naver.com")
        @NotBlank(message = "This field must not be null.")
        @Email(message = "올바른 형식의 이메일 주소여야 합니다.")
        String email;
    }



}
