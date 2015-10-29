package com.cgi.seminar.web.rest;

import com.cgi.seminar.processors.WorkorderProcessor;
import com.codahale.metrics.annotation.Timed;
import com.cgi.seminar.domain.Workorder;
import com.cgi.seminar.repository.WorkorderRepository;
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
 * REST controller for managing Workorder.
 */
@RestController
@RequestMapping("/api")
public class WorkorderResource {

    private final Logger log = LoggerFactory.getLogger(WorkorderResource.class);

    @Inject
    private WorkorderProcessor workorderProcessor;

    @Inject
    private WorkorderRepository workorderRepository;

    /**
     * POST  /workorders -> Create a new workorder.
     */
    @RequestMapping(value = "/workorders",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Workorder> createWorkorder(@Valid @RequestBody Workorder workorder) throws URISyntaxException {
        log.debug("REST request to save Workorder : {}", workorder);
        if (workorder.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new workorder cannot already have an ID").body(null);
        }
        Workorder result = workorderProcessor.create(workorder);
        return ResponseEntity.created(new URI("/api/workorders/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("workorder", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /workorders -> Updates an existing workorder.
     */
    @RequestMapping(value = "/workorders",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Workorder> updateWorkorder(@Valid @RequestBody Workorder workorder) throws URISyntaxException {
        log.debug("REST request to update Workorder : {}", workorder);
        if (workorder.getId() == null) {
            return createWorkorder(workorder);
        }
        Workorder result = workorderRepository.save(workorder);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("workorder", workorder.getId().toString()))
                .body(result);
    }

    /**
     * GET  /workorders -> get all the workorders.
     */
    @RequestMapping(value = "/workorders",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Workorder> getAllWorkorders() {
        log.debug("REST request to get all Workorders");
        return workorderRepository.findAll();
    }

    /**
     * GET  /workorders/:id -> get the "id" workorder.
     */
    @RequestMapping(value = "/workorders/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Workorder> getWorkorder(@PathVariable Long id) {
        log.debug("REST request to get Workorder : {}", id);
        return Optional.ofNullable(workorderRepository.findOne(id))
            .map(workorder -> new ResponseEntity<>(
                workorder,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /workorders/:id -> delete the "id" workorder.
     */
    @RequestMapping(value = "/workorders/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWorkorder(@PathVariable Long id) {
        log.debug("REST request to delete Workorder : {}", id);
        workorderRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("workorder", id.toString())).build();
    }
}
