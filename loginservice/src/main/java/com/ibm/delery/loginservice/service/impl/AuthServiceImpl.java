package com.ibm.delery.loginservice.service.impl;



import com.ibm.delery.loginservice.dto.LoginDto;
import com.ibm.delery.loginservice.dto.RegisterDto;
import com.ibm.delery.loginservice.entity.User;
import com.ibm.delery.loginservice.exception.JwtAPIException;
import com.ibm.delery.loginservice.repository.UserRepository;
import com.ibm.delery.loginservice.security.JwtTokenProvider;
import com.ibm.delery.loginservice.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

   private AuthenticationManager authenticationManager;
   private UserRepository userRepository;
   private PasswordEncoder passwordEncoder;
   private JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String generateToken(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }

    @Override
    public String register(RegisterDto registerDto) {

         //add check for username exists in database
        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new JwtAPIException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }

         //add check for email exists in database
        if(userRepository.existsByEmail(registerDto.getEmail())) {
            throw new JwtAPIException(HttpStatus.BAD_REQUEST, "Email already exists!");
        }

        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        // Set<Role> roles = new HashSet<>();
        //Role userRole = roleRepository.findByName("ROLE_USER").get();
        //roles.add(userRole);
        //user.setRoles(roles);

        userRepository.save(user);

        return "User registered successfully";
    }



//    @Override
//    public Optional<RegisterDto> findByEmail(String email) {
//        return userRepository.findByEmail(email)
//                .map(RegisterDto::new);
//    }

    @Override
    public List<RegisterDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users
                .stream()
                .map(user -> mapToDTO(user))
                .collect(Collectors.toList());
    }

    @Override
    public void validateToken(String token) {

        jwtTokenProvider.validateToken(token);
    }
//    public List<RegistrationDto> getAllEmployees() {
//
//        List<Registration> employees = employeeRegistrationRepository.findAll();
//
//        return employees
//                .stream()
//                .map(registration -> mapToDTO(registration))
//                .collect(Collectors.toList());
//    }

//    @Override
//    public LoginDto updateUser(RegisterDto registerDto, String email) throws UserNotFoundException, UserAlreadyExistException {
//
//        if (!userRepository.findByEmail(email).isPresent()) {
//            logger.error("User email {} not found.", email);
//            throw new UserNotFoundException("User email " + email + " not found.");
//        }
//
//        if (userRepository.findByEmail(registerDto.email()).isPresent()
//            && !registerDto.email().equals(email)) {
//            logger.error("User email {} already exists.", registerDto.email());
//            throw new UserAlreadyExistException("User email " + registerDto.email() + " already exists.");
//        }
//
//        User updateUser = userRepository.findByEmail(email).orElseThrow();
//
//        if (registerDto.email() != null) {
//            updateUser.setEmail(registerDto.email());
//        }
//        if (registerDto.password() != null) {
//            updateUser.setPassword(registerDto.password());
//        }
//
//        updateUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
//
//        User saveUser = userRepository.save(updateUser);
//        logger.info("User email {} updated successfully.", saveUser.getEmail());
//
//        return new LoginDto(saveUser);
//    }


    private RegisterDto mapToDTO(User user) {

        // convert User entity to RegisterDto
        RegisterDto registerDto = new RegisterDto();
        registerDto.setId(user.getId());
        registerDto.setName(user.getName());
        registerDto.setUsername(user.getUsername());
        registerDto.setEmail(user.getEmail());
        registerDto.setPassword(user.getPassword());

        return registerDto;
    }

    private User mapToEntity(RegisterDto registerDto) {

        // convert RegisterDto to User entity

        User user = new User();
        user.setId(registerDto.getId());
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(registerDto.getPassword());

        return user;
    }



//    @Override
//    public String register(RegisterDto registerDto) {
//
//        // add check for username exists in database
////        if(userRepository.existsByUsername(registerDto.getUsername())){
////            throw new (HttpStatus.BAD_REQUEST, "Username already exists!");
////        }
//
//        // add check for email exists in database
////        if(userRepository.existsByEmail(registerDto.getEmail())) {
////            throw new EmployeeAlreadyExistsException(HttpStatus.BAD_REQUEST, "Email already exists!");
////        }
//
//        User user = new User();
//        user.setName(registerDto.getName());
//        user.setUsername(registerDto.getUsername());
//        user.setEmail(registerDto.getEmail());
//        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
//
//        // Set<Role> roles = new HashSet<>();
//        //Role userRole = roleRepository.findByName("ROLE_USER").get();
//        //roles.add(userRole);
//        //user.setRoles(roles);
//
//        userRepository.save(user);
//
//        return "User registered successfully";
//    }

}