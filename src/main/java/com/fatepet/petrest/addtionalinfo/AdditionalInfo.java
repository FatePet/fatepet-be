package com.fatepet.petrest.addtionalinfo;

import com.fatepet.petrest.funeralbusiness.FuneralBusiness;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AdditionalInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private FuneralBusiness business;

    private String imageUrl;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
