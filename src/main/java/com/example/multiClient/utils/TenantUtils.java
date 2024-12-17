package com.example.multiClient.utils;

import org.json.JSONObject;

import java.util.UUID;

public class TenantUtils {

    public static JSONObject decodeToken(String tenantName, UUID tenantId){
        JSONObject tenantInfo = new JSONObject();
        tenantInfo.put("tenant_name", tenantName);
        tenantInfo.put("tenant_id", tenantId);
        return tenantInfo;
    }
}
