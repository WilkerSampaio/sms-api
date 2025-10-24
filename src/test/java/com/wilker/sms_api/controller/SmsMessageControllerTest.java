package com.wilker.sms_api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wilker.sms_api.infrastructure.dto.request.SmsMessageRequestDTO;
import com.wilker.sms_api.infrastructure.dto.response.SmsMessageResponseDTO;
import com.wilker.sms_api.infrastructure.entity.SmsMessageEntity;
import com.wilker.sms_api.infrastructure.enums.StatusEnvioEnum;
import com.wilker.sms_api.infrastructure.exception.GlobalExceptionHandler;
import com.wilker.sms_api.infrastructure.exception.ResourceNotFoundException;
import com.wilker.sms_api.infrastructure.mapper.SmsMessageConverter;
import com.wilker.sms_api.infrastructure.request.SmsMessageRequestDTOFixture;
import com.wilker.sms_api.infrastructure.response.SmsMessageResponseDTOFixture;
import com.wilker.sms_api.service.SmsMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class SmsMessageControllerTest {

    @InjectMocks
    SmsMessageController smsMessageController;

    @Mock
    SmsMessageService smsMessageService;

    SmsMessageConverter smsMessageConverter;


    ObjectMapper objectMapper = new ObjectMapper().
            registerModule(new JavaTimeModule()).
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private MockMvc mockMvc;
    private String json;
    private String url;

    SmsMessageRequestDTO smsMessageRequestDTO;
    SmsMessageResponseDTO smsMessageResponseDTO;
    SmsMessageRequestDTO smsMessageRequestDTOPhoneNull;
    SmsMessageRequestDTO smsMessageRequestDTOStatusNull;
    SmsMessageRequestDTO smsMessageRequestDTOStatusRecebido;
    SmsMessageResponseDTO smsMessageResponseDTOStatusRecebido;
    SmsMessageResponseDTO  smsMessageResponseDTOListVazia;

    List<SmsMessageEntity> smsMessageEntityList;
    List<SmsMessageResponseDTO> smsMessageResponseDTOList;

    LocalDateTime dataHora;

    @BeforeEach
    void setup() throws JsonProcessingException {

        mockMvc = MockMvcBuilders.standaloneSetup(smsMessageController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .alwaysDo(print())
                .build();

        smsMessageConverter = Mappers.getMapper(SmsMessageConverter.class);

        dataHora = LocalDateTime.of(2025, 1,3, 12, 10, 12);

        url = "/mensagem";

        smsMessageRequestDTO = SmsMessageRequestDTOFixture.build(
                "123112341",
                StatusEnvioEnum.ENVIADO);

        smsMessageRequestDTOPhoneNull = SmsMessageRequestDTOFixture.build(
                " ",
                StatusEnvioEnum.ENVIADO);

        smsMessageRequestDTOStatusNull = SmsMessageRequestDTOFixture.build(
                "123112341",
                null);

        smsMessageRequestDTOStatusRecebido = SmsMessageRequestDTOFixture.build(
                "123112341",
                StatusEnvioEnum.RECEBIDO);

        smsMessageResponseDTO = SmsMessageResponseDTOFixture.build(
                1L,
                "123112341",
                StatusEnvioEnum.ENVIADO,
                dataHora);

        smsMessageResponseDTOStatusRecebido = SmsMessageResponseDTOFixture.build(
                1L,
                "123112341",
                StatusEnvioEnum.RECEBIDO,
                dataHora);

        smsMessageResponseDTOList = List.of(smsMessageResponseDTO);

        json = objectMapper.writeValueAsString(smsMessageRequestDTO);

    }

    @Test
    void deveCriarMensagemComSucesso() throws Exception {
        when(smsMessageService.criaSMS(smsMessageRequestDTO)).thenReturn(smsMessageResponseDTO);

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());

        verify(smsMessageService).criaSMS(smsMessageRequestDTO);
        verifyNoMoreInteractions(smsMessageService);
    }

    @Test
    void deveRetornar400QuandoCriarMensagemPhoneNullouVazio() throws Exception {

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(smsMessageRequestDTOPhoneNull))
        ).andExpect(status().isBadRequest());

        verifyNoMoreInteractions(smsMessageService);
    }

    @Test
    void deveRetornar400QuandoCriarMensagemForStatusNullouVazio() throws Exception {

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(smsMessageRequestDTOStatusNull))
        ).andExpect(status().isBadRequest());

        verifyNoMoreInteractions(smsMessageService);
    }

    @Test
    void deveAtualizarSMSComSucesso() throws Exception {
        Long id = 1L;
        when(smsMessageService.atualizaStatusSMS(id, StatusEnvioEnum.RECEBIDO)).thenReturn(smsMessageResponseDTOStatusRecebido);

        mockMvc.perform(patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("id", id.toString())
                .param("statusEnvioEnum", StatusEnvioEnum.RECEBIDO.name())

        ).andExpect(status().isOk());

        verify(smsMessageService).atualizaStatusSMS(id, StatusEnvioEnum.RECEBIDO);
        verifyNoMoreInteractions(smsMessageService);
    }

    @Test
    void deveRetornar404QuandoAtualizarSMSCasoIdInexistente() throws Exception {
        Long idInexistente = 11231L;
        when(smsMessageService.atualizaStatusSMS(idInexistente, StatusEnvioEnum.RECEBIDO)).thenThrow(new ResourceNotFoundException("ID não encontrado"));

        mockMvc.perform(patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("id", idInexistente.toString())
                .param("statusEnvioEnum", StatusEnvioEnum.RECEBIDO.name())

        ).andExpect(status().isNotFound());

        verify(smsMessageService).atualizaStatusSMS(idInexistente, StatusEnvioEnum.RECEBIDO);
        verifyNoMoreInteractions(smsMessageService);
    }

    @Test
    void devebuscarRelatórioComStatusEspecificoComSucesso() throws Exception {
        when(smsMessageService.buscarRelatorioComStatusEpecifico(StatusEnvioEnum.ENVIADO)).thenReturn(smsMessageResponseDTOList);

        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("statusEnvioEnum", StatusEnvioEnum.ENVIADO.name())

        ).andExpect(status().isOk());


        verify(smsMessageService).buscarRelatorioComStatusEpecifico(StatusEnvioEnum.ENVIADO);
        verifyNoMoreInteractions(smsMessageService);
    }


    @Test
    void deveRetornar404AobuscarRelatórioComStatusEspecificoComListaVazia() throws Exception {
        when(smsMessageService.buscarRelatorioComStatusEpecifico(StatusEnvioEnum.ERRO_DE_ENVIO)).thenThrow(new ResourceNotFoundException("Nenhuma mensagem encontrada nas últimas 24 horas"));

        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("statusEnvioEnum", StatusEnvioEnum.ERRO_DE_ENVIO.name())

        ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Nenhuma mensagem encontrada nas últimas 24 horas"));

        verify(smsMessageService).buscarRelatorioComStatusEpecifico(StatusEnvioEnum.ERRO_DE_ENVIO);
        verifyNoMoreInteractions(smsMessageService);
    }




}
