package com.cpas.alert_evaluator_service.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "local_user_preferences")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreference {
    @Id
    private UUID id;
    private String phoneNumber;
    private String coinName;
    private Double priceTargeted;
    private Boolean notifyWhenBelow;
    private Boolean notifyWhenAbove;
    private Integer cooldownMinutes;
    private LocalDateTime lastAlertTime;
}


