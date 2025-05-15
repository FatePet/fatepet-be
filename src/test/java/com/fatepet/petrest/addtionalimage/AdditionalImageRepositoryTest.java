package com.fatepet.petrest.addtionalimage;

import com.fatepet.petrest.addtionalinfo.AdditionalInfo;
import com.fatepet.petrest.addtionalinfo.AdditionalInfoRepository;
import com.fatepet.petrest.business.FuneralBusiness;
import com.fatepet.petrest.business.FuneralBusinessRepository;
import com.fatepet.petrest.user.Role;
import com.fatepet.petrest.user.User;
import com.fatepet.petrest.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@DataJpaTest
@ActiveProfiles("test")
class AdditionalImageRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FuneralBusinessRepository funeralBusinessRepository;

    @Autowired
    private AdditionalInfoRepository additionalInfoRepository;

    @Autowired
    private AdditionalImageRepository additionalImageRepository;

    @DisplayName("추가정보Id로 추가정보사진들을 조회한다.")
    @Test
    void findAllByAdditionalInfoId() {
        // given
        User user = User.builder()
                .username("username")
                .password("pwd")
                .name("name")
                .role(Role.ADMIN)
                .build();
        User owner = userRepository.save(user);
        FuneralBusiness business = createBusiness("b1", owner, "adr");
        FuneralBusiness savedBusiness = funeralBusinessRepository.save(business);

        AdditionalInfo info = AdditionalInfo.builder()
                .business(business)
                .imageUrl("url1")
                .description("info")
                .build();

        AdditionalInfo savedInfo = additionalInfoRepository.save(info);

        AdditionalImage image1 = createAdditionalImage(savedInfo, "example.jpg1");
        AdditionalImage image2 = createAdditionalImage(savedInfo, "example.jpg2");
        additionalImageRepository.saveAll(List.of(image1, image2));

        // when
        List<AdditionalImage> foundImages = additionalImageRepository.findAllByAdditionalInfoId(savedInfo.getId());

        // then
        Assertions.assertThat(foundImages).hasSize(2)
                .extracting("additionalInfo", "imageUrl")
                .containsExactlyInAnyOrder(
                        tuple(savedInfo, "example.jpg1"),
                        tuple(savedInfo, "example.jpg2")
                );
    }

    private AdditionalImage createAdditionalImage(AdditionalInfo info, String imageUrl) {
        return AdditionalImage.builder()
                .additionalInfo(info)
                .imageUrl(imageUrl)
                .build();
    }

    private FuneralBusiness createBusiness(String name, User owner, String address) {
        return FuneralBusiness.builder()
                .name(name)
                .owner(owner)
                .address(address)
                .build();
    }
}