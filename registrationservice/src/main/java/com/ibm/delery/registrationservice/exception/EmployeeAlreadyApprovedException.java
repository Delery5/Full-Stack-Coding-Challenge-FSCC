package com.ibm.delery.registrationservice.exception;

public class EmployeeAlreadyApprovedException extends Exception{

    public EmployeeAlreadyApprovedException(String message){
        super(message);
    }
}
