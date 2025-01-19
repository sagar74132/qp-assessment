package com.qp.qpassessment.service.impl;

import com.qp.qpassessment.constant.Enums;
import com.qp.qpassessment.entity.Users;
import com.qp.qpassessment.exception.UserExceptions;
import com.qp.qpassessment.mapper.UsersMapper;
import com.qp.qpassessment.model.UserRoleUpdateRequestDto;
import com.qp.qpassessment.model.UsersDto;
import com.qp.qpassessment.repository.UsersRepository;
import com.qp.qpassessment.service.UsersService;
import com.qp.qpassessment.utils.AppConfig;
import com.qp.qpassessment.utils.BCryptPasswordEncoderUtil;
import com.qp.qpassessment.utils.GenericResponse;
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
    public GenericResponse<UsersDto> createUser(UsersDto user) {

        if (null != user.getId()) {
            throw new UserExceptions(appConfig.getProperty("user.id.not.allowed"));
        }

        Optional<Users> existingUser = usersRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new UserExceptions(appConfig.getProperty("user.already.exist"));
        } else if (null != user.getRole()) {
            throw new UserExceptions(appConfig.getProperty("user.role.not.allowed"));
        }

        String hashedPassword = hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        user.setRole(Enums.Role.USER);

        Users createdUser = createOrUpdateUser(UsersMapper.modelToEntity(user));

        return GenericResponse.<UsersDto>builder()
                .message(appConfig.getProperty("user.created"))
                .data(UsersMapper.entityToModel(createdUser))
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public GenericResponse<UsersDto> updateUser(UsersDto user) {
        Optional<Users> existingUser = usersRepository.findById(user.getId());
        if (existingUser.isEmpty()) {
            throw new UserExceptions(appConfig.getProperty("user.not.found"));
        } else if (null != user.getRole()) {
            throw new UserExceptions(appConfig.getProperty("user.role.modification.not.allowed"));
        }

        if (null != user.getPassword()) {
            // Hash the Password
            String hashedPassword = hashPassword(user.getPassword());
            existingUser.get().setPassword(hashedPassword);
        }

        if (null != user.getName() && !user.getName().equals(existingUser.get().getName())) {
            existingUser.get().setName(user.getName());
        }

        if (null != user.getEmail() && !user.getEmail().equals(existingUser.get().getEmail())) {

            if (!uniqueEmailCheck(user.getEmail())) {
                throw new UserExceptions(appConfig.getProperty("user.update.email.already.exist"));
            }
            existingUser.get().setEmail(user.getEmail());
        }

        Users createdUser = createOrUpdateUser(existingUser.get());

        return GenericResponse.<UsersDto>builder()
                .message(appConfig.getProperty("user.details.modified"))
                .data(UsersMapper.entityToModel(createdUser))
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public GenericResponse<UsersDto> updateUserRole(UserRoleUpdateRequestDto userRoleUpdateRequestDto) {
        Optional<Users> existingUser = usersRepository.findById(userRoleUpdateRequestDto.getId());
        if (existingUser.isEmpty()) {
            throw new UserExceptions(appConfig.getProperty("user.not.found"));
        }

        existingUser.get().setRole(userRoleUpdateRequestDto.getRole());
        Users createdUser = createOrUpdateUser(existingUser.get());

        return GenericResponse.<UsersDto>builder()
                .message(appConfig.getProperty("user.role.updated"))
                .data(UsersMapper.entityToModel(createdUser))
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public GenericResponse<UsersDto> getUserByEmailAndPassword(String email, String password) {

        String hashedPassword = hashPassword(password);
        Optional<Users> user = usersRepository.findByEmailAndPassword(email, hashedPassword);

        return buildGenericResponse(user);
    }

    @Override
    public GenericResponse<UsersDto> getUserByEmail(String email) {
        Optional<Users> user = usersRepository.findByEmail(email);

        return buildGenericResponse(user);
    }

    @Override
    public GenericResponse<UsersDto> deleteUser(UUID id) {
        Optional<Users> user = usersRepository.findById(id);

        if (user.isPresent()) {
            user.get().setIsDeleted(1);
            usersRepository.save(user.get());
        } else {
            throw new UserExceptions(appConfig.getProperty("user.not.found"));
        }

        return GenericResponse.<UsersDto>builder()
                .message(appConfig.getProperty("user.deleted"))
                .data(UsersMapper.entityToModel(user.get()))
                .status(HttpStatus.OK)
                .build();
    }

    private Users createOrUpdateUser(Users user) {
        return usersRepository.save(user);
    }

    private String hashPassword(String password) {
        return BCryptPasswordEncoderUtil.encode(password);
    }

    private GenericResponse<UsersDto> buildGenericResponse(Optional<Users> user) {
        if (user.isPresent()) {
            return GenericResponse.<UsersDto>builder()
                    .message(appConfig.getProperty("user.found"))
                    .data(UsersMapper.entityToModel(user.get()))
                    .status(HttpStatus.FOUND)
                    .build();
        } else {
            return GenericResponse.<UsersDto>builder()
                    .message(appConfig.getProperty("user.not.found"))
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    private boolean uniqueEmailCheck(String email) {
        GenericResponse<UsersDto> existingUser = getUserByEmail(email);
        return null == existingUser.getData();
    }
}
