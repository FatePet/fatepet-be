//package com.fatepet.petrest.business;
//
//import com.fatepet.global.exception.FuneralBusinessException;
//import com.fatepet.global.response.ResponseCode;
//import com.fatepet.petrest.addtionalimage.AdditionalImage;
//import com.fatepet.petrest.addtionalimage.AdditionalImageRepository;
//import com.fatepet.petrest.addtionalinfo.AdditionalInfo;
//import com.fatepet.petrest.addtionalinfo.AdditionalInfoRepository;
//import com.fatepet.petrest.business.controller.dto.response.BusinessResponse;
//import com.fatepet.petrest.business.controller.dto.response.FuneralBusinessDetailsResponse;
//import com.fatepet.petrest.funeralproduct.FuneralProduct;
//import com.fatepet.petrest.funeralproduct.FuneralProductRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Comparator;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class FuneralBusinessService {
//
//    private final FuneralBusinessRepository funeralBusinessRepository;
//    private final FuneralProductRepository funeralProductRepository;
//    private final AdditionalInfoRepository additionalInfoRepository;
//    private final AdditionalImageRepository additionalImageRepository;
//
//    public List<BusinessResponse> getBusinessList(Pageable pageable) {
//        return funeralBusinessRepository.findAll(pageable)
//                .map(BusinessResponse::from).getContent();
//    }
//
//    public List<BusinessResponse> getBusinessListSortedByDistance(double userLat, double userLng, Pageable pageable) {
//        List<FuneralBusiness> businesses = funeralBusinessRepository.findAll();
//
//        return businesses.stream()
//                .sorted(Comparator.comparingDouble(b ->
//                        haversine(userLat, userLng, b.getLatitude(), b.getLongitude())))
//                .skip(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .map(BusinessResponse::from)
//                .toList();
//    }
//
//    public FuneralBusinessDetailsResponse getBusinessDetails(Long businessId) {
//        FuneralBusiness business = funeralBusinessRepository.findById(businessId)
//                .orElseThrow(() -> new FuneralBusinessException(ResponseCode.NOT_FOUND));
//
//        List<FuneralProduct> products = funeralProductRepository.findAllByBusinessId(businessId);
//
//        AdditionalInfo additionalInfo = additionalInfoRepository.findByBusinessId(businessId);
//        List<AdditionalImage> additionalImages = additionalImageRepository.findAllByAdditionalInfoId(additionalInfo.getId());
//
//        return FuneralBusinessDetailsResponse.from(business, products,additionalInfo, additionalImages);
//    }
//
//    private double haversine(double lat1, double lon1, double lat2, double lon2) {
//        final int R = 6371; // 지구 반지름 (km)
//        double dLat = Math.toRadians(lat2 - lat1);
//        double dLon = Math.toRadians(lon2 - lon1);
//        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
//                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
//                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        return R * c;
//    }
//}
