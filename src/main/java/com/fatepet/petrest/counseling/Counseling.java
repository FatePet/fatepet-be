package com.fatepet.petrest.counseling;

import com.fatepet.petrest.business.FuneralBusiness;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

    private LocalDateTime createdAt;

}
