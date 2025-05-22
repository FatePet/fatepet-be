package com.fatepet.petrest.business;

import com.fatepet.petrest.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FuneralBusiness {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String category;

    private String address;

    private double latitude;

    private double longitude;

    private String mainImageUrl;

    private String businessHours;

    private String phoneNumber;

    private String email;

    private String additionalInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    private Integer recommendRank;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    private FuneralBusiness(String name, String category, String address, double latitude, double longitude, String mainImageUrl,
                            String businessHours, String phoneNumber, String email, String additionalInfo,
                            User owner, Integer recommendRank) {
        this.name = name;
        this.address = address;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mainImageUrl = mainImageUrl;
        this.businessHours = businessHours;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.additionalInfo = additionalInfo;
        this.owner = owner;
        this.recommendRank = recommendRank;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}
