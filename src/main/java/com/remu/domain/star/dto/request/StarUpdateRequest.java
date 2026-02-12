package com.remu.domain.star.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StarUpdateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    @Size(min = 2, max = 32, message = "제목은 2자 이상 32자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    @Size(min = 2, message = "내용은 2자 이상 입력해주세요.")
    private String content;

    @NotNull(message = "여행 날짜는 필수입니다.")
    private LocalDate recordDate; // 여행 날짜

    @NotBlank(message = "카드 색상은 필수입니다.")
    private String cardColor; // 선택한 배경색 (String)

    @Size(max = 3, message = "이모지는 최대 3개까지 선택 가능합니다.")
    private List<String> emojis; // 선택한 이모지 이름 리스트 (String)

    // 기존 이미지를 삭제할지 여부 (true면 삭제)
    private Boolean isImageDeleted;
}