package com.fatepet.petrest.addtionalinfo.controller.dto.response;

import com.fatepet.petrest.addtionalimage.AdditionalImage;
import com.fatepet.petrest.addtionalimage.controller.dto.response.AdditionalImageResponse;
import com.fatepet.petrest.addtionalinfo.AdditionalInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdditionalInfoResponse {

    private List<AdditionalImageResponse> images;
    private String description;

    @Builder
    private AdditionalInfoResponse(List<AdditionalImageResponse> images, String description) {
        this.images = images;
        this.description = description;
    }

    public static AdditionalInfoResponse from(AdditionalInfo additionalInfo, List<AdditionalImage> additionalImages) {
        List<AdditionalImageResponse> imageResponses = additionalImages.stream()
                .map(AdditionalImageResponse::from)
                .toList();

        return AdditionalInfoResponse.builder()
                .images(imageResponses)
                .description(additionalInfo.getDescription())
                .build();


    }
}
