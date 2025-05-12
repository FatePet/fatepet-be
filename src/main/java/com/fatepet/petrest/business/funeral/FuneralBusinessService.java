package com.fatepet.petrest.business.funeral;

import com.fatepet.petrest.business.controller.dto.request.BusinessSearchRequest;
import com.fatepet.petrest.business.controller.dto.response.BusinessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FuneralBusinessService {

    private final FuneralBusinessRepository funeralBusinessRepository;

    public List<BusinessResponse> getBusinessList(Pageable pageable) {
        return funeralBusinessRepository.findAll(pageable)
                .map(BusinessResponse::from).getContent();
    }

    public List<BusinessResponse> getBusinessListSortedByDistance(double userLat, double userLng, Pageable pageable) {
        List<FuneralBusiness> businesses = funeralBusinessRepository.findAll();

        return businesses.stream()
                .sorted(Comparator.comparingDouble(b ->
                        haversine(userLat, userLng, b.getLatitude(), b.getLongitude())))
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .map(BusinessResponse::from)
                .toList();
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구 반지름 (km)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
