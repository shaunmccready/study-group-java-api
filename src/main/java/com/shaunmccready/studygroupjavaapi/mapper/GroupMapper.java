package com.shaunmccready.studygroupjavaapi.mapper;

import com.shaunmccready.studygroupjavaapi.domain.Group;
import com.shaunmccready.studygroupjavaapi.dto.GroupDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mapping(target = "members", ignore = true)
    GroupDTO groupToDto(Group group);
}
