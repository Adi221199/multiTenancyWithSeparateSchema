package com.example.multiClient.service;

import java.util.UUID;

public interface TenantService {

    void createTenant(String tenantName, UUID tenantId);

    void updateAllSchemas();
}
