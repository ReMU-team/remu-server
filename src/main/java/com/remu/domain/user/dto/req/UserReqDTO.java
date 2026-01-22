package com.remu.domain.user.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public class UserReqDTO {

    @Builder
    public record ProfileDTO(
            String imageUrl,
            @NotBlank(message = "이름은 필수입니다.")
            @Size(min = 2, max = 15, message = "이름은 2 ~ 15자까지 가능합니다.")
            String name,
            String introduction
    ) {}
}
