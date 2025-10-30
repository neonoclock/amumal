package com.example.ktbapi.post.service;

import com.example.ktbapi.common.TimeUtil;
import com.example.ktbapi.common.exception.PostNotFoundException;
import com.example.ktbapi.post.api.PostSortKey;
import com.example.ktbapi.post.dto.*;
import com.example.ktbapi.post.mapper.PostMapper;
import com.example.ktbapi.post.model.Post;
import com.example.ktbapi.post.repo.CommentRepository;
import com.example.ktbapi.post.repo.PostRepository;
import org.springframework.stereotype.Service;
import com.example.ktbapi.common.auth.RequireUserId;


import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepo;
    private final CommentRepository commentRepo;

    public PostServiceImpl(PostRepository postRepo, CommentRepository commentRepo) {
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
    }

    @Override
    public List<PostSummaryResponse> getAllPosts(int page, int limit, PostSortKey sort) {
        List<Post> posts = postRepo.findAll();

        posts.sort(PostSort.resolve(sort));

        int start = Math.max(0, (page - 1) * limit);
        int end = Math.min(start + limit, posts.size());
        List<Post> paged = posts.subList(start, end);

        return paged.stream().map(PostMapper::toSummary).toList();
    }

    @Override
    public PostDetailResponse getPostById(Long id) {
        Post post = postRepo.increaseViewsById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        return PostMapper.toDetail(post, commentRepo.findAllByPostId(id));
    }

    @RequireUserId(paramIndex = 0)
    @Override
    public PostDetailResponse createPost(Long userId, String authorName, PostCreateRequest req) {
        String now = TimeUtil.nowText();
        Post saved = postRepo.save(new Post(null, req.title, req.content, authorName, req.image_url, now, 0, 0));
        return PostMapper.toDetail(saved, List.of());
    }

    @RequireUserId(paramIndex = 0)
    @Override
    public PostUpdatedResponse updatePost(Long userId, Long postId, PostUpdateRequest req) {
        Post post = postRepo.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        post.updateDetails(req.title, req.content, req.image_url);
        postRepo.save(post);
        return new PostUpdatedResponse(post.getId(), post.getTitle(), post.getContent(), post.getImageUrl(), TimeUtil.nowText());
    }
}

