package com.fatepet.petrest.counseling;

import com.fatepet.petrest.global.exception.FuneralBusinessException;
import com.fatepet.petrest.global.response.ResponseCode;
import com.fatepet.petrest.SmsService;
import com.fatepet.petrest.business.FuneralBusiness;
import com.fatepet.petrest.business.FuneralBusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CounselingService {

    private final SmsService smsService;

    private final CounselingRepository counselingRepository;

    private final FuneralBusinessRepository funeralBusinessRepository;

    @Transactional
    public void sendCounselingRequest(Long businessId, String customerPhoneNumber, String contactType, String inquiry) {
        FuneralBusiness business = funeralBusinessRepository.findById(businessId)
                .orElseThrow(() -> new FuneralBusinessException(ResponseCode.NOT_FOUND));


        String businessPhoneNumber = business.getPhoneNumber();
        smsService.sendSMS(businessPhoneNumber, customerPhoneNumber, ContactType.getDisplayNameByKey(contactType), inquiry);

    }


}
