package com.example.ktbapi.post.service;

import com.example.ktbapi.post.dto.LikeCountResponse;
import com.example.ktbapi.post.dto.LikeStatusResponse;

public interface LikeService {

    LikeStatusResponse like(Long userId, Long postId);

    LikeStatusResponse unlike(Long userId, Long postId);

    LikeStatusResponse status(Long userId, Long postId);

    LikeCountResponse count(Long postId);
}