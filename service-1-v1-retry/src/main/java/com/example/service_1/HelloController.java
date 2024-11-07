package com.example.service_1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        try {
            // Simulate unresponsiveness for 5 seconds
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello from Service-1! - V1 , Retry simulation";
    }
    
    @GetMapping("/service-info")
    public String serviceInfo() {
        return "Service-1 -V1 with Retry simulation is up and running!";
    }
}