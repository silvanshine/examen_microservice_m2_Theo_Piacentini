package com.howtodoinjava.example.apigateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceDiscovery {

    private final DiscoveryClient discoveryClient;

    @Autowired
    public ServiceDiscovery(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    /**
     * Get a list of all registered services.
     *
     * @return List of service names
     */
    public List<String> getAllServices() {
        return discoveryClient.getServices();
    }

    /**
     * Get instances of a specific service.
     *
     * @param serviceName Name of the service
     * @return List of ServiceInstance
     */
    public List<ServiceInstance> getServiceInstances(String serviceName) {
        return discoveryClient.getInstances(serviceName);
    }

    /**
     * Get a specific service's URI.
     *
     * @param serviceName Name of the service
     * @return URI of the first available instance or null if none found
     */
    public String getServiceUri(String serviceName) {
        List<ServiceInstance> instances = getServiceInstances(serviceName);
        if (instances != null && !instances.isEmpty()) {
            return instances.get(0).getUri().toString();
        }
        return null;
    }
}