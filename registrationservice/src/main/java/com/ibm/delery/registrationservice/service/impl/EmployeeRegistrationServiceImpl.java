package com.ibm.delery.registrationservice.service.impl;

import com.ibm.delery.registrationservice.dto.RegistrationDto;
import com.ibm.delery.registrationservice.entity.Registration;
import com.ibm.delery.registrationservice.repository.EmployeeRegistrationRepository;
import com.ibm.delery.registrationservice.service.EmployeeRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeRegistrationServiceImpl implements EmployeeRegistrationService {

    @Autowired
    private final EmployeeRegistrationRepository employeeRegistrationRepository;

    public EmployeeRegistrationServiceImpl(EmployeeRegistrationRepository employeeRegistrationRepository) {
        this.employeeRegistrationRepository = employeeRegistrationRepository;
    }

    @Override
    public List<RegistrationDto> getAllEmployees() {

        List<Registration> employees = employeeRegistrationRepository.findAll();

        return employees
                .stream()
                .map(registration -> mapToDTO(registration))
                .collect(Collectors.toList());
    }

//        public List<EmployeeDTO> getAllEmployees() {
//            List<Employee> employees = employeeRepository.findAll();
//            return employees.stream().map(employee -> mapToDTO(employee)).collect(Collectors.toList());
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
        // registration.getStatus(registrationDto.getStatus());

        return registration;
    }

}
