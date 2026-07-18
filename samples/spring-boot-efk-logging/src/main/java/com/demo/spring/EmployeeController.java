package com.demo.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    private static final Logger log =
            LoggerFactory.getLogger(EmployeeController.class);

    @GetMapping("/employee/{id}")
    public String find(@PathVariable int id){

        log.info("Finding employee {}", id);

        return "Employee " + id;
    }
}