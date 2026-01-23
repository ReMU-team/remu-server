package com.remu.domain.galaxy.service;

import com.remu.domain.galaxy.converter.GalaxyConverter;
import com.remu.domain.galaxy.dto.request.GalaxyReqDTO;
import com.remu.domain.galaxy.dto.response.GalaxyResDTO;
import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.galaxy.exception.GalaxyException;
import com.remu.domain.galaxy.exception.code.GalaxyErrorCode;
import com.remu.domain.galaxy.repository.GalaxyRepository;
import com.remu.domain.place.entity.Place;
import com.remu.domain.place.repository.PlaceRepository;
import com.remu.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GalaxyCommandService {
    private final GalaxyRepository galaxyRepository;
    private final PlaceRepository placeRepository;
    /*
    1. 은하 생성
     */
    public GalaxyResDTO.CreateDTO createGalaxy(GalaxyReqDTO.GalaxyCreateDTO request, User user) {
        // TODO 은하 개수 검증

        // 1) 날짜 논리 검증 (Start <= Arrival <= End)
        validateTravelDates(request.startDate(), request.arrivalDate(), request.endDate());

        // 2) 장소 처리: 기존 장소면 가져오고, 없으면 새로 저장 (googlePlaceId 기준)
        // TODO: 구글맵 위치 필드값 확인 후 적용
        Place place = placeRepository.findByGooglePlaceId(request.googlePlaceId())
                .orElseGet(()->placeRepository.save(
                        Place.builder()
                                .name(request.placeName())
                                .googlePlaceId(request.googlePlaceId())
                                .build()
                ));


        // 3) 은하 생성 및 저장
        Galaxy galaxy = GalaxyConverter.toGalaxy(request, place, user);
        Galaxy savedGalaxy = galaxyRepository.save(galaxy);

        return GalaxyConverter.toCreateDTO(savedGalaxy);

    }
    private void validateTravelDates(LocalDate start, LocalDate arrival, LocalDate end) {
        if (start.isAfter(arrival) || arrival.isAfter(end)) {
            throw new GalaxyException(GalaxyErrorCode.INVALID_DATE_SEQUENCE);
        }
    }


    /*
    2. 은하 삭제
     */
    @Transactional
    public void deleteGalaxy(Long galaxyId, User user) {
        // 1) 삭제할 은하 조회
        Galaxy galaxy = galaxyRepository.findById(galaxyId).orElseThrow(()->new GalaxyException(
                GalaxyErrorCode.GALAXY_NOT_FOUND));

        // 2) 권한 확인 (본인 소유인지 체크)
        if (!galaxy.getUser().getId().equals(user.getId())) {
            throw new GalaxyException(GalaxyErrorCode.GALAXY_FORBIDDEN);
        }

        // 3) Hard Delete: DB에서 해당 데이터를 완전히 삭제
        galaxyRepository.deleteById(galaxyId);
    }

    /*
    3. 은하 수정
     */
    @Transactional
    public void updateGalaxy(Long galaxyId, GalaxyReqDTO.GalaxyUpdateDTO request, User user) {
        // 1) 수정할 은하 조회 및 권한 체크
        Galaxy galaxy = galaxyRepository.findById(galaxyId).orElseThrow(()->new GalaxyException(
                GalaxyErrorCode.GALAXY_NOT_FOUND));

        if (!galaxy.getUser().getId().equals(user.getId())) {
            throw new GalaxyException(GalaxyErrorCode.GALAXY_FORBIDDEN);
        }

        // 2) 엔티티 정보 업데이트
        galaxy.updateInfo(
                request.name(),
                request.emojiResourceName(),
                request.startDate(),
                request.arrivalDate(),
                request.endDate()
        );
        // 3) 장소 정보 업데이트
        if (request.googlePlaceId() != null) {
            // DB에서 해당 구글 ID를 가진 장소가 있는지 먼저 찾음
            // Optional을 사용해 있으면 가져오고, 없으면 새로 만들어 저장
            Place newPlace = placeRepository.findByGooglePlaceId(request.googlePlaceId())
                    .orElseGet(() -> placeRepository.save(
                            Place.builder()
                                    .name(request.placeName())
                                    .googlePlaceId(request.googlePlaceId())
                                    .build()
                    ));
            galaxy.setPlace(newPlace);
        }

    }
}
