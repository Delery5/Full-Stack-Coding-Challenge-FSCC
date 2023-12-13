package com.ibm.delery.registrationservice.service;

import com.ibm.delery.registrationservice.dto.RegistrationDto;

import java.util.List;

public interface EmployeeRegistrationService {

    public List<RegistrationDto> getAllEmployees();
}
