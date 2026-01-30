package com.remu.domain.feedback.converter;

import com.remu.domain.feedback.dto.response.AiFeedbackResDTO;
import com.remu.domain.feedback.entity.AiFeedback;

public class AiFeedbackConverter {

    // Entity -> Converter
    public static AiFeedbackResDTO.AiFeedbackCreateDTO toCreateDTO(
            AiFeedback aiFeedback
    ) {
        return AiFeedbackResDTO.AiFeedbackCreateDTO.builder()
                .content(aiFeedback.getContent())
                .createdAt(aiFeedback.getCreatedAt())
                .build();
    }

    // Entity -> Converter
    public static AiFeedbackResDTO.AiFeedbackUpdateDTO toUpdateDTO(
            AiFeedback aiFeedback
    ) {
        return AiFeedbackResDTO.AiFeedbackUpdateDTO.builder()
                .content(aiFeedback.getContent())
                .updatedAt(aiFeedback.getUpdatedAt())
                .build();
    }
}
