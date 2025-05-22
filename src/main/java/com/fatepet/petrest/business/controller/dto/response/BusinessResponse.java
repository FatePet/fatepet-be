package com.fatepet.petrest.business.controller.dto.response;

import com.fatepet.petrest.business.FuneralBusiness;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BusinessResponse {

    private Long businessId;

    private String name;

    private String address;

    private String category;

    private String mainImageUrl;

    private String businessHours;

    private String phoneNumber;

    @Builder
    private BusinessResponse(Long businessId, String name, String address, String category, String mainImageUrl, String businessHours, String phoneNumber) {
        this.businessId = businessId;
        this.name = name;
        this.address = address;
        this.category = category;
        this.mainImageUrl = mainImageUrl;
        this.businessHours = businessHours;
        this.phoneNumber = phoneNumber;
    }

    public static BusinessResponse from(FuneralBusiness business) {
        return BusinessResponse.builder()
                .businessId(business.getId())
                .name(business.getName())
                .address(business.getAddress())
                .category("장묘")
                .mainImageUrl(business.getMainImageUrl())
                .businessHours(business.getBusinessHours())
                .phoneNumber(business.getPhoneNumber())
                .build();
    }
}
