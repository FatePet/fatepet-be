package com.fatepet.petrest.addtionalinfo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdditionalInfoRepository extends JpaRepository<AdditionalInfo, Long> {
    AdditionalInfo findByBusinessId(Long businessId);
}
