package com.example.backend.repository;

import com.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndUserIdNot(String username, Long userId);

    @Query("SELECT u FROM User u WHERE u.delFlag = '0'")
    List<User> findAllActive();

    @Query("SELECT u FROM User u WHERE u.delFlag = '0'")
    Page<User> findAllActive(Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE u.delFlag = :delFlag")
    long countByDelFlag(String delFlag);

    @Query("SELECT u FROM User u WHERE u.delFlag = '0' AND u.username LIKE %:keyword%")
    Page<User> findByUsernameContaining(String keyword, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.delFlag = '0' AND u.email LIKE %:keyword%")
    Page<User> findByEmailContaining(String keyword, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.delFlag = '0' AND (u.username LIKE %:username% OR u.email LIKE %:email%)")
    Page<User> findByUsernameOrEmail(String username, String email, Pageable pageable);
}