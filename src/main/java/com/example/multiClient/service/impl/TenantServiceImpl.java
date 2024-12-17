package com.example.multiClient.service.impl;

import com.example.multiClient.model.Tenants;
import com.example.multiClient.repository.TenantRepository;
import com.example.multiClient.service.TenantService;
import jakarta.annotation.PostConstruct;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TenantServiceImpl implements TenantService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private DataSource dataSource;

    @Override
    public void createTenant(String tenantName, UUID tenantId) {
        String schemaName = tenantName.toLowerCase() + "_" + tenantId.toString().replace("-", "_");

        // Step 1: Create Schema
        createSchema(schemaName);

        // Step 2: Save Tenant Metadata in Public Schema
        saveTenantMetadata(tenantId, tenantName, schemaName);

        // Step 3: Create tables in the new schema
        applyEntitiesToSchema(schemaName);
    }

    void createSchema(String schemaName) {
        String sql = "CREATE SCHEMA IF NOT EXISTS " + schemaName;
        jdbcTemplate.execute(sql);
    }

    public void saveTenantMetadata(UUID tenantId, String tenantName, String schemaName) {
        Tenants tenants = new Tenants();
        tenants.setTenantId(tenantId);
        tenants.setTenantName(tenantName);
        tenants.setSchemaName(schemaName);
        tenantRepository.save(tenants);
    }

    void applyEntitiesToSchema(String schemaName) {
        // Create and configure EntityManagerFactory
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("com.example.multiClient.model");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Map<String, Object> properties = new HashMap<>();
        properties.put(AvailableSettings.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        properties.put(AvailableSettings.HBM2DDL_AUTO, "update");
        properties.put(AvailableSettings.DEFAULT_SCHEMA, schemaName);
        properties.put(AvailableSettings.PHYSICAL_NAMING_STRATEGY, "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");

        factoryBean.setJpaPropertyMap(properties);
        factoryBean.afterPropertiesSet();

        EntityManagerFactory emf = factoryBean.getObject();
        assert emf != null;
        emf.createEntityManager(); // This will trigger the schema creation

        // Reset to public schema
        jdbcTemplate.execute("SET search_path TO public");
    }

    public void updateAllSchemas() {
        List<Tenants> tenantsList = tenantRepository.findAll();
        for (Tenants tenant : tenantsList) {
            applyEntitiesToSchema(tenant.getSchemaName());
        }
    }

    @PostConstruct
    public void onStartup() {
        updateAllSchemas();
    }
}
