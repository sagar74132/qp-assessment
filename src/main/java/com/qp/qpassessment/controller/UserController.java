package com.qp.qpassessment.controller;

import com.qp.qpassessment.model.UserRoleUpdateRequestDto;
import com.qp.qpassessment.model.UsersDetailsUpdateRequest;
import com.qp.qpassessment.model.UsersRequestDto;
import com.qp.qpassessment.model.UsersResponseDto;
import com.qp.qpassessment.service.UsersService;
import com.qp.qpassessment.service.impl.UsersServiceImpl;
import com.qp.qpassessment.utils.AppConfig;
import com.qp.qpassessment.utils.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private final UsersService usersService;
    private final AppConfig appConfig;

    @Autowired
    public UserController(final UsersServiceImpl usersService,
                          final AppConfig appConfig) {
        this.usersService = usersService;
        this.appConfig = appConfig;
    }

    @PostMapping("")
    public ResponseEntity<GenericResponse<UsersResponseDto>> createUser(@RequestBody UsersRequestDto usersRequestDto) {
        GenericResponse<UsersResponseDto> response = usersService.createUser(usersRequestDto);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("")
    public ResponseEntity<GenericResponse<UsersResponseDto>> updateUser(@RequestBody UsersDetailsUpdateRequest usersDetailsUpdateRequest) {
        GenericResponse<UsersResponseDto> response = usersService.updateUser(usersDetailsUpdateRequest);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/role")
    public ResponseEntity<GenericResponse<UsersResponseDto>> updateUserRole(@RequestBody UserRoleUpdateRequestDto payload) {
        GenericResponse<UsersResponseDto> response = usersService.updateUserRole(payload);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<UsersResponseDto>> deleteUser(@PathVariable UUID id) {
        GenericResponse<UsersResponseDto> response = usersService.deleteUser(id);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
