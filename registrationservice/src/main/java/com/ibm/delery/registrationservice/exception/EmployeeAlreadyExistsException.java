package com.ibm.delery.registrationservice.exception;

public class EmployeeAlreadyExistsException extends Exception{

    public EmployeeAlreadyExistsException(String message){
        super(message);
    }
}
