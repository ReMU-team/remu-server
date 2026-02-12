package com.remu.domain.user.dto.res;

import lombok.Builder;

public class UserResDTO {

    // 닉네임 사용 가능 여부에 대한 응답 결과
    public record NameCheckDTO(
            boolean available,
            String message
    ){
        public static NameCheckDTO valid() {
            return new NameCheckDTO(true, "사용 가능한 닉네임입니다.");
        }

        public static NameCheckDTO duplicate() {
            return new NameCheckDTO(false, "이미 사용 중인 닉네임이예요.");
        }

        public static NameCheckDTO invalid_short() {
            return new NameCheckDTO(false, "2자 이상 입력해주세요.");
        }

        public static NameCheckDTO invalid_long() {
            return new NameCheckDTO(false, "15자 이하로 입력해주세요.");
        }
    }

    // 프로필 조회에 대한 응답 결과
    @Builder
    public record ProfileDTO(
            String imageUrl,
            String name,
            String introduction
    ){}
}
