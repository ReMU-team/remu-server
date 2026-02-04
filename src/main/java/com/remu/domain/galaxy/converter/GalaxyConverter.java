package com.remu.domain.galaxy.converter;

import com.remu.domain.galaxy.dto.request.GalaxyReqDTO;
import com.remu.domain.galaxy.dto.response.GalaxyResDTO;
import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.galaxy.enums.GalaxyStatus;
import com.remu.domain.place.entity.Place;
import com.remu.domain.user.entity.User;

import java.time.LocalDate;

public class GalaxyConverter {
    /*
    은하 생성
     */
    // request dto->entity
    public static Galaxy toGalaxy(GalaxyReqDTO.GalaxyCreateDTO req, LocalDate date, Place place, User user) {
        return Galaxy.builder()
                .name(req.name())
                .user(user)
                .place(place)
                .emojiResourceName(req.emojiResourceName())
                .startDate(req.startDate())
                .arrivalDate(date)
                .endDate(req.endDate())
                .status(GalaxyStatus.READY)
                .build();
    }

    // entity->response dto
    public static GalaxyResDTO.GalaxyCreateDTO toCreateDTO(Galaxy galaxy) {
        return new GalaxyResDTO.GalaxyCreateDTO(
                galaxy.getId(),
                galaxy.getName(),
                galaxy.getStartDate(),
                galaxy.getArrivalDate(),
                galaxy.getEndDate()
        );
    }

        /*
    은하 상세 조회
     */
    // request dto->entity

    // entity->response dto
    public static GalaxyResDTO.GalaxyDetailDTO toDetailDTO(Galaxy galaxy, Long dDay){
        return new GalaxyResDTO.GalaxyDetailDTO(
                galaxy.getId(),
                galaxy.getName(),
                galaxy.getEmojiResourceName(),
                dDay,
                galaxy.getStartDate(),
                galaxy.getArrivalDate(),
                galaxy.getEndDate(),
                galaxy.getPlace().getName()
        );
    }

    public static GalaxyResDTO.GalaxySummaryDTO toSummaryDTO(Galaxy galaxy) {
        return new GalaxyResDTO.GalaxySummaryDTO(
                galaxy.getId(),
                galaxy.getName(),
                galaxy.getEmojiResourceName()
        );
    }
}
