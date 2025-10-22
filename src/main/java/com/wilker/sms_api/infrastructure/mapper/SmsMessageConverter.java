package com.wilker.sms_api.infrastructure.mapper;

import com.wilker.sms_api.infrastructure.dto.request.SmsMessageRequestDTO;
import com.wilker.sms_api.infrastructure.dto.response.SmsMessageResponseDTO;
import com.wilker.sms_api.infrastructure.entity.SmsMessageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SmsMessageConverter {

    SmsMessageEntity paraEntity (SmsMessageRequestDTO smsMessageRequestDTO);
    SmsMessageResponseDTO paraDto (SmsMessageEntity smsMessageEntity);

}
