package com.wilker.sms_api.infrastructure.dto.request;

import com.wilker.sms_api.infrastructure.enums.StatusEnvioEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record SmsMessageRequestDTO(

        @NotBlank(message = "O número é obrigatório")
        String phoneNumber,

        @NotNull(message = "O status do envio é obrigatório")
        StatusEnvioEnum statusEnvioEnum

) { }
