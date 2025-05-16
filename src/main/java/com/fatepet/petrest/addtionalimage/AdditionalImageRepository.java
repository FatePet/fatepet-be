package com.fatepet.petrest.addtionalimage;

import com.fatepet.petrest.business.FuneralBusiness;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdditionalImageRepository extends JpaRepository<AdditionalImage, Long> {
//    List<AdditionalImage> findAllByAdditionalInfoId(Long additionalInfoId);

    void deleteAllByBusiness(FuneralBusiness business);
}
