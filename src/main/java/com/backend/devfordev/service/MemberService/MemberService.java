package com.backend.devfordev.service.MemberService;


import com.backend.devfordev.dto.MemberDto.*;

public interface MemberService {
    public SignUpResponse signUp(SignUpRequest request);
    public SignInResponse signIn(SignInRequest request);

    public MemberResponse.MemberInfoResponse getMember(Long userId);
    public boolean checkEmailDuplicate(String email);
    public String refreshAccessToken(String refreshToken, String oldAccessToken);
}
