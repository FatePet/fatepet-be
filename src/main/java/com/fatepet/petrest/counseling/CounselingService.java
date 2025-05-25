package com.fatepet.petrest.counseling;

import com.fatepet.petrest.MailService;
import com.fatepet.petrest.global.exception.FuneralBusinessException;
import com.fatepet.petrest.global.response.ResponseCode;
import com.fatepet.petrest.SmsService;
import com.fatepet.petrest.business.FuneralBusiness;
import com.fatepet.petrest.business.FuneralBusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CounselingService {

    private final SmsService smsService;

    private final MailService mailService;

    private final CounselingRepository counselingRepository;

    private final FuneralBusinessRepository funeralBusinessRepository;

    public void sendCounselingRequest(Long businessId, String customerPhoneNumber, String contactType, String inquiry) {
        FuneralBusiness business = funeralBusinessRepository.findById(businessId)
                .orElseThrow(() -> new FuneralBusinessException(ResponseCode.NOT_FOUND));

        String businessPhoneNumber = business.getPhoneNumber();
        String businessEmail = business.getEmail();
        String contactTypeName = ContactType.getDisplayNameByKey(contactType);

        smsService.sendSMS(businessPhoneNumber, customerPhoneNumber, contactTypeName, inquiry);
        mailService.sendMail(businessEmail, customerPhoneNumber, contactTypeName, inquiry);

        Counseling counseling = Counseling.builder()
                .business(business)
                .contactType(ContactType.valueOf(contactType))
                .phoneNumber(customerPhoneNumber)
                .message(inquiry)
                .build();
        counselingRepository.save(counseling);
    }


}
