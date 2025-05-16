package com.fatepet.petrest.business.admin;

import com.fatepet.global.response.ApiResponse;
import com.fatepet.global.response.ResponseCode;
import com.fatepet.petrest.business.admin.request.FuneralProductRequest;
import com.fatepet.petrest.business.admin.response.BusinessListResponse;
import com.fatepet.petrest.user.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminBusinessController {

    private final AdminService adminService;

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
                                                         @RequestPart("thumbnail") MultipartFile thumbnail,
                                                         @RequestParam("address") String address,
                                                         @RequestParam("latitude") Double latitude,
                                                         @RequestParam("longitude") Double longitude,
                                                         @RequestParam("businessHours") String businessHours,
                                                         @RequestParam("phoneNumber") String phoneNumber,
                                                         @RequestParam("email") String email,

                                                         @RequestParam("service") String serviceJson, // JSON 문자열로 받음
                                                         @RequestPart(value = "serviceImage", required = false) MultipartFile[] serviceImages,
                                                         @RequestPart(value = "additionalImage", required = false) MultipartFile[] additionalImages,
                                                         @RequestParam(value = "additionalInfo", required = false) String additionalInfo) {
        adminService.addBusiness(name, category, thumbnail, address, latitude, longitude,
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
}
