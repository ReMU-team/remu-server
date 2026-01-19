package com.remu.domain.user.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public class UserReqDTO {

    @Builder
    public record ProfileDTO(
            String imageUrl,
            @NotBlank
            @Size(min = 2, max = 15)
            String name,
            String introduction
    ) {}
}
