package com.backend.devfordev.repository.CommunityRepository;

import com.backend.devfordev.domain.CommunityEntity.Community;
import com.backend.devfordev.domain.CommunityEntity.CommunityAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityAnswerRepository extends JpaRepository<CommunityAnswer, Long> {
    List<CommunityAnswer> findByCommunityOrderByCreatedAtDesc(Community community);
}

