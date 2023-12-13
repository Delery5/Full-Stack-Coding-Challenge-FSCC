package com.ibm.delery.employeservice.dto;

import com.ibm.delery.employeservice.entity.Employee;

import java.util.UUID;

public class EmployeeDTO {

    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String address;
    private String state;
    private String zip;
    private String cellPhone;
    private String homePhone;
    private String email;

    public EmployeeDTO() {

    }

    public EmployeeDTO(Long id, String userId, String firstName, String lastName, String address, String state, String zip, String cellPhone, String homePhone, String email) {
        this.id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.state = state;
        this.zip = zip;
        this.cellPhone = cellPhone;
        this.homePhone = homePhone;
        this.email = email;
    }

    public EmployeeDTO(Employee employee) {
    }

    public EmployeeDTO(EmployeeDTO employeeDTO) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = UUID.randomUUID().toString();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String email() {
        return email;
    }
}
