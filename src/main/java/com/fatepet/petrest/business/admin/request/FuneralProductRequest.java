package com.fatepet.petrest.business.admin.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuneralProductRequest {
    private String type;        // ex. "CREMATION"
    private String name;
    private String description;
    private String price;
    private Boolean image;
}
