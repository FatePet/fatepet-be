package com.fatepet.petrest.business.admin.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BusinessListResponse {
    private Long businessId;
    private String name;
    private String address;
    private String category;
    private String mainImageUrl;
}
