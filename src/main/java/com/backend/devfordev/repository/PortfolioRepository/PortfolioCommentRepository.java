package com.backend.devfordev.repository.PortfolioRepository;

import com.backend.devfordev.domain.CommunityEntity.CommunityComment;
import com.backend.devfordev.domain.PortfolioEntity.PortfolioComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PortfolioCommentRepository extends JpaRepository<PortfolioComment, Long> {
    List<PortfolioComment> findByPortfolioIdAndParentIsNull(Long portId);
}
