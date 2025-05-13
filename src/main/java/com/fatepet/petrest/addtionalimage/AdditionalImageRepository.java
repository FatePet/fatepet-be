package com.fatepet.petrest.addtionalimage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdditionalImageRepository extends JpaRepository<AdditionalImage, Long> {
    List<AdditionalImage> findAllByAdditionalInfoId(Long additionalInfoId);
}
