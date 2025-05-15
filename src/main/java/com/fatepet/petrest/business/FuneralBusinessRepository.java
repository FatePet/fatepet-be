package com.fatepet.petrest.business;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FuneralBusinessRepository extends JpaRepository<FuneralBusiness, Long> {

    Boolean existsByName(String name);
}
