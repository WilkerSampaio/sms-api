package com.wilker.sms_api.infrastructure.dto.request;

import com.wilker.sms_api.infrastructure.enums.StatusEnvioEnum;
import java.time.LocalDateTime;

public record SmsMessageRequestDTO(String phoneNumber,
                                   StatusEnvioEnum statusEnvioEnum,
                                   LocalDateTime sentAt) {
}
