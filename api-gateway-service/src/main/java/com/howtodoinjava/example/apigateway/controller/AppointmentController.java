package com.howtodoinjava.example.apigateway.controller;

import com.howtodoinjava.example.apigateway.service.ServiceDiscovery;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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

    // Create a LoadBalanced RestTemplate to interact with the Appointment Service
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // Helper method to fetch the service URL dynamically
    private String getAppointmentServiceUrl() {
        String serviceUrl = serviceDiscovery.getServiceUri(APPOINTMENT_SERVICE_NAME);
        if (serviceUrl == null) {
            throw new RuntimeException("Service not found: " + APPOINTMENT_SERVICE_NAME);
        }
        return serviceUrl;
    }

    // GET endpoint to fetch an appointment by ID with Hystrix fallback
    @HystrixCommand(fallbackMethod = "getAppointmentById_fallback")
    @GetMapping("/{id}")
    public String getAppointmentById(@PathVariable("id") String id) {
        String url = getAppointmentServiceUrl() + "/appointments/" + id;
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {}).getBody();
    }

    public String getAppointmentById_fallback(String id) {
        return "Fallback: Unable to fetch appointment with ID " + id;
    }

    // POST endpoint to create an appointment with Hystrix fallback
    @HystrixCommand(fallbackMethod = "createAppointment_fallback")
    @PostMapping("/")
    public String createAppointment(@RequestBody String appointment) {
        String url = getAppointmentServiceUrl() + "/appointments";
        HttpEntity<String> request = new HttpEntity<>(appointment);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {}).getBody();
    }

    public String createAppointment_fallback(String appointment) {
        return "Fallback: Unable to create appointment";
    }

    // PUT endpoint to update an appointment with Hystrix fallback
    @HystrixCommand(fallbackMethod = "updateAppointment_fallback")
    @PutMapping("/{id}")
    public String updateAppointment(@PathVariable("id") String id, @RequestBody String appointment) {
        String url = getAppointmentServiceUrl() + "/appointments/" + id;
        HttpEntity<String> request = new HttpEntity<>(appointment);
        return restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<String>() {}).getBody();
    }

    public String updateAppointment_fallback(String id, String appointment) {
        return "Fallback: Unable to update appointment with ID " + id;
    }

    // DELETE endpoint to delete an appointment with Hystrix fallback
    @HystrixCommand(fallbackMethod = "deleteAppointment_fallback")
    @DeleteMapping("/{id}")
    public String deleteAppointment(@PathVariable("id") String id) {
        String url = getAppointmentServiceUrl() + "/appointments/" + id;
        return restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<String>() {}).getBody();
    }

    public String deleteAppointment_fallback(String id) {
        return "Fallback: Unable to delete appointment with ID " + id;
    }
}
