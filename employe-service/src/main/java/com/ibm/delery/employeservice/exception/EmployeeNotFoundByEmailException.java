package com.ibm.delery.employeservice.exception;

public class EmployeeNotFoundByEmailException extends RuntimeException {

    public EmployeeNotFoundByEmailException() {
        super();
    }

    public EmployeeNotFoundByEmailException(String message) {
        super(message);
    }

    public EmployeeNotFoundByEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
