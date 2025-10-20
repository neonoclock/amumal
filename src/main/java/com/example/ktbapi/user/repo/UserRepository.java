package com.example.ktbapi.user.repo;

import com.example.ktbapi.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {
  private final Map<Long, User> store = new LinkedHashMap<>();
  private long seq = 0L;

  public synchronized User save(User u){
    if (u.getId() == null) {
      u = new User(++seq, u.getEmail(), u.getPassword(), u.getNickname(), u.getCreatedAt());
    }
    store.put(u.getId(), u);
    return u;
  }

  public Optional<User> findByEmail(String email){
    return store.values().stream()
        .filter(u -> u.getEmail().equalsIgnoreCase(email))
        .findFirst();
  }

  // ID로 사용자 찾기
  public Optional<User> findById(Long id){
    return Optional.ofNullable(store.get(id));
  }

  // 저장소가 비어있는지 확인
  public boolean isEmpty(){ return store.isEmpty(); }

  // 이메일 또는 닉네임 중복 확인
  public boolean existsByEmailOrNickname(String email, String nickname){
    return store.values().stream().anyMatch(u ->
        u.getEmail().equalsIgnoreCase(email) || u.getNickname().equalsIgnoreCase(nickname)
    );
  }

  // 특정 ID를 제외한 닉네임 중복 확인
  public boolean existsByNicknameExceptId(String nickname, Long exceptId){
    return store.values().stream().anyMatch(u ->
        !u.getId().equals(exceptId) && u.getNickname().equalsIgnoreCase(nickname)
    );
  }
}

