package com.fatepet.petrest.funeralproduct;

import com.fatepet.petrest.business.funeral.FuneralBusiness;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
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

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
