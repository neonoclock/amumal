package com.example.ktbapi.post.repo;

import com.example.ktbapi.common.TimeUtil;
import com.example.ktbapi.post.model.LikeRecord;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class LikeRepository {

    private final Set<LikeRecord> likes = new HashSet<>();

    // 좋아요 추가: 이미 존재하면 false
    public synchronized boolean addLike(Long userId, Long postId) {
        return likes.add(new LikeRecord(userId, postId, TimeUtil.nowText()));
    }

    // 좋아요 제거: 없으면 false
    public synchronized boolean removeLike(Long userId, Long postId) {
        return likes.remove(LikeRecord.keyOf(userId, postId));
    }

    //특정 사용자가 특정 게시글을 좋아했는지 여부
    public synchronized boolean hasLiked(Long userId, Long postId) {
        return likes.contains(LikeRecord.keyOf(userId, postId));
    }

    //특정 게시글의 좋아요 수
    public synchronized long countLikes(Long postId) {
        return likes.stream().filter(lr -> lr.getPostId().equals(postId)).count();
    }
}

