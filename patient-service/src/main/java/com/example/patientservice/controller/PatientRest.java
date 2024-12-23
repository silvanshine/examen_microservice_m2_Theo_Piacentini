package com.example.patientservice.controller;

import com.example.patientservice.model.Patient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;
import java.util.Vector;

@RestController
@EnableSwagger2
@Api(value = "patient", description = "CRUD for patients")
public class PatientRest {

    static private final Vector<Patient> patients = new Vector<>();

    static {
        // initialize patient list
        patients.add(new Patient("Thomas Jefferson"));
        patients.add(new Patient("John Kennedy"));
    }

    private final String apiRoot = "/patients";

    @ApiOperation(value = "get patient by name", response = Patient.class)
    @RequestMapping(value = apiRoot + "/with-name/{name}", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "not authorized"),
            @ApiResponse(code = 403, message = "forbidden"),
            @ApiResponse(code = 404, message = "not found")
    })
    @HystrixCommand(fallbackMethod = "getPatientByName_Fallback")
    public ResponseEntity<List<Patient>> getPatientByName(@PathVariable(value = "name") String name) {
        Vector<Patient> result = new Vector<>();

        for (Patient patient : patients) {
            if (patient.getName().equals(name)) {
                result.add(patient);
            }
        }

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(result);
    }

    // Fallback method
    public ResponseEntity<Patient> getPatientByName_Fallback(@PathVariable(value = "name") String name) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @ApiOperation(value = "Add a new patient", response = Patient.class)
    @RequestMapping(value = apiRoot + "/", method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @HystrixCommand(fallbackMethod = "createPatient_Fallback")
    public ResponseEntity<Patient> createPatient(@RequestBody String name) {
        Patient created_patient = new Patient(name);
        patients.add(created_patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(created_patient);
    }

    // Fallback method
    public ResponseEntity<Patient> createPatient_Fallback(@RequestBody String name) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @ApiOperation(value = "get patient by id", response = Patient.class)
    @RequestMapping(value = apiRoot + "/{id}", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "not authorized"),
            @ApiResponse(code = 403, message = "forbidden"),
            @ApiResponse(code = 404, message = "not found")
    })
    @HystrixCommand(fallbackMethod = "getPatientById_Fallback")
    public ResponseEntity<Patient> getPatientById(@PathVariable(value = "id") int id) {
        for (Patient patient : patients) {
            if (patient.getId() == id) {
                return ResponseEntity.ok(patient);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // Fallback method
    public ResponseEntity<Patient> getPatientById_Fallback(@PathVariable(value = "id") int id) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @ApiOperation(value = "get all patients", response = Patient.class)
    @RequestMapping(value = apiRoot + "/", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "not authorized"),
            @ApiResponse(code = 403, message = "forbidden"),
            @ApiResponse(code = 404, message = "not found")
    })
    @HystrixCommand(fallbackMethod = "getAllPatients_Fallback")
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patients);
    }

    // Fallback method
    public ResponseEntity<List<Patient>> getAllPatients_Fallback() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @ApiOperation(value = "Update an existing patient", response = Patient.class)
    @RequestMapping(value = apiRoot + "/{id}", method = RequestMethod.PUT)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Updated Successfully"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @HystrixCommand(fallbackMethod = "updatePatient_Fallback")
    public ResponseEntity<Patient> updatePatient(@PathVariable("id") int id, @RequestBody Patient updatedPatient) {
        for (Patient patient : patients) {
            if (patient.getId() == id) {
                patient.setName(updatedPatient.getName());
                return ResponseEntity.ok(patient);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // Fallback method
    public ResponseEntity<Patient> updatePatient_Fallback() {
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
        if (patients.removeIf(patient -> patient.getId() == id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Fallback method
    public ResponseEntity<Patient> deletePatient_Fallback() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
