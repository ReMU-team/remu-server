package com.remu.domain.user.service;

import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.galaxy.repository.GalaxyRepository;
import com.remu.domain.user.converter.UserConverter;
import com.remu.domain.user.dto.req.UserReqDTO;
import com.remu.domain.user.dto.res.UserResDTO;
import com.remu.domain.user.entity.User;
import com.remu.domain.user.exception.UserException;
import com.remu.domain.user.exception.code.UserErrorCode;
import com.remu.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final GalaxyRepository galaxyRepository;

    // 프로필 업데이트
    @Transactional
    public Void updateProfile(Long userId, UserReqDTO.ProfileDTO dto){

        // 1. 유저 존재 여부 검증
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 2. 닉네임 업데이트
        updateName(user, dto.name());

        // 3. 한 줄 소개 업데이트
        updateIntroduction(user, dto.introduction());

        // 4. 프로필 이미지 업데이트
        updateImageUrl(user, dto.imageUrl());

        return null;
    }

    // 사용 가능한 닉네임인지 검증
    public UserResDTO.NameCheckDTO checkName(String rawName, Long userId) {

        // 1. 닉네임 값 null 확인
        if (rawName == null || rawName.isBlank()) {
            return UserResDTO.NameCheckDTO.invalid_short();
        }

        // 2. 닉네임 공백 제거
        String name = rawName.trim();

        // 3. 닉네임 2 ~ 15자 확인
        if (name.length() < 2) {
            return UserResDTO.NameCheckDTO.invalid_short();
        }
        else if (name.length() > 15) {
            return UserResDTO.NameCheckDTO.invalid_long();
        }

        // 4. 유저 존재 여부 검증 및 조회
        User me = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 5. 현재 사용자의 닉네임과 동일할 때 valid 반환
        if (name.equals(me.getName())) {
            return UserResDTO.NameCheckDTO.valid();
        }

        boolean exists = userRepository.existsByName(name);

        // 6. 다른 닉네임과 중복되면 duplicate 반환, 중볻되지 않으면 valid 반환
        return exists
                ? UserResDTO.NameCheckDTO.duplicate() : UserResDTO.NameCheckDTO.valid();
    }

    // 프로필 조회
    public UserResDTO.ProfileDTO getProfile(
            Long userId
    ){
        // 1. 유저 존재 여부 검증 및 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 2. 컨버터를 이용한 DTO 반환
        return UserConverter.toProfileDTO(user);
    }

    // 회원 탈퇴
    @Transactional
    public Void deleteAccount(Long userId) {
        //TODO 소셜 로그인 구현 후 소셜 로그인 연동 해제 기능 추가

        // 1. 유저 존재 여부 검증 및 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 2. 유저 삭제 : CascadeType.ALL 설정으로 연관된 엔티티 데이터 자동으로 삭제
        userRepository.delete(user);

        return null;
    }

    // 이름 업데이트
    private void updateName(User user, String rawName) {
        if (rawName != null) {
            String name = rawName.trim();
            if(name.length() < 2 || name.length() > 15) {
                throw new UserException(UserErrorCode.INVALID_NAME);
            }

            if(userRepository.existsByName(name)){
                throw new UserException(UserErrorCode.NAME_DUPLICATE);
            }

            user.updateName(name);
        }
    }

    // 한 줄 소개 업데이트
    private void updateIntroduction(User user, String rawIntro) {
        if (rawIntro != null) {
            String intro = rawIntro.trim();
            if (!intro.isEmpty()) {
                user.updateIntroduction(intro);
            }
        }
    }

    // 이미지 업데이트
    private void updateImageUrl(User user, String imageUrl) {
        if (imageUrl != null) {
            String url = imageUrl.trim();
            user.updateImageUrl(url.isEmpty() ? null : url);
        }
    }

}
