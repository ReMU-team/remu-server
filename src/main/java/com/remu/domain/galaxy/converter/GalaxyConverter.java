package com.remu.domain.galaxy.converter;

import com.remu.domain.emoji.entity.Emoji;
import com.remu.domain.galaxy.dto.request.GalaxyReqDTO;
import com.remu.domain.galaxy.dto.response.GalaxyResDTO;
import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.galaxy.enums.GalaxyStatus;
import com.remu.domain.place.entity.Place;
import com.remu.domain.user.entity.User;

public class GalaxyConverter {
    /*
    은하 생성
     */
    // request dto->entity
    public static Galaxy toGalaxy(GalaxyReqDTO.CreateDTO req, Place place, User user, Emoji emoji) {
        return Galaxy.builder()
                .name(req.name())
                .user(user)
                .place(place)
                .galaxyEmoji(emoji)
                .startDate(req.startDate())
                .arrivalDate(req.arrivalDate())
                .endDate(req.endDate())
                .status(GalaxyStatus.READY)
                .build();
    }

    // entity->response dto
    public static GalaxyResDTO.CreateDTO toCreateDTO(Galaxy galaxy) {
        return new GalaxyResDTO.CreateDTO(
                galaxy.getId(),
                galaxy.getName(),
                galaxy.getStartDate(),
                galaxy.getArrivalDate(),
                galaxy.getEndDate(),
                galaxy.getGalaxyEmoji().getImageUrl()
        );
    }

        /*
    은하 상세 조회
     */
    // request dto->entity

    // entity->response dto
    public static GalaxyResDTO.DetailDTO toDetailDTO(Galaxy galaxy, Long dDay){
        return new GalaxyResDTO.DetailDTO(
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

    public static GalaxyResDTO.SummaryDTO toSummaryDTO(Galaxy galaxy) {
        return new GalaxyResDTO.SummaryDTO(
                galaxy.getId(),
                galaxy.getName(),
                galaxy.getEmojiResourceName()
        );
    }
}
