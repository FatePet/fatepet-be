package com.fatepet.petrest.business.admin.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatepet.petrest.business.admin.dto.AddServiceDto;
import com.fatepet.petrest.business.admin.dto.UpdateServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BusinessUtil {

    private final ObjectMapper objectMapper;

    public List<AddServiceDto> parseAddServiceJson(String serviceJson) {
        if (serviceJson == null || serviceJson.isEmpty()) { return null;}
        List<AddServiceDto> serviceList;
        try {
            serviceList = objectMapper.readValue(serviceJson, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("서비스 항목 JSON 파싱 실패", e);
        }
        return serviceList;
    }

    public List<UpdateServiceDto> parseUpdateServiceJson(String serviceJson) {
        if (serviceJson == null || serviceJson.isEmpty()) { return null;}
        List<UpdateServiceDto> serviceList;
        try {
            serviceList = objectMapper.readValue(serviceJson, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("수정 서비스 항목 JSON 파싱 실패", e);
        }
        return serviceList;
    }
}
