package com.example.springbootswagger2.model;

// 1- complete student def : country and class
public class Student {

    // this DTO contain : name, Country, cls

    private String name;
    private String country;
    private String cls;

    public Student(String name, String country, String cls) {
        this.name = name;
        this.country = country;
        this.cls = cls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }


    // 5- add swagger api model for all attributes

}
