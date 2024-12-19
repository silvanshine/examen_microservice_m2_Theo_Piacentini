package com.howtodoinjava.example.apigateway.controller;

import com.howtodoinjava.example.apigateway.service.ServiceDiscovery;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/medical-record")
public class MedicalRecordController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ServiceDiscovery serviceDiscovery;

    private static final String MEDICAL_RECORD_SERVICE_NAME = "medical-record-service";


    // Create a LoadBalanced RestTemplate to interact with the Medical Record Service
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // Helper method to fetch the service URL dynamically
    private String getMedicalRecordServiceUrl() {
        String serviceUrl = serviceDiscovery.getServiceUri(MEDICAL_RECORD_SERVICE_NAME);
        if (serviceUrl == null) {
            throw new RuntimeException("Service not found: " + MEDICAL_RECORD_SERVICE_NAME);
        }
        return serviceUrl;
    }

    // GET endpoint to fetch a medical record by ID with Hystrix fallback
    @HystrixCommand(fallbackMethod = "getMedicalRecordById_fallback")
    @GetMapping("/{id}")
    public String getMedicalRecordById(@PathVariable("id") String id) {
        String url = getMedicalRecordServiceUrl() + "/medical-records/" + id;
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {}).getBody();
    }

    public String getMedicalRecordById_fallback(String id) {
        return "Fallback: Unable to fetch medical record with ID " + id;
    }

    // POST endpoint to create a medical record with Hystrix fallback
    @HystrixCommand(fallbackMethod = "createMedicalRecord_fallback")
    @PostMapping("/")
    public String createMedicalRecord(@RequestBody String medicalRecord) {
        String url = getMedicalRecordServiceUrl() + "/medical-records";
        HttpEntity<String> request = new HttpEntity<>(medicalRecord);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {}).getBody();
    }

    public String createMedicalRecord_fallback(String medicalRecord) {
        return "Fallback: Unable to create medical record";
    }

    // PUT endpoint to update a medical record with Hystrix fallback
    @HystrixCommand(fallbackMethod = "updateMedicalRecord_fallback")
    @PutMapping("/{id}")
    public String updateMedicalRecord(@PathVariable("id") String id, @RequestBody String medicalRecord) {
        String url = getMedicalRecordServiceUrl() + "/medical-records/" + id;
        HttpEntity<String> request = new HttpEntity<>(medicalRecord);
        return restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<String>() {}).getBody();
    }

    public String updateMedicalRecord_fallback(String id, String medicalRecord) {
        return "Fallback: Unable to update medical record with ID " + id;
    }

    // DELETE endpoint to delete a medical record with Hystrix fallback
    @HystrixCommand(fallbackMethod = "deleteMedicalRecord_fallback")
    @DeleteMapping("/{id}")
    public String deleteMedicalRecord(@PathVariable("id") String id) {
        String url = getMedicalRecordServiceUrl() + "/medical-records/" + id;
        return restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<String>() {}).getBody();
    }

    public String deleteMedicalRecord_fallback(String id) {
        return "Fallback: Unable to delete medical record with ID " + id;
    }


}
