package com.fatepet.petrest.business.funeral;

import com.fatepet.global.exception.FuneralBusinessException;
import com.fatepet.petrest.addtionalimage.AdditionalImage;
import com.fatepet.petrest.addtionalimage.AdditionalImageRepository;
import com.fatepet.petrest.addtionalimage.controller.dto.response.AdditionalImageResponse;
import com.fatepet.petrest.addtionalinfo.AdditionalInfo;
import com.fatepet.petrest.addtionalinfo.AdditionalInfoRepository;
import com.fatepet.petrest.addtionalinfo.controller.dto.response.AdditionalInfoResponse;
import com.fatepet.petrest.business.controller.dto.response.BusinessResponse;
import com.fatepet.petrest.business.controller.dto.response.FuneralBusinessDetailsResponse;
import com.fatepet.petrest.funeralproduct.FuneralProduct;
import com.fatepet.petrest.funeralproduct.FuneralProductRepository;
import com.fatepet.petrest.funeralproduct.ProductCategory;
import com.fatepet.petrest.funeralproduct.controller.dto.response.FuneralProductResponse;
import com.fatepet.petrest.user.Role;
import com.fatepet.petrest.user.User;
import com.fatepet.petrest.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.fatepet.petrest.SortOption.*;
import static com.fatepet.petrest.funeralproduct.ProductCategory.*;
import static com.fatepet.petrest.funeralproduct.ProductCategory.OPTIONAL;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class FuneralBusinessServiceTest {

    @Autowired
    private FuneralBusinessService funeralBusinessService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FuneralBusinessRepository funeralBusinessRepository;

    @Autowired
    private FuneralProductRepository funeralProductRepository;

    @Autowired
    private AdditionalInfoRepository additionalInfoRepository;

    @Autowired
    private AdditionalImageRepository additionalImageRepository;

    @DisplayName("장묘업체들을 RECOMMEND 조건으로 내림차순 정렬하여 조회한다.")
    @Test
    void getBusinessListOrderByRecommend() {
        // given
        User user = createUser();
        User owner = userRepository.save(user);

        FuneralBusiness b1 = createBusiness("b1", owner, "adr1", 10, 0, 0);
        FuneralBusiness b2 = createBusiness("b2", owner, "adr2", 1, 0, 0);
        FuneralBusiness b3 = createBusiness("b3", owner, "adr3", 5, 0, 0);
        funeralBusinessRepository.saveAll(List.of(b1, b2, b3));

        Pageable pageable = PageRequest.of(0, 10, RECOMMEND.getSort());

        // when
        List<BusinessResponse> result = funeralBusinessService.getBusinessList(pageable);

        // then
        assertThat(result).hasSize(3)
                .extracting("name", "address")
                .containsExactly(
                        tuple("b2", "adr2"),
                        tuple("b3", "adr3"),
                        tuple("b1", "adr1")
                );
    }

    @DisplayName("장묘업체들을 DISTANCE 조건으로 정렬하여 조회한다.")
    @Test
    void getBusinessListOrderByDistance() {
        // given
        User user = createUser();
        User owner = userRepository.save(user);

        FuneralBusiness b1 = createBusiness("b1", owner, "adr1", 10, 37.5700, 126.9768);    // 가까움
        FuneralBusiness b2 = createBusiness("b2", owner, "adr2", 10, 37.4800, 127.0300);    // 중간
        FuneralBusiness b3 = createBusiness("b3", owner, "adr3", 5, 37.3500, 127.1000);     // 멀다
        funeralBusinessRepository.saveAll(List.of(b1, b2, b3));

        Pageable pageable = PageRequest.of(0, 10);

        // when
        List<BusinessResponse> result = funeralBusinessService.getBusinessListSortedByDistance(37.5563, 126.9723, pageable);
        // then
        assertThat(result).hasSize(3)
                .extracting("name", "address")
                .containsExactly(
                        tuple("b1", "adr1"),
                        tuple("b2", "adr2"),
                        tuple("b3", "adr3")
                );

    }

    @DisplayName("업체Id로 업체 상세정보를 조회한다.")
    @Test
    void getBusinessDetails() {
        // given
        User user = createUser();
        User owner = userRepository.save(user);

        // 업체 저장
        FuneralBusiness b1 = createBusiness("b1", owner, "adr1", 10, 0, 0);
        FuneralBusiness savedBusiness = funeralBusinessRepository.save(b1);

        // 서비스 저장
        FuneralProduct product1 = createProduct(savedBusiness, BASIC, "product1", "description1");
        FuneralProduct product2 = createProduct(savedBusiness, OPTIONAL, "product2", "description2");
        funeralProductRepository.saveAll(List.of(product1, product2));

        // 추가정보 저장
        AdditionalInfo info = createAdditionalInfo(savedBusiness);
        AdditionalInfo savedInfo = additionalInfoRepository.save(info);

        // 추가 이미지 저장
        AdditionalImage image1 = createAdditionalImage(info, "image1.jpg");
        AdditionalImage image2 = createAdditionalImage(info, "image2.jpg");
        additionalImageRepository.saveAll(List.of(image1, image2));

        // when
        FuneralBusinessDetailsResponse response = funeralBusinessService.getBusinessDetails(savedBusiness.getId());

        // then
        assertThat(response.getName()).isEqualTo(savedBusiness.getName());
        assertThat(response.getEmail()).isEqualTo(savedBusiness.getEmail());

        List<FuneralProductResponse> services = response.getServices();
        assertThat(services).hasSize(2)
                .extracting("category", "name", "description")
                .containsExactlyInAnyOrder(
                        tuple(BASIC.getDisplayName(), "product1", "description1"),
                        tuple(OPTIONAL.getDisplayName(), "product2", "description2")
                );

        AdditionalInfoResponse additionalInfo = response.getAdditionalInfo();
        assertThat(additionalInfo.getDescription()).isEqualTo(savedInfo.getDescription());

        List<AdditionalImageResponse> images = additionalInfo.getImages();
        assertThat(images).hasSize(2)
                .extracting("imageId", "url")
                .containsExactlyInAnyOrder(
                        tuple(image1.getId(), image1.getImageUrl()),
                        tuple(image2.getId(), image2.getImageUrl())
                );

    }

    @DisplayName("존재하지 않는 업체Id로 상세정보를 조회하면 예외가 발생한다.")
    @Test
    void getBusinessDetailsWhenBusinessNotFound() {
        // when & then
        assertThatThrownBy(()-> funeralBusinessService.getBusinessDetails(1L))
                .isInstanceOf(FuneralBusinessException.class)
                .hasMessage("리소스를 찾을 수 없습니다.");
    }

    private static AdditionalInfo createAdditionalInfo(FuneralBusiness business) {
        return AdditionalInfo.builder()
                .business(business)
                .imageUrl("url")
                .description("info")
                .build();
    }

    private User createUser() {
        return User.builder()
                .username("username")
                .password("pwd")
                .name("name")
                .role(Role.ADMIN)
                .build();
    }

    private FuneralBusiness createBusiness(String name, User owner, String address, int recommendRank, double latitude, double longitude) {
        return FuneralBusiness.builder()
                .name(name)
                .owner(owner)
                .address(address)
                .recommendRank(recommendRank)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    private FuneralProduct createProduct(FuneralBusiness business, ProductCategory category, String name, String description) {
        return FuneralProduct.builder()
                .business(business)
                .category(category)
                .name(name)
                .description(description)
                .build();
    }

    private AdditionalImage createAdditionalImage(AdditionalInfo info, String imageUrl) {
        return AdditionalImage.builder()
                .additionalInfo(info)
                .imageUrl(imageUrl)
                .build();
    }

}