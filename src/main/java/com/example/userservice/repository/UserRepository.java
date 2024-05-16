package com.example.userservice.repository;

import com.example.userservice.model.ForumUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<ForumUser, String> {
    Optional<ForumUser> findUserByEmail(String email);
    Optional<ForumUser> findById(String userId);


}
