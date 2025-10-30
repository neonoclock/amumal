package com.example.ktbapi.post.service;

import com.example.ktbapi.common.TimeUtil;
import com.example.ktbapi.common.exception.CommentNotFoundException;
import com.example.ktbapi.common.exception.PostNotFoundException;
import com.example.ktbapi.post.dto.CommentCreateOrUpdateRequest;
import com.example.ktbapi.post.dto.CommentResponse;
import com.example.ktbapi.post.dto.CommentUpdatedResponse;
import com.example.ktbapi.post.model.Comment;
import com.example.ktbapi.post.repo.CommentRepository;
import com.example.ktbapi.post.repo.PostRepository;
import org.springframework.stereotype.Service;
import com.example.ktbapi.common.auth.RequireUserId;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepo;
    private final CommentRepository commentRepo;

    public CommentServiceImpl(PostRepository postRepo, CommentRepository commentRepo) {
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
    }

    @Override
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        postRepo.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        return commentRepo.findAllByPostId(postId).stream()
                .map(c -> new CommentResponse(c.getId(), postId, c.getAuthor(), c.getContent(), c.getCreatedAt()))
                .toList();
    }

    @RequireUserId(paramIndex = 0)
    @Override
    public CommentResponse createComment(Long userId, Long postId, String authorName, CommentCreateOrUpdateRequest req) {
        postRepo.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        var saved = commentRepo.addComment(postId, new Comment(null, authorName, req.content, TimeUtil.nowText()));
        return new CommentResponse(saved.getId(), postId, saved.getAuthor(), saved.getContent(), saved.getCreatedAt());
    }

    @RequireUserId(paramIndex = 0)
    @Override
    public CommentUpdatedResponse updateComment(Long userId, Long postId, Long commentId, CommentCreateOrUpdateRequest req) {
        postRepo.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        var c = commentRepo.findComment(postId, commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
        c.updateContent(req.content);
        return new CommentUpdatedResponse(c.getId(), TimeUtil.nowText());
    }

    @RequireUserId(paramIndex = 0)
    @Override
    public void deleteComment(Long userId, Long postId, Long commentId) {
        postRepo.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        boolean ok = commentRepo.deleteComment(postId, commentId);
        if (!ok) throw new CommentNotFoundException(commentId);
    }
}
