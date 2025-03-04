package com.backend.devfordev.service;

import com.backend.devfordev.apiPayload.code.status.ErrorStatus;
import com.backend.devfordev.apiPayload.exception.handler.MemberHandler;
import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.dto.EmailMessage;
import com.backend.devfordev.dto.EmailRequest;
import com.backend.devfordev.dto.EmailResponse;
import com.backend.devfordev.dto.PasswordResetRequest;
import com.backend.devfordev.repository.MemberRepository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public EmailResponse sendMail(EmailRequest emailRequest) {
        try {
            // 사용자 정보를 가져오는 로직 추가
            Member member = memberRepository.findByEmail(emailRequest.email())
                    .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

            // 토큰 생성 및 저장
            String resetToken = generateResetToken(member);

            // MimeMessage 객체 생성
            MimeMessage mimeMessage = emailSender.createMimeMessage();

            // 타임리프 Context에 변수 설정
            // URL 생성
            String resetLink = "http://example.com/reset-password?token=" + resetToken;

            // 타임리프 컨텍스트 설정
            Context context = new Context();
            context.setVariable("name", member.getName());
            context.setVariable("resetLink", "http://localhost:3000/reset-password?token=" + resetToken);

            String message = templateEngine.process("sendEmail", context); // 타임리프 템플릿 처리

            System.out.println("Generated HTML: \n" + message); // 렌더링된 HTML 출력

            // 이메일 메시지 빌드
            EmailMessage emailMessage = EmailMessage.builder()
                    .to(emailRequest.email()) // 수신자 이메일
                    .subject("[DevForDev] 비밀번호 찾기")
                    .message(message) // 타임리프로 렌더링된 메시지
                    .build();

            // MimeMessageHelper로 이메일 설정
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo()); // 수신자
            mimeMessageHelper.setSubject(emailMessage.getSubject()); // 제목
            mimeMessageHelper.setText(emailMessage.getMessage(), true); // 본문

            // 이메일 전송
            emailSender.send(mimeMessage);
            return new EmailResponse(emailRequest.email());

        } catch (MessagingException e) {
            throw new MemberHandler(ErrorStatus.EMAIL_SEND_ERROR);
        }
    }

    public String generateResetToken(Member member) {
        String resetToken = UUID.randomUUID().toString(); // 고유한 토큰 생성
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(60); // 유효 시간 1시간

        member.setResetToken(resetToken);
        member.setTokenExpiry(expiryTime);

        memberRepository.save(member); // 변경 사항 저장

        return resetToken;
    }


    public void resetPassword(PasswordResetRequest request) {
        // 비밀번호 확인 검증
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new MemberHandler(ErrorStatus.PASSWORD_NOT_MATCH);
        }

        // 토큰으로 사용자 조회
        Member member = memberRepository.findByResetToken(request.token())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.INVALID_RESET_TOKEN));

        // 토큰 만료 여부 확인
        if (member.getTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new MemberHandler(ErrorStatus.RESET_TOKEN_EXPIRED);
        }

        // 비밀번호 암호화 및 저장
        member.setPassword(passwordEncoder.encode(request.newPassword()));
        member.setResetToken(null); // 토큰 제거
        member.setTokenExpiry(null); // 토큰 만료 제거
        memberRepository.save(member);
    }


}
