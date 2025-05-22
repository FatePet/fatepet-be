package com.fatepet.petrest.funeralproduct.controller.dto.response;

import com.fatepet.petrest.funeralproduct.FuneralProduct;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FuneralProductResponse {

    private Long serviceId;

    private String category;

    private String name;

    private String description;

    private String imageUrl;

    private String price;

    @Builder
    private FuneralProductResponse(Long serviceId, String category, String name, String description, String imageUrl, String price) {
        this.serviceId = serviceId;
        this.category = category;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public static FuneralProductResponse from(FuneralProduct product){
        return FuneralProductResponse.builder()
                .serviceId(product.getId())
                .category(product.getCategory().getDisplayName())
                .name(product.getName())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .price(product.getPrice())
                .build();
    }
}
