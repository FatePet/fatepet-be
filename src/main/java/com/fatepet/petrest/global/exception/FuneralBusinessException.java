package com.fatepet.petrest.global.exception;

import com.fatepet.petrest.global.response.ResponseCode;

public class FuneralBusinessException extends BaseException {

    public FuneralBusinessException(ResponseCode responseCode) {
        super(responseCode);
    }

    public FuneralBusinessException(int status, String message) {
        super(status, message);
    }

    public FuneralBusinessException(String message){
        super(message);
    }

}
