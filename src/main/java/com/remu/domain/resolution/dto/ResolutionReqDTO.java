package com.remu.domain.resolution.dto;

import com.remu.domain.galaxy.entity.Galaxy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Req: 유저가 시스템에 요청

public class ResolutionReqDTO {

    // 다짐 생성 요청 DTO
    public record CreateDTO(
            @NotBlank(message = "다짐 내용은 필수입니다.")
            @Size(max = 100, message = "다짐은 최대 100자까지 가능합니다.")
            String content
    ) {}

    // 다짐 수정 DTO
    public record UpdateDTO(
            String content
    ) {}
}
