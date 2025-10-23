package com.wilker.sms_api.infrastructure.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wilker.sms_api.infrastructure.enums.StatusEnvioEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SmsMessageRequestDTO(

        @NotBlank(message = "O número é obrigatório")
        String phoneNumber,

        @NotNull(message = "O status do envio é obrigatório")
        StatusEnvioEnum statusEnvioEnum,

        @NotNull(message = "A data de envio é obrigatória")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime sentAt
) { }
