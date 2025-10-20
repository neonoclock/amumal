package com.example.ktbapi.post.service;

import com.example.ktbapi.common.exception.PostNotFoundException;
import com.example.ktbapi.common.exception.UnauthorizedException;
import com.example.ktbapi.post.dto.LikeCountResponse;
import com.example.ktbapi.post.dto.LikeStatusResponse;
import com.example.ktbapi.post.repo.LikeRepository;
import com.example.ktbapi.post.repo.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private final PostRepository postRepo;
    private final LikeRepository likeRepo;

    public LikeService(PostRepository postRepo, LikeRepository likeRepo) {
        this.postRepo = postRepo;
        this.likeRepo = likeRepo;
    }

    public LikeCountResponse add(Long userId, Long postId) {
        if (userId == null) throw new UnauthorizedException();

        postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        boolean added = likeRepo.addLike(userId, postId);
        if (!added) throw new IllegalStateException("already_liked");

        long count = likeRepo.countLikes(postId);
        return new LikeCountResponse(postId, count);
    }

    public LikeCountResponse remove(Long userId, Long postId) {
        if (userId == null) throw new UnauthorizedException();

        postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        boolean removed = likeRepo.removeLike(userId, postId);
        if (!removed) throw new IllegalStateException("not_liked");

        long count = likeRepo.countLikes(postId);
        return new LikeCountResponse(postId, count);
    }

    public LikeStatusResponse status(Long postId, Long userId) {
        postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        long count = likeRepo.countLikes(postId);
        boolean liked = (userId != null) && likeRepo.hasLiked(userId, postId);
        return new LikeStatusResponse(postId, count, liked);
    }
}
