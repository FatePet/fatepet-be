package com.fatepet.petrest.business.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatepet.petrest.addtionalimage.AdditionalImage;
import com.fatepet.petrest.addtionalimage.AdditionalImageRepository;
import com.fatepet.petrest.business.FuneralBusiness;
import com.fatepet.petrest.business.FuneralBusinessRepository;
import com.fatepet.petrest.business.admin.request.FuneralProductRequest;
import com.fatepet.petrest.business.admin.response.BusinessListResponse;
import com.fatepet.petrest.common.S3Uploader;
import com.fatepet.petrest.counseling.CounselingRepository;
import com.fatepet.petrest.funeralproduct.FuneralProduct;
import com.fatepet.petrest.funeralproduct.FuneralProductRepository;
import com.fatepet.petrest.funeralproduct.ProductCategory;
import com.fatepet.petrest.user.User;
import com.fatepet.petrest.user.UserRepository;
import com.fatepet.petrest.user.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final FuneralBusinessRepository funeralBusinessRepository;
    private final UserRepository userRepository;
    private final AdditionalImageRepository additionalImageRepository;
    private final FuneralProductRepository funeralProductRepository;
    private final CounselingRepository counselingRepository;
    private final ObjectMapper objectMapper;
    private final S3Uploader s3Uploader;

    public boolean CheckDuplicated(String businessName) {
        return funeralBusinessRepository.existsByName(businessName);
    }

    @Transactional
    public List<BusinessListResponse> getBusinessListAdmin(CustomUserDetails customUserDetails) {
        User user = userRepository.findByUsername(customUserDetails.getUsername());
        List<FuneralBusiness> businesses = funeralBusinessRepository.findByOwner(user);

        return businesses.stream()
                .map(business -> BusinessListResponse.builder()
                        .businessId(business.getId())
                        .name(business.getName())
                        .address(business.getAddress())
                        .category(business.getCategory())
                        .thumbnailUrl(business.getMainImageUrl())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void addBusiness(String name, String category, MultipartFile thumbnail,
                            String address, Double latitude, Double longitude,
                            String businessHours, String phoneNumber, String email,
                            String serviceJson, MultipartFile[] serviceImages,
                            MultipartFile[] additionalImages, String additionalInfo,
                            CustomUserDetails customUserDetails) {
        User user = userRepository.findByUsername(customUserDetails.getUsername());
        String mainImageUrl = s3Uploader.uploadFile(thumbnail);

        FuneralBusiness business = FuneralBusiness.builder()
                .name(name)
                .mainImageUrl(mainImageUrl)
                .category(category)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .businessHours(businessHours)
                .phoneNumber(phoneNumber)
                .email(email)
                .additionalInfo(additionalInfo)
                .owner(user)
                .build();
        funeralBusinessRepository.save(business);

        List<FuneralProductRequest> serviceList = parseServiceJson(serviceJson);

        createService(serviceImages, serviceList, business);

        createAdditionalImage(additionalImages, business);
    }

    private void createAdditionalImage(MultipartFile[] additionalImages, FuneralBusiness business) {
        if (additionalImages != null) {
            for (MultipartFile file : additionalImages) {
                String url = s3Uploader.uploadFile(file);
                AdditionalImage img = AdditionalImage.builder()
                        .business(business)
                        .imageUrl(url)
                        .build();
                additionalImageRepository.save(img);
            }
        }
    }

    private void createService(MultipartFile[] serviceImages, List<FuneralProductRequest> serviceList, FuneralBusiness business) {
        int imageIndex = 0;
        for (FuneralProductRequest dto : serviceList) {

            String imageUrl = null;
            if (dto.isImage()) {
                if (serviceImages == null || imageIndex >= serviceImages.length) {
                    throw new IllegalArgumentException("서비스 이미지 개수가 부족합니다.");
                }
                imageUrl = s3Uploader.uploadFile(serviceImages[imageIndex++]);
            }

            FuneralProduct product = FuneralProduct.builder()
                    .business(business)
                    .category(ProductCategory.valueOf(dto.getType()))
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .price(dto.getPrice())
                    .imageUrl(imageUrl)
                    .build();

            funeralProductRepository.save(product);
        }
    }

    private List<FuneralProductRequest> parseServiceJson(String serviceJson) {
        List<FuneralProductRequest> serviceList;
        try {
            serviceList = objectMapper.readValue(serviceJson, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("서비스 항목 JSON 파싱 실패", e);
        }
        return serviceList;
    }

    @Transactional
    public void deleteBusiness(CustomUserDetails customUserDetails, Long businessId) {
        User user = userRepository.findByUsername(customUserDetails.getUsername());
        Optional<FuneralBusiness> business = funeralBusinessRepository.findById(businessId);
        if (business.isEmpty()) {
            log.error("해당 없체 존대 하지 않음 오류");
            throw new IllegalArgumentException("해당 업체가 존재하지 않습니다.");
        }

        FuneralBusiness funeralBusiness = business.get();

        if (!funeralBusiness.getOwner().equals(user)) {
            log.error("등록한 업체 아닌 오류");
            throw new IllegalArgumentException("등록한 업체가 아닙니다.");
        }

        funeralProductRepository.deleteAllByBusiness(funeralBusiness);
        counselingRepository.deleteAllByBusiness(funeralBusiness);
        additionalImageRepository.deleteAllByBusiness(funeralBusiness);

        funeralBusinessRepository.delete(funeralBusiness);
    }

}
