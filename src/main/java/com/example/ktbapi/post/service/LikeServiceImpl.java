package com.example.ktbapi.post.service;

import com.example.ktbapi.common.exception.AlreadyLikedException;
import com.example.ktbapi.common.exception.NotLikedException;
import com.example.ktbapi.common.exception.PostNotFoundException;
import com.example.ktbapi.common.exception.UnauthorizedException;
import com.example.ktbapi.post.dto.LikeCountResponse;
import com.example.ktbapi.post.dto.LikeStatusResponse;
import com.example.ktbapi.post.repo.LikeRepository;
import com.example.ktbapi.post.repo.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    private final PostRepository postRepo;
    private final LikeRepository likeRepo;

    public LikeServiceImpl(PostRepository postRepo, LikeRepository likeRepo) {
        this.postRepo = postRepo;
        this.likeRepo = likeRepo;
    }

    @Override
    public LikeStatusResponse like(Long userId, Long postId) {
        if (userId == null) throw new UnauthorizedException();
        postRepo.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        if (likeRepo.hasLiked(userId, postId)) throw new AlreadyLikedException();

        likeRepo.addLike(userId, postId);
        long count = likeRepo.countLikes(postId);

        return new LikeStatusResponse(postId, count, true);
    }

    @Override
    public LikeStatusResponse unlike(Long userId, Long postId) {
        if (userId == null) throw new UnauthorizedException();
        postRepo.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        if (!likeRepo.hasLiked(userId, postId)) throw new NotLikedException();

        likeRepo.removeLike(userId, postId);
        long count = likeRepo.countLikes(postId);
        return new LikeStatusResponse(postId, count, false);
    }

    @Override
    public LikeStatusResponse status(Long userId, Long postId) {
        postRepo.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        boolean liked = (userId != null) && likeRepo.hasLiked(userId, postId);
        long count = likeRepo.countLikes(postId);
        return new LikeStatusResponse(postId, count, liked);
    }

    @Override
    public LikeCountResponse count(Long postId) {
        postRepo.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        long count = likeRepo.countLikes(postId);
        return new LikeCountResponse(postId, count);
    }
}
