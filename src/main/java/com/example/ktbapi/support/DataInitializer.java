package com.example.ktbapi.support;

import com.example.ktbapi.user.model.User;
import com.example.ktbapi.user.repo.UserRepository;
import com.example.ktbapi.post.model.Post;
import com.example.ktbapi.post.repo.PostRepository;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {
  private final UserRepository userRepo;
  private final PostRepository postRepo;

  public DataInitializer(UserRepository userRepo, PostRepository postRepo){
    this.userRepo = userRepo;
    this.postRepo = postRepo;
  }

  @PostConstruct
  public void init(){
    if(userRepo.isEmpty()){
      userRepo.save(new User(null, "user@example.com", "Aa123456!", "neon",
              "https://image.example/profile.jpg"));
      userRepo.save(new User(null, "dev@example.com", "Pass1234!", "dev",
              "https://image.example/dev.jpg"));
    }

    if(postRepo.isEmpty()){
      postRepo.save(new Post(null, "첫 번째 게시글", "내용입니다", "neon",
              "https://image.example/post1.jpg", "2025-10-01 10:00:00", 123, 10));
      postRepo.save(new Post(null, "두 번째 게시글", "두 번째 내용", "dev",
              "https://image.example/post2.jpg", "2025-10-02 09:30:00", 345, 20));
      postRepo.save(new Post(null, "세 번째 게시글", "세 번째 내용", "neon",
              "https://image.example/post3.jpg", "2025-10-03 08:15:00", 678, 30));
    }
  }
}
