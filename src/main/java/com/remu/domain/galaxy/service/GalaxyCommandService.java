package com.remu.domain.galaxy.service;

import com.remu.domain.emoji.entity.Emoji;
import com.remu.domain.emoji.enums.EmojiType;
import com.remu.domain.emoji.exception.EmojiException;
import com.remu.domain.emoji.exception.code.EmojiErrorCode;
import com.remu.domain.emoji.repository.EmojiRepository;
import com.remu.domain.galaxy.converter.GalaxyConverter;
import com.remu.domain.galaxy.dto.request.GalaxyReqDTO;
import com.remu.domain.galaxy.dto.response.GalaxyResDTO;
import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.galaxy.enums.GalaxyStatus;
import com.remu.domain.galaxy.exception.GalaxyException;
import com.remu.domain.galaxy.exception.code.GalaxyErrorCode;
import com.remu.domain.galaxy.repository.GalaxyRepository;
import com.remu.domain.place.entity.Place;
import com.remu.domain.place.repository.PlaceRepository;
import com.remu.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GalaxyCommandService {
    private final GalaxyRepository galaxyRepository;
    private final PlaceRepository placeRepository;
    private final EmojiRepository emojiRepository;

    public GalaxyResDTO.CreateDTO createGalaxy(GalaxyReqDTO.CreateDTO request, User user) {
        // TODO 은하 개수 검증

        // 1. 날짜 논리 검증 (Start <= Arrival <= End)
        validateTravelDates(request.startDate(), request.arrivalDate(), request.endDate());

        // 2. 장소 처리: 기존 장소면 가져오고, 없으면 새로 저장 (googlePlaceId 기준)
        // TODO: 구글맵 위치 필드값 확인 후 적용
        Place place = placeRepository.findByGooglePlaceId(request.googlePlaceId());
        if (place == null) {
            placeRepository.save(
                    Place.builder()
                            .googlePlaceId(request.googlePlaceId())
                           // .name(request.placeName())
                            .build()
            );
        }


        // 3. 대표 이모지 존재 여부 확인 & 은하용 이모지 확인
        Emoji isExistedEmoji = emojiRepository.findById(request.emojiId())
                .orElseThrow(()->new EmojiException(EmojiErrorCode.EMOJI_NOT_FOUND));
        if (isExistedEmoji.getType() != EmojiType.GALAXY) {
            throw new EmojiException(EmojiErrorCode.INVALID_EMOJI_TYPE);
        }

        // 4. 은하 생성 및 저장
        Galaxy galaxy = GalaxyConverter.toGalaxy(request, place, user, isExistedEmoji);
        Galaxy savedGalaxy = galaxyRepository.save(galaxy);

        return GalaxyConverter.toCreateDTO(savedGalaxy);

    }
    private void validateTravelDates(LocalDate start, LocalDate arrival, LocalDate end) {
        if (start.isAfter(arrival) || arrival.isAfter(end)) {
            throw new GalaxyException(GalaxyErrorCode.INVALID_DATE_SEQUENCE);
        }
    }


}
