package com.ibm.delery.employeservice.service;


import com.ibm.delery.employeservice.dto.EmployeeDTO;
import com.ibm.delery.employeservice.exception.EmployeeAlreadyExistsException;
import com.ibm.delery.employeservice.exception.EmployeeNotFoundByEmailException;
import com.ibm.delery.employeservice.exception.EmployeeNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeService {

    List<EmployeeDTO> getAllEmployees();

    Optional<EmployeeDTO> findEmployeeByEmail(String email) throws EmployeeNotFoundByEmailException;

    EmployeeDTO CreateEmployee(EmployeeDTO employeeDTO) throws EmployeeAlreadyExistsException;

    EmployeeDTO updateEmployee(EmployeeDTO employeeDTO, Long id) throws EmployeeNotFoundException, EmployeeAlreadyExistsException;

    void deleteEmployeeById(Long id) throws EmployeeNotFoundException;
}

