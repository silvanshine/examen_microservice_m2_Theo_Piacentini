//package com.howtodoinjava.example.apigateway.controller;
//
//import com.howtodoinjava.example.apigateway.service.ServiceDiscovery;
//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//
//@RestController
//@RequestMapping("/api/patient")
//public class PatientController {
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Autowired
//    private ServiceDiscovery serviceDiscovery;
//
//    private static final String PATIENT_SERVICE_NAME = "patient-service";
//
//    // Create a LoadBalanced RestTemplate to interact with the Patient Service
//    @Bean
//    @LoadBalanced
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
//
//    // Helper method to fetch the service URL dynamically
//    private String getPatientServiceUrl() {
//        String serviceUrl = serviceDiscovery.getServiceUri(PATIENT_SERVICE_NAME);
//        if (serviceUrl == null) {
//            throw new RuntimeException("Service not found: " + PATIENT_SERVICE_NAME);
//        }
//        return serviceUrl;
//    }
//
//    // GET endpoint to fetch a patient by ID with Hystrix fallback
//    @HystrixCommand(fallbackMethod = "getPatientById_fallback")
//    @GetMapping("/{id}")
//    public String getPatientById(@PathVariable("id") String id) {
//        String url = getPatientServiceUrl() + "/patients/" + id;
//        return restTemplate.exchange(
//                url,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<String>() {}).getBody();
//    }
//
//    public String getPatientById_fallback(String id) {
//        return "Fallback: Unable to fetch patient with ID " + id;
//    }
//
//    // POST endpoint to create a patient with Hystrix fallback
//    @HystrixCommand(fallbackMethod = "createPatient_fallback")
//    @PostMapping("/")
//    public String createPatient(@RequestBody String patient) {
//        String url = getPatientServiceUrl() + "/patients";
//        HttpEntity<String> request = new HttpEntity<>(patient);
//        return restTemplate.exchange(
//                url,
//                HttpMethod.POST,
//                request,
//                new ParameterizedTypeReference<String>() {}).getBody();
//    }
//
//    public String createPatient_fallback(String patient) {
//        return "Fallback: Unable to create patient";
//    }
//
//    // PUT endpoint to update a patient with Hystrix fallback
//    @HystrixCommand(fallbackMethod = "updatePatient_fallback")
//    @PutMapping("/{id}")
//    public String updatePatient(@PathVariable("id") String id, @RequestBody String patient) {
//        String url = getPatientServiceUrl() + "/patients/" + id;
//        HttpEntity<String> request = new HttpEntity<>(patient);
//        return restTemplate.exchange(
//                url,
//                HttpMethod.PUT,
//                request,
//                new ParameterizedTypeReference<String>() {}).getBody();
//    }
//
//    public String updatePatient_fallback(String id, String patient) {
//        return "Fallback: Unable to update patient with ID " + id;
//    }
//
//    // DELETE endpoint to delete a patient with Hystrix fallback
//    @HystrixCommand(fallbackMethod = "deletePatient_fallback")
//    @DeleteMapping("/{id}")
//    public String deletePatient(@PathVariable("id") String id) {
//        String url = getPatientServiceUrl() + "/patients/" + id;
//        return restTemplate.exchange(
//                url,
//                HttpMethod.DELETE,
//                null,
//                new ParameterizedTypeReference<String>() {}).getBody();
//    }
//
//    public String deletePatient_fallback(String id) {
//        return "Fallback: Unable to delete patient with ID " + id;
//    }
//}
