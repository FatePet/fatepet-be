package com.fatepet.petrest.counseling.controller;

import com.fatepet.petrest.global.response.ApiResponse;
import com.fatepet.petrest.global.response.ResponseCode;
import com.fatepet.petrest.counseling.CounselingService;
import com.fatepet.petrest.counseling.controller.dto.request.CounselingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business/consultations")
@RequiredArgsConstructor
public class CounselingController {

    private final CounselingService counselingService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> sendCounselingRequest(@RequestBody CounselingRequest request){
        counselingService.sendCounselingRequest(request.getBusinessId(), request.getPhoneNumber(), request.getContactType() ,request.getInquiry());
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS));
    }


}
