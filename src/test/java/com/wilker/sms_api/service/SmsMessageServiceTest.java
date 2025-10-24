package com.wilker.sms_api.service;

import com.wilker.sms_api.infrastructure.dto.request.SmsMessageRequestDTO;
import com.wilker.sms_api.infrastructure.dto.response.SmsMessageResponseDTO;
import com.wilker.sms_api.infrastructure.entity.SmsMessageEntity;
import com.wilker.sms_api.infrastructure.enums.StatusEnvioEnum;
import com.wilker.sms_api.infrastructure.exception.ResourceNotFoundException;
import com.wilker.sms_api.infrastructure.mapper.SmsMessageConverter;
import com.wilker.sms_api.infrastructure.repository.SmsMessageRepository;
import com.wilker.sms_api.infrastructure.request.SmsMessageRequestDTOFixture;
import com.wilker.sms_api.infrastructure.response.SmsMessageResponseDTOFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SmsMessageServiceTest {

    @InjectMocks
    SmsMessageService smsMessageService;

    @Mock
    SmsMessageRepository smsMessageRepository;

    @Mock
    SmsMessageConverter smsMessageConverter;

    Clock clock;

    SmsMessageEntity smsMessageEntity;
    SmsMessageRequestDTO smsMessageRequestDTO;
    SmsMessageResponseDTO smsMessageResponseDTO;
    LocalDateTime dataHora;
    List<SmsMessageEntity> smsMessageEntityList;
    List<SmsMessageResponseDTO> smsMessageResponseDTOList;

    @BeforeEach
    void setup() {

        dataHora = LocalDateTime.now().minusHours(1);

        ZoneId zoneId = ZoneId.systemDefault();
        clock = Clock.fixed(dataHora.atZone(zoneId).toInstant(), zoneId);

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

        smsMessageEntityList = List.of(smsMessageEntity);
        smsMessageResponseDTOList = List.of(smsMessageResponseDTO);

        // Cria o serviço com os mocks e clock real
        smsMessageService = new SmsMessageService(smsMessageRepository, smsMessageConverter, clock);
    }

    @Test
    void deveCriarSmsComSucesso() {
        when(smsMessageConverter.paraEntity(smsMessageRequestDTO)).thenReturn(smsMessageEntity);
        when(smsMessageRepository.save(smsMessageEntity)).thenReturn(smsMessageEntity);
        when(smsMessageConverter.paraDto(smsMessageEntity)).thenReturn(smsMessageResponseDTO);

        SmsMessageResponseDTO response = smsMessageService.criaSMS(smsMessageRequestDTO);

        assertEquals(smsMessageResponseDTO, response);

        verify(smsMessageConverter).paraEntity(smsMessageRequestDTO);
        verify(smsMessageRepository).save(smsMessageEntity);
        verify(smsMessageConverter).paraDto(smsMessageEntity);

        verifyNoMoreInteractions(smsMessageRepository, smsMessageConverter);
    }

    @Test
    void deveAtualizarStatusSMSComSucesso() {
        Long id = 1L;
        when(smsMessageRepository.findById(id)).thenReturn(Optional.of(smsMessageEntity));
        when(smsMessageRepository.save(smsMessageEntity)).thenReturn(smsMessageEntity);
        when(smsMessageConverter.paraDto(smsMessageEntity)).thenReturn(smsMessageResponseDTO);

        SmsMessageResponseDTO response = smsMessageService.atualizaStatusSMS(id, StatusEnvioEnum.RECEBIDO);

        assertEquals(smsMessageResponseDTO, response);

        verify(smsMessageRepository).findById(id);
        verify(smsMessageRepository).save(smsMessageEntity);
        verify(smsMessageConverter).paraDto(smsMessageEntity);

        verifyNoMoreInteractions(smsMessageRepository, smsMessageConverter);
    }

    @Test
    void deveRetornar404AoAtualizarStatusSMSCasoIdNaoEncontrado() {
        Long idInexistente = 1231L;
        when(smsMessageRepository.findById(idInexistente)).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> smsMessageService.atualizaStatusSMS(idInexistente, StatusEnvioEnum.ENVIADO));

        assertThat(e.getMessage(), notNullValue());
        assertThat(e.getMessage(), is("ID não encontrado"));

        verifyNoInteractions(smsMessageConverter);
    }

    @Test
    void deveBuscarRelatorioComStatusEspecifico() {
        LocalDateTime dataFinal = LocalDateTime.now(clock);
        LocalDateTime dataInicial = dataFinal.minusHours(24);

        when(smsMessageRepository.findBySentAtBetweenAndStatusEnvioEnum(dataInicial, dataFinal, StatusEnvioEnum.ENVIADO))
                .thenReturn(smsMessageEntityList);
        when(smsMessageConverter.paraListaDTO(smsMessageEntityList)).thenReturn(smsMessageResponseDTOList);

        List<SmsMessageResponseDTO> responseDTOList = smsMessageService.buscarRelatorioComStatusEpecifico(StatusEnvioEnum.ENVIADO);

        assertEquals(1, responseDTOList.size());
        assertEquals(smsMessageResponseDTOList, responseDTOList);

        verify(smsMessageRepository).findBySentAtBetweenAndStatusEnvioEnum(dataInicial, dataFinal, StatusEnvioEnum.ENVIADO);
        verify(smsMessageConverter).paraListaDTO(smsMessageEntityList);

        verifyNoMoreInteractions(smsMessageRepository, smsMessageConverter);
    }

    @Test
    void deveRetornar404AoBuscarRelatorioComStatusEpecifico() {
        LocalDateTime dataFinal = LocalDateTime.now(clock);
        LocalDateTime dataInicial = dataFinal.minusHours(24);

        when(smsMessageRepository.findBySentAtBetweenAndStatusEnvioEnum(dataInicial, dataFinal, StatusEnvioEnum.ERRO_DE_ENVIO))
                .thenReturn(List.of());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> smsMessageService.buscarRelatorioComStatusEpecifico(StatusEnvioEnum.ERRO_DE_ENVIO));

        assertThat(e.getMessage(), notNullValue());
        assertThat(e.getMessage(), is("Nenhuma mensagem encontrada nas últimas 24 horas"));

        verify(smsMessageRepository).findBySentAtBetweenAndStatusEnvioEnum(dataInicial, dataFinal, StatusEnvioEnum.ERRO_DE_ENVIO);
        verifyNoInteractions(smsMessageConverter);
    }
}
