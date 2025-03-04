package com.backend.devfordev.converter;

import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.MemberEntity.MemberInfo;
import com.backend.devfordev.dto.MemberDto.MemberResponse;
import com.backend.devfordev.dto.MemberDto.SignInResponse;
import com.backend.devfordev.dto.MemberDto.SignUpRequest;
import com.backend.devfordev.dto.MemberDto.SignUpResponse;
import com.backend.devfordev.repository.MemberRepository.MemberInfoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;

public class MemberConverter {
    public static Member toMember(SignUpRequest signUpRequest, PasswordEncoder encoder) {
        // 깃허브 URL 생성 (입력 값이 null이거나 비어있을 경우 null 저장)
        String githubUrl = (signUpRequest.gitHub() == null || signUpRequest.gitHub().isEmpty())
                ? null
                : "https://github.com/" + signUpRequest.gitHub();

        return Member.builder()
                .email(signUpRequest.email())
                .password(encoder.encode(signUpRequest.password()))
                .name(signUpRequest.name())
                .github(githubUrl) // 깃허브 URL 또는 null 저장
                .build();
    }


    // 랜덤 닉네임 생성 메소드
    private static String generateRandomNickname() {
        int length = 10;
        String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        Random random = new Random();
        StringBuilder nickname = new StringBuilder();

        for (int i = 0; i < length; i++) {
            nickname.append(alphanumeric.charAt(random.nextInt(alphanumeric.length())));
        }

        return nickname.toString();
    }

    // 닉네임 중복 검사와 유니크한 닉네임 생성
    private static String generateUniqueNickname(MemberInfoRepository memberInfoRepository) {
        String nickname;
        do {
            nickname = generateRandomNickname();
        } while (memberInfoRepository.existsByNickname(nickname)); // 중복 닉네임 검사
        return nickname;
    }
    public static MemberInfo toMemberInfo(SignUpRequest signUpRequest, Member member, MemberInfoRepository memberInfoRepository) {
        String defaultImageUrl = "https://dfdnew.s3.ap-northeast-2.amazonaws.com/pen%20%283%29.png";
        String uniqueNickname = generateUniqueNickname(memberInfoRepository);
        return MemberInfo.builder()
                .nickname(uniqueNickname) // 기본 닉네임은 name 필드로 설정
                .imageUrl(defaultImageUrl)
                .completionRate(0L)
                .member(member)
                .build();
    }


    public static SignUpResponse toSignUpResponse(Member member, MemberInfo memberInfo) {
        return new SignUpResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getGithub(),
                memberInfo.getNickname(),
                memberInfo.getImageUrl()
        );
    }

    public static SignInResponse toSignInResponse(Member member, MemberInfo memberInfo, String accessToken, String refreshToken) {
        return new SignInResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                memberInfo.getNickname(),
                memberInfo.getImageUrl(),
                accessToken,
                refreshToken
        );
    }

    public static MemberResponse.MemberInfoResponse toGetMemberResponse(Member member, MemberInfo memberInfo) {
        return new MemberResponse.MemberInfoResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                memberInfo.getNickname(),
                memberInfo.getImageUrl()
        );
    }



}

