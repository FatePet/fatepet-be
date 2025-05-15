package com.fatepet.petrest.business.admin;

import com.fatepet.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
