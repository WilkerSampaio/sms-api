package com.wilker.sms_api.infrastructure.controller;

import com.wilker.sms_api.infrastructure.dto.request.SmsMessageRequestDTO;
import com.wilker.sms_api.infrastructure.dto.response.SmsMessageResponseDTO;
import com.wilker.sms_api.infrastructure.enums.StatusEnvioEnum;
import com.wilker.sms_api.service.SmsMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mensagem")
@RequiredArgsConstructor
public class SmsMessageController {

    private final SmsMessageService smsMessageService;


    @PostMapping
    public ResponseEntity<SmsMessageResponseDTO> criarSMS (@RequestBody SmsMessageRequestDTO smsMessageRequestDTO){
        return ResponseEntity.ok(smsMessageService.criaSMS(smsMessageRequestDTO));
    }


}
