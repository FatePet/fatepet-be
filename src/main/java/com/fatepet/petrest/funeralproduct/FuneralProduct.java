package com.fatepet.petrest.funeralproduct;

import com.fatepet.petrest.business.FuneralBusiness;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class FuneralProduct {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private FuneralBusiness business;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private PriceType priceType;

    private String price;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    private FuneralProduct(FuneralBusiness business, ProductCategory category, String name, String description, PriceType priceType, String price, String imageUrl) {
        this.business = business;
        this.category = category;
        this.name = name;
        this.description = description;
        this.priceType = priceType;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
