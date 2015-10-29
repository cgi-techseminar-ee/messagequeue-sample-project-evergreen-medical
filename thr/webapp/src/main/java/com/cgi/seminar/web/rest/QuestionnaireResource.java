package com.cgi.seminar.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cgi.seminar.domain.Questionnaire;
import com.cgi.seminar.repository.QuestionnaireRepository;
import com.cgi.seminar.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Questionnaire.
 */
@RestController
@RequestMapping("/api")
public class QuestionnaireResource {

    private final Logger log = LoggerFactory.getLogger(QuestionnaireResource.class);

    @Inject
    private QuestionnaireRepository questionnaireRepository;

    /**
     * POST  /questionnaires -> Create a new questionnaire.
     */
    @RequestMapping(value = "/questionnaires",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Questionnaire> createQuestionnaire(@Valid @RequestBody Questionnaire questionnaire) throws URISyntaxException {
        log.debug("REST request to save Questionnaire : {}", questionnaire);
        if (questionnaire.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new questionnaire cannot already have an ID").body(null);
        }
        Questionnaire result = questionnaireRepository.save(questionnaire);
        return ResponseEntity.created(new URI("/api/questionnaires/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("questionnaire", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /questionnaires -> Updates an existing questionnaire.
     */
    @RequestMapping(value = "/questionnaires",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Questionnaire> updateQuestionnaire(@Valid @RequestBody Questionnaire questionnaire) throws URISyntaxException {
        log.debug("REST request to update Questionnaire : {}", questionnaire);
        if (questionnaire.getId() == null) {
            return createQuestionnaire(questionnaire);
        }
        Questionnaire result = questionnaireRepository.save(questionnaire);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("questionnaire", questionnaire.getId().toString()))
                .body(result);
    }

    /**
     * GET  /questionnaires -> get all the questionnaires.
     */
    @RequestMapping(value = "/questionnaires",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Questionnaire> getAllQuestionnaires() {
        log.debug("REST request to get all Questionnaires");
        return questionnaireRepository.findAll();
    }

    /**
     * GET  /questionnaires/:id -> get the "id" questionnaire.
     */
    @RequestMapping(value = "/questionnaires/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Questionnaire> getQuestionnaire(@PathVariable Long id) {
        log.debug("REST request to get Questionnaire : {}", id);
        return Optional.ofNullable(questionnaireRepository.findOne(id))
            .map(questionnaire -> new ResponseEntity<>(
                questionnaire,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /questionnaires/:id -> delete the "id" questionnaire.
     */
    @RequestMapping(value = "/questionnaires/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteQuestionnaire(@PathVariable Long id) {
        log.debug("REST request to delete Questionnaire : {}", id);
        questionnaireRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("questionnaire", id.toString())).build();
    }
}
