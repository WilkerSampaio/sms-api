package com.wilker.sms_api.infrastructure.dto.response;

import com.wilker.sms_api.infrastructure.enums.StatusEnvioEnum;
import java.time.LocalDateTime;

public record SmsMessageResponseDTO (Long id,
                                     String phoneNumber,
                                     StatusEnvioEnum statusEnvioEnum,
                                     LocalDateTime sentAt) {
}
