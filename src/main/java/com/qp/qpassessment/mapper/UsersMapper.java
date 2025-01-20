package com.qp.qpassessment.mapper;

import com.qp.qpassessment.entity.Users;
import com.qp.qpassessment.model.UsersRequestDto;
import com.qp.qpassessment.model.UsersResponseDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class UsersMapper {

    public static UsersResponseDto entityToModel(Users users) {
        return UsersResponseDto.builder()
                .id(users.getId())
                .name(users.getName())
                .email(users.getEmail())
                .password(users.getPassword())
                .role(users.getRole())
                .build();
    }

    public static Users modelToEntity(UsersRequestDto usersRequestDto) {
        return Users.builder()
                .name(usersRequestDto.getName())
                .email(usersRequestDto.getEmail())
                .password(usersRequestDto.getPassword())
                .build();
    }

}
