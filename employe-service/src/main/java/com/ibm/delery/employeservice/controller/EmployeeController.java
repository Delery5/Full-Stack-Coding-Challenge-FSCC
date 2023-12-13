package com.ibm.delery.employeservice.controller;


import com.ibm.delery.employeservice.dto.EmployeeDTO;
import com.ibm.delery.employeservice.exception.EmployeeAlreadyExistsException;
import com.ibm.delery.employeservice.exception.EmployeeNotFoundByEmailException;
import com.ibm.delery.employeservice.exception.EmployeeNotFoundException;
import com.ibm.delery.employeservice.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/health")
    public String status() {
        return "Employee path is health!";
    }

    @GetMapping
    public ResponseEntity<Object> getAllEmployees() {

        List<EmployeeDTO> employees = employeeService.getAllEmployees();

        return ResponseEntity.status((HttpStatus.OK))
                .body(new SuccessResponse(
                        200,
                        "successfully fetched all employees",
                        employees));
    }

    @GetMapping("/email/{email}")
    public EmployeeDTO findEmployeeByEmail(@PathVariable(name = "email") String email) {

            Optional<EmployeeDTO> employeeDTO = employeeService.findEmployeeByEmail(email);

        if (employeeDTO.isPresent()) {
            return employeeDTO.get();
        } else {
            throw new EmployeeNotFoundByEmailException("Employee not found with email: " + email);

        }
    }


    @PostMapping("/create")
    public ResponseEntity<Object> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {

        try {
            EmployeeDTO newEmployee = employeeService.CreateEmployee(employeeDTO);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new SuccessResponse(
                            201,
                            "Successfully created Employee",
                            newEmployee));
        } catch (EmployeeAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(
                            409,
                            "Employee already exists",
                            e.getMessage()));
        }
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<Object> updateEmployee(@Valid @RequestBody EmployeeDTO employeeDTO,
                                                      @PathVariable(name = "id") Long id) {

//        EmployeeDTO employeeResponse = employeeService.updateEmployee(employeeDTO, id);
//        return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
        try {

            EmployeeDTO updateEmployee = employeeService.updateEmployee(employeeDTO, id);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse(
                            200,
                            "Successfully updated employee",
                            updateEmployee));

        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(
                            404,
                            "Not Found",
                            e.getMessage()));

        } catch (EmployeeAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(
                            409,
                            "Conflict",
                            e.getMessage()));
        }
    }

    @DeleteMapping(path = "/deleted/{id}")
    public ResponseEntity<Object> deleteEmployeeById(@PathVariable(name = "id") Long id) {
        try {
            employeeService.deleteEmployeeById(id);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse(
                            200,
                            "Successfully deleted employee",
                            id));

        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(
                            404,
                            "Not Found",
                            "Could not find employee with Id: " + id));
        }
    }
}
