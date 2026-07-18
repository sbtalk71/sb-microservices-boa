package com.demo.spring;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    @Value("${employee.department}")
    private String department;

    @Value("${employee.country}")
    private String country;

    @Value("${employee.message}")
    private String message;

    @GetMapping("/config")
    public Map<String,String> config(){

        return Map.of(
                "department",department,
                "country",country,
                "message",message
        );
    }

}