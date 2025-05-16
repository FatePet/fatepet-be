package com.fatepet.petrest.business;

import com.fatepet.petrest.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FuneralBusinessRepository extends JpaRepository<FuneralBusiness, Long> {

    boolean existsByName(String name);

    List<FuneralBusiness> findByOwner(User owner);
}
