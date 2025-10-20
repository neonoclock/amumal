package com.example.ktbapi.post.service;

import com.example.ktbapi.common.exception.AlreadyLikedException;
import com.example.ktbapi.common.exception.NotLikedException;
import com.example.ktbapi.common.exception.PostNotFoundException;
import com.example.ktbapi.common.exception.UnauthorizedException;
import com.example.ktbapi.post.dto.LikeCountResponse;
import com.example.ktbapi.post.dto.LikeStatusResponse;
import com.example.ktbapi.post.model.Post;
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

    // 좋아요 추가
    public LikeCountResponse add(Long userId, Long postId) {
        if (userId == null) throw new UnauthorizedException();

        Post post = postRepo.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        boolean added = likeRepo.addLike(userId, postId);
        if (!added) throw new AlreadyLikedException();

        Post updated = new Post(
                post.getId(), post.getTitle(), post.getContent(), post.getAuthor(),
                post.getImageUrl(), post.getCreatedAt(), post.getViews(), post.getLikes() + 1
        );
        postRepo.save(updated);

        long count = likeRepo.countLikes(postId);
        return new LikeCountResponse(postId, count);
    }

    // 좋아요 취소
    public LikeCountResponse remove(Long userId, Long postId) {
        if (userId == null) throw new UnauthorizedException();

        Post post = postRepo.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        boolean removed = likeRepo.removeLike(userId, postId);
        if (!removed) throw new NotLikedException();

        Post updated = new Post(
                post.getId(), post.getTitle(), post.getContent(), post.getAuthor(),
                post.getImageUrl(), post.getCreatedAt(), post.getViews(), post.getLikes() - 1
        );
        postRepo.save(updated);

        long count = likeRepo.countLikes(postId);
        return new LikeCountResponse(postId, count);
    }

    // 좋아요 조회
    public LikeStatusResponse status(Long postId, Long userId) {
        postRepo.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        long count = likeRepo.countLikes(postId);
        boolean liked = (userId != null) && likeRepo.hasLiked(userId, postId);
        return new LikeStatusResponse(postId, count, liked);
    }
}
