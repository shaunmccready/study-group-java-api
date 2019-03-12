package com.shaunmccready.studygroupjavaapi.repository;

import com.shaunmccready.studygroupjavaapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, String> {
}
