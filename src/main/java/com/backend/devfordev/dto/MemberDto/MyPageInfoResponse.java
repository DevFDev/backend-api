package com.backend.devfordev.dto.MemberDto;

import com.backend.devfordev.domain.enums.Affiliation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MyPageInfoResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileResponse {
        @Schema(description = "회원 아이디", example = "1")
        Long id;
        @Schema(description = "회원 이메일", example = "xxx@naver.com")
        String email;

        @Schema(description = "회원 이름", example = "김민지")
        String name;

        @Schema(description = "회원 닉네임", example = "뽀꼬")
        String nickname;
        @Schema(description = "프로필 사진 url", example = "aaa.com")
        String imageUrl;
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
        @Schema(description = "포트폴리오 완성률(%)", example = "30")
        Long completionRate;


    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileUpdateResponse {
        @Schema(description = "회원 닉네임", example = "뽀꼬")
        String nickname;
        @Schema(description = "프로필 사진 url", example = "aaa.com")
        String imageUrl;
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
        @Schema(description = "포트폴리오 완성률(%)", example = "30")
        Long completionRate;
    }
}
