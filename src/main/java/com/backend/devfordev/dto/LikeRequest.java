package com.backend.devfordev.dto;

import com.backend.devfordev.domain.enums.LikeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LikeRequest {


    @Schema(description = "좋아요하는 게시글의 id", example = "1")
    private Long likeId;

    @Schema(description = "타입", example = "COMMUNITY")
    private String likeType;

}