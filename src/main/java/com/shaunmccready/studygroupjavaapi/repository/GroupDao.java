package com.shaunmccready.studygroupjavaapi.repository;

import com.shaunmccready.studygroupjavaapi.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupDao extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String groupName);

    @Query(value = "SELECT g from Group g JOIN g.userGroups ug where ug.approved = true AND ug.memberId = :userId")
    List<Group> findGroupsByUserId(@Param("userId")String userId);
}
