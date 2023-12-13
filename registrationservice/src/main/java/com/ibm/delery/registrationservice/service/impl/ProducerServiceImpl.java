package com.ibm.delery.registrationservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.delery.registrationservice.dto.RegistrationDto;
import com.ibm.delery.registrationservice.entity.Registration;
import com.ibm.delery.registrationservice.exception.EmployeeAlreadyApprovedException;
import com.ibm.delery.registrationservice.exception.EmployeeAlreadyExistsException;
import com.ibm.delery.registrationservice.exception.EmployeeNotFoundException;
import com.ibm.delery.registrationservice.kafka.KafkaProducer;
import com.ibm.delery.registrationservice.repository.EmployeeRegistrationRepository;
import com.ibm.delery.registrationservice.service.ProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProducerServiceImpl implements ProducerService {

    private static Logger logger = LoggerFactory.getLogger(ProducerServiceImpl.class);




    private final EmployeeRegistrationRepository employeeRegistrationRepository;
    private final KafkaProducer kafkaProducer;

    public ProducerServiceImpl(EmployeeRegistrationRepository employeeRegistrationRepository, KafkaProducer kafkaProducer) {
        this.employeeRegistrationRepository = employeeRegistrationRepository;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public RegistrationDto createEmployee(RegistrationDto registrationDto) throws EmployeeAlreadyExistsException {

        Registration registration = mapToEntity(registrationDto);

        if (employeeRegistrationRepository.findByEmail(registration.email()).isPresent()) {
            logger.error("Employee email {} already exists.", registration.email());
            throw new EmployeeAlreadyExistsException("Employee email " + registration.email() + " already exists!");
        } else {

            // Set employee status to "PENDING" before saving
            registration.setStatus("PENDING");

            Registration newRegistration = employeeRegistrationRepository.save(registration);

           ;

            // convert Employee entity to EmployeeDTO
            RegistrationDto employeeResponse = mapToDTO(newRegistration);


            ObjectMapper objectMapper = new ObjectMapper();
            String employeeStr;

            try {
                employeeStr = objectMapper.writeValueAsString(newRegistration);
            } catch (JsonProcessingException ex) {
                logger.error("Error serializing RegistrationEntity to string: {}", ex.getMessage());
                throw new RuntimeException("Error serializing RegistrationEntity to string:", ex);
            }

            kafkaProducer.send("registrationservice", employeeStr);

            logger.info("Employee email {} successfully registered.", employeeResponse.getEmail());

            return employeeResponse;
        }
    }


    @Override
    public RegistrationDto approveEmployee(String email, RegistrationDto registrationDto) throws EmployeeAlreadyApprovedException, EmployeeNotFoundException {


//        if (!employeeRegistrationRepository.findByEmail(email).isPresent()) {
//            logger.error("Employee email {} not found.", email);
//            throw new EmployeeNotFoundException("Employee email " + email + " not found.");
//        }
//
//        Registration employee = employeeRegistrationRepository.findByEmail(email).orElseThrow();
//
//        if ("APPROVED".equals(employee.getStatus())) //(employee.getStatus().equals("APPROVED"))
//        {
//            logger.error("Employee email {} already approved.", employee.getEmail());
//            throw new EmployeeNotFoundException("Employee email " + employee.getEmail() + " already approved.");
//        }
//
//        employee.setStatus("APPROVED");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String employeeStr;
//
//
//        try {
//            employeeStr = objectMapper.writeValueAsString(employee);
//        } catch (JsonProcessingException ex) {
//            logger.error("Error serializing RegistrationEntity to string: {}", ex.getMessage());
//            throw new RuntimeException("Error serializing RegistrationEntity to string:", ex);
//        }
//        employee.setStatus("APPROVED");
//        kafkaProducer.send("registrationservice", employeeStr);
//
//        logger.info("Employee email {} successfully approved.", employee.getEmail());
//
//
//
//        return new RegistrationDto(employee);
        Optional<Registration> optionalEmployee = employeeRegistrationRepository.findByEmail(email);

        if (optionalEmployee.isPresent()) {
            // Employee found, update the status
            Registration existingEmployee = optionalEmployee.get();
            existingEmployee.setStatus("APPROVED");

//            existingEmployee.setStatus(registrationDto.getStatus());
//            existingEmployee.setStatus("APPROVED");

            // Save the updated employee
            employeeRegistrationRepository.save(existingEmployee);


//            if ("APPROVED".equals(existingEmployee.getStatus())) //(existingEmployee.getStatus().equals("APPROVED"))
//        {
//            logger.error("Employee email {} already approved.", existingEmployee.getEmail());
//            throw new EmployeeNotFoundException("Employee email " + existingEmployee.getEmail() + " already approved.");
//        }

            ObjectMapper objectMapper = new ObjectMapper();
        String employeeStr;
        try {
            employeeStr = objectMapper.writeValueAsString(existingEmployee);
        } catch (JsonProcessingException ex) {
            logger.error("Error serializing RegistrationEntity to string: {}", ex.getMessage());
            throw new RuntimeException("Error serializing RegistrationEntity to string:", ex);
        }
//        existingEmployee.setStatus("APPROVED");
        kafkaProducer.send("registrationservice", employeeStr);

        logger.info("Employee email {} successfully approved.", existingEmployee.getEmail());

            // Convert and return the updated employee as RegistrationDto
            return mapToDTO(existingEmployee);
        } else {
            // Employee not found, throw an exception or handle accordingly
            throw new EmployeeNotFoundException("Employee with email " + email + " not found.");

        }

    }


    @Override
    public void declineEmployees(String email) throws EmployeeNotFoundException, EmployeeAlreadyApprovedException {

        if (!employeeRegistrationRepository.findByEmail(email).isPresent()) {
            logger.error("Employee email {} not found.", email);
            throw new EmployeeNotFoundException("Employee email " + email + " not found");
        }

        Registration employee = employeeRegistrationRepository.findByEmail(email).orElseThrow();

            employeeRegistrationRepository.deleteById(employee.getId());
            logger.info("Employee email {} successfully deleted.", employee.getEmail());
    }

    private RegistrationDto mapToDTO(Registration registration) {

        // convert Registration entity to RegistrationDto
        RegistrationDto registrationDto = new RegistrationDto();

        registrationDto.setId(registration.getId());
        registrationDto.setUserId(registration.getUserId());
        registrationDto.setFirstName(registration.getFirstName());
        registrationDto.setLastName(registration.getLastName());
        registrationDto.setAddress(registration.getAddress());
        registrationDto.setState(registration.getState());
        registrationDto.setZip(registration.getZip());
        registrationDto.setCellPhone(registration.getCellPhone());
        registrationDto.setHomePhone(registration.getHomePhone());
        registrationDto.setEmail(registration.getEmail());
        registrationDto.setStatus(registration.getStatus());

        return registrationDto;
    }

    private Registration mapToEntity(RegistrationDto registrationDto) {

        // convert RegistrationDto entity to Registration
        Registration registration = new Registration();

        registration.setId(registrationDto.getId());
        registration.setUserId(registrationDto.getUserId());
        registration.setFirstName(registrationDto.getFirstName());
        registration.setLastName(registrationDto.getLastName());
        registration.setAddress(registrationDto.getAddress());
        registration.setState(registrationDto.getState());
        registration.setZip(registrationDto.getZip());
        registration.setCellPhone(registrationDto.getCellPhone());
        registration.setHomePhone(registrationDto.getHomePhone());
        registration.setEmail(registrationDto.getEmail());

        return registration;
    }
}