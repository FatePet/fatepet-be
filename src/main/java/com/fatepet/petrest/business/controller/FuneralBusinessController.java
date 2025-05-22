package com.fatepet.petrest.business.controller;

import com.fatepet.petrest.SortOption;
import com.fatepet.petrest.business.FuneralBusinessService;
import com.fatepet.petrest.business.admin.valid.BusinessValidator;
import com.fatepet.petrest.business.controller.dto.request.BusinessSearchRequest;
import com.fatepet.petrest.business.controller.dto.response.BusinessResponse;
import com.fatepet.petrest.business.controller.dto.response.FuneralBusinessDetailsResponse;
import com.fatepet.petrest.global.response.ApiResponse;
import com.fatepet.petrest.global.response.ResponseCode;
import com.fatepet.petrest.user.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
public class FuneralBusinessController {

    private final FuneralBusinessService funeralBusinessService;
    private final BusinessValidator businessValidator;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BusinessResponse>>> getBusinessList(BusinessSearchRequest request,
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
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, response));
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<ApiResponse<FuneralBusinessDetailsResponse>> getBusinessDetails(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long businessId) {
        businessValidator.isValidAdmin(customUserDetails, businessId);

        FuneralBusinessDetailsResponse response = funeralBusinessService.getBusinessDetails(businessId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, response));
    }

}
