package com.qp.qpassessment.controller;

import com.qp.qpassessment.model.UserRoleUpdate;
import com.qp.qpassessment.model.UsersModel;
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
    public ResponseEntity<GenericResponse<UsersModel>> createUser(@RequestBody UsersModel usersModel) {
        GenericResponse<UsersModel> response = usersService.createUser(usersModel);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("")
    public ResponseEntity<GenericResponse<UsersModel>> updateUser(@RequestBody UsersModel usersModel) {
        GenericResponse<UsersModel> response = usersService.updateUser(usersModel);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/role")
    public ResponseEntity<GenericResponse<UsersModel>> updateUserRole(@RequestBody UserRoleUpdate payload) {
        GenericResponse<UsersModel> response = usersService.updateUserRole(payload);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<UsersModel>> deleteUser(@PathVariable UUID id) {
        GenericResponse<UsersModel> response = usersService.deleteUser(id);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
