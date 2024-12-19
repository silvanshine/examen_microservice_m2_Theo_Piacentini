package com.example.appointmentservice.controller;

import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.service.ServiceDiscovery;
import com.google.api.client.util.DateTime;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Vector;
import org.springframework.web.client.RestTemplate;

@RestController
@EnableSwagger2
@Api(value = "Appointment", description = "CRUD for appoinments")
public class AppointmentRest {

    private static final String GATEWAY_SERVICE_NAME = "api-gateway";

    @Autowired
    private ServiceDiscovery serviceDiscovery;


    static private final Vector<Appointment> Appointments = new Vector<>();

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    static {
        // initialize Appointment list
        Appointments.add(new Appointment(
                1,
                2,
                LocalDateTime.of(2024, 12, 25, 10, 30, 45),
                "appointment 1")
        );
        Appointments.add(new Appointment(
                2,
                2,
                LocalDateTime.of(2024, 12, 25, 10, 30, 45),
                "appointment 2")
        );
    }

    private final String apiRoot = "/appointments";

    @ApiOperation(value = "Add a new Appointment", response = Appointment.class)
    @RequestMapping(value = apiRoot + "/", method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @HystrixCommand(fallbackMethod = "createAppointment_Fallback")
    public ResponseEntity<Appointment> createAppointment(@RequestBody int patientId, @RequestBody int practitionerId, @RequestBody LocalDateTime datetime, @RequestBody String description) {

        if (!doesPractitionerExist(practitionerId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // Practitioner not found
        }

        if (!doesPatientExist(patientId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // Patient not found
        }
        Appointment created_Appointment = new Appointment(patientId, practitionerId, datetime, description);
        Appointments.add(created_Appointment);
        return ResponseEntity.status(HttpStatus.CREATED).body(created_Appointment);
    }

    // Fallback method
    public ResponseEntity<Appointment> createAppointment_Fallback(@RequestBody String name) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @ApiOperation(value = "get Appointment by id", response = Appointment.class)
    @RequestMapping(value = apiRoot + "/{id}", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "not authorized"),
            @ApiResponse(code = 403, message = "forbidden"),
            @ApiResponse(code = 404, message = "not found")
    })
    @HystrixCommand(fallbackMethod = "getAppointmentById_Fallback")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable(value = "id") int id) {
        for (Appointment Appointment : Appointments) {
            if (Appointment.getId() == id) {
                return ResponseEntity.ok(Appointment);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // Fallback method
    public ResponseEntity<Appointment> getAppointmentById_Fallback(@PathVariable(value = "id") int id) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @ApiOperation(value = "get all Appointments", response = Appointment.class)
    @RequestMapping(value = apiRoot + "/", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "not authorized"),
            @ApiResponse(code = 403, message = "forbidden"),
            @ApiResponse(code = 404, message = "not found")
    })
    @HystrixCommand(fallbackMethod = "getAllAppointments_Fallback")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(Appointments);
    }

    // Fallback method
    public ResponseEntity<List<Appointment>> getAllAppointments_Fallback() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @ApiOperation(value = "Update an existing Appointment", response = Appointment.class)
    @RequestMapping(value = apiRoot + "/{id}", method = RequestMethod.PUT)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Updated Successfully"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @HystrixCommand(fallbackMethod = "updateAppointment_Fallback")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable("id") int id, @RequestBody Appointment updatedAppointment) {
        if (!doesPractitionerExist(updatedAppointment.getPractitionerId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // Practitioner not found
        }

        if (!doesPatientExist(updatedAppointment.getPatientId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // Patient not found
        }

        for (Appointment Appointment : Appointments) {
            if (Appointment.getId() == id) {

                Appointment.setDateTime(updatedAppointment.getDateTime());
                Appointment.setDescription(updatedAppointment.getDescription());
                Appointment.setPatientId(updatedAppointment.getPatientId());
                Appointment.setPractitionerId(updatedAppointment.getPractitionerId());

                return ResponseEntity.ok(Appointment);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // Fallback method
    public ResponseEntity<Appointment> updateAppointment_Fallback() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @ApiOperation(value = "Delete an appointment by id")
    @RequestMapping(value = apiRoot + "/{id}", method = RequestMethod.DELETE)
    @ApiResponses({
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @HystrixCommand(fallbackMethod = "deleteAppointment_Fallback")
    public ResponseEntity<Void> deleteAppointment(@PathVariable("id") int id) {
        if (Appointments.removeIf(Appointment -> Appointment.getId() == id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Fallback method
    public ResponseEntity<Appointment> deleteAppointment_Fallback() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    private boolean doesPractitionerExist(int practitionerId) {
        String serviceUrl = serviceDiscovery.getServiceUri(GATEWAY_SERVICE_NAME);
        if (serviceUrl == null) {
            throw new RuntimeException("Service not found: " + GATEWAY_SERVICE_NAME);
        }
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    serviceUrl + "/api/practitioner/" + practitionerId, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean doesPatientExist(int patientId) {
        String serviceUrl = serviceDiscovery.getServiceUri(GATEWAY_SERVICE_NAME);
        if (serviceUrl == null) {
            throw new RuntimeException("Service not found: " + GATEWAY_SERVICE_NAME);
        }
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    serviceUrl + "/api/patient/" + patientId, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }

}
