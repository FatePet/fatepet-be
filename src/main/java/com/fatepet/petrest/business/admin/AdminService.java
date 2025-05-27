package com.fatepet.petrest.business.admin;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatepet.petrest.addtionalimage.AdditionalImage;
import com.fatepet.petrest.addtionalimage.AdditionalImageRepository;
import com.fatepet.petrest.business.FuneralBusiness;
import com.fatepet.petrest.business.FuneralBusinessRepository;
import com.fatepet.petrest.business.admin.dto.AddServiceDto;
import com.fatepet.petrest.business.admin.dto.BusinessInfoDto;
import com.fatepet.petrest.business.admin.dto.UpdateServiceDto;
import com.fatepet.petrest.business.admin.dto.response.BusinessListResponse;
import com.fatepet.petrest.business.admin.util.BusinessUtil;
import com.fatepet.petrest.business.admin.valid.BusinessValidator;
import com.fatepet.petrest.common.S3Uploader;
import com.fatepet.petrest.counseling.CounselingRepository;
import com.fatepet.petrest.funeralproduct.FuneralProduct;
import com.fatepet.petrest.funeralproduct.FuneralProductRepository;
import com.fatepet.petrest.funeralproduct.PriceType;
import com.fatepet.petrest.funeralproduct.ProductCategory;
import com.fatepet.petrest.user.User;
import com.fatepet.petrest.user.UserRepository;
import com.fatepet.petrest.user.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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
    private final BusinessValidator businessValidator;
    private final BusinessUtil businessUtil;

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
                        .mainImageUrl(business.getMainImageUrl())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void createBusinessProc(String name, String category, MultipartFile thumbnail,
                                   String address, Double latitude, Double longitude,
                                   String businessHours, String phoneNumber, String email,
                                   String serviceJson, MultipartFile[] serviceImages,
                                   MultipartFile[] additionalImages, String additionalInfo,
                                   CustomUserDetails customUserDetails) {
        BusinessInfoDto businessInfoDto = BusinessInfoDto.builder()
                .name(name)
                .category(category)
                .thumbnail(thumbnail)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .businessHours(businessHours)
                .phoneNumber(phoneNumber)
                .email(email)
                .additionalInfo(additionalInfo)
                .build();

        FuneralBusiness business = createBusiness(businessInfoDto, customUserDetails);

        List<AddServiceDto> addServiceDtoList = businessUtil.parseAddServiceJson(serviceJson);

        createService(serviceImages, addServiceDtoList, business);

        createAdditionalImage(additionalImages, business);
    }

    @NotNull
    private FuneralBusiness createBusiness(BusinessInfoDto businessInfoDto, CustomUserDetails customUserDetails) {

        businessValidator.validDuplicateBusinessName(businessInfoDto.getName());
        businessValidator.validEmail(businessInfoDto.getEmail());
        businessValidator.validPhoneNumber(businessInfoDto.getPhoneNumber());
        businessValidator.validCategory(businessInfoDto.getCategory());

        User user = userRepository.findByUsername(customUserDetails.getUsername());
        String mainImageUrl = s3Uploader.uploadFile(businessInfoDto.getThumbnail());

        FuneralBusiness business = FuneralBusiness.builder()
                .name(businessInfoDto.getName())
                .mainImageUrl(mainImageUrl)
                .category(businessInfoDto.getCategory())
                .address(businessInfoDto.getAddress())
                .latitude(businessInfoDto.getLatitude())
                .longitude(businessInfoDto.getLongitude())
                .businessHours(businessInfoDto.getBusinessHours())
                .phoneNumber(businessInfoDto.getPhoneNumber())
                .email(businessInfoDto.getEmail())
                .additionalInfo(businessInfoDto.getAdditionalInfo())
                .owner(user)
                .build();
        funeralBusinessRepository.save(business);
        return business;
    }


    private void createAdditionalImage(MultipartFile[] additionalImages, FuneralBusiness business) {
        if (additionalImages == null) { return; }
        businessValidator.validAdditionalImageCount(additionalImages);
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

    private void createService(MultipartFile[] serviceImages, List<AddServiceDto> serviceList, FuneralBusiness business) {
        if (serviceList == null || serviceList.isEmpty()) { return;}
        int imageIndex = 0;
        for (AddServiceDto dto : serviceList) {

            businessValidator.validAddService(dto);

            String imageUrl = null;
            if (dto.getImage()) {
                if (serviceImages == null) {
                    throw new IllegalArgumentException("서비스 이미지 개수가 부족합니다.");
                }
                try {
                    imageUrl = s3Uploader.uploadFile(serviceImages[imageIndex++]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("서비스 이미지 개수가 부족합니다.");
                }
            }

            ProductCategory productCategory = ProductCategory.fromDisplayName(dto.getType());
            PriceType priceType = PriceType.fromDisplayName(dto.getPriceType());

            FuneralProduct product = FuneralProduct.builder()
                    .business(business)
                    .category(productCategory)
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .priceType(priceType)
                    .price(dto.getPrice())
                    .imageUrl(imageUrl)
                    .build();

            funeralProductRepository.save(product);
        }
    }

    @Transactional
    public void deleteBusiness(CustomUserDetails customUserDetails, Long businessId) {
        User user = userRepository.findByUsername(customUserDetails.getUsername());
        Optional<FuneralBusiness> business = funeralBusinessRepository.findById(businessId);
        if (business.isEmpty()) {
            throw new IllegalArgumentException("해당 업체가 존재하지 않습니다.");
        }

        FuneralBusiness funeralBusiness = business.get();

        if (!funeralBusiness.getOwner().equals(user)) {
            throw new IllegalArgumentException("등록한 업체가 아닙니다.");
        }

        funeralProductRepository.deleteAllByBusiness(funeralBusiness);
        counselingRepository.deleteAllByBusiness(funeralBusiness);
        additionalImageRepository.deleteAllByBusiness(funeralBusiness);

        funeralBusinessRepository.delete(funeralBusiness);
    }

    @Transactional
    public void processBusinessUpdate(
            Long businessId,
            String name,
            String category,
            MultipartFile thumbnail,
            String address,
            Double latitude,
            Double longitude,
            String businessHours,
            String phoneNumber,
            String email,
            String additionalInfo,
            String addServiceJson,
            MultipartFile[] addServiceImages,
            String updateServiceJson,
            MultipartFile[] updateServiceImages,
            List<Long> removeServiceIds,
            MultipartFile[] addAdditionalImages,
            List<Long> removeAdditionalImageIds
    ) {
        BusinessInfoDto businessInfoDto = BusinessInfoDto.builder()
                .name(name)
                .category(category)
                .thumbnail(thumbnail)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .businessHours(businessHours)
                .phoneNumber(phoneNumber)
                .email(email)
                .additionalInfo(additionalInfo)
                .build();

        List<AddServiceDto> addServiceDtoList = businessUtil.parseAddServiceJson(addServiceJson);
        List<UpdateServiceDto> updateServiceDtoList = businessUtil.parseUpdateServiceJson(updateServiceJson);

        FuneralBusiness business = updateBusiness(businessId, businessInfoDto);



        businessValidator.validServiceCount(addServiceDtoList == null ? 0 : addServiceDtoList.size(), removeServiceIds == null ? 0 : removeServiceIds.size(), business);

        createService(addServiceImages, addServiceDtoList, business);
        updateService(updateServiceImages, updateServiceDtoList, business);
        removeService(removeServiceIds, business);

        businessValidator.validAdditionalImageCount(addAdditionalImages == null ? 0 : addAdditionalImages.length, removeAdditionalImageIds == null ? 0 : removeAdditionalImageIds.size(), business);

        createAdditionalImage(addAdditionalImages, business);
        removeAdditionalImage(removeAdditionalImageIds, business);
    }

    private FuneralBusiness updateBusiness(Long businessId, BusinessInfoDto businessInfoDto) {

        FuneralBusiness business = funeralBusinessRepository.findById(businessId).get();

        if (businessInfoDto.getName() != null) {
            businessValidator.validDuplicateBusinessName(businessInfoDto.getName());
            business.setName(businessInfoDto.getName());
        }
        if (businessInfoDto.getCategory() != null) {
            businessValidator.validCategory(businessInfoDto.getCategory());
            business.setCategory(businessInfoDto.getCategory());
        }
        if (businessInfoDto.getAddress() != null) business.setAddress(businessInfoDto.getAddress());
        if (businessInfoDto.getLatitude() != null) business.setLatitude(businessInfoDto.getLatitude());
        if (businessInfoDto.getLongitude() != null) business.setLongitude(businessInfoDto.getLongitude());
        if (businessInfoDto.getBusinessHours() != null) business.setBusinessHours(businessInfoDto.getBusinessHours());
        if (businessInfoDto.getPhoneNumber() != null) {
            businessValidator.validPhoneNumber(businessInfoDto.getPhoneNumber());
            business.setPhoneNumber(businessInfoDto.getPhoneNumber());
        }
        if (businessInfoDto.getEmail() != null) {
            businessValidator.validEmail(businessInfoDto.getEmail());
            business.setEmail(businessInfoDto.getEmail());
        }
        if (businessInfoDto.getAdditionalInfo() != null) business.setAdditionalInfo(businessInfoDto.getAdditionalInfo());
        if (businessInfoDto.getThumbnail() != null) {
            String thumbnailUrl = s3Uploader.uploadFile(businessInfoDto.getThumbnail());
            business.setMainImageUrl(thumbnailUrl);
        }
        business.setUpdatedAt(LocalDateTime.now());

        return business;
    }

    private void updateService(MultipartFile[] serviceImages, List<UpdateServiceDto> serviceList, FuneralBusiness business) {
        if (serviceList == null || serviceList.isEmpty()) return;

        int imageIndex = 0;

        for (int i = 0; i < serviceList.size(); i++) {
            UpdateServiceDto dto = serviceList.get(i);
            businessValidator.validExistService(dto.getServiceId());
            businessValidator.validIsBusinessInService(dto.getServiceId(), business);
            FuneralProduct service = funeralProductRepository.findById(dto.getServiceId()).get();

            if (dto.getType() != null) {
                businessValidator.validServiceType(dto.getType());
                ProductCategory productCategory = ProductCategory.fromDisplayName(dto.getType());
                service.setCategory(productCategory);
            }
            if (dto.getName() != null) service.setName(dto.getName());
            if (dto.getDescription() != null) service.setDescription(dto.getDescription());
            if (dto.getPriceType() != null) {
                businessValidator.validPriceType(dto.getPriceType(), dto.getPrice());
                PriceType priceType = PriceType.fromDisplayName(dto.getPriceType());
                service.setPriceType(priceType);
            }
            if (dto.getPrice() != null) service.setPrice(dto.getPrice());

            if (dto.getImageType() == 1) {
                if (serviceImages == null) {
                    throw new IllegalArgumentException("수정할 서비스 이미지 개수가 부족합니다.");
                }
                try {
                    service.setImageUrl(s3Uploader.uploadFile(serviceImages[imageIndex++]));
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("수정할 서비스 이미지 개수가 부족합니다.");
                }
            } else if (dto.getImageType() == 2) {
                service.setImageUrl(null);
            }

            service.setUpdatedAt(LocalDateTime.now());
        }
    }

    private void removeService(List<Long> removeServiceIds, FuneralBusiness business) {
        if (removeServiceIds == null || removeServiceIds.isEmpty()) return;
        for (Long removeServiceId : removeServiceIds) {
            businessValidator.validExistService(removeServiceId);
            businessValidator.validIsBusinessInService(removeServiceId, business);
            funeralProductRepository.deleteById(removeServiceId);
        }
    }

    private void removeAdditionalImage(List<Long> removeAdditionalImageIds, FuneralBusiness business) {
        if (removeAdditionalImageIds == null || removeAdditionalImageIds.isEmpty()) return;
        for (Long removeAdditionalImageId : removeAdditionalImageIds) {
            businessValidator.validExistAdditionalImage(removeAdditionalImageId);
            businessValidator.validIsBusinessInAdditionalImage(removeAdditionalImageId, business);
            additionalImageRepository.deleteById(removeAdditionalImageId);
        }
    }
}
