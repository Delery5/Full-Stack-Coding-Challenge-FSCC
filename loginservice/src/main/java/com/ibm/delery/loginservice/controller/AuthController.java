package com.ibm.delery.loginservice.controller;

import com.ibm.delery.loginservice.dto.JwtAuthResponse;
import com.ibm.delery.loginservice.dto.LoginDto;
import com.ibm.delery.loginservice.dto.RegisterDto;
import com.ibm.delery.loginservice.exception.UserAlreadyExistException;
import com.ibm.delery.loginservice.security.JwtTokenProvider;
import com.ibm.delery.loginservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

   private AuthService authService;

   private JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping(path = "/health")
    public String status() {

        return "Login path is health!";
    }

    @GetMapping
    public List<RegisterDto> getAllUsers() {

        return authService.getAllUsers();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto) {
       String response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/validate-token")
    public ResponseEntity<String> validateTokens(@RequestParam(name = "token") String token) {
        if (jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.ok("Token is valid");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @PostMapping("/generate-token")
    public ResponseEntity<JwtAuthResponse> generateToken(@RequestBody LoginDto loginDto) {
        String token = authService.generateToken(loginDto);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }
}


