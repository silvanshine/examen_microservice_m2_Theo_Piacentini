package com.howtodoinjava.example.apigateway.controller;

import com.howtodoinjava.example.apigateway.service.ServiceDiscovery;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/practitioner")
public class PractitionerController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ServiceDiscovery serviceDiscovery;

    private static final String PRACTITIONER_SERVICE_NAME = "practitioner-service";

    // Create a LoadBalanced RestTemplate to interact with the Practitioner Service
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // Helper method to fetch the service URL dynamically
    private String getPractitionerServiceUrl() {
        String serviceUrl = serviceDiscovery.getServiceUri(PRACTITIONER_SERVICE_NAME);
        if (serviceUrl == null) {
            throw new RuntimeException("Service not found: " + PRACTITIONER_SERVICE_NAME);
        }
        return serviceUrl;
    }

    // GET endpoint to fetch a practitioner by ID with Hystrix fallback
    @HystrixCommand(fallbackMethod = "getPractitionerById_fallback")
    @GetMapping("/{id}")
    public String getPractitionerById(@PathVariable("id") String id) {
        String url = getPractitionerServiceUrl() + "/practitioners/" + id;
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {}).getBody();
    }

    public String getPractitionerById_fallback(String id) {
        return "Fallback: Unable to fetch practitioner with ID " + id;
    }

    // POST endpoint to create a practitioner with Hystrix fallback
    @HystrixCommand(fallbackMethod = "createPractitioner_fallback")
    @PostMapping("/")
    public String createPractitioner(@RequestBody String practitioner) {
        String url = getPractitionerServiceUrl() + "/practitioners";
        HttpEntity<String> request = new HttpEntity<>(practitioner);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {}).getBody();
    }

    public String createPractitioner_fallback(String practitioner) {
        return "Fallback: Unable to create practitioner";
    }

    // PUT endpoint to update a practitioner with Hystrix fallback
    @HystrixCommand(fallbackMethod = "updatePractitioner_fallback")
    @PutMapping("/{id}")
    public String updatePractitioner(@PathVariable("id") String id, @RequestBody String practitioner) {
        String url = getPractitionerServiceUrl() + "/practitioners/" + id;
        HttpEntity<String> request = new HttpEntity<>(practitioner);
        return restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<String>() {}).getBody();
    }

    public String updatePractitioner_fallback(String id, String practitioner) {
        return "Fallback: Unable to update practitioner with ID " + id;
    }

    // DELETE endpoint to delete a practitioner with Hystrix fallback
    @HystrixCommand(fallbackMethod = "deletePractitioner_fallback")
    @DeleteMapping("/{id}")
    public String deletePractitioner(@PathVariable("id") String id) {
        String url = getPractitionerServiceUrl() + "/practitioners/" + id;
        return restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<String>() {}).getBody();
    }

    public String deletePractitioner_fallback(String id) {
        return "Fallback: Unable to delete practitioner with ID " + id;
    }
}
