package com.shaunmccready.studygroupjavaapi.repository;

import com.shaunmccready.studygroupjavaapi.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserGroupDao extends JpaRepository<UserGroup, Long> {

    Optional<UserGroup> findByMemberIdAndGroupId(String id, Long id1);

    List<UserGroup> findByGroupId(Long groupId);

    @Modifying
    @Query(value = "DELETE FROM UserGroup ug where ug.groupId = :groupId")
    void deleteAllWithGroupId(@Param("groupId") Long groupId);
}