package com.qp.qpassessment.service;

import com.qp.qpassessment.model.UserRoleUpdateRequestDto;
import com.qp.qpassessment.model.UsersDto;
import com.qp.qpassessment.utils.GenericResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UsersService {

    GenericResponse<UsersDto> createUser(UsersDto user);

    GenericResponse<UsersDto> updateUser(UsersDto user);

    GenericResponse<UsersDto> updateUserRole(UserRoleUpdateRequestDto userRoleUpdateRequestDto);

    GenericResponse<UsersDto> getUserByEmailAndPassword(String email, String password);

    GenericResponse<UsersDto> getUserByEmail(String email);

    GenericResponse<UsersDto> deleteUser(UUID id);
}
