package com.backend.devfordev.repository.TeamRepository;

import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.TeamEntity.Team;
import com.backend.devfordev.domain.TeamEntity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    boolean existsByTeamAndMember(Team team, Member memberToInvite);
    List<TeamMember> findByTeamId(Long teamId);
    Optional<TeamMember> findByTeamIdAndMemberId(Long teamId, Long memberId);

    long countByTeam(Team team); // 팀의 현재 멤버 수 조회
}
