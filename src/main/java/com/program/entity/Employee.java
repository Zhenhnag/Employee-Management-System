package com.program.entity;

import java.util.Date;

public class Employee {
    private Integer id;
    private String name;
    private Double salary;
    private Date birthday;
    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Employee(Integer id, String name, Double salary, Date birthday, String photo) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.birthday = birthday;
        this.photo = photo;
    }

    public Employee() {
    }

    public Employee(Integer id, String name, Double salary, Date birthday) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.birthday = birthday;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
