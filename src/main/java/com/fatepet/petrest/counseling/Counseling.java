package com.fatepet.petrest.counseling;

import com.fatepet.petrest.business.FuneralBusiness;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Counseling {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funeral_business_id")
    private FuneralBusiness business;

    private ContactType contactType;

    private String phoneNumber;

    private String message;

    @Builder
    private Counseling(FuneralBusiness business, ContactType contactType, String phoneNumber, LocalDateTime createdAt, String message) {
        this.business = business;
        this.contactType = contactType;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.message = message;
    }

    private LocalDateTime createdAt;

}
