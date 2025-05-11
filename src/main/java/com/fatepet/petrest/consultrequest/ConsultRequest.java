package com.fatepet.petrest.consultrequest;

import com.fatepet.petrest.funeralbusiness.FuneralBusiness;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ConsultRequest {

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
