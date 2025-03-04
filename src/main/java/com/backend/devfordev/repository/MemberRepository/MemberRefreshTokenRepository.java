package com.backend.devfordev.repository.MemberRepository;

import com.backend.devfordev.domain.MemberEntity.MemberRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRefreshTokenRepository extends JpaRepository<MemberRefreshToken, Long> {
    Optional<MemberRefreshToken> findByMemberIdAndReissueCountLessThan(Long id, Long count);
    Optional<MemberRefreshToken> findByMemberId(Long memberId);
}