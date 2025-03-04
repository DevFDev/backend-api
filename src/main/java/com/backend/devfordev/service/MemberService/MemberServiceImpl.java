package com.backend.devfordev.service.MemberService;

import com.backend.devfordev.apiPayload.ExceptionHandler;
import com.backend.devfordev.apiPayload.code.status.ErrorStatus;
import com.backend.devfordev.apiPayload.exception.handler.MemberHandler;
import com.backend.devfordev.converter.MemberConverter;
import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.MemberEntity.MemberInfo;
import com.backend.devfordev.domain.MemberEntity.MemberRefreshToken;
import com.backend.devfordev.dto.MemberDto.*;
import com.backend.devfordev.repository.MemberRepository.MemberInfoRepository;
import com.backend.devfordev.repository.MemberRepository.MemberRefreshTokenRepository;
import com.backend.devfordev.repository.MemberRepository.MemberRepository;
import com.backend.devfordev.security.TokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final MemberRefreshTokenRepository memberRefreshTokenRepository;
    private final PasswordEncoder encoder;
    private final TokenProvider tokenProvider;
    private final MemberInfoRepository memberInfoRepository;

    // 회원가입
    // 이미지 추가
    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {

            Member member = MemberConverter.toMember(request, encoder);

//            if(memberRepository.findByName(member.getName()).isPresent()) {
//                throw new ExceptionHandler(ErrorStatus.DUPLICATED_NAME);
//            }

            if(memberRepository.findByEmail(member.getEmail()).isPresent()) {
                throw new ExceptionHandler(ErrorStatus.DUPLICATED_EMAIL);
            }


            member = memberRepository.save(member);

            MemberInfo memberInfo = MemberConverter.toMemberInfo(request, member, memberInfoRepository);
            memberInfo = memberInfoRepository.save(memberInfo); // memberInfoRepository는 MemberInfo의 Repository입니다.

        return MemberConverter.toSignUpResponse(member, memberInfo);

    }

    // 로그인
    @Transactional
    public SignInResponse signIn(SignInRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .filter(it -> encoder.matches(request.password(), it.getPassword()))
                .orElseThrow(() -> new MemberHandler(ErrorStatus.LOGIN_FAILED_PASSWORD_INCORRECT));

        MemberInfo memberInfo = memberInfoRepository.findByMember(member);


        String accessToken = tokenProvider.createAccessToken(String.format("%s:%s", member.getId(), member.getName()));
        String refreshToken = tokenProvider.createRefreshToken();

        System.out.println(memberRefreshTokenRepository.findByMemberId(member.getId()) + "!!!!!!!!!!!!!!!!!!!!!!!!!");
        // 리프레시 토큰이 이미 있으면 토큰을 갱신하고 없으면 토큰 추가
        memberRefreshTokenRepository.findByMemberId(member.getId())
                .ifPresentOrElse(
                        it -> it.updateRefreshToken(refreshToken),
                        ()-> memberRefreshTokenRepository.save(new MemberRefreshToken(member, refreshToken))
                );
        return MemberConverter.toSignInResponse(member, memberInfo, accessToken, refreshToken);
    }

    @Transactional
    public MemberResponse.MemberInfoResponse getMember(Long userId) {
        // Member 및 MemberInfo 조회
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        MemberInfo memberInfo = memberInfoRepository.findByMember(member);

        // 컨버터를 통해 DTO 변환
        return MemberConverter.toGetMemberResponse(member, memberInfo);
    }
    @Transactional
    // 이메일 중복체크
    public boolean checkEmailDuplicate(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new MemberHandler(ErrorStatus.EMAIL_DUPLICATED); // 커스텀 예외 처리
        }
        return false;
    }


    @Transactional
    public String refreshAccessToken(String refreshToken, String oldAccessToken) {
        try {
            // refreshToken 유효성 검사
            tokenProvider.validateTokenAndGetSubject(refreshToken);

            // 기존 accessToken의 사용자 정보 복호화
            String subject = tokenProvider.validateTokenAndGetSubject(oldAccessToken);

            // refreshToken이 유효한지 확인
            String[] userInfo = subject.split(":");
            Long memberId = Long.valueOf(userInfo[0]);

            MemberRefreshToken savedRefreshToken = memberRefreshTokenRepository.findByMemberId(memberId)
                    .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

            // 저장된 refreshToken과 요청 refreshToken 비교
            if (!savedRefreshToken.getRefreshToken().equals(refreshToken)) {
                throw new MemberHandler(ErrorStatus.INVALID_JWT_TOKEN);
            }

            // 새로운 accessToken 생성
            return tokenProvider.createAccessToken(subject);

        } catch (ExpiredJwtException ex) {
            throw new MemberHandler(ErrorStatus.EXPIRED_JWT_ERROR);
        } catch (IllegalArgumentException ex) {
            throw new MemberHandler(ErrorStatus.INVALID_JWT_TOKEN);
        }
    }
}
