package com.fatepet.petrest.business.controller.dto.response;

import com.fatepet.petrest.addtionalimage.AdditionalImage;
import com.fatepet.petrest.addtionalimage.controller.dto.response.AdditionalImageResponse;
import com.fatepet.petrest.business.FuneralBusiness;
import com.fatepet.petrest.funeralproduct.FuneralProduct;
import com.fatepet.petrest.funeralproduct.controller.dto.response.FuneralProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class FuneralBusinessDetailsResponse {

    private String name;

    private String address;

    private String category;

    private String mainImageUrl;

    private String businessHours;

    private String phoneNumber;

    private String email;

    private List<FuneralProductResponse> services;

    private AdditionalInfoResponse additionalInfo;

    @Builder
    private FuneralBusinessDetailsResponse(String name, String address, String category, String mainImageUrl, String businessHours, String phoneNumber,
                                           String email, List<FuneralProductResponse> services,
                                           String additionalInfo, List<AdditionalImageResponse> images) {

        AdditionalInfoResponse additionalInfoResponse = new AdditionalInfoResponse(images, additionalInfo);
        this.name = name;
        this.address = address;
        this.category = category;
        this.mainImageUrl = mainImageUrl;
        this.businessHours = businessHours;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.services = services;
        this.additionalInfo = additionalInfoResponse;
    }

    public static FuneralBusinessDetailsResponse from(FuneralBusiness business, List<FuneralProduct> products,
                                                      List<AdditionalImage> images) {
        List<FuneralProductResponse> services = products.stream()
                .map(FuneralProductResponse::from)
                .toList();

        List<AdditionalImageResponse> additionalImages = images.stream()
                .map(AdditionalImageResponse::from)
                .toList();

        return FuneralBusinessDetailsResponse.builder()
                .name(business.getName())
                .address(business.getAddress())
                .category("장묘")
                .mainImageUrl(business.getMainImageUrl())
                .businessHours(business.getBusinessHours())
                .phoneNumber(business.getPhoneNumber())
                .email(business.getEmail())
                .services(services)
                .additionalInfo(business.getAdditionalInfo())
                .images(additionalImages)
                .build();
    }

    @Getter
    @AllArgsConstructor
    public static class AdditionalInfoResponse {
        List<AdditionalImageResponse> images;
        private String description;
    }

}
