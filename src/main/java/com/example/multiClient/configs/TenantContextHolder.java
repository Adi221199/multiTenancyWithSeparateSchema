package com.example.multiClient.configs;

public class TenantContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setTenant(String tenant) {
        contextHolder.set(tenant);
    }

    public static String getTenant() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }
}

