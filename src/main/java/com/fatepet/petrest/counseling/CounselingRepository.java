package com.fatepet.petrest.counseling;

import com.fatepet.petrest.business.FuneralBusiness;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounselingRepository extends JpaRepository<Counseling, Long> {
    void deleteAllByBusiness(FuneralBusiness business);
}
