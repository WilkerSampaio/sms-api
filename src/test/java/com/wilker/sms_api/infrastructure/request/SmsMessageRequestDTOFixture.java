package com.wilker.sms_api.infrastructure.request;

import com.wilker.sms_api.infrastructure.dto.request.SmsMessageRequestDTO;
import com.wilker.sms_api.infrastructure.enums.StatusEnvioEnum;

public class SmsMessageRequestDTOFixture {

    public static SmsMessageRequestDTO build (String phoneNumber,  StatusEnvioEnum statusEnvioEnum){

        return new SmsMessageRequestDTO(phoneNumber, statusEnvioEnum);

    }
}
