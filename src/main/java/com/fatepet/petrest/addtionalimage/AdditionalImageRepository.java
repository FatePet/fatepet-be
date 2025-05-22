package com.fatepet.petrest.addtionalimage;

import com.fatepet.petrest.business.FuneralBusiness;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdditionalImageRepository extends JpaRepository<AdditionalImage, Long> {

    void deleteAllByBusiness(FuneralBusiness business);

    List<AdditionalImage> findAllByBusiness_Id(Long businessId);

    List<AdditionalImage> findByBusiness(FuneralBusiness business);
}
