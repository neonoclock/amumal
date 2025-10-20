package com.example.ktbapi.post.service;

import com.example.ktbapi.common.TimeUtil;
import com.example.ktbapi.common.exception.CommentNotFoundException;
import com.example.ktbapi.common.exception.PostNotFoundException;
import com.example.ktbapi.common.exception.UnauthorizedException;
import com.example.ktbapi.post.dto.CommentCreateOrUpdateRequest;
import com.example.ktbapi.post.dto.CommentResponse;
import com.example.ktbapi.post.dto.CommentUpdatedResponse;
import com.example.ktbapi.post.model.Comment;
import com.example.ktbapi.post.repo.CommentRepository;
import com.example.ktbapi.post.repo.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final PostRepository postRepo;
    private final CommentRepository commentRepo;

    public CommentService(PostRepository postRepo, CommentRepository commentRepo) {
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
    }

    public CommentResponse create(Long userId, Long postId, String authorName, CommentCreateOrUpdateRequest req) {
        if (userId == null) throw new UnauthorizedException();

        postRepo.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        String now = TimeUtil.nowText();
        Comment saved = commentRepo.addComment(postId, new Comment(null, authorName, req.content, now));
        return new CommentResponse(saved.getId(), postId, saved.getAuthor(), saved.getContent(), saved.getCreatedAt());
    }

    public CommentUpdatedResponse update(Long userId, Long postId, Long commentId, CommentCreateOrUpdateRequest req) {
        if (userId == null) throw new UnauthorizedException();

        postRepo.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        Comment c = commentRepo.findComment(postId, commentId).orElseThrow(() -> new CommentNotFoundException(commentId));

        c.updateContent(req.content);
        return new CommentUpdatedResponse(c.getId(), TimeUtil.nowText());
    }

    public void delete(Long userId, Long postId, Long commentId) {
        if (userId == null) throw new UnauthorizedException();

        postRepo.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        boolean ok = commentRepo.deleteComment(postId, commentId);
        if (!ok) throw new CommentNotFoundException(commentId);
    }
}
