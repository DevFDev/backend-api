package com.backend.devfordev.dto.MemberDto;

import com.backend.devfordev.domain.enums.Affiliation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class MyPageInfoRequest {
    @Getter
    @Setter
    public static class ProfileUpdateRequest {
        /*
       이미지
       닉네임
       소개
       깃허브
       포지션
       기술스택
       소속
       완성률은 자동 계산
         */
        @Schema(description = "회원 닉네임", example = "뽀꼬")
        String nickname;

        @Schema(description = "소개문구", example = "간단한 소개글을 작성해보세요!")
        String introduction;
        @Schema(description = "깃허브 링크", example = "http://github.com/bboggo")
        String gitHub;

        @Schema(description = "포지션", example = "[\"포지션1\", \"포지션2\", \"포지션3\"]")
        List<String> position;
        @Schema(description = "기술스택", example = "[\"태그1\", \"태그2\", \"태그3\"]")
        List<String> techStacks;
        @Schema(description = "소속", example = "COMPANY_SCHOOL")
        Affiliation affiliation;

    }

    @Getter
    @Setter
    public static class PasswordUpdateRequest {
        @NotNull(message = "This field must not be null.")
        @Schema(description = "비밀번호", example = "new_password1234!")
        String password;
    }

    @Getter
    @Setter
    public static class checkNicknameRequest{
        @Schema(description = "회원 닉네임", example = "new_nickname")
        @NotBlank(message = "This field must not be null.")
        String nickname;
    }

}
