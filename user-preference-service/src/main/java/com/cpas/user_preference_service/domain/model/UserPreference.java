package com.cpas.user_preference_service.domain.model;

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
@Table(name = "user_preferences")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreference {
    @Id
    private UUID id;
    private UUID userId;
    private String phoneNumber;
    private String coinName;
    private Double priceTargeted;
    private Boolean notifyWhenBelow;
    private Boolean notifyWhenAbove;
    private Integer cooldownMinutes;
    private LocalDateTime lastAlertTime;
}


