package com.example.practitionerservice.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;

@ApiModel(description = "Details about the patient")
public class Practitioner {

    private static int idCounter = 0;

    @Id
    @ApiModelProperty(notes = "The unique ID of the patient, auto-incremented")
    private int id;

    @ApiModelProperty(notes = "The name of the patient")
    private String name;


    public Practitioner(String name) {
        this.id = ++idCounter;
        this.name = name;
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

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
