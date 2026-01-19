package com.remu.domain.user.service;

import com.remu.domain.user.dto.req.UserReqDTO;
import com.remu.domain.user.entity.User;
import com.remu.domain.user.exception.UserException;
import com.remu.domain.user.exception.code.UserErrorCode;
import com.remu.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Void updateProfile(Long userId, UserReqDTO.ProfileDTO dto){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        updateName(user, dto.name());
        updateIntroduction(user, dto.introduction());
        updateImageUrl(user, dto.imageUrl());

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
