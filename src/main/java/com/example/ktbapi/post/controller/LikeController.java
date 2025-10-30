package com.example.ktbapi.post.controller;

import com.example.ktbapi.common.ApiResponse;
import com.example.ktbapi.common.Msg;
import com.example.ktbapi.post.dto.LikeCountResponse;
import com.example.ktbapi.post.dto.LikeStatusResponse;
import com.example.ktbapi.post.service.LikeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    public ApiResponse<LikeStatusResponse> like(@PathVariable Long postId,
                                                @RequestHeader(value = "X-USER-ID", required = false) Long userId) {
        LikeStatusResponse data = likeService.like(userId, postId);
        return ApiResponse.ok(Msg.Success.LIKE_ADD, data);
    }

    @DeleteMapping
    public ApiResponse<LikeStatusResponse> unlike(@PathVariable Long postId,
                                                  @RequestHeader(value = "X-USER-ID", required = false) Long userId) {
        LikeStatusResponse data = likeService.unlike(userId, postId);
        return ApiResponse.ok(Msg.Success.LIKE_REMOVE, data);
    }

    @GetMapping
    public ApiResponse<LikeStatusResponse> status(@PathVariable Long postId,
                                                  @RequestHeader(value = "X-USER-ID", required = false) Long userId) {
        LikeStatusResponse data = likeService.status(userId, postId);
        return ApiResponse.ok(Msg.Success.LIKE_STATUS, data);
    }

    @GetMapping("/count")
    public ApiResponse<LikeCountResponse> count(@PathVariable Long postId) {
        LikeCountResponse data = likeService.count(postId);
        return ApiResponse.ok(Msg.Success.LIKE_COUNT, data);
    }
}
