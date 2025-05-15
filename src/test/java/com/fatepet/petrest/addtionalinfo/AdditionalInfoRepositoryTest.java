package com.fatepet.petrest.addtionalinfo;

import com.fatepet.petrest.business.FuneralBusiness;
import com.fatepet.petrest.business.FuneralBusinessRepository;
import com.fatepet.petrest.user.Role;
import com.fatepet.petrest.user.User;
import com.fatepet.petrest.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AdditionalInfoRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FuneralBusinessRepository funeralBusinessRepository;

    @Autowired
    private AdditionalInfoRepository additionalInfoRepository;

    @DisplayName("업체Id로 업체 추가정보를 조회한다.")
    @Test
    void findByBusinessId() {
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

        // when
        AdditionalInfo foundInfo = additionalInfoRepository.findByBusinessId(savedBusiness.getId());

        // then
        assertThat(foundInfo.getBusiness()).isEqualTo(savedInfo.getBusiness());
        assertThat(foundInfo.getDescription()).isEqualTo(savedInfo.getDescription());

    }

    private FuneralBusiness createBusiness(String name, User owner, String address) {
        return FuneralBusiness.builder()
                .name(name)
                .owner(owner)
                .address(address)
                .build();
    }
}