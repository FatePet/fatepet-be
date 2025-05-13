package com.fatepet.petrest.addtionalimage.controller.dto.response;

import com.fatepet.petrest.addtionalimage.AdditionalImage;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AdditionalImageResponse {

    private Long imageId;
    private String url;

    @Builder
    private AdditionalImageResponse(Long imageId, String url) {
        this.imageId = imageId;
        this.url = url;
    }

    public static AdditionalImageResponse from(AdditionalImage additionalImage) {
        return AdditionalImageResponse.builder()
                .imageId(additionalImage.getId())
                .url(additionalImage.getImageUrl())
                .build();
    }
}
