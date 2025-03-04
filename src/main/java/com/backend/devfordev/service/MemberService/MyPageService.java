package com.backend.devfordev.service.MemberService;

import com.backend.devfordev.dto.MemberDto.MyPageInfoRequest;
import com.backend.devfordev.dto.MemberDto.MyPageInfoResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MyPageService {
    public MyPageInfoResponse.ProfileResponse getProfile(Long userId);
    public MyPageInfoResponse.ProfileUpdateResponse updateProfile(Long memberId, MyPageInfoRequest.ProfileUpdateRequest request, MultipartFile profileImage);
    public void updatePassword(Long memberId, MyPageInfoRequest.PasswordUpdateRequest passwordUpdateRequest);
    public boolean checkNicknameDuplicate(String nickname);
}
