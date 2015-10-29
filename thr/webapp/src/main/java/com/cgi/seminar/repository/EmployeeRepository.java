package com.cgi.seminar.repository;

import com.cgi.seminar.domain.Employee;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Employee entity.
 */
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

}
