package com.wilker.sms_api.service;

import com.wilker.sms_api.infrastructure.dto.request.SmsMessageRequestDTO;
import com.wilker.sms_api.infrastructure.dto.response.SmsMessageResponseDTO;
import com.wilker.sms_api.infrastructure.entity.SmsMessageEntity;
import com.wilker.sms_api.infrastructure.enums.StatusEnvioEnum;
import com.wilker.sms_api.infrastructure.exception.ResourceNotFoundException;
import com.wilker.sms_api.infrastructure.mapper.SmsMessageConverter;
import com.wilker.sms_api.infrastructure.repository.SmsMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SmsMessageService {

    private final SmsMessageRepository smsMessageRepository;
    private final SmsMessageConverter smsMessageConverter;

    public SmsMessageResponseDTO criaSMS(SmsMessageRequestDTO smsMessageRequestDTO){

        SmsMessageEntity smsMessageEntity = new SmsMessageEntity();

        smsMessageEntity.setPhoneNumber(smsMessageRequestDTO.phoneNumber());
        smsMessageEntity.setStatusEnvioEnum(smsMessageRequestDTO.statusEnvioEnum());
        smsMessageEntity.setSentAt(LocalDateTime.now());

        return smsMessageConverter.paraDto(smsMessageRepository.save(smsMessageEntity));
    }

    public SmsMessageResponseDTO atualizaStatusSMS(Long id, StatusEnvioEnum statusEnvioEnum){

        SmsMessageEntity smsMessageEntity = smsMessageRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("ID não encontrado"));
            smsMessageEntity.setStatusEnvioEnum(statusEnvioEnum);

            return smsMessageConverter.paraDto(smsMessageRepository.save(smsMessageEntity));
    }

    public List<SmsMessageResponseDTO> BuscarRelatorioComStatusEpecifico(StatusEnvioEnum statusEnvioEnum){

        LocalDateTime dataFinal = LocalDateTime.now();
        LocalDateTime dataInicial = dataFinal.minusHours(24);

        List<SmsMessageEntity> smsMessageEntityList =
                smsMessageRepository.findBySentAtBetweenAndStatusEnvioEnum(dataInicial, dataFinal, statusEnvioEnum);

        if(smsMessageEntityList.isEmpty()){
           throw new ResourceNotFoundException("Nenhuma mensagem encontrada nas últimas 24 horas");
        }
        return smsMessageConverter.paraListaDTO(smsMessageEntityList);
    }
}
