package com.backend.devfordev.repository.MemberRepository;


import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.MemberEntity.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {

    MemberInfo findByMember(Member member);

    List<MemberInfo> findByNicknameContainingIgnoreCase(String nickname);

    Optional<MemberInfo> findByMemberId(Long memberId);

   boolean existsByNickname(String nickname);

}
