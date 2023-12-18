package com.ibm.delery.registrationservice.controller;

import com.ibm.delery.registrationservice.dto.RegistrationDto;
import com.ibm.delery.registrationservice.exception.EmployeeAlreadyApprovedException;
import com.ibm.delery.registrationservice.exception.EmployeeAlreadyExistsException;
import com.ibm.delery.registrationservice.exception.EmployeeNotFoundException;
import com.ibm.delery.registrationservice.service.impl.EmployeeRegistrationServiceImpl;
import com.ibm.delery.registrationservice.service.impl.ProducerServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/registration")
@Validated
public class RegistrationController {

    private final ProducerServiceImpl producerService;


    private final EmployeeRegistrationServiceImpl employeeRegistrationService;



    public RegistrationController(ProducerServiceImpl producerService, EmployeeRegistrationServiceImpl employeeRegistrationService) {
        this.producerService = producerService;
        this.employeeRegistrationService = employeeRegistrationService;
    }


    @GetMapping("/healthy")
    public String status(){
        return "Registration path is healthy!";
    }


    @GetMapping
    public List<RegistrationDto> getAllEmployees() {

        return employeeRegistrationService.getAllEmployees();
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createEmployee(@Valid @RequestBody RegistrationDto registrationDto) {

            try {


                RegistrationDto employees = producerService.createEmployee(registrationDto);
                return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse(
                        201,
                        "Successfully registered employee. Status Pending",
                        employees));
            } catch (EmployeeAlreadyExistsException ex) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(
                        409,
                        "Employee already registered!",
                        ex.getMessage()));
            }
    }


    @PutMapping("/approve/{email}")
    public ResponseEntity<Object> approveEmployee(@PathVariable(name = "email") String email,@RequestBody(required = false) RegistrationDto registrationDto) {
        try {

            RegistrationDto employee = producerService.approveEmployee(email,registrationDto);


            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(200,
                    "Successfully fetched registration",
                    employee));
        } catch (EmployeeNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse(404,
                    "Employee not found",
                    ex.getMessage()));

        } catch (EmployeeAlreadyApprovedException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SuccessResponse(400,
                    "Employee already approved",
                    ex.getMessage()));
        }
    }

    @PutMapping("/decline/{email}")
    public ResponseEntity<Object> declineEmployee(@PathVariable(name = "email") String email) {
        try {
            producerService.declineEmployees(email);

            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(200,
                    "Successfully deleted employee",
                    "Employee email " + email + " successfully deleted"));
        } catch (EmployeeNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse(404,
                    "Employee not found",
                    ex.getMessage()));
        } catch (EmployeeAlreadyApprovedException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SuccessResponse(400,
                    "Employee already approved",
                    ex.getMessage()));
        }
  }
        }
