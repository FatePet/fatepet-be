package com.fatepet.petrest.funeralproduct;

import com.fatepet.petrest.business.funeral.FuneralBusiness;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private String price;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    private FuneralProduct(FuneralBusiness business, ProductCategory category, String name, String description, String price, String imageUrl) {
        this.business = business;
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
