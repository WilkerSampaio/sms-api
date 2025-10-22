package com.wilker.sms_api.infrastructure.repository;

import com.wilker.sms_api.infrastructure.entity.SmsMessageEntity;
import com.wilker.sms_api.infrastructure.enums.StatusEnvioEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SmsMessageRepository extends JpaRepository<SmsMessageEntity, Long> {

    Optional<SmsMessageEntity> findById(Long id);
    List<SmsMessageEntity> findBySentAtBetweenAndStatusEnvioEnum(LocalDateTime dataInicial,
                                                                 LocalDateTime dataFinal,
                                                                 StatusEnvioEnum statusEnvioEnum);
}
