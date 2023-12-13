package com.ibm.delery.registrationservice.controller;

import com.ibm.delery.registrationservice.dto.RegistrationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse {

    private int status;
    private String message;
    private Object data;

}
