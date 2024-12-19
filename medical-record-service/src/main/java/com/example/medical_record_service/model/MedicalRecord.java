package com.example.medical_record_service.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;

@ApiModel(description = "Details about the medical record")
public class MedicalRecord {

    private static int idCounter = 0;

    @Id
    @ApiModelProperty(notes = "The unique ID of the medical record, auto-incremented")
    private int id;

    @ApiModelProperty(notes = "The name of the medical record")
    private String name;

    @ApiModelProperty(notes = "The ID of the associated patient")
    private int patientId;

    @ApiModelProperty(notes = "The ID of the associated practitioner")
    private int practitionerId;

    public MedicalRecord(int patientId, int practitionerId, String name) {
        this.id = ++idCounter;
        this.name = name;
        this.patientId = patientId;
        this.practitionerId = practitionerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getPractitionerId() {
        return practitionerId;
    }

    public void setPractitionerId(int practitionerId) {
        this.practitionerId = practitionerId;
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", patientId=" + patientId +
                ", practitionerId=" + practitionerId +
                '}';
    }
}
