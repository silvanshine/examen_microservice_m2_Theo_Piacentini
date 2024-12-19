package com.howtodoinjava.example.apigateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class EmployeeController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/employeeDetails/{employeeid}", method = RequestMethod.GET)
    // what hystrix annotation to expose fallback ?
    public String getStudents(@PathVariable int employeeid) {
        System.out.println("Getting Employee details for " + employeeid);

        String response = this.restTemplate.exchange("http://employee-service/findEmployeeDetails/{employeeid}",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }, employeeid).getBody();

        System.out.println("Response Body " + response);

        return "Employee Id -  " + employeeid + " [ Employee Details " + response + " ]";
    }


    // TODO expose fallback method (free impl)

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
