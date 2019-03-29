package com.shaunmccready.studygroupjavaapi.mapper;

import com.shaunmccready.studygroupjavaapi.domain.User;
import com.shaunmccready.studygroupjavaapi.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "groups", ignore = true)
    UserDTO userToDto(User user);

    @Mapping(target = "groups", ignore = true)
    User dtoToUser(UserDTO userDTO);

}
