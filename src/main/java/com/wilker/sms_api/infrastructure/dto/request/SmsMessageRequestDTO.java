package com.wilker.sms_api.infrastructure.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wilker.sms_api.infrastructure.enums.StatusEnvioEnum;
import java.time.LocalDateTime;

public record SmsMessageRequestDTO(String phoneNumber,
                                   StatusEnvioEnum statusEnvioEnum,
                                   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
                                   LocalDateTime sentAt) {
}
