package com.demo.spring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DemoController {

    @GetMapping("/greet/{name}")
    public String greet(@PathVariable String name,
                        @RequestHeader(required = false,
                                       name = "X-Gateway") String gateway) {

        return "Hello " + name +
                ", Header=" + gateway;
    }

    @GetMapping("/response/test")
    public String responseDemo() {
        return "Response Modified";
    }

    @GetMapping("/rate/test")
    public String rateDemo() {
        return "Rate Limited Endpoint";
    }

    @GetMapping("/cb/test")
    public String circuitBreakerDemo() {

        if (Math.random() > 0.5) {
            throw new RuntimeException("Backend Failure");
        }

        return "Success";
    }
}