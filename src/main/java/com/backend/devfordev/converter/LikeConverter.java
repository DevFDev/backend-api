package com.backend.devfordev.converter;

import com.backend.devfordev.domain.Heart;
import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.dto.LikeDto.LikeRequest;
import com.backend.devfordev.dto.LikeDto.LikeResponse;


public class LikeConverter {

    public static Heart toLike(LikeRequest request, Member member) {
        return Heart.builder()
                .member(member)  // 로그인한 유저 정보
                .likeId(request.getLikeId())
                .likeType(request.getLikeType())  // LikeType 설정
                .build();
    }

    public static LikeResponse toLikeResponse(Heart heart, Long likes, Long memberId) {
        if (heart == null) {
            // 좋아요 취소 상태 (-1)
            return LikeResponse.MinusLikeResponse.builder()
                    .memberId(memberId)
                    .likes(likes)
                    .likeStatus("-")  // 취소 상태
                    .build();
        }

        // 좋아요 추가 상태 (+1)
        return LikeResponse.PlusLikeResponse.builder()
                .memberId(memberId)
                .likeId(heart.getLikeId())
                .likeType(heart.getLikeType())
                .likes(likes)
                .likeStatus("+")  // 추가 상태
                .build();
    }
}