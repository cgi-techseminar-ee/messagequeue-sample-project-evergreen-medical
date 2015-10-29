package com.cgi.seminar.repository;

import com.cgi.seminar.domain.Workorder;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Workorder entity.
 */
public interface WorkorderRepository extends JpaRepository<Workorder,Long> {

}
