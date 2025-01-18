package com.qp.qpassessment.mapper;

import com.qp.qpassessment.entity.Users;
import com.qp.qpassessment.model.UsersModel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class UsersMapper {

    public static UsersModel entityToModel(Users users) {
        return UsersModel.builder()
                .id(users.getId())
                .name(users.getName())
                .email(users.getEmail())
                .password(users.getPassword())
                .role(users.getRole())
                .build();
    }

    public static Users modelToEntity(UsersModel usersModel) {
        return Users.builder()
                .id(usersModel.getId())
                .name(usersModel.getName())
                .email(usersModel.getEmail())
                .password(usersModel.getPassword())
                .role(usersModel.getRole())
                .build();
    }

}
