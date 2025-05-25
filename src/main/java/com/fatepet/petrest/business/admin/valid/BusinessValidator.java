package com.fatepet.petrest.business.admin.valid;

import com.fatepet.petrest.addtionalimage.AdditionalImage;
import com.fatepet.petrest.addtionalimage.AdditionalImageRepository;
import com.fatepet.petrest.business.FuneralBusiness;
import com.fatepet.petrest.business.FuneralBusinessRepository;
import com.fatepet.petrest.business.admin.dto.AddServiceDto;
import com.fatepet.petrest.funeralproduct.FuneralProduct;
import com.fatepet.petrest.funeralproduct.FuneralProductRepository;
import com.fatepet.petrest.user.User;
import com.fatepet.petrest.user.UserRepository;
import com.fatepet.petrest.user.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class BusinessValidator {

    private final FuneralBusinessRepository funeralBusinessRepository;
    private final UserRepository userRepository;
    private final FuneralProductRepository funeralProductRepository;
    private final AdditionalImageRepository additionalImageRepository;

    public void validDuplicateBusinessName(String businessName) {
        if (funeralBusinessRepository.existsByName(businessName)) {
            throw new IllegalArgumentException("이미 등록된 업체 명 입니다.");
        }
    }

    public void validAdditionalImageCount(MultipartFile[] additionalImages) {
        int count;
        if (additionalImages == null) {count = 0;}
        else {count = additionalImages.length;}
        if (count > 10) {
            throw new IllegalArgumentException("추가사진은 최대 10장 입니다.");
        }
    }

    public void validAdditionalImageCount(int addCount, int removeCount, FuneralBusiness business) {
        List<AdditionalImage> byBusiness = additionalImageRepository.findByBusiness(business);
        int count = byBusiness.size() + addCount - removeCount;
        if (!(0 <= count && count <= 10)) {
            throw new IllegalArgumentException("추가 사진은 0~10장이여야 합니다.");
        }
    }

    public void validAddService(AddServiceDto dto) {
        if (dto.getType() == null || dto.getName() == null || dto.getPrice() == null || dto.getImage() == null) {
            throw new IllegalArgumentException("서비스 Json 필수값 누락.");
        }
    }

    public void validEmail(String email) {
        if (!Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", email)) {
            throw new IllegalArgumentException("이메일 형식을 맞춰주세요.");
        }
    }

    public void validPhoneNumber(String phone) {
        if (!Pattern.matches("^\\d{10,11}$", phone)) {
            throw new IllegalArgumentException("전화번호는 숫자만 입력하며 10자리 또는 11자리여야 합니다.");
        }
    }

    public void validServiceCount(int addCount, int removeCount, FuneralBusiness business) {
        List<FuneralProduct> byBusiness = funeralProductRepository.findByBusiness(business);
        int count = byBusiness.size() + addCount - removeCount;
        if (!(count > 0)) {
            throw new IllegalArgumentException("서비스는 최소 1개이상 이어야 합니다.");
        }
    }

    public void validExistService(Long serviceId) {
        if (!funeralProductRepository.existsById(serviceId)) {
            throw new IllegalArgumentException("잘못된 서비스 ID");
        }
    }

    public void validIsBusinessInService(Long serviceId, FuneralBusiness business) {
        FuneralProduct service = funeralProductRepository.findById(serviceId).get();
        if (service.getBusiness().getId() != business.getId()) {
            throw new IllegalArgumentException("잘못된 서비스 ID");
        }
    }

    public void validExistAdditionalImage(Long removeAdditionalImageId) {
        if (!additionalImageRepository.existsById(removeAdditionalImageId)) {
            throw new IllegalArgumentException("잘못된 추가사진 ID");
        }
    }

    public void validIsBusinessInAdditionalImage(Long removeAdditionalImageId, FuneralBusiness business) {
        AdditionalImage additionalImage = additionalImageRepository.findById(removeAdditionalImageId).get();
        if (additionalImage.getBusiness().getId() != business.getId()) {
            throw new IllegalArgumentException("잘못된 추가사진 ID");
        }
    }

    public void isValidAdmin(CustomUserDetails customUserDetails, Long businessId) {
        if (customUserDetails == null) {
            return;
        }
        User user = userRepository.findByUsername(customUserDetails.getUsername());
        FuneralBusiness funeralBusiness;
        try {
            funeralBusiness = funeralBusinessRepository.findById(businessId).get();
        } catch (Exception e) {
            throw new IllegalArgumentException("해당 업체가 없습니다.");
        }
        if (!funeralBusiness.getOwner().equals(user)) {
            throw new IllegalArgumentException("등록한 업체가 아닙니다.");
        }
    }

}
