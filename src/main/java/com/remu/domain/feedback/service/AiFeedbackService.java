package com.remu.domain.feedback.service;

import com.remu.domain.feedback.dto.response.AiFeedbackResDTO;

public interface AiFeedbackService {
    AiFeedbackResDTO.AiFeedbackCreateDTO createFeedback(
            Long galaxyId
    );
}
