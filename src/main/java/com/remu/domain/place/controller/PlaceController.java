package com.remu.domain.place.controller;

import com.remu.domain.place.dto.response.PlaceResDTO;
import com.remu.domain.place.exception.code.PlaceSuccessCode;
import com.remu.domain.place.service.PlaceQueryService;
import com.remu.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
public class PlaceController {
    private final PlaceQueryService placeQueryService;

    @GetMapping
    public ApiResponse<PlaceResDTO.PlaceSearchResDTO> searchPlace(@RequestParam(name = "query") String query) {
        PlaceResDTO.PlaceSearchResDTO response = placeQueryService.searchPlace(query);
        return ApiResponse.onSuccess(PlaceSuccessCode.PLACE_SEARCH_SUCCESS, response);
    }

}
