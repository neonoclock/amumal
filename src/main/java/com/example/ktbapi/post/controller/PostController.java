package com.example.ktbapi.post.controller;

import com.example.ktbapi.common.ApiResponse;
import com.example.ktbapi.common.Msg;
import com.example.ktbapi.post.dto.PostCreateRequest;
import com.example.ktbapi.post.dto.PostDetailResponse;
import com.example.ktbapi.post.dto.PostSummaryResponse;
import com.example.ktbapi.post.dto.PostUpdateRequest;
import com.example.ktbapi.post.dto.PostUpdatedResponse;
import com.example.ktbapi.post.service.PostService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ApiResponse<?> list(
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
                    description = "정렬 기준: `date`(최신순), `likes`(좋아요순), `views`(조회수순)",
                    in = ParameterIn.QUERY,
                    schema = @Schema(defaultValue = "date", allowableValues = {"date", "likes", "views"})
            )
            @RequestParam(defaultValue = "date") String sort
    ) {
        List<PostSummaryResponse> data = postService.getAllPosts(page, limit, sort);
        return ApiResponse.ok(Msg.Success.POSTS_FETCH, data);
    }


    @GetMapping("/{id}")
    public ApiResponse<?> get(@PathVariable Long id) {
        PostDetailResponse data = postService.getPostById(id);
        return ApiResponse.ok(Msg.Success.POST_FETCH, data);
    }

    @PostMapping
    public ApiResponse<?> create(@RequestHeader(value = "X-USER-ID", required = false) Long userId,
                                 @RequestHeader(value = "X-USER-NAME", required = false) String authorName,
                                 @Valid @RequestBody PostCreateRequest req) {
        PostDetailResponse data = postService.createPost(userId, authorName, req);
        return ApiResponse.ok(Msg.Success.POST_CREATE, data);
    }

    @PutMapping("/{id}")
    public ApiResponse<?> update(@RequestHeader(value = "X-USER-ID", required = false) Long userId,
                                 @PathVariable Long id,
                                 @Valid @RequestBody PostUpdateRequest req) {
        PostUpdatedResponse data = postService.updatePost(userId, id, req);
        return ApiResponse.ok(Msg.Success.POST_UPDATE, data);
    }
}
