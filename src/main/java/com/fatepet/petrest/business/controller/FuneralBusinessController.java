package com.fatepet.petrest.business.controller;

import com.fatepet.global.response.ApiResponse;
import com.fatepet.global.response.ResponseCode;
import com.fatepet.petrest.SortOption;
import com.fatepet.petrest.business.funeral.FuneralBusinessService;
import com.fatepet.petrest.business.controller.dto.request.BusinessSearchRequest;
import com.fatepet.petrest.business.controller.dto.response.BusinessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/funeral-business")
@RequiredArgsConstructor
public class FuneralBusinessController {

    private final FuneralBusinessService funeralBusinessService;

    @GetMapping
    public ApiResponse<List<BusinessResponse>> getBusinessList(BusinessSearchRequest request,
                                                               @RequestParam(defaultValue = "POPULAR") String sort,
                                                               @PageableDefault(size = 10)
                                                               Pageable pageable) {
        SortOption sortOption = SortOption.from(sort);
        List<BusinessResponse> response;

        if (sortOption.isDistanceSort()) {
            response = funeralBusinessService.getBusinessListSortedByDistance(request.getLatitude(), request.getLongitude(), pageable);
        } else {
            Pageable sortedPageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    SortOption.from(sort).getSort());
            response = funeralBusinessService.getBusinessList(sortedPageable);
        }

        return ApiResponse.of(ResponseCode.SUCCESS, response);
    }

}
