package com.example.medical_record_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;


@SpringBootApplication
@EnableCircuitBreaker
@EnableHystrix
@EnableEurekaClient
public class PatientServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatientServiceApplication.class, args);
    }
}
