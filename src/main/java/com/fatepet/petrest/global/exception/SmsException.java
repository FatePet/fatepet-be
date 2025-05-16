package com.fatepet.petrest.global.exception;

import com.fatepet.petrest.global.response.ResponseCode;

public class SmsException extends BaseException{
    public SmsException(ResponseCode responseCode) {
        super(responseCode);
    }

    public SmsException(String message) {
        super(message);
    }

    public SmsException(int status, String message) {
        super(status, message);
    }
}
