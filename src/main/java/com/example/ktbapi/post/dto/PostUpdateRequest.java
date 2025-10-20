package com.example.ktbapi.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 수정 요청")
public class PostUpdateRequest {
    @NotBlank(message = "title is required")
    @Size(max = 200, message = "title must be <= 200 chars")
    @Schema(description = "제목", example = "수정된 제목")
    public String title;

    @NotBlank(message = "content is required")
    @Schema(description = "내용", example = "수정된 본문")
    public String content;

    @Schema(description = "이미지 URL", example = "https://example.com/updated.png")
    public String image_url;
}
