package com.example.ktbapi.post.service;

import com.example.ktbapi.common.TimeUtil;
import com.example.ktbapi.common.exception.PostNotFoundException;
import com.example.ktbapi.common.exception.UnauthorizedException;
import com.example.ktbapi.post.dto.PostCreateRequest;
import com.example.ktbapi.post.dto.PostDetailResponse;
import com.example.ktbapi.post.dto.PostSummaryResponse;
import com.example.ktbapi.post.dto.PostUpdateRequest;
import com.example.ktbapi.post.dto.PostUpdatedResponse;
import com.example.ktbapi.post.mapper.PostMapper;
import com.example.ktbapi.post.model.Post;
import com.example.ktbapi.post.repo.CommentRepository;
import com.example.ktbapi.post.repo.PostRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepo;
    private final CommentRepository commentRepo;

    public PostService(PostRepository postRepo, CommentRepository commentRepo) {
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
    }

    public List<PostSummaryResponse> getAllPosts(int page, int limit, String sort) {
        List<Post> posts = postRepo.findAll();
        if ("date".equalsIgnoreCase(sort)) Collections.reverse(posts);

        int start = Math.max(0, (page - 1) * limit);
        int end = Math.min(start + limit, posts.size());
        List<Post> paged = posts.subList(start, end);

        return paged.stream()
                .map(PostMapper::toSummary)
                .toList();
    }


    public PostDetailResponse getPostById(Long id) {
        Post post = postRepo.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        Post updated = new Post(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getImageUrl(),
                post.getCreatedAt(),
                post.getViews() + 1,
                post.getLikes()
        );
        postRepo.save(updated);

        return PostMapper.toDetail(updated, commentRepo.findAllByPostId(id));
    }

    public PostDetailResponse createPost(Long userId, String authorName, PostCreateRequest req) {
        if (userId == null) throw new UnauthorizedException();

        String now = TimeUtil.nowText();
        Post saved = postRepo.save(new Post(
                null,
                req.title,
                req.content,
                authorName,
                req.image_url,
                now,
                0,
                0
        ));
        return PostMapper.toDetail(saved, List.of());
    }

    public PostUpdatedResponse updatePost(Long userId, Long postId, PostUpdateRequest req) {
        if (userId == null) throw new UnauthorizedException();

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        Post patched = new Post(
                post.getId(),
                req.title,
                req.content,
                post.getAuthor(),
                (req.image_url != null ? req.image_url : post.getImageUrl()),
                post.getCreatedAt(),
                post.getViews(),
                post.getLikes()
        );
        postRepo.save(patched);

        return new PostUpdatedResponse(
                patched.getId(),
                patched.getTitle(),
                patched.getContent(),
                patched.getImageUrl(),
                TimeUtil.nowText()
        );
    }
}
