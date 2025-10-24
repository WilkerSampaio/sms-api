package com.wilker.sms_api.controller;

import com.wilker.sms_api.infrastructure.dto.request.SmsMessageRequestDTO;
import com.wilker.sms_api.infrastructure.dto.response.SmsMessageResponseDTO;
import com.wilker.sms_api.infrastructure.enums.StatusEnvioEnum;
import com.wilker.sms_api.service.SmsMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mensagem")
@RequiredArgsConstructor
public class SmsMessageController {

    private final SmsMessageService smsMessageService;

    @PostMapping
    public ResponseEntity<SmsMessageResponseDTO> criarSMS (@Valid @RequestBody SmsMessageRequestDTO smsMessageRequestDTO){
        return ResponseEntity.ok(smsMessageService.criaSMS(smsMessageRequestDTO));
    }

    @PatchMapping
    public ResponseEntity<SmsMessageResponseDTO> atualizarSMS(@RequestParam("id") Long id,
                                                             @RequestParam("statusEnvioEnum") StatusEnvioEnum statusEnvioEnum){
        return ResponseEntity.ok(smsMessageService.atualizaStatusSMS(id, statusEnvioEnum));
    }

    @GetMapping
    public ResponseEntity<List<SmsMessageResponseDTO>> buscarRelatórioComStatusEspecifico(@RequestParam("statusEnvioEnum") StatusEnvioEnum statusEnvioEnum){
        return ResponseEntity.ok(smsMessageService.buscarRelatorioComStatusEpecifico(statusEnvioEnum));
    }

}
