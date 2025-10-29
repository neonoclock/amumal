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

  public synchronized Optional<User> findByEmail(String email){
    return store.values().stream()
        .filter(u -> u.getEmail().equalsIgnoreCase(email))
        .findFirst();
  }

  public synchronized Optional<User> findById(Long id){
    return Optional.ofNullable(store.get(id));
  }

  public synchronized boolean isEmpty(){ return store.isEmpty(); }

  public synchronized boolean existsByEmailOrNickname(String email, String nickname){
    return store.values().stream().anyMatch(u ->
        u.getEmail().equalsIgnoreCase(email) || u.getNickname().equalsIgnoreCase(nickname)
    );
  }

  public synchronized boolean existsByNicknameExceptId(String nickname, Long exceptId){
    return store.values().stream().anyMatch(u ->
        !u.getId().equals(exceptId) && u.getNickname().equalsIgnoreCase(nickname)
    );
  }

  public synchronized void deleteById(Long userId) {
    store.remove(userId);
  }
}
