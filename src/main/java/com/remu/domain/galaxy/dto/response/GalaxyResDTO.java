package com.remu.domain.galaxy.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class GalaxyResDTO {

    public record CreateDTO (
            Long galaxyId,
            String name,
            LocalDate startDate,
            LocalDate arrivalDate,
            LocalDate endDate,
            String emojiUrl
    ){}
}
