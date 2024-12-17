package com.example.multiClient.controller;

import com.example.multiClient.model.Employee;
import com.example.multiClient.service.EmployeeService;
import jakarta.persistence.GeneratedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("createEmployee")
    public Employee createEmployee(@RequestParam(value = "tenantName") String tenantName,
                                   @RequestParam(value = "tenantId") UUID tenantId,
                                   @RequestBody Employee employee){
        return employeeService.createEmployee(employee);
    }

    @GetMapping("getAllEmployee")
    public List<Employee> getAllEmployees(@RequestParam(value = "tenantName") String tenantName,
                                          @RequestParam(value = "tenantId") UUID tenantId){
        return employeeService.getAllEmployees();
    }
}
