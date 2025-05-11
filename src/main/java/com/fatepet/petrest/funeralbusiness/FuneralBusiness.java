package com.fatepet.petrest.funeralbusiness;

import com.fatepet.petrest.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class FuneralBusiness {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String address;

    private double latitude;

    private double longitude;

    private String mainImageUrl;

    private String businessHours;

    private String phoneNumber;

    private String email;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
