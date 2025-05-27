package com.fatepet.petrest.business.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddServiceDto {
    private String type;
    private String name;
    private String description;
    private String priceType;
    private String price;
    private Boolean image;
}
