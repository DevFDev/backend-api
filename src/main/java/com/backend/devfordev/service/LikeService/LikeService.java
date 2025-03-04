package com.backend.devfordev.service.LikeService;

import com.backend.devfordev.dto.LikeDto.LikeRequest;
import com.backend.devfordev.dto.LikeDto.LikeResponse;

import java.util.List;
import java.util.Map;

public interface LikeService {
    public LikeResponse createLike(LikeRequest request, Long userId);
    public Map<Long, Long> getProjectLikeCounts(List<Long> projectIds);
}
