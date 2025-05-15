package com.fatepet.petrest.business.admin;

import com.fatepet.petrest.business.FuneralBusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final FuneralBusinessRepository businessRepository;

    public boolean CheckDuplicated(String businessName) {
        return businessRepository.existsByName(businessName);
    }
}
