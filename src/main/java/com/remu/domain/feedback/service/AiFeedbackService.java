package com.remu.domain.feedback.service;

import com.remu.domain.feedback.dto.response.AiFeedbackResDTO;
import org.springframework.transaction.annotation.Transactional;

public interface AiFeedbackService {
    AiFeedbackResDTO.AiFeedbackCreateDTO createFeedback(
            Long galaxyId
    );

    @Transactional(readOnly = true)
    AiFeedbackResDTO.AiFeedbackCreateDTO readFeedback(
            Long galaxyId
    );
}
