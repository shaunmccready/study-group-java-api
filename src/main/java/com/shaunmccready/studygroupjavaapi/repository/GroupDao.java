package com.shaunmccready.studygroupjavaapi.repository;

import com.shaunmccready.studygroupjavaapi.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupDao extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String groupName);
}
