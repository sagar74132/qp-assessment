package com.qp.qpassessment.mapper;

import com.qp.qpassessment.entity.Users;
import com.qp.qpassessment.model.UsersDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class UsersMapper {

    public static UsersDto entityToModel(Users users) {
        return UsersDto.builder()
                .id(users.getId())
                .name(users.getName())
                .email(users.getEmail())
                .password(users.getPassword())
                .role(users.getRole())
                .build();
    }

    public static Users modelToEntity(UsersDto usersDto) {
        return Users.builder()
                .id(usersDto.getId())
                .name(usersDto.getName())
                .email(usersDto.getEmail())
                .password(usersDto.getPassword())
                .role(usersDto.getRole())
                .build();
    }

}
