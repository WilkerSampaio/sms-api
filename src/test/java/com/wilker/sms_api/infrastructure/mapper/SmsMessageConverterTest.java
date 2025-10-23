package com.wilker.sms_api.infrastructure.mapper;

import com.wilker.sms_api.infrastructure.dto.request.SmsMessageRequestDTO;
import com.wilker.sms_api.infrastructure.dto.response.SmsMessageResponseDTO;
import com.wilker.sms_api.infrastructure.entity.SmsMessageEntity;
import com.wilker.sms_api.infrastructure.enums.StatusEnvioEnum;
import com.wilker.sms_api.infrastructure.request.SmsMessageRequestDTOFixture;
import com.wilker.sms_api.infrastructure.response.SmsMessageResponseDTOFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class SmsMessageConverterTest {

    SmsMessageConverter smsMessageConverter;

    SmsMessageEntity smsMessageEntity;

    SmsMessageRequestDTO smsMessageRequestDTO;

    SmsMessageResponseDTO smsMessageResponseDTO;

    LocalDateTime dataHora;

    List<SmsMessageEntity> smsMessageEntityList;

    List<SmsMessageResponseDTO> smsMessageResponseDTOList;


    @BeforeEach
    void setup(){

        smsMessageConverter = Mappers.getMapper(SmsMessageConverter.class);

        dataHora = LocalDateTime.of(2025, 1,3, 12, 10, 12);


        smsMessageEntity = SmsMessageEntity.builder()
                .id(1L)
                .phoneNumber("123112341")
                .statusEnvioEnum(StatusEnvioEnum.ENVIADO)
                .sentAt(dataHora)
                .build();

        smsMessageRequestDTO = SmsMessageRequestDTOFixture.build(
                "123112341",
                StatusEnvioEnum.ENVIADO);

        smsMessageResponseDTO = SmsMessageResponseDTOFixture.build(
                1L,
                "123112341",
                StatusEnvioEnum.ENVIADO,
                dataHora);

        smsMessageResponseDTOList = List.of(smsMessageResponseDTO);

        smsMessageEntityList = List.of(smsMessageEntity);


    }

    @Test
    void deveConverterParaEntityComSucesso(){
        SmsMessageEntity entity = smsMessageConverter.paraEntity(smsMessageRequestDTO);

        assertEquals(smsMessageEntity.getPhoneNumber(), entity.getPhoneNumber());
        assertEquals(smsMessageEntity.getStatusEnvioEnum(), entity.getStatusEnvioEnum());

    }

    @Test
    void deveConverterParaResponseComSucesso(){

        SmsMessageResponseDTO response = smsMessageConverter.paraDto(smsMessageEntity);

        assertEquals(smsMessageResponseDTO, response);
    }

    @Test
    void deveConverteParaListaDtoComSucesso(){

        List<SmsMessageResponseDTO> responseDTOList = smsMessageConverter.paraListaDTO(smsMessageEntityList);

        assertEquals(1, responseDTOList.size());

        assertEquals(smsMessageResponseDTOList, responseDTOList);

    }


}
