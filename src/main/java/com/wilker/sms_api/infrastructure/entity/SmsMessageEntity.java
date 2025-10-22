package com.wilker.sms_api.infrastructure.entity;

import com.wilker.sms_api.infrastructure.enums.StatusEnvioEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "sms")

public class SmsMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "statusEnvioEnum")
    private StatusEnvioEnum statusEnvioEnum;

    @Column(name = "sentAt")
    private LocalDateTime sentAt;

}
