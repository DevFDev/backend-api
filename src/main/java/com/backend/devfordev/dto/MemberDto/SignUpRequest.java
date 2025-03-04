package com.backend.devfordev.dto.MemberDto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SignUpRequest(
        @Schema(description = "회원 이메일", example = "xxx@naver.com")
        @NotBlank(message = "This field must not be null.")
        @Email(message = "올바른 형식의 이메일 주소여야 합니다.")
        String email,

        @Schema(description = "회원 비밀번호", example = "bboggo1234!")
        //@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = " 비밀번호는 영문, 숫자, 특수문자 중 3종류 이상 조합하여 최소 8자리 이상이어야 합니다.")

        @Pattern(
                regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,16}$",
                message = "비밀번호는 영문 대소문자, 숫자 2가지 이상으로 조합해 8자 이상 16자 이하로 입력해주세요."
        )
        @NotBlank(message = "This field must not be null.")
        String password,

//        @Schema(description = "이미지 url", example = "이미지 도메인(추후 수정 예정)")
//        String imageUrl,


        @Pattern(regexp = "^[a-zA-Z0-9가-힣]*$", message = " 닉네임은 영문, 숫자, 한글만 허용됩니다.")
        @Schema(description = "회원 이름", example = "BBOGGO")
        @NotBlank(message = "This field must not be null.")
        String name,

        @Schema(description = "깃허브", example = "bboggo")
        //@NotBlank(message = "This field must not be null.")
        String gitHub

) {
}
