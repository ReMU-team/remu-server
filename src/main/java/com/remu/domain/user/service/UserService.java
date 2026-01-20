package com.remu.domain.user.service;

import com.remu.domain.user.dto.req.UserReqDTO;
import com.remu.domain.user.dto.res.UserResDTO;
import com.remu.domain.user.entity.User;
import com.remu.domain.user.exception.UserException;
import com.remu.domain.user.exception.code.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 프로필 업데이트
    @Transactional
    public Void updateProfile(Long userId, UserReqDTO.ProfileDTO dto){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        updateName(user, dto.name());
        updateIntroduction(user, dto.introduction());
        updateImageUrl(user, dto.imageUrl());

        return null;
    }

    // 사용 가능한 닉네임인지 검증
    public UserResDTO.NameCheckDTO checkName(String rawName, Long userId) {

        if (rawName == null || rawName.isBlank()) {
            return UserResDTO.NameCheckDTO.invalid_short();
        }

        String name = rawName.trim();

        if (name.length() < 2) {
            return UserResDTO.NameCheckDTO.invalid_short();
        }
        else if (name.length() > 15) {
            return UserResDTO.NameCheckDTO.invalid_long();
        }

        User me = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        if (name.equals(me.getName())) {
            return UserResDTO.NameCheckDTO.valid();
        }

        boolean exists = userRepository.existsByName(name);

        return exists
                ? UserResDTO.NameCheckDTO.duplicate() : UserResDTO.NameCheckDTO.valid();
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
