package com.cgi.seminar.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cgi.seminar.domain.Location;
import com.cgi.seminar.repository.LocationRepository;
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
 * REST controller for managing Location.
 */
@RestController
@RequestMapping("/api")
public class LocationResource {

    private final Logger log = LoggerFactory.getLogger(LocationResource.class);

    @Inject
    private LocationRepository locationRepository;

    /**
     * POST  /locations -> Create a new location.
     */
    @RequestMapping(value = "/locations",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Location> createLocation(@Valid @RequestBody Location location) throws URISyntaxException {
        log.debug("REST request to save Location : {}", location);
        if (location.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new location cannot already have an ID").body(null);
        }
        Location result = locationRepository.save(location);
        return ResponseEntity.created(new URI("/api/locations/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("location", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /locations -> Updates an existing location.
     */
    @RequestMapping(value = "/locations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Location> updateLocation(@Valid @RequestBody Location location) throws URISyntaxException {
        log.debug("REST request to update Location : {}", location);
        if (location.getId() == null) {
            return createLocation(location);
        }
        Location result = locationRepository.save(location);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("location", location.getId().toString()))
                .body(result);
    }

    /**
     * GET  /locations -> get all the locations.
     */
    @RequestMapping(value = "/locations",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Location> getAllLocations() {
        log.debug("REST request to get all Locations");
        return locationRepository.findAll();
    }

    /**
     * GET  /locations/:id -> get the "id" location.
     */
    @RequestMapping(value = "/locations/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Location> getLocation(@PathVariable Long id) {
        log.debug("REST request to get Location : {}", id);
        return Optional.ofNullable(locationRepository.findOne(id))
            .map(location -> new ResponseEntity<>(
                location,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /locations/:id -> delete the "id" location.
     */
    @RequestMapping(value = "/locations/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        log.debug("REST request to delete Location : {}", id);
        locationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("location", id.toString())).build();
    }
}
