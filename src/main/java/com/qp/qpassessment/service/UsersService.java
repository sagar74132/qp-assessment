package com.qp.qpassessment.service;

import com.qp.qpassessment.model.UserRoleUpdate;
import com.qp.qpassessment.model.UsersModel;
import com.qp.qpassessment.utils.GenericResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UsersService {

    GenericResponse<UsersModel> createUser(UsersModel user);

    GenericResponse<UsersModel> updateUser(UsersModel user);

    GenericResponse<UsersModel> updateUserRole(UserRoleUpdate userRoleUpdate);

    GenericResponse<UsersModel> getUserByEmailAndPassword(String email, String password);

    GenericResponse<UsersModel> getUserByEmail(String email);

    GenericResponse<UsersModel> deleteUser(UUID id);
}
