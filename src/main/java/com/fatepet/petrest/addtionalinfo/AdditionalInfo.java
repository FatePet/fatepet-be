package com.fatepet.petrest.addtionalinfo;

import com.fatepet.petrest.business.FuneralBusiness;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    private AdditionalInfo(FuneralBusiness business, String imageUrl, String description) {
        this.business = business;
        this.imageUrl = imageUrl;
        this.description = description;
    }
}
