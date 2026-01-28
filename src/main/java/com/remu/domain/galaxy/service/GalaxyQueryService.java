package com.remu.domain.galaxy.service;

import com.remu.domain.galaxy.converter.GalaxyConverter;
import com.remu.domain.galaxy.dto.response.GalaxyResDTO;
import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.galaxy.exception.GalaxyException;
import com.remu.domain.galaxy.exception.code.GalaxyErrorCode;
import com.remu.domain.galaxy.repository.GalaxyRepository;
import com.remu.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GalaxyQueryService {
    private final GalaxyRepository galaxyRepository;

    // 은하 상세 조회
    public GalaxyResDTO.GalaxyDetailDTO getGalaxyDetail(Long galaxyId, User user) {
        // 1. 은하 조회 및 권한 확인
        Galaxy galaxy = galaxyRepository.findById(galaxyId)
                .orElseThrow(()->new GalaxyException(GalaxyErrorCode.GALAXY_NOT_FOUND));

        validateOwner(galaxy, user);

        // 2. 실시간 데이터 계산
        LocalDate today = LocalDate.now();
        Long dDay = ChronoUnit.DAYS.between(today, galaxy.getStartDate());

        // 3. DTO로 변환하여 반환
        return GalaxyConverter.toDetailDTO(galaxy, dDay);
    }
    private void validateOwner(Galaxy galaxy, User user) {
        if (!galaxy.getUser().getId().equals(user.getId())) {
            throw new GalaxyException(GalaxyErrorCode.GALAXY_FORBIDDEN);
        }

    }

    //
    @Transactional(readOnly = true)
    public GalaxyResDTO.SummaryListDTO getGalaxyList(User user, Pageable pageable) {
        // 1. 해당 유저의 모든 은하 조회
        Slice<Galaxy> galaxyList = galaxyRepository.findAllByUserId(user.getId(), pageable);
        // 2. 전체 개수 조회
        Long totalCount = galaxyRepository.countByUserId(user.getId());
        // 3. 응답 DTO 변환
        List<GalaxyResDTO.GalaxySummaryDTO> summaryDTOList = galaxyList.stream()
                .map(galaxy -> {
                    return GalaxyConverter.toSummaryDTO(galaxy);
                })
                .toList();

        return new GalaxyResDTO.SummaryListDTO(totalCount, summaryDTOList, galaxyList.getNumber(), galaxyList.hasNext());
    }
}
