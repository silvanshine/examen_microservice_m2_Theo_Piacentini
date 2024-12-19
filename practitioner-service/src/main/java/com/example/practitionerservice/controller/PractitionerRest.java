package com.example.practitionerservice.controller;

import com.example.practitionerservice.model.Practitioner;
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
@Api(value = "Practitioner", description = "CRUD for Practitioners")
public class PractitionerRest {

    static private final Vector<Practitioner> practitioners = new Vector<>();

    static {
        // initialize Practitioner list
        practitioners.add(new Practitioner("Thomas Jefferson"));
        practitioners.add(new Practitioner("John Kennedy"));
    }

    private final String apiRoot = "/practitioners";

    @ApiOperation(value = "get Practitioner by name", response = Practitioner.class)
    @RequestMapping(value = apiRoot + "/with-name/{name}", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "not authorized"),
            @ApiResponse(code = 403, message = "forbidden"),
            @ApiResponse(code = 404, message = "not found")
    })
    @HystrixCommand(fallbackMethod = "getPractitionerByName_Fallback")
    public ResponseEntity<List<Practitioner>> getPractitionerByName(@PathVariable(value = "name") String name) {
        Vector<Practitioner> result = new Vector<>();

        for (Practitioner Practitioner : practitioners) {
            if (Practitioner.getName().equals(name)) {
                result.add(Practitioner);
            }
        }

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(result);
    }

    // Fallback method
    public ResponseEntity<Practitioner> getPractitionerByName_Fallback(@PathVariable(value = "name") String name) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @ApiOperation(value = "Add a new Practitioner", response = Practitioner.class)
    @RequestMapping(value = apiRoot + "/", method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @HystrixCommand(fallbackMethod = "createPractitioner_Fallback")
    public ResponseEntity<Practitioner> createPractitioner(@RequestBody String name) {
        Practitioner created_Practitioner = new Practitioner(name);
        practitioners.add(created_Practitioner);
        return ResponseEntity.status(HttpStatus.CREATED).body(created_Practitioner);
    }

    // Fallback method
    public ResponseEntity<Practitioner> createPractitioner_Fallback(@RequestBody String name) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @ApiOperation(value = "get Practitioner by id", response = Practitioner.class)
    @RequestMapping(value = apiRoot + "/{id}", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "not authorized"),
            @ApiResponse(code = 403, message = "forbidden"),
            @ApiResponse(code = 404, message = "not found")
    })
    @HystrixCommand(fallbackMethod = "getPractitionerById_Fallback")
    public ResponseEntity<Practitioner> getPractitionerById(@PathVariable(value = "id") int id) {
        for (Practitioner Practitioner : practitioners) {
            if (Practitioner.getId() == id) {
                return ResponseEntity.ok(Practitioner);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // Fallback method
    public ResponseEntity<Practitioner> getPractitionerById_Fallback(@PathVariable(value = "id") int id) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @ApiOperation(value = "get all Practitioners", response = Practitioner.class)
    @RequestMapping(value = apiRoot + "/", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "not authorized"),
            @ApiResponse(code = 403, message = "forbidden"),
            @ApiResponse(code = 404, message = "not found")
    })
    @HystrixCommand(fallbackMethod = "getAllPractitioners_Fallback")
    public ResponseEntity<List<Practitioner>> getAllPractitioners() {
        return ResponseEntity.ok(practitioners);
    }

    // Fallback method
    public ResponseEntity<List<Practitioner>> getAllPractitioners_Fallback() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @ApiOperation(value = "Update an existing Practitioner", response = Practitioner.class)
    @RequestMapping(value = apiRoot + "/{id}", method = RequestMethod.PUT)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Updated Successfully"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @HystrixCommand(fallbackMethod = "updatePractitioner_Fallback")
    public ResponseEntity<Practitioner> updatePractitioner(@PathVariable("id") int id, @RequestBody Practitioner updatedPractitioner) {
        for (Practitioner Practitioner : practitioners) {
            if (Practitioner.getId() == id) {
                Practitioner.setName(updatedPractitioner.getName());
                return ResponseEntity.ok(Practitioner);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // Fallback method
    public ResponseEntity<Practitioner> updatePractitioner_Fallback() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @ApiOperation(value = "Delete a Practitioner by ID")
    @RequestMapping(value = apiRoot + "/{id}", method = RequestMethod.DELETE)
    @ApiResponses({
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @HystrixCommand(fallbackMethod = "deletePractitioner_Fallback")
    public ResponseEntity<Void> deletePractitioner(@PathVariable("id") int id) {
        if (practitioners.removeIf(Practitioner -> Practitioner.getId() == id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Fallback method
    public ResponseEntity<Practitioner> deletePractitioner_Fallback() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
