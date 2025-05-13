package com.fatepet.petrest.addtionalimage;

import com.fatepet.petrest.addtionalinfo.AdditionalInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdditionalImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "additional_info_id")
    private AdditionalInfo additionalInfo;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    private AdditionalImage(AdditionalInfo additionalInfo, String imageUrl) {
        this.additionalInfo = additionalInfo;
        this.imageUrl = imageUrl;
    }
}
