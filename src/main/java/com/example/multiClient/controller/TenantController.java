package com.example.multiClient.controller;

import com.example.multiClient.model.Tenants;
import com.example.multiClient.service.TenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/")
public class TenantController {

    private static final Logger log = LoggerFactory.getLogger(TenantController.class);
    @Autowired
    private TenantService tenantService;

    @PostMapping("createTenant")
    public String createTenant(@RequestBody Tenants tenants){
        try {
            tenantService.createTenant(tenants.getTenantName(), tenants.getTenantId());
            return "Tenant Created Successfully!";
        } catch (Exception exception){
            log.error("Error while creating Tenant ", exception.getMessage());
            return "Tenant Creation Failed!";
        }
    }

    @GetMapping("updateAllSchemas")
    public String updateAllSchemas(){
        try {
            tenantService.updateAllSchemas();
            return "Schema Updated Successfully for All Tenants!";
        } catch (Exception exception){
            log.error("Error while schema update ", exception.getMessage());
            return "Schema Updation Failed!";
        }
    }
}
