package com.example.ktbapi.post.repo;

import com.example.ktbapi.post.model.Post;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class PostRepository {
  private final Map<Long, Post> store = new LinkedHashMap<>();
  private long seq = 0L;

  public synchronized Post save(Post post) {
    if (post.getId() == null) {
      post = new Post(++seq, post.getTitle(), post.getContent(), post.getAuthor(),
              post.getImageUrl(), post.getCreatedAt(), post.getViews(), post.getLikes());
    }
    store.put(post.getId(), post);
    return post;
  }

  public List<Post> findAll() {
    return new ArrayList<>(store.values());
  }

  public Optional<Post> findById(Long id) {
    return Optional.ofNullable(store.get(id));
  }

  public boolean isEmpty() {
    return store.isEmpty();
  }
}
