package com.shaunmccready.studygroupjavaapi.repository;

import com.shaunmccready.studygroupjavaapi.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserGroupDao extends JpaRepository<UserGroup, Long> {

    Optional<UserGroup> findByMemberIdAndGroupId(String id, Long id1);

}