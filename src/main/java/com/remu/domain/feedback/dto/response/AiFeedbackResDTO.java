package com.remu.domain.feedback.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

public class AiFeedbackResDTO {

    @Builder
    public record AiFeedbackCreateDTO(
            String content,
            LocalDateTime createdAt
    ) {}
}
