package com.example.ktbapi.post.controller;

import com.example.ktbapi.common.ApiResponse;
import com.example.ktbapi.common.Msg;
import com.example.ktbapi.post.dto.PostCreateRequest;
import com.example.ktbapi.post.dto.PostDetailResponse;
import com.example.ktbapi.post.dto.PostSummaryResponse;
import com.example.ktbapi.post.dto.PostUpdateRequest;
import com.example.ktbapi.post.dto.PostUpdatedResponse;
import com.example.ktbapi.post.service.PostService;
import com.example.ktbapi.post.api.PostSortKey;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ApiResponse<List<PostSummaryResponse>> list(
            @Parameter(
                    name = "page",
                    description = "페이지 번호 (기본값: 1)",
                    in = ParameterIn.QUERY,
                    schema = @Schema(defaultValue = "1")
            )
            @RequestParam(defaultValue = "1") int page,

            @Parameter(
                    name = "limit",
                    description = "한 페이지당 게시글 수 (기본값: 10)",
                    in = ParameterIn.QUERY,
                    schema = @Schema(defaultValue = "10")
            )
            @RequestParam(defaultValue = "10") int limit,

            @Parameter(
                name = "sort",
                description = "정렬 기준 (DATE, LIKES, VIEWS)",
                in = ParameterIn.QUERY,
                schema = @Schema(implementation = PostSortKey.class, defaultValue = "DATE")
            )
            @RequestParam(defaultValue = "DATE") PostSortKey sort
    ) {
        List<PostSummaryResponse> data = postService.getAllPosts(page, limit, sort);
        return ApiResponse.ok(Msg.Success.POSTS_FETCH, data);
    }

    @GetMapping("/{id}")
    public ApiResponse<PostDetailResponse> get(@PathVariable Long id) {
        PostDetailResponse data = postService.getPostById(id);
        return ApiResponse.ok(Msg.Success.POST_FETCH, data);
    }

    @PostMapping
    public ApiResponse<PostDetailResponse> create(
            @RequestHeader(value = "X-USER-ID", required = false) Long userId,
            @RequestHeader(value = "X-USER-NAME", required = false) String authorName,
            @Valid @RequestBody PostCreateRequest req
    ) {
        PostDetailResponse data = postService.createPost(userId, authorName, req);
        return ApiResponse.ok(Msg.Success.POST_CREATE, data);
    }

    @PutMapping("/{id}")
    public ApiResponse<PostUpdatedResponse> update(
            @RequestHeader(value = "X-USER-ID", required = false) Long userId,
            @PathVariable Long id,
            @Valid @RequestBody PostUpdateRequest req
    ) {
        PostUpdatedResponse data = postService.updatePost(userId, id, req);
        return ApiResponse.ok(Msg.Success.POST_UPDATE, data);
    }
}
