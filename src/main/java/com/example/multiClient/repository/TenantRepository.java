package com.example.multiClient.repository;

import com.example.multiClient.model.Tenants;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenants, Long> {
}
