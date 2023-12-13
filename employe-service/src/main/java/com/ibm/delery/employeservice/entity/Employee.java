package com.ibm.delery.employeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userId") // Generated with UUID
    private String userId;
    @Column(name = "firstName", nullable = false, unique = false)
    @NotBlank(message = "Must type First name")
    @Size(min = 2, max = 30, message = "First name must be between {min} and {max} characters")
    private String firstName;

    @Column(name = "lastName", nullable = false, unique = false)
    @NotBlank(message = "Must type Last name")
    @Size(min = 2, max = 30, message = "Last name must be between {min} and {max} characters")
    private String lastName;

    @Column(name = "address", nullable = false, unique = false)
    @NotBlank(message = "Must type in an Address")
    @Size(max = 100, message = "Address must be less than {max} characters")
    private String address;

    @Column(name = "state", nullable = false, unique = false)
    @NotBlank(message = "Must type in a State")
    @Size(max = 25, message = "State must be less than {max} characters")
    private String state;

    @Column(name = "zip", nullable = false, unique = false)
    @NotBlank(message = "Must type in a Zip")
    @Size(max = 8, message = "State must be less than {max} characters")
    private String zip;

    @Column(name = "cellPhone", nullable = false, unique = false)
    @NotBlank(message = "Must type in valid Cellphone number")
    @Pattern(regexp = "^[0-9]{3}-[0-9]{3}-[0-9]{4}$", message = "Invalid Cellphone number format ex: XXX-XXX-XXXX")
    private String cellPhone;

    @Column(name = "homePhone", nullable = false, unique = false)
    @NotBlank(message = "Must type in valid Homephone number")
    @Pattern(regexp = "^[0-9]{3}-[0-9]{3}-[0-9]{4}$", message = "Invalid Homephone number format ex: XXX-XXX-XXXX")
    private String homePhone;

    @Column(name = "email", nullable = false, unique = false)
    @NotBlank(message = "Must type in a valid Email")
    @Size(min = 10, max = 150, message = "Email must be between {min} and {max} characters")
    @Email(message = "Invalid email format")
    private String email;

    public Employee() {

    }

    public Employee(Long id, String userId, String firstName, String lastName, String address, String state, String zip, String cellPhone, String homePhone, String email) {
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
}
