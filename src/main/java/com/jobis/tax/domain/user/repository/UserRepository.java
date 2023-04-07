package com.jobis.tax.domain.user.repository;

import com.jobis.tax.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);

//    Page<User> findByNameContainingOrUserIdContaining(String name, String userId, Pageable pageable);
//
//    Page<User> findByUserIdContaining(String userId, Pageable pageable);
//
//    Page<User> findByNameContaining(String name, Pageable pageable);
}
