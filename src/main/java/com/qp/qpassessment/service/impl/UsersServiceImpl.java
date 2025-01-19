package com.qp.qpassessment.service.impl;

import com.qp.qpassessment.constant.Enums;
import com.qp.qpassessment.entity.Users;
import com.qp.qpassessment.exception.UserAlreadyExistException;
import com.qp.qpassessment.mapper.UsersMapper;
import com.qp.qpassessment.model.UserRoleUpdate;
import com.qp.qpassessment.model.UsersModel;
import com.qp.qpassessment.repository.UsersRepository;
import com.qp.qpassessment.service.UsersService;
import com.qp.qpassessment.utils.AppConfig;
import com.qp.qpassessment.utils.GenericResponse;
import com.qp.qpassessment.utils.MD5Util;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final AppConfig appConfig;

    public UsersServiceImpl(final UsersRepository usersRepository,
                            final AppConfig appConfig) {
        this.usersRepository = usersRepository;
        this.appConfig = appConfig;
    }

    @Override
    @Transactional
    public GenericResponse<UsersModel> createUser(UsersModel user) {

        if (null != user.getId()) {
            throw new UserAlreadyExistException(appConfig.getProperty("user.id.not.allowed"));
        }

        Optional<Users> existingUser = usersRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistException(appConfig.getProperty("user.already.exist"));
        } else if (null != user.getRole()) {
            throw new UserAlreadyExistException(appConfig.getProperty("user.role.not.allowed"));
        }

        String hashedPassword = hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        user.setRole(Enums.Role.USER);

        Users createdUser = createOrUpdateUser(UsersMapper.modelToEntity(user));

        return GenericResponse.<UsersModel>builder()
                .message(appConfig.getProperty("user.created"))
                .data(UsersMapper.entityToModel(createdUser))
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public GenericResponse<UsersModel> updateUser(UsersModel user) {
        Optional<Users> existingUser = usersRepository.findById(user.getId());
        if (existingUser.isEmpty()) {
            throw new UserAlreadyExistException(appConfig.getProperty("user.not.found"));
        } else if (null != user.getRole()) {
            throw new UserAlreadyExistException(appConfig.getProperty("user.role.not.allowed"));
        }

        String hashedPassword = hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        Users createdUser = createOrUpdateUser(UsersMapper.modelToEntity(user));

        return GenericResponse.<UsersModel>builder()
                .message(appConfig.getProperty("user.details.modified"))
                .data(UsersMapper.entityToModel(createdUser))
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public GenericResponse<UsersModel> updateUserRole(UserRoleUpdate userRoleUpdate) {
        Optional<Users> existingUser = usersRepository.findById(userRoleUpdate.getId());
        if (existingUser.isEmpty()) {
            throw new UserAlreadyExistException(appConfig.getProperty("user.not.found"));
        }

        existingUser.get().setRole(userRoleUpdate.getRole());
        Users createdUser = createOrUpdateUser(existingUser.get());

        return GenericResponse.<UsersModel>builder()
                .message(appConfig.getProperty("user.created"))
                .data(UsersMapper.entityToModel(createdUser))
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public GenericResponse<UsersModel> getUserByEmailAndPassword(String email, String password) {

        String hashedPassword = hashPassword(password);
        Optional<Users> user = usersRepository.findByEmailAndPassword(email, hashedPassword);

        return buildGenericResponse(user);
    }

    @Override
    public GenericResponse<UsersModel> getUserByEmail(String email) {
        Optional<Users> user = usersRepository.findByEmail(email);

        return buildGenericResponse(user);
    }

    @Override
    public GenericResponse<UsersModel> deleteUser(UUID id) {
        Optional<Users> user = usersRepository.findById(id);

        if (user.isPresent()) {
            usersRepository.delete(user.get());
        } else {
            throw new UserAlreadyExistException(appConfig.getProperty("user.not.found"));
        }

        return buildGenericResponse(user);
    }

    private Users createOrUpdateUser(Users user) {
        return usersRepository.save(user);
    }

    private String hashPassword(String password) {
        return MD5Util.generateMD5Hash(password);
    }

    private GenericResponse<UsersModel> buildGenericResponse(Optional<Users> user) {
        if (user.isPresent()) {
            return GenericResponse.<UsersModel>builder()
                    .message(appConfig.getProperty("user.found"))
                    .data(UsersMapper.entityToModel(user.get()))
                    .status(HttpStatus.FOUND)
                    .build();
        } else {
            return GenericResponse.<UsersModel>builder()
                    .message(appConfig.getProperty("user.not.found"))
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }
}
