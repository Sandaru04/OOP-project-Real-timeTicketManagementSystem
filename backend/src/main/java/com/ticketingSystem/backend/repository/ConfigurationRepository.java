package com.ticketingSystem.backend.repository;

import com.ticketingSystem.backend.model.ConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends JpaRepository<ConfigurationEntity, Long> {
}
