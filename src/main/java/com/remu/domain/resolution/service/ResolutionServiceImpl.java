package com.remu.domain.resolution.service;

import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.galaxy.repository.GalaxyRepository;
import com.remu.domain.resolution.converter.ResolutionConverter;
import com.remu.domain.resolution.dto.ResolutionReqDTO;
import com.remu.domain.resolution.dto.ResolutionResDTO;
import com.remu.domain.resolution.entity.Resolution;
import com.remu.domain.resolution.exception.ResolutionException;
import com.remu.domain.resolution.repository.ResolutionRepository;
import com.remu.global.apiPayload.code.GeneralErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 추후 시스템 복잡도에 따라 service 클래스 command, query로 구분 예정

@Service
@Transactional
@RequiredArgsConstructor
public class ResolutionServiceImpl implements ResolutionService {

    private final ResolutionRepository resolutionRepository;
    private final GalaxyRepository galaxyRepository;

    // === Command 로직 (상태 변경) ===
    @Override
    public ResolutionResDTO.CreateDTO create(
            ResolutionReqDTO.CreateDTO dto
    ) {
        // 1. 은하 조회
        Galaxy galaxy = galaxyRepository.findById(dto.galaxyId())
                .orElseThrow(() -> new ResolutionException(GeneralErrorCode.NOT_FOUND));

        // 2. 권한 검증(유저 ID 대조)
        if (!galaxy.getUser().getId().equals(dto.userId())) {
            throw new ResolutionException(GeneralErrorCode.FORBIDDEN);
        }

        // 3. DTO -> Entity 변환
        Resolution resolution = ResolutionConverter.toResolution(dto, galaxy);

        // 4. 저장 및 응답 반환
        Resolution saved = resolutionRepository.save(resolution);
        return ResolutionConverter.toCreateDTO(saved);
    }

    // === Query 로직 (조회) ===
    @Override
    @Transactional(readOnly = true)
    public ResolutionResDTO.ResolutionPreviewListDTO findResolutions(
            Long userId,
            Long galaxyId,
            Integer page
    ) {
        // 1. galaxy 존재 여부 확인 및 소유권 조회
        Galaxy galaxy = galaxyRepository.findById(galaxyId)
                .orElseThrow(() -> new ResolutionException(GeneralErrorCode.NOT_FOUND));

        if (!galaxy.getUser().getId().equals(userId)) {
            throw new ResolutionException(GeneralErrorCode.FORBIDDEN);
        }

        // 2. 페이징 설정
        PageRequest pageRequest = PageRequest.of(page, 5);

        // 3. Repository 통한 데이터 조회
        Page<Resolution> resolutions = resolutionRepository.findAllByGalaxyId(galaxyId, pageRequest);

        // 4. Converter를 이용한 반환
        return ResolutionConverter.toResolutionPreviewListDTO(resolutions);
    }
}
