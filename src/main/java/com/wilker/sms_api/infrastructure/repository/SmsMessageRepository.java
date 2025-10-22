package com.wilker.sms_api.infrastructure.repository;

import com.wilker.sms_api.infrastructure.entity.SmsMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SmsMessageRepository extends JpaRepository<SmsMessageEntity, Long> {

    Optional<SmsMessageEntity> findById(Long id);
}
