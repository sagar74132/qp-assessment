package com.qp.qpassessment.controller;

import com.qp.qpassessment.model.UserRoleUpdateRequestDto;
import com.qp.qpassessment.model.UsersDto;
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
    public ResponseEntity<GenericResponse<UsersDto>> createUser(@RequestBody UsersDto usersDto) {
        GenericResponse<UsersDto> response = usersService.createUser(usersDto);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("")
    public ResponseEntity<GenericResponse<UsersDto>> updateUser(@RequestBody UsersDto usersDto) {
        GenericResponse<UsersDto> response = usersService.updateUser(usersDto);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/role")
    public ResponseEntity<GenericResponse<UsersDto>> updateUserRole(@RequestBody UserRoleUpdateRequestDto payload) {
        GenericResponse<UsersDto> response = usersService.updateUserRole(payload);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<UsersDto>> deleteUser(@PathVariable UUID id) {
        GenericResponse<UsersDto> response = usersService.deleteUser(id);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
