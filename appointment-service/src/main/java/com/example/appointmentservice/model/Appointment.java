package com.example.appointmentservice.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@ApiModel(description = "Details about the appointment")
public class Appointment {

    private static int idCounter = 0;

    @Id
    @ApiModelProperty(notes = "The unique ID of the appointment, auto-incremented")
    private int id;

    @ApiModelProperty(notes = "The id of the associated patient")
    private int patientId;

    @ApiModelProperty(notes = "The id of the associated practitioner")
    private int practitionerId;

    @ApiModelProperty(notes = "The date and time of the appointment")
    private LocalDateTime dateTime;

    @ApiModelProperty(notes = "The description of the appointment")
    private String description;

    public Appointment() {
        // Default constructor
    }

    public Appointment(int patientId, int practitionerId, LocalDateTime dateTime, String description) {
        this.id = ++idCounter;
        this.patientId = patientId;
        this.practitionerId = practitionerId;
        this.dateTime = dateTime;
        this.description = description;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "Appointment{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", practitionerId=" + practitionerId +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                '}';
    }
}