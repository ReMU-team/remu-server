package com.remu.domain.place.service;

import com.remu.domain.place.dto.response.PlaceResDTO;
import com.remu.domain.place.exception.PlaceException;
import com.remu.domain.place.exception.code.PlaceErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class PlaceQueryService {
    @Value("${google.api.key}")
    private String apiKey;

    private final RestClient restClient;

    public PlaceResDTO.PlaceSearchResDTO searchPlace(String query) {

        if (query == null || query.isBlank()) {
            throw new PlaceException(PlaceErrorCode.INVALID_PLACE_QUERY);
        }try {
            PlaceResDTO.PlaceSearchResDTO response=restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/maps/api/place/queryautocomplete/json")
                            .queryParam("input", query)
                            .queryParam("key", apiKey)
                            .queryParam("language", "ko")
                            .build())
                    .retrieve()
                    .body(PlaceResDTO.PlaceSearchResDTO.class);
            if (response==null || "ZERO_RESULTS".equals(response.status())) {
                throw new PlaceException(PlaceErrorCode.PLACE_NOT_FOUND);
            }
            return response;
        }catch (Exception e) {
            // 구글 API 호출 중 발생하는 네트워크 에러 등 처리
            throw new PlaceException(PlaceErrorCode.GOOGLE_API_EXCEPTION);
        }

    }
}
