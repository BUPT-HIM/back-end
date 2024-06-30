package com.xgc.himsystem.repository;

import com.xgc.himsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByContact(String contact);
}
