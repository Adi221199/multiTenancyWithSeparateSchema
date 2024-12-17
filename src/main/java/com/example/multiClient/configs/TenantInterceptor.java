package com.example.multiClient.configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tenantName = request.getParameter("tenantName");
        String tenantId = request.getParameter("tenantId");

        if (tenantName != null && tenantId != null) {
            String schemaName = tenantName.toLowerCase() + "_" + tenantId.replace("-", "_");
            TenantContextHolder.setTenant(schemaName);
            jdbcTemplate.execute("SET search_path TO " + schemaName);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        TenantContextHolder.clear();
        jdbcTemplate.execute("SET search_path TO public");
    }
}
