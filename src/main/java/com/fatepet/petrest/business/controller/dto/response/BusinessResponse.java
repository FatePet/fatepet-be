package com.fatepet.petrest.business.controller.dto.response;

import com.fatepet.petrest.business.funeral.FuneralBusiness;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BusinessResponse {

    private Long businessId;

    private String name;

    private String address;

    private String mainImageUrl;

    private String businessHours;

    private String phoneNumber;

    @Builder
    private BusinessResponse(Long businessId, String name, String address, String mainImageUrl, String businessHours, String phoneNumber) {
        this.businessId = businessId;
        this.name = name;
        this.address = address;
        this.mainImageUrl = mainImageUrl;
        this.businessHours = businessHours;
        this.phoneNumber = phoneNumber;
    }

    public static BusinessResponse from(FuneralBusiness entity) {
        return BusinessResponse.builder()
                .businessId(entity.getId())
                .name(entity.getName())
                .address(entity.getAddress())
                .mainImageUrl(entity.getMainImageUrl())
                .businessHours(entity.getBusinessHours())
                .phoneNumber(entity.getPhoneNumber())
                .build();
    }
}
