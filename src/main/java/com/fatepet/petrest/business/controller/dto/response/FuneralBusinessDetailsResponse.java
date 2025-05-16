//package com.fatepet.petrest.business.controller.dto.response;
//
//import com.fatepet.petrest.addtionalimage.AdditionalImage;
//import com.fatepet.petrest.addtionalinfo.AdditionalInfo;
//import com.fatepet.petrest.addtionalinfo.controller.dto.response.AdditionalInfoResponse;
//import com.fatepet.petrest.business.FuneralBusiness;
//import com.fatepet.petrest.funeralproduct.FuneralProduct;
//import com.fatepet.petrest.funeralproduct.controller.dto.response.FuneralProductResponse;
//import lombok.Builder;
//import lombok.Getter;
//
//import java.util.List;
//
//@Getter
//public class FuneralBusinessDetailsResponse {
//
//    private String name;
//
//    private String address;
//
//    private String mainImageUrl;
//
//    private String businessHours;
//
//    private String phoneNumber;
//
//    private String email;
//
//    private List<FuneralProductResponse> services;
//
//    private AdditionalInfoResponse additionalInfo;
//
//    @Builder
//    private FuneralBusinessDetailsResponse(String name, String address, String mainImageUrl, String businessHours, String phoneNumber,
//                                           String email, List<FuneralProductResponse> services, AdditionalInfoResponse additionalInfo) {
//        this.name = name;
//        this.address = address;
//        this.mainImageUrl = mainImageUrl;
//        this.businessHours = businessHours;
//        this.phoneNumber = phoneNumber;
//        this.email = email;
//        this.services = services;
//        this.additionalInfo = additionalInfo;
//    }
//
//    public static FuneralBusinessDetailsResponse from(FuneralBusiness business, List<FuneralProduct> products,
//                                             AdditionalInfo additionalInfo, List<AdditionalImage> additionalImages) {
//        List<FuneralProductResponse> services = products.stream()
//                .map(FuneralProductResponse::from)
//                .toList();
//
//        AdditionalInfoResponse additionalInfoResponse = AdditionalInfoResponse.from(additionalInfo, additionalImages);
//
//        return FuneralBusinessDetailsResponse.builder()
//                .name(business.getName())
//                .address(business.getAddress())
//                .mainImageUrl(business.getMainImageUrl())
//                .businessHours(business.getBusinessHours())
//                .phoneNumber(business.getPhoneNumber())
//                .email(business.getEmail())
//                .services(services)
//                .additionalInfo(additionalInfoResponse)
//                .build();
//    }
//}
