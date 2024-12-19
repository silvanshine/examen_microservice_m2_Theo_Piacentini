package com.howtodoinjava.example.apigateway.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    @LoadBalanced // Optional: Use if working with service discovery
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
