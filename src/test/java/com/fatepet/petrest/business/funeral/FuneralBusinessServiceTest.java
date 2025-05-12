package com.fatepet.petrest.business.funeral;

import com.fatepet.petrest.SortOption;
import com.fatepet.petrest.business.controller.dto.response.BusinessResponse;
import com.fatepet.petrest.user.Role;
import com.fatepet.petrest.user.User;
import com.fatepet.petrest.user.UserRepository;
import org.assertj.core.api.Assertions;
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

    @DisplayName("장묘업체들을 RECOMMEND 조건으로 내림차순 정렬하여 조회한다.")
    @Test
    void getBusinessListOrderByRecommend() {
        // given
        User user = User.builder()
                .username("username")
                .password("pwd")
                .name("name")
                .role(Role.ADMIN)
                .build();
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
        User user = User.builder()
                .username("username")
                .password("pwd")
                .name("name")
                .role(Role.ADMIN)
                .build();
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

}