package com.derplicity.blog.api.user.mappers;

import com.derplicity.blog.api.user.domain.User;
import com.derplicity.blog.api.user.domain.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User userDtoToUser(UserDto userDto);
}
