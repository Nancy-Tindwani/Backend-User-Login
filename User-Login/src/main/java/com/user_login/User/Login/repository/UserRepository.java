package com.user_login.User.Login.repository;

import com.user_login.User.Login.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String Email);

    Boolean existsByEmail(String email);
}
