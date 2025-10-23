package com.wilker.sms_api.infrastructure.mapper;

import com.wilker.sms_api.infrastructure.dto.request.SmsMessageRequestDTO;
import com.wilker.sms_api.infrastructure.dto.response.SmsMessageResponseDTO;
import com.wilker.sms_api.infrastructure.entity.SmsMessageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SmsMessageConverter {

    @Mapping(target = "id", ignore = true)
    SmsMessageEntity paraEntity (SmsMessageRequestDTO smsMessageRequestDTO);

    SmsMessageResponseDTO paraDto (SmsMessageEntity smsMessageEntity);

    List<SmsMessageEntity> paraListaEntity(List<SmsMessageRequestDTO> smsMessageRequestDTOList);

    List<SmsMessageResponseDTO> paraListaDTO(List<SmsMessageEntity> smsMessageEntityList);
}
