package com.ibm.delery.employeservice.service;


import com.ibm.delery.employeservice.dto.EmployeeDTO;
import com.ibm.delery.employeservice.entity.Employee;
import com.ibm.delery.employeservice.exception.EmployeeAlreadyExistsException;
import com.ibm.delery.employeservice.exception.EmployeeNotFoundByEmailException;
import com.ibm.delery.employeservice.exception.EmployeeNotFoundException;
import com.ibm.delery.employeservice.repository.EmployeeRepository;
import jakarta.servlet.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    @Autowired
    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {

        List<Employee> employees = employeeRepository.findAll();

        return employees.
                stream()
                .map(employee -> mapToDTO(employee))
                .collect(Collectors.toList());
    }


    @Override
    public Optional<EmployeeDTO> findEmployeeByEmail(String email) throws EmployeeNotFoundByEmailException {

        return employeeRepository.findByEmail(email)
                .map(employee -> mapToDTO(employee));
    }

    @Override
    public EmployeeDTO CreateEmployee(EmployeeDTO employeeDTO) throws EmployeeAlreadyExistsException {


        Employee employee = mapToEntity(employeeDTO);

        // Check if user with email already exists
        if (employeeRepository.findByEmail(employeeDTO.email()).isPresent()) {
            logger.error("Employee email {} already exists. ", employeeDTO.email());
            throw new EmployeeAlreadyExistsException("Employee email {} " + employeeDTO.email() + " already exists.");
        }

        Employee newEmployee = employeeRepository.save(employee);

        // convert Employee entity to EmployeeDTO
        EmployeeDTO employeeResponse = mapToDTO(newEmployee);
        logger.info("Employee email {} created successfully.", employee.getEmail() );

        return employeeResponse;
    }

    @Override
    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO, Long id) throws EmployeeNotFoundException, EmployeeAlreadyExistsException {


        if (!employeeRepository.findById(id).isPresent()) {
            logger.error("Employee Id {} not found.", id);
            throw new EmployeeNotFoundException("Employee Id " + id + " not found.");
        }

        Employee employee = employeeRepository
                .findById(id)
                        .orElseThrow();

//        employee.setId(employeeDTO.getId());
//        employee.setUserId(employeeDTO.getUserId());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setAddress(employeeDTO.getAddress());
        employee.setState(employeeDTO.getState());
        employee.setZip(employeeDTO.getZip());
        employee.setCellPhone(employeeDTO.getCellPhone());
        employee.setHomePhone(employeeDTO.getHomePhone());
        employee.setEmail(employeeDTO.getEmail());

        Employee updateEmployee = employeeRepository.save(employee);
        logger.info("Employee Id {} updated successfully.", employee.getId());

        return mapToDTO(updateEmployee);
    }

    @Override
    public void deleteEmployeeById(Long id) throws EmployeeNotFoundException{

        if (!employeeRepository.findById(id).isPresent()) {
            logger.error("Employee userId {} not found.", id);
            throw new EmployeeNotFoundException("Employee Id " + id + " not found.");
        }
        Employee employeeDelete = employeeRepository
                .findById(id)
                        .orElseThrow();

        employeeRepository.deleteById(employeeDelete.getId());
        logger.info("Employee userId {} deleted successfully.", employeeDelete.getId());

    }

    private EmployeeDTO mapToDTO(Employee employee) {

        // convert Employee entity to EmployeeDTO
        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setId(employee.getId());
        employeeDTO.setUserId(employee.getUserId());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setAddress(employee.getAddress());
        employeeDTO.setState(employee.getState());
        employeeDTO.setZip(employee.getZip());
        employeeDTO.setCellPhone(employee.getCellPhone());
        employeeDTO.setHomePhone(employee.getHomePhone());
        employeeDTO.setEmail(employee.getEmail());

        return employeeDTO;
    }

    private Employee mapToEntity(EmployeeDTO employeeDTO) {

        // convert EmployeeDTO to Employee entity
        Employee employee = new Employee();

        employee.setId(employeeDTO.getId());
        employee.setUserId(employeeDTO.getUserId());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setAddress(employeeDTO.getAddress());
        employee.setState(employeeDTO.getState());
        employee.setZip(employeeDTO.getZip());
        employee.setCellPhone(employeeDTO.getCellPhone());
        employee.setHomePhone(employeeDTO.getHomePhone());
        employee.setEmail(employeeDTO.getEmail());

        return employee;
    }

}

