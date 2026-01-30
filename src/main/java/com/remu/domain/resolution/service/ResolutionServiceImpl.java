package com.remu.domain.resolution.service;

import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.galaxy.repository.GalaxyRepository;
import com.remu.domain.resolution.converter.ResolutionConverter;
import com.remu.domain.resolution.dto.ResolutionReqDTO;
import com.remu.domain.resolution.dto.ResolutionResDTO;
import com.remu.domain.resolution.entity.Resolution;
import com.remu.domain.resolution.exception.ResolutionException;
import com.remu.domain.resolution.exception.code.ResolutionErrorCode;
import com.remu.domain.resolution.repository.ResolutionRepository;
import com.remu.global.apiPayload.code.GeneralErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 추후 시스템 복잡도에 따라 service 클래스 command, query로 구분 예정

@Service
@Transactional
@RequiredArgsConstructor
public class ResolutionServiceImpl implements ResolutionService {

    private final ResolutionRepository resolutionRepository;
    private final GalaxyRepository galaxyRepository;

    // === Command 로직 (상태 변경) ===

    // 다짐 배치 생성
    @Override
    public ResolutionResDTO.ResolutionBatchCreateDTO batchCreate(
            Long userId,
            Long galaxyId,
            ResolutionReqDTO.ResolutionBatchCreateDTO dto
    ) {
        // 1. 은하 조회

        Galaxy galaxy = galaxyRepository.findById(galaxyId)
                .orElseThrow(() -> new ResolutionException(GeneralErrorCode.NOT_FOUND));

        // 2. 권한 검증(유저 ID 대조)
        if (!galaxy.getUser().getId().equals(userId)) {
            throw new ResolutionException(GeneralErrorCode.FORBIDDEN);
        }

        // 3. 다짐 이모지 및 일러스트 업데이트
        galaxy.updateResolutionEmoji(dto.emojiId());
        galaxy.updateResolutionIllust(dto.illustId());

        // 4. 리스트 저장
        List<Resolution> resolutions = dto.contents().stream()
                .map(content -> ResolutionConverter.toResolutionFromBatch(content, galaxy))
                .toList();

        // 5. 일괄 저장
        List<Resolution> savedResolutions = resolutionRepository.saveAll(resolutions);

        return ResolutionConverter.toBatchCreateDTO(galaxy, savedResolutions);

    }

    // 배치 다짐 수정
    @Override
    public ResolutionResDTO.ResolutionBatchCreateDTO batchUpdate(
            Long userId,
            Long galaxyId,
            ResolutionReqDTO.ResolutionBatchUpdateDTO dto
    ) {
        // 1. 은하 조회
        Galaxy galaxy = galaxyRepository.findById(galaxyId)
                .orElseThrow(() -> new ResolutionException(GeneralErrorCode.NOT_FOUND));

        // 2. 권한 검증(유저 ID 대조)
        if (!galaxy.getUser().getId().equals(userId)) {
            throw new ResolutionException(GeneralErrorCode.FORBIDDEN);
        }

        // 3. 이모지 업데이트
        galaxy.updateResolutionEmoji(dto.emojiId());

        // 4. 다짐 리스트 순회하며 업데이트
        List<Resolution> updatedResolutions = dto.resolutions().stream()
                .map(item -> {
                    Resolution resolution = resolutionRepository.findById(item.resolutionId())
                            .orElseThrow(() -> new ResolutionException(ResolutionErrorCode.NOT_FOUND));

                    if (!resolution.getGalaxy().getId().equals(galaxyId)) {
                        throw new ResolutionException(GeneralErrorCode.FORBIDDEN);
                    }

                    resolution.updateContent(item.content());
                    return resolution;
                })
                .toList();
        return ResolutionConverter.toBatchCreateDTO(galaxy, updatedResolutions);
    }

    // === Query 로직 (조회) ===
    @Override
    @Transactional(readOnly = true)
    public ResolutionResDTO.ResolutionPreviewListDTO findResolutions(
            Long userId,
            Long galaxyId
    ) {
        // 1. galaxy 존재 여부 확인 및 소유권 조회
        Galaxy galaxy = galaxyRepository.findById(galaxyId)
                .orElseThrow(() -> new ResolutionException(GeneralErrorCode.NOT_FOUND));

        if (!galaxy.getUser().getId().equals(userId)) {
            throw new ResolutionException(GeneralErrorCode.FORBIDDEN);
        }

        // 3. 이모지 및 일러스트 정보 획득
        String resolutionEmojiId = galaxy.getResolutionEmojiId();
        String resolutionIllustId = galaxy.getResolutionIllustId();

        // 4. Repository 통한 데이터 조회
        List<Resolution> resolutions = resolutionRepository.findAllByGalaxyId(galaxyId);

        // 5. Converter를 이용한 반환
        return ResolutionConverter.toResolutionPreviewListDTO(resolutions, resolutionEmojiId, resolutionIllustId);
    }
}
