package com.fatepet.petrest.business.admin;

import com.fatepet.petrest.business.admin.valid.BusinessValidator;
import com.fatepet.petrest.global.response.ApiResponse;
import com.fatepet.petrest.global.response.ResponseCode;
import com.fatepet.petrest.business.admin.dto.response.BusinessListResponse;
import com.fatepet.petrest.user.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminBusinessController {

    private final AdminService adminService;
    private final BusinessValidator businessValidator;

    @GetMapping("/business/check-name")
    public ResponseEntity<ApiResponse<Void>> checkBusinessName(@RequestParam("name") String businessName) {
        boolean dupleicate = adminService.CheckDuplicated(businessName);

        if (dupleicate) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.of(HttpStatus.CONFLICT.value(), "중복된 값 입니다."));
        } else {
            return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "중복 검사를 통과했습니다."));
        }
    }

    @PostMapping("/business")
    public ResponseEntity<ApiResponse<Void>> addBusiness(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                         @RequestParam("name") String name,
                                                         @RequestParam("category") String category,
                                                         @RequestParam("address") String address,
                                                         @RequestParam("latitude") Double latitude,
                                                         @RequestParam("longitude") Double longitude,
                                                         @RequestParam("businessHours") String businessHours,
                                                         @RequestParam("phoneNumber") String phoneNumber,
                                                         @RequestParam("email") String email,
                                                         @RequestParam(value = "additionalInfo", required = false) String additionalInfo,

                                                         @RequestParam("service") String serviceJson, // JSON 문자열로 받음
                                                         @RequestPart("thumbnail") MultipartFile thumbnail,
                                                         @RequestPart(value = "serviceImage", required = false) MultipartFile[] serviceImages,
                                                         @RequestPart(value = "additionalImage", required = false) MultipartFile[] additionalImages) {

        adminService.createBusinessProc(name, category, thumbnail, address, latitude, longitude,
                businessHours, phoneNumber, email, serviceJson,
                serviceImages, additionalImages, additionalInfo,
                customUserDetails);

        return ResponseEntity.status(ResponseCode.CREATED.getStatusCode()).body(ApiResponse.of(ResponseCode.CREATED));
    }

    @GetMapping("/business")
    public ResponseEntity<ApiResponse<List<BusinessListResponse>>> getBusinessListAdmin(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<BusinessListResponse> businessListAdmin = adminService.getBusinessListAdmin(customUserDetails);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, businessListAdmin));
    }

    @DeleteMapping("/business/{businessId}")
    public ResponseEntity<ApiResponse<Void>> deleteBusiness(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("businessId") Long businessId) {
        adminService.deleteBusiness(customUserDetails, businessId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS));
    }

    @PatchMapping("/business/{businessId}")
    public ResponseEntity<ApiResponse<Void>> updateBusiness(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("businessId") Long businessId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "category", required = false) String category,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude,
            @RequestParam(value = "businessHours", required = false) String businessHours,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "addService", required = false) String addServiceJson,
            @RequestPart(value = "addServiceImage", required = false) MultipartFile[] addServiceImages,
            @RequestParam(value = "updateService", required = false) String updateServiceJson,
            @RequestPart(value = "updateServiceImage", required = false) MultipartFile[] updateServiceImages,
            @RequestParam(value = "removeServiceIds", required = false) List<Long> removeServiceIds,
            @RequestPart(value = "addAdditionalImage", required = false) MultipartFile[] addAdditionalImages,
            @RequestParam(value = "removeAdditionalImageIds", required = false) List<Long> removeAdditionalImageIds,
            @RequestParam(value = "additionalInfo", required = false) String additionalInfo
    ) {
        businessValidator.isValidAdmin(customUserDetails, businessId);
        adminService.processBusinessUpdate(
                businessId,
                name, category, thumbnail, address, latitude, longitude,
                businessHours, phoneNumber, email, additionalInfo,
                addServiceJson, addServiceImages,
                updateServiceJson, updateServiceImages,
                removeServiceIds,
                addAdditionalImages, removeAdditionalImageIds
        );

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS));
    }
}
