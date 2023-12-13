package com.ibm.delery.registrationservice.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.delery.registrationservice.dto.RegistrationDto;
import com.ibm.delery.registrationservice.entity.Registration;
import com.ibm.delery.registrationservice.repository.EmployeeRegistrationRepository;
import com.ibm.delery.registrationservice.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    private static Logger logger = LoggerFactory.getLogger(ProducerServiceImpl.class);


    private final EmployeeRegistrationRepository kafkaRepository;


    @Value("${registrationservice.url}")
    private String registrationServiceUrl;

    public ConsumerServiceImpl(EmployeeRegistrationRepository kafkaRepository) {
        this.kafkaRepository = kafkaRepository;
    }

    @Override
    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String employeeString) {
        Registration employee = null;

        if (employeeString != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                employee = objectMapper.readValue(employeeString, Registration.class);
            } catch (JsonProcessingException e) {
                logger.error("Error deserializing string to RegistrationEntity: {}", e.getMessage());
                throw new RuntimeException("Error deserializing string to RegistrationEntity", e);
            }

            sendEmployeePostRequest(employee);

            kafkaRepository.save(employee);
        }
    }

//    public void sendEmployeePostRequest(Registration employee) {
//        // Creates request body
//        RegistrationDto requestBody = new RegistrationDto(employee);
//
//        // set headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        // create HTTP entity with request body and headers
//        HttpEntity<RegistrationDto> requestEntity = new HttpEntity<>(requestBody, headers);
//
//        // create an instance of RestTemplate
//        RestTemplate restTemplate = new RestTemplate();
//
//        try {
//            restTemplate.exchange(registrationServiceUrl, HttpMethod.POST, requestEntity, Object.class);
//            logger.info("POST request to employee-service succeeded");
//        } catch (Exception e) {
//            logger.error("POST request to employee-service failed error: {}", e.getMessage());
//        }
//    }

    public void sendEmployeePostRequest(Registration employee) {
        // Creates request body
        RegistrationDto requestBody = new RegistrationDto(employee);

        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // create HTTP entity with request body and headers
        HttpEntity<RegistrationDto> requestEntity = new HttpEntity<>(requestBody, headers);

        // Log the request body before sending
        logger.info("Request Body: {}", requestBody);

        // create an instance of RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        try {
            restTemplate.exchange(registrationServiceUrl, HttpMethod.POST, requestEntity, Object.class);
            logger.info("POST request to employee-service succeeded");
        } catch (HttpClientErrorException e) {
            // Log the details of the HTTP error, including the response body
            logger.error("POST request to employee-service failed with HTTP error: {}", e.getStatusCode());
            logger.error("Response Body: {}", e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("POST request to employee-service failed error: {}", e.getMessage());
        }
    }

}











//@Service
//public class ConsumerServiceImpl implements ConsumerService {
//
//    private static Logger logger = LoggerFactory.getLogger(ProducerServiceImpl.class);
//
//
//    private final EmployeeRegistrationRepository kafkaRepository;
//
//    private String url;
//
//    public ConsumerServiceImpl(EmployeeRegistrationRepository kafkaRepository, Environment env) {
//        this.kafkaRepository = kafkaRepository;
//        this.url = env.getProperty("registrationservice.url");
//        if (this.url == null) {
//            throw new IllegalStateException("registrationservice.url property not found in enviroment");
//        }
//    }
//
//    private String getUrl() {
//        return this.url;
//    }
//
//
//    @Override
//    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
//    public void listen(String employeeString) {
//        Registration employee = null;
//
//        if (employeeString != null) {
//            ObjectMapper objectMapper = new ObjectMapper();
//            try {
//
//                employee = objectMapper.readValue(employeeString, Registration.class);
//            } catch (JsonProcessingException e) {
//                logger.error("Error deserializing string to RegistrationEntity: {}", e.getMessage());
//                throw new RuntimeException("Error deserializing string to RegistrationEntity", e);
//            }
//
//            sendEmployeePostRequest(employee);
//
//            kafkaRepository.save(employee);}
//
////            if ("PENDING".equals(employee.getStatus())) { //(employee.getStatus().equals("PENDING"))
////
////                sendEmployeePostRequest(employee);
////
////                kafkaRepository.save(employee);
////
////
////                logger.info("Employee email {} saved to database successfully....", employee.getEmail());
////
////
////            } if ("APPROVED".equals(employee.getStatus())) { //(employee.getStatus().equals("APPROVED"))
////                // POST request is sent to Employee Service
////                sendEmployeePostRequest(employee);
////
////                // DB updates registration
////                kafkaRepository.save(employee);
////                logger.info("Employee email {} saved to database successfully.", employee.getEmail());
////            }
////        }
//    }
//
//    public void sendEmployeePostRequest(Registration employee) {
//
//        // Creates request body
//        RegistrationDto requestBody = new RegistrationDto(employee);
//
//        // set headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        // create HTTP entity with request body and headers
//        HttpEntity<RegistrationDto> requestEntity = new HttpEntity<>(requestBody, headers);
//
//        // create an instance of RestTemplate
//        RestTemplate restTemplate = new RestTemplate();
//
//        try{
//            restTemplate.exchange(getUrl(), HttpMethod.POST, requestEntity, Object.class);
//            logger.info("POST request to employee-service succeeded");
//        } catch (Exception e) {
//            logger.error("POST request to employee-service failed error: {}", e.getMessage());
//        }
//    }
//}
