package com.ibm.delery.loginservice.service;


import com.ibm.delery.loginservice.dto.LoginDto;
import com.ibm.delery.loginservice.dto.RegisterDto;
import com.ibm.delery.loginservice.exception.UserAlreadyExistException;

import java.util.List;

public interface AuthService {


   String generateToken(LoginDto loginDto) ;

    String register(RegisterDto registerDto);

    public List<RegisterDto> getAllUsers();

    void validateToken(String token);

//   String saveUser(RegisterDto registerDto);
//   String generateToken(String email);
//
//   void validateToken(String token);
//
//   Optional<RegisterDto> findByEmail(String email);
//
//
//
//   LoginDto updateUser(RegisterDto registerDto, String email) throws UserNotFoundException, UserAlreadyExistException;
//
//   void deleteUser(String email) throws UserNotFoundException;

}
