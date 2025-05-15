package com.fatepet.global.exception;

import com.fatepet.global.response.ResponseCode;

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
