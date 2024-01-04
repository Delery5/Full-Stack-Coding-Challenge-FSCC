package com.ibm.delery.registrationservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.delery.registrationservice.dto.RegistrationDto;
import com.ibm.delery.registrationservice.entity.Registration;
import com.ibm.delery.registrationservice.repository.EmployeeRegistrationRepository;
import com.ibm.delery.registrationservice.service.ConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


@Service
public class ConsumerServiceImpl implements ConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(ProducerServiceImpl.class);

    private final EmployeeRegistrationRepository kafkaRepository;
    private final String url;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper; // Reuse ObjectMapper

    public ConsumerServiceImpl(
            EmployeeRegistrationRepository kafkaRepository,
            Environment env,
            RestTemplateBuilder restTemplateBuilder,
            ObjectMapper objectMapper) {
        this.kafkaRepository = kafkaRepository;
        this.url = env.getProperty("employee-service.url");

        if (this.url == null) {
            throw new IllegalStateException("employee-service.url property not found");
        }

        // Log the employee service URL during initialization
        logger.info("Employee service URL: {}", this.url);

        // Create a RestTemplate bean using RestTemplateBuilder
        this.restTemplate = restTemplateBuilder.build();

        // Reuse the provided ObjectMapper
        this.objectMapper = objectMapper;
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    @KafkaListener(topics = "registered-employees", groupId = "employee-group")
    public void listen(String employeeString) {
        logger.info("Received JSON String: {}", employeeString);

        Registration employee = null;

        if (employeeString != null) {
            try {
                employee = objectMapper.readValue(employeeString, Registration.class);
                logger.info("Deserialized Employee Object: {}", employee);
            } catch (JsonProcessingException e) {
                handleJsonProcessingException(e);
                return;
            }

            if (employee != null) {
                if ("PENDING".equals(employee.getStatus())) {
                    kafkaRepository.save(employee);
                    logger.info("Employee email {} successfully saved to database.", employee.getEmail());
                } else if ("APPROVED".equals(employee.getStatus())) {
                    employeePostRequestSent(employee);
                    // Assuming that employeePostRequestSent method does not modify the employee object
                    kafkaRepository.save(employee);
                    logger.info("Employee email {} successfully saved to database.", employee.getEmail());
                } else {
                    logger.warn("Unknown status: {}", employee.getStatus());
                }
            }
        }
    }

    private void handleJsonProcessingException(JsonProcessingException e) {
    }

    private void employeePostRequestSent(Registration employee) {
        // Step 1: Verify Data in the Registration Object
        logger.info("Step 1: Registration object: {}", employee);

        // Step 2: Inspect the RegistrationDto Conversion
        RegistrationDto requestBody = mapToDTO(employee);
        logger.info("Step 2: Request Body (RegistrationDto): {}", requestBody);

        // Set up HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create an HttpEntity with the request body and headers
        HttpEntity<RegistrationDto> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // Step 3: Logging Request Payload
            logger.info("Step 3: Request Payload: {}", requestBody);

            // Send the HTTP POST request using RestTemplate's exchange method
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Object.class);

            // Check the response status if needed
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("POST request to employee-service succeeded");
            } else {
                handleErrorResponse(response);
            }
        } catch (HttpClientErrorException e) {
            handleHttpClientErrorException(e);
        } catch (HttpServerErrorException e) {
            handleHttpServerErrorException(e);
        } catch (Exception e) {
            handleGenericException(e);
        }
    }

    private void handleGenericException(Exception e) {
    }

    private void handleHttpServerErrorException(HttpServerErrorException e) {
    }

    private void handleHttpClientErrorException(HttpClientErrorException e) {
    }

    private void handleErrorResponse(ResponseEntity<Object> response) {
    }

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
    }}
   
