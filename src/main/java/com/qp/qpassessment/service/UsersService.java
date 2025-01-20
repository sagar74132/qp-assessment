package com.qp.qpassessment.service;

import com.qp.qpassessment.model.UserRoleUpdateRequestDto;
import com.qp.qpassessment.model.UsersDetailsUpdateRequest;
import com.qp.qpassessment.model.UsersRequestDto;
import com.qp.qpassessment.model.UsersResponseDto;
import com.qp.qpassessment.utils.GenericResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UsersService {

    GenericResponse<UsersResponseDto> createUser(UsersRequestDto user);

    GenericResponse<UsersResponseDto> updateUser(UsersDetailsUpdateRequest user);

    GenericResponse<UsersResponseDto> updateUserRole(UserRoleUpdateRequestDto userRoleUpdateRequestDto);

    GenericResponse<UsersResponseDto> getUserByEmailAndPassword(String email, String password);

    GenericResponse<UsersResponseDto> getUserByEmail(String email);

    GenericResponse<UsersResponseDto> deleteUser(UUID id);
}
