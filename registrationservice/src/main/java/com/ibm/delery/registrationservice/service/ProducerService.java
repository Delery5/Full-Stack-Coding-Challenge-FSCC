package com.ibm.delery.registrationservice.service;

import com.ibm.delery.registrationservice.dto.RegistrationDto;
import com.ibm.delery.registrationservice.exception.EmployeeAlreadyApprovedException;
import com.ibm.delery.registrationservice.exception.EmployeeAlreadyExistsException;
import com.ibm.delery.registrationservice.exception.EmployeeNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProducerService {

    RegistrationDto createEmployee(RegistrationDto registrationDto) throws EmployeeAlreadyExistsException;

    public RegistrationDto approveEmployee(@PathVariable("email") String email, RegistrationDto registrationDto) throws EmployeeAlreadyApprovedException, EmployeeNotFoundException;

    public void declineEmployees(String email) throws EmployeeNotFoundException, EmployeeAlreadyApprovedException;
}
