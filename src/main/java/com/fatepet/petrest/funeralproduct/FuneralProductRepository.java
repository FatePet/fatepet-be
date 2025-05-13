package com.fatepet.petrest.funeralproduct;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FuneralProductRepository extends JpaRepository<FuneralProduct, Long> {
    List<FuneralProduct> findAllByBusinessId(Long businessId);
}
