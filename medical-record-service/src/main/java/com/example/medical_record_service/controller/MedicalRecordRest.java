package com.example.medical_record_service.controller;

import com.example.medical_record_service.model.MedicalRecord;
import com.example.medical_record_service.service.ServiceDiscovery;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;
import java.util.Vector;

@RestController
@EnableSwagger2
@Api(value = "patient", description = "CRUD for patients")
public class MedicalRecordRest {

    static private final Vector<MedicalRecord> medical_records = new Vector<>();

    private static final String GATEWAY_SERVICE_NAME = "api-gateway";

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Autowired
    private ServiceDiscovery serviceDiscovery;

    @Autowired
    private RestTemplate restTemplate;

    private final String PRACTITIONER_SERVICE_NAME = "practitioner-service";
    private final String PATIENT_SERVICE_NAME = "patient-service";

    static {
        // initialize patient list
        medical_records.add(new MedicalRecord(1, 1, "medical record 1"));
        medical_records.add(new MedicalRecord(1, 2, "medical record 2"));
    }

    private final String apiRoot = "/medical-record";

    @ApiOperation(value = "get patient by name", response = MedicalRecord.class)
    @RequestMapping(value = apiRoot + "/with-name/{name}", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "not authorized"),
            @ApiResponse(code = 403, message = "forbidden"),
            @ApiResponse(code = 404, message = "not found")
    })
    @HystrixCommand(fallbackMethod = "getPatientByName_Fallback")
    public ResponseEntity<List<MedicalRecord>> getPatientByName(@PathVariable(value = "name") String name) {
        Vector<MedicalRecord> result = new Vector<>();

        for (MedicalRecord medicalRecord : medical_records) {
            if (medicalRecord.getName().equals(name)) {
                result.add(medicalRecord);
            }
        }

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(result);
    }

    // Fallback method
    public ResponseEntity<MedicalRecord> getPatientByName_Fallback(@PathVariable(value = "name") String name) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @ApiOperation(value = "Add a new patient", response = MedicalRecord.class)
    @RequestMapping(value = apiRoot + "/", method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @HystrixCommand(fallbackMethod = "createPatient_Fallback")
    public ResponseEntity<MedicalRecord> createPatient(@RequestBody int patientId, @RequestBody int practitionerId, @RequestBody String name) {

        if (!doesPractitionerExist(practitionerId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // Practitioner not found
        }

        if (!doesPatientExist(patientId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // Patient not found
        }

        MedicalRecord created_medicalRecord = new MedicalRecord(patientId, practitionerId, name);
        medical_records.add(created_medicalRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body(created_medicalRecord);
    }

    // Fallback method
    public ResponseEntity<MedicalRecord> createPatient_Fallback(@RequestBody String name) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @ApiOperation(value = "get patient by id", response = MedicalRecord.class)
    @RequestMapping(value = apiRoot + "/{id}", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "not authorized"),
            @ApiResponse(code = 403, message = "forbidden"),
            @ApiResponse(code = 404, message = "not found")
    })
    @HystrixCommand(fallbackMethod = "getPatientById_Fallback")
    public ResponseEntity<MedicalRecord> getPatientById(@PathVariable(value = "id") int id) {
        for (MedicalRecord medicalRecord : medical_records) {
            if (medicalRecord.getId() == id) {
                return ResponseEntity.ok(medicalRecord);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // Fallback method
    public ResponseEntity<MedicalRecord> getPatientById_Fallback(@PathVariable(value = "id") int id) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @ApiOperation(value = "get all patients", response = MedicalRecord.class)
    @RequestMapping(value = apiRoot + "/", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "not authorized"),
            @ApiResponse(code = 403, message = "forbidden"),
            @ApiResponse(code = 404, message = "not found")
    })
    @HystrixCommand(fallbackMethod = "getAllPatients_Fallback")
    public ResponseEntity<List<MedicalRecord>> getAllPatients() {
        return ResponseEntity.ok(medical_records);
    }

    // Fallback method
    public ResponseEntity<List<MedicalRecord>> getAllPatients_Fallback() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @ApiOperation(value = "Update an existing patient", response = MedicalRecord.class)
    @RequestMapping(value = apiRoot + "/{id}", method = RequestMethod.PUT)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Updated Successfully"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @HystrixCommand(fallbackMethod = "updatePatient_Fallback")
    public ResponseEntity<MedicalRecord> updatePatient(@PathVariable("id") int id, @RequestBody MedicalRecord updatedMedicalRecord) {
        if (!doesPractitionerExist(updatedMedicalRecord.getPractitionerId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // Practitioner not found
        }

        if (!doesPatientExist(updatedMedicalRecord.getPractitionerId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // Patient not found
        }

        for (MedicalRecord medicalRecord : medical_records) {
            if (medicalRecord.getId() == id) {

                medicalRecord.setName(updatedMedicalRecord.getName());
                medicalRecord.setPatientId(updatedMedicalRecord.getPatientId());
                medicalRecord.setPractitionerId(updatedMedicalRecord.getPractitionerId());
                return ResponseEntity.ok(medicalRecord);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // Fallback method
    public ResponseEntity<MedicalRecord> updatePatient_Fallback() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @ApiOperation(value = "Delete a patient by ID")
    @RequestMapping(value = apiRoot + "/{id}", method = RequestMethod.DELETE)
    @ApiResponses({
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @HystrixCommand(fallbackMethod = "deletePatient_Fallback")
    public ResponseEntity<Void> deletePatient(@PathVariable("id") int id) {
        if (medical_records.removeIf(medicalRecord -> medicalRecord.getId() == id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Fallback method
    public ResponseEntity<MedicalRecord> deletePatient_Fallback() {
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
