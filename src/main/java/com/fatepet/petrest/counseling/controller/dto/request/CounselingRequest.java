package com.fatepet.petrest.counseling.controller.dto.request;

import lombok.Getter;

@Getter
public class CounselingRequest {

    private Long businessId;

    private String contactType;

    private String phoneNumber;

    private String inquiry;

}
