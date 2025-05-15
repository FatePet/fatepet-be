package com.fatepet.petrest.funeralproduct;

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

import static com.fatepet.petrest.funeralproduct.ProductCategory.*;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@DataJpaTest
@ActiveProfiles("test")
class FuneralProductRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FuneralBusinessRepository funeralBusinessRepository;

    @Autowired
    private FuneralProductRepository funeralProductRepository;

    @DisplayName("업체Id로 업체 서비스들을 조회한다.")
    @Test
    void findAllByBusinessId() {
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

        FuneralProduct product1 = createProduct(business, BASIC, "p1");

        FuneralProduct product2 = createProduct(business, OPTIONAL, "p2");
        funeralProductRepository.saveAll(List.of(product1, product2));

        // when
        List<FuneralProduct> foundProducts = funeralProductRepository.findAllByBusinessId(savedBusiness.getId());

        // then
        Assertions.assertThat(foundProducts).hasSize(2)
                .extracting("category", "name")
                .containsExactlyInAnyOrder(
                        tuple(BASIC, "p1"),
                        tuple(OPTIONAL, "p2")
                );

    }

    private FuneralProduct createProduct(FuneralBusiness business, ProductCategory category, String name) {
        return FuneralProduct.builder()
                .business(business)
                .category(category)
                .name(name)
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