package com.cgi.seminar.repository;

import com.cgi.seminar.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Employee entity.
 */
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Employee findByExternalId(String externalId);
}
