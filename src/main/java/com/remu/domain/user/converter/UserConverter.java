package com.remu.domain.user.converter;

import com.remu.domain.user.dto.res.UserResDTO;
import com.remu.domain.user.entity.User;

public class UserConverter {

    // User -> ProfileDTO
    public static UserResDTO.ProfileDTO toProfileDTO(
            User user
    ){
        return UserResDTO.ProfileDTO.builder()
                .imageUrl(user.getImageUrl())
                .name(user.getName())
                .introduction(user.getIntroduction())
                .build();
    }
}
