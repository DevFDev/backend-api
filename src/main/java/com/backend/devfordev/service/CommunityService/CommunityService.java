package com.backend.devfordev.service.CommunityService;

import com.backend.devfordev.domain.enums.CommunityCategory;
import com.backend.devfordev.dto.CommunityDto.CommunityCommentRequest;
import com.backend.devfordev.dto.CommunityDto.CommunityCommentResponse;
import com.backend.devfordev.dto.CommunityDto.CommunityRequest;
import com.backend.devfordev.dto.CommunityDto.CommunityResponse;

import java.util.List;
import java.util.Optional;

public interface CommunityService {
    public CommunityResponse.CommunityCreateResponse createCommunity(CommunityRequest.CommunityCreateRequest request, Long userId);
    public List<CommunityResponse.CommunityListResponse> getCommunityList(Optional<CommunityCategory> categoryOpt,  Optional<String> searchTermOpt, String sortBy);

    public CommunityResponse.CommunityDetailResponse getCommunityDetail(Long id);
    public List<CommunityResponse.CommunityTop5Response> getTop5UsersByTotalLikes();


    public void deleteCommunity(Long id, Long userId);
    public CommunityResponse.CommunityUpdateResponse updateCommunity(Long id, CommunityRequest.CommunityUpdateRequest request, Long userId);
    //public CommunityCommentResponse createComment(Long communityId, Long memberId, CommunityCommentRequest request);
}
