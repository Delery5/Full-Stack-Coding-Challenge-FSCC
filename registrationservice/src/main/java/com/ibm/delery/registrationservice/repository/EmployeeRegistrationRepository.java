package com.ibm.delery.registrationservice.repository;

import com.ibm.delery.registrationservice.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRegistrationRepository extends JpaRepository<Registration, Long> {

    Optional<Registration> findByEmail(String email);
}
