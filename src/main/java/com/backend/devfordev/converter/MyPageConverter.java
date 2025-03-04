package com.backend.devfordev.converter;

import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.MemberEntity.MemberInfo;
import com.backend.devfordev.dto.MemberDto.MyPageInfoRequest;
import com.backend.devfordev.dto.MemberDto.MyPageInfoResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;

public class MyPageConverter {
    public static MyPageInfoResponse.ProfileResponse toGetProfileResponse(Member member, MemberInfo memberInfo) {
        return new MyPageInfoResponse.ProfileResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                memberInfo.getNickname(),
                memberInfo.getImageUrl(),
                memberInfo.getIntroduction(),
                member.getGithub(),
                memberInfo.getPosition() != null ? Arrays.asList(memberInfo.getPosition().split(",")) : new ArrayList<>(),
                memberInfo.getTechStacks() != null ? Arrays.asList(memberInfo.getTechStacks().split(",")) : new ArrayList<>(),
                memberInfo.getAffiliation(),
                memberInfo.getCompletionRate()
        );
    }

    public static void toUpdateProfileRequest(
            Member member,
            MemberInfo memberInfo,
            MyPageInfoRequest.ProfileUpdateRequest request,
            String imageUrl) {
        // Member 업데이트
        if (request.getGitHub() != null) {
            member.setGithub(request.getGitHub());
        }

        // MemberInfo 업데이트
        if (request.getNickname() != null) {
            memberInfo.setNickname(request.getNickname());
        }


        memberInfo.setImageUrl(imageUrl);


        if (request.getIntroduction() != null) {
            memberInfo.setIntroduction(request.getIntroduction());
        }

        if (request.getPosition() != null) {
            memberInfo.setPosition(String.join(",", request.getPosition()));
        }

        if (request.getTechStacks() != null && !request.getTechStacks().isEmpty()) {
            // 기술 스택 리스트를 쉼표로 구분된 문자열로 저장
            memberInfo.setTechStacks(String.join(",", request.getTechStacks()));
        }

        if (request.getAffiliation() != null) {
            memberInfo.setAffiliation(request.getAffiliation());
        }

        // 완성률은 자동 계산 (예: 프로필 필드 중 입력된 비율로 계산)
        calculateCompletionRate(memberInfo, member);


    }
    private static void calculateCompletionRate(MemberInfo memberInfo, Member member) {
        double completionRate = 0;

        // 각 필드의 비율을 설정
        final double NICKNAME_WEIGHT = 16.0;
        final double INTRODUCTION_WEIGHT = 16.0;
        final double GITHUB_WEIGHT = 16.0;
        final double POSITION_WEIGHT = 16.0;
        final double TECH_STACKS_WEIGHT = 16.0;
        final double IMAGE_URL_WEIGHT = 10.0;
        final double AFFILIATION_WEIGHT = 10.0;

        // 기본값 정의
        final String DEFAULT_IMAGE_URL = "https://default-imageUrl.com";
        final String DEFAULT_NICKNAME = member.getName();

        // 닉네임
        if (memberInfo.getNickname() != null && !memberInfo.getNickname().isEmpty()
                && !memberInfo.getNickname().equals(DEFAULT_NICKNAME)) {
            completionRate += NICKNAME_WEIGHT;
            System.out.println("아님아님");
        }

        // 소개
        if (memberInfo.getIntroduction() != null && !memberInfo.getIntroduction().isEmpty()) {
            completionRate += INTRODUCTION_WEIGHT;
        }

        // 깃허브
        if (memberInfo.getMember() != null && memberInfo.getMember().getGithub() != null
                && !memberInfo.getMember().getGithub().isEmpty()) {
            completionRate += GITHUB_WEIGHT;
        }

        // 포지션
        if (memberInfo.getPosition() != null && !memberInfo.getPosition().isEmpty()) {
            completionRate += POSITION_WEIGHT;
        }

        // 기술 스택
        if (memberInfo.getTechStacks() != null && !memberInfo.getTechStacks().isEmpty()) {
            completionRate += TECH_STACKS_WEIGHT;
        }

        // 프로필 이미지
        if (memberInfo.getImageUrl() != null && !memberInfo.getImageUrl().isEmpty()
                && !memberInfo.getImageUrl().equals(DEFAULT_IMAGE_URL)) {
            completionRate += IMAGE_URL_WEIGHT;
        }

        // 소속
        if (memberInfo.getAffiliation() != null) {
            completionRate += AFFILIATION_WEIGHT;
        }

        // 최종 완성률 설정
        memberInfo.setCompletionRate((long) completionRate);
    }


    public static MyPageInfoResponse.ProfileUpdateResponse ProfileUpdateResponse(Member member, MemberInfo memberInfo) {
        return new MyPageInfoResponse.ProfileUpdateResponse(
                memberInfo.getNickname(),
                memberInfo.getImageUrl(),
                memberInfo.getIntroduction(),
                member.getGithub(),
                memberInfo.getPosition() != null ? Arrays.asList(memberInfo.getPosition().split(",")) : new ArrayList<>(),
                memberInfo.getTechStacks() != null ? Arrays.asList(memberInfo.getTechStacks().split(",")) : new ArrayList<>(),
                memberInfo.getAffiliation(),
                memberInfo.getCompletionRate()
        );
    }

    public static void PasswordUpdateRequest(Member member, MyPageInfoRequest.PasswordUpdateRequest passwordUpdateRequest, PasswordEncoder encoder) {
        member.setPassword(encoder.encode(passwordUpdateRequest.getPassword()));
    }

}


