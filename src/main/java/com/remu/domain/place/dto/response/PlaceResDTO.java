package com.remu.domain.place.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PlaceResDTO {
    public record PlaceSearchResDTO(
            List<Prediction> predictions,
            String status
    ){
        // 구글 응답 결과
        public record Prediction(
                @JsonProperty("description")
                String name,
                @JsonProperty("place_id")
                String placeId
        ) {}
    }
}
