package com.backend.devfordev.dto.LikeDto;

import com.backend.devfordev.domain.enums.LikeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class LikeResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlusLikeResponse extends LikeResponse{
        @Schema(description = "로그인한 유저 id", example = "1")
        private Long memberId;

        @Schema(description = "좋아요하는 게시글의 id", example = "1")
        private Long likeId;

        @Schema(description = "타입", example = "COMMUNITY")
        private LikeType likeType;

        @Schema(description = "전체 좋아요 수", example = "10")
        private Long likes;

        @Schema(description = "좋아요 상태 (+: 추가, -: 취소)", example = "+")
        private String likeStatus;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MinusLikeResponse extends LikeResponse {
        @Schema(description = "로그인한 유저 id", example = "1")
        private Long memberId;

        @Schema(description = "전체 좋아요 수", example = "10")
        private Long likes;

        @Schema(description = "좋아요 상태 (+: 추가, -: 취소)", example = "-")
        private String likeStatus;
    }
}
