package com.fatepet.petrest.business.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateServiceDto {
    private Long serviceId;
    private String type;
    private String name;
    private String description;
    private String priceType;
    private String price;
    private Integer imageType; // 0: 유지, 1: 수정/추가, 2: 삭제
}
