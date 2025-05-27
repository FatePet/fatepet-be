package com.fatepet.petrest.funeralproduct.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fatepet.petrest.funeralproduct.FuneralProduct;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FuneralProductResponse {

    private Long serviceId;

    private String type;

    private String name;

    private String description;

    private String imageUrl;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String price;

    private String priceType;

    @Builder
    private FuneralProductResponse(Long serviceId, String type, String name, String description, String imageUrl, String price, String priceType) {
        this.serviceId = serviceId;
        this.type = type;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.priceType = priceType;
    }

    public static FuneralProductResponse from(FuneralProduct product){
        return FuneralProductResponse.builder()
                .serviceId(product.getId())
                .type(product.getCategory().getDisplayName())
                .name(product.getName())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .price(product.getPrice())
                .priceType(product.getPriceType().getDisplayName())
                .build();
    }
}
