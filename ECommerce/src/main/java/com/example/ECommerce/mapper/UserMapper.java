package com.example.ECommerce.mapper;

import com.example.ECommerce.dto.request.UserRequestDTO;
import com.example.ECommerce.dto.response.UserResponseDTO;
import com.example.ECommerce.model.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    User userRequestDtoToUser(UserRequestDTO userRequestDTO);
    UserResponseDTO userToUserResponseDTO(User user);
    void updateUserFromUserRequestDto(UserRequestDTO userRequestDTO, @MappingTarget User user);
}

