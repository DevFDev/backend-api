package com.backend.devfordev.repository.CommunityRepository;

import com.backend.devfordev.domain.CommunityEntity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
    List<CommunityComment> findByCommunityIdAndParentIsNull(Long communityId);
}
