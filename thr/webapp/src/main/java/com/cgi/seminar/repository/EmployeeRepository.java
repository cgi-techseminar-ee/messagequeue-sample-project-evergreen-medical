package com.cgi.seminar.repository;

import com.cgi.seminar.domain.Employee;

/**
 * Spring Data JPA repository for the Employee entity.
 */
public interface EmployeeRepository extends EntityWithExternalIdRepository<Employee> {
}
