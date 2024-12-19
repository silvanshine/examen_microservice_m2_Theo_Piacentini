//package com.example.medical_record_service.controller;
//
//import com.example.medical_record_service.model.MedicalRecord;
//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.List;
//import java.util.Vector;
//
//@RestController
//@EnableSwagger2
//@Api(value = "patient", description = "CRUD for patients")
//public class MedicalRecordRest {
//
//    static private final Vector<MedicalRecord> medical_records = new Vector<>();
//
//    static {
//        // initialize patient list
//        medical_records.add(new MedicalRecord("Thomas Jefferson"));
//        medical_records.add(new MedicalRecord("John Kennedy"));
//    }
//
//    private final String apiRoot = "/medical-record";
//
//    @ApiOperation(value = "get patient by name", response = MedicalRecord.class)
//    @RequestMapping(value = apiRoot + "/with-name/{name}", method = RequestMethod.GET)
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "OK"),
//            @ApiResponse(code = 401, message = "not authorized"),
//            @ApiResponse(code = 403, message = "forbidden"),
//            @ApiResponse(code = 404, message = "not found")
//    })
//    @HystrixCommand(fallbackMethod = "getPatientByName_Fallback")
//    public ResponseEntity<List<MedicalRecord>> getPatientByName(@PathVariable(value = "name") String name) {
//        Vector<MedicalRecord> result = new Vector<>();
//
//        for (MedicalRecord medicalRecord : medical_records) {
//            if (medicalRecord.getName().equals(name)) {
//                result.add(medicalRecord);
//            }
//        }
//
//        if (result.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//
//        return ResponseEntity.ok(result);
//    }
//
//    // Fallback method
//    public ResponseEntity<MedicalRecord> getPatientByName_Fallback(@PathVariable(value = "name") String name) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//    }
//
//    @ApiOperation(value = "Add a new patient", response = MedicalRecord.class)
//    @RequestMapping(value = apiRoot + "/", method = RequestMethod.POST)
//    @ApiResponses({
//            @ApiResponse(code = 201, message = "Created"),
//            @ApiResponse(code = 400, message = "Bad Request")
//    })
//    @HystrixCommand(fallbackMethod = "createPatient_Fallback")
//    public ResponseEntity<MedicalRecord> createPatient(@RequestBody String name) {
//        MedicalRecord created_medicalRecord = new MedicalRecord(name);
//        medical_records.add(created_medicalRecord);
//        return ResponseEntity.status(HttpStatus.CREATED).body(created_medicalRecord);
//    }
//
//    // Fallback method
//    public ResponseEntity<MedicalRecord> createPatient_Fallback(@RequestBody String name) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//    }
//
//    @ApiOperation(value = "get patient by id", response = MedicalRecord.class)
//    @RequestMapping(value = apiRoot + "/{id}", method = RequestMethod.GET)
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "OK"),
//            @ApiResponse(code = 401, message = "not authorized"),
//            @ApiResponse(code = 403, message = "forbidden"),
//            @ApiResponse(code = 404, message = "not found")
//    })
//    @HystrixCommand(fallbackMethod = "getPatientById_Fallback")
//    public ResponseEntity<MedicalRecord> getPatientById(@PathVariable(value = "id") int id) {
//        for (MedicalRecord medicalRecord : medical_records) {
//            if (medicalRecord.getId() == id) {
//                return ResponseEntity.ok(medicalRecord);
//            }
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//    }
//
//    // Fallback method
//    public ResponseEntity<MedicalRecord> getPatientById_Fallback(@PathVariable(value = "id") int id) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//    }
//
//    @ApiOperation(value = "get all patients", response = MedicalRecord.class)
//    @RequestMapping(value = apiRoot + "/", method = RequestMethod.GET)
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "OK"),
//            @ApiResponse(code = 401, message = "not authorized"),
//            @ApiResponse(code = 403, message = "forbidden"),
//            @ApiResponse(code = 404, message = "not found")
//    })
//    @HystrixCommand(fallbackMethod = "getAllPatients_Fallback")
//    public ResponseEntity<List<MedicalRecord>> getAllPatients() {
//        return ResponseEntity.ok(medical_records);
//    }
//
//    // Fallback method
//    public ResponseEntity<List<MedicalRecord>> getAllPatients_Fallback() {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//    }
//
//    @ApiOperation(value = "Update an existing patient", response = MedicalRecord.class)
//    @RequestMapping(value = apiRoot + "/{id}", method = RequestMethod.PUT)
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "Updated Successfully"),
//            @ApiResponse(code = 404, message = "Not Found")
//    })
//    @HystrixCommand(fallbackMethod = "updatePatient_Fallback")
//    public ResponseEntity<MedicalRecord> updatePatient(@PathVariable("id") int id, @RequestBody MedicalRecord updatedMedicalRecord) {
//        for (MedicalRecord medicalRecord : medical_records) {
//            if (medicalRecord.getId() == id) {
//                medicalRecord.setName(updatedMedicalRecord.getName());
//                return ResponseEntity.ok(medicalRecord);
//            }
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//    }
//
//    // Fallback method
//    public ResponseEntity<MedicalRecord> updatePatient_Fallback() {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//    }
//
//    @ApiOperation(value = "Delete a patient by ID")
//    @RequestMapping(value = apiRoot + "/{id}", method = RequestMethod.DELETE)
//    @ApiResponses({
//            @ApiResponse(code = 204, message = "No Content"),
//            @ApiResponse(code = 404, message = "Not Found")
//    })
//    @HystrixCommand(fallbackMethod = "deletePatient_Fallback")
//    public ResponseEntity<Void> deletePatient(@PathVariable("id") int id) {
//        if (medical_records.removeIf(medicalRecord -> medicalRecord.getId() == id)) {
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }
//
//    // Fallback method
//    public ResponseEntity<MedicalRecord> deletePatient_Fallback() {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//    }
//
//}
