package com.howtodoinjava.example.apigateway.controller;

import com.howtodoinjava.example.apigateway.service.ServiceDiscovery;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ServiceDiscovery serviceDiscovery;

    private static final String APPOINTMENT_SERVICE_NAME = "appointment-service";

    // Helper method to fetch the service URL dynamically
    private String getAppointmentServiceUrl() {
        String serviceUrl = serviceDiscovery.getServiceUri(APPOINTMENT_SERVICE_NAME);
        if (serviceUrl == null) {
            throw new RuntimeException("Service not found: " + APPOINTMENT_SERVICE_NAME);
        }
        return serviceUrl;
    }

    // GET endpoint to fetch an appointment by ID with Hystrix fallback
    @HystrixCommand(fallbackMethod = "getAppointmentByIdFallback")
    @GetMapping("/{id}")
    public ResponseEntity<String> getAppointmentById(@PathVariable("id") String id) {
        String url = getAppointmentServiceUrl() + "/appointments/" + id;
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {}
        );
        return ResponseEntity.ok(response.getBody());
    }

    public ResponseEntity<String> getAppointmentByIdFallback(String id) {
        return ResponseEntity.status(404).body("Fallback: Unable to fetch appointment with ID " + id);
    }

    // POST endpoint to create an appointment with Hystrix fallback
    @HystrixCommand(fallbackMethod = "createAppointmentFallback")
    @PostMapping("/")
    public ResponseEntity<String> createAppointment(@RequestBody String appointment) {
        String url = getAppointmentServiceUrl() + "/appointments";
        HttpEntity<String> request = new HttpEntity<>(appointment);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {}
        );
        return ResponseEntity.status(201).body(response.getBody());
    }

    public ResponseEntity<String> createAppointmentFallback(String appointment) {
        return ResponseEntity.status(400).body("Fallback: Unable to create appointment");
    }

    // PUT endpoint to update an appointment with Hystrix fallback
    @HystrixCommand(fallbackMethod = "updateAppointmentFallback")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateAppointment(@PathVariable("id") String id, @RequestBody String appointment) {
        String url = getAppointmentServiceUrl() + "/appointments/" + id;
        HttpEntity<String> request = new HttpEntity<>(appointment);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<String>() {}
        );
        return ResponseEntity.ok(response.getBody());
    }

    public ResponseEntity<String> updateAppointmentFallback(String id, String appointment) {
        return ResponseEntity.status(400).body("Fallback: Unable to update appointment with ID " + id);
    }

    // DELETE endpoint to delete an appointment with Hystrix fallback
    @HystrixCommand(fallbackMethod = "deleteAppointmentFallback")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable("id") String id) {
        String url = getAppointmentServiceUrl() + "/appointments/" + id;
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<String>() {}
        );
        return ResponseEntity.ok(response.getBody());
    }

    public ResponseEntity<String> deleteAppointmentFallback(String id) {
        return ResponseEntity.status(404).body("Fallback: Unable to delete appointment with ID " + id);
    }
}
