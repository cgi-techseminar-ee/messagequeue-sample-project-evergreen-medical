package com.cgi.seminar.repository;

import com.cgi.seminar.domain.Questionnaire;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Questionnaire entity.
 */
public interface QuestionnaireRepository extends JpaRepository<Questionnaire,Long> {

}
