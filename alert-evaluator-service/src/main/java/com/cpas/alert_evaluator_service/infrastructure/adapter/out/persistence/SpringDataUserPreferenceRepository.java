package com.cpas.alert_evaluator_service.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cpas.alert_evaluator_service.domain.model.UserPreference;

@Repository
public interface SpringDataUserPreferenceRepository extends JpaRepository<UserPreference, UUID> {

    @Query("SELECT p FROM UserPreference p WHERE p.coinName = :coinName "
            + "AND p.notifyWhenBelow = true AND p.priceTargeted >= :currentPrice")
    List<UserPreference> findByBelowAlerts(@Param("coinName") String coinName, @Param("currentPrice") Double currentPrice);

    @Query("SELECT p FROM UserPreference p WHERE p.coinName = :coinName "
            + "AND p.notifyWhenAbove = true AND p.priceTargeted <= :currentPrice")
    List<UserPreference> findByAboveAlerts(@Param("coinName") String coinName, @Param("currentPrice") Double currentPrice);
}
