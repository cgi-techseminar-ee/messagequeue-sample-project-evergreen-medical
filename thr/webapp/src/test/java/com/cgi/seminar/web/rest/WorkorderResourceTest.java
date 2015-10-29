package com.cgi.seminar.web.rest;

import com.cgi.seminar.Application;
import com.cgi.seminar.domain.Workorder;
import com.cgi.seminar.repository.WorkorderRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the WorkorderResource REST controller.
 *
 * @see WorkorderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class WorkorderResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final DateTime DEFAULT_PLANNED_START = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_PLANNED_START = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_PLANNED_START_STR = dateTimeFormatter.print(DEFAULT_PLANNED_START);

    private static final DateTime DEFAULT_PLANNED_END = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_PLANNED_END = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_PLANNED_END_STR = dateTimeFormatter.print(DEFAULT_PLANNED_END);

    private static final DateTime DEFAULT_ACTUAL_START = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_ACTUAL_START = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_ACTUAL_START_STR = dateTimeFormatter.print(DEFAULT_ACTUAL_START);

    private static final DateTime DEFAULT_ACTUAL_END = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_ACTUAL_END = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_ACTUAL_END_STR = dateTimeFormatter.print(DEFAULT_ACTUAL_END);

    @Inject
    private WorkorderRepository workorderRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWorkorderMockMvc;

    private Workorder workorder;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WorkorderResource workorderResource = new WorkorderResource();
        ReflectionTestUtils.setField(workorderResource, "workorderRepository", workorderRepository);
        this.restWorkorderMockMvc = MockMvcBuilders.standaloneSetup(workorderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        workorder = new Workorder();
        workorder.setPlannedStart(DEFAULT_PLANNED_START);
        workorder.setPlannedEnd(DEFAULT_PLANNED_END);
        workorder.setActualStart(DEFAULT_ACTUAL_START);
        workorder.setActualEnd(DEFAULT_ACTUAL_END);
    }

    @Test
    @Transactional
    public void createWorkorder() throws Exception {
        int databaseSizeBeforeCreate = workorderRepository.findAll().size();

        // Create the Workorder

        restWorkorderMockMvc.perform(post("/api/workorders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workorder)))
                .andExpect(status().isCreated());

        // Validate the Workorder in the database
        List<Workorder> workorders = workorderRepository.findAll();
        assertThat(workorders).hasSize(databaseSizeBeforeCreate + 1);
        Workorder testWorkorder = workorders.get(workorders.size() - 1);
        assertThat(testWorkorder.getPlannedStart().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_PLANNED_START);
        assertThat(testWorkorder.getPlannedEnd().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_PLANNED_END);
        assertThat(testWorkorder.getActualStart().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_ACTUAL_START);
        assertThat(testWorkorder.getActualEnd().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_ACTUAL_END);
    }

    @Test
    @Transactional
    public void checkPlannedStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = workorderRepository.findAll().size();
        // set the field null
        workorder.setPlannedStart(null);

        // Create the Workorder, which fails.

        restWorkorderMockMvc.perform(post("/api/workorders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workorder)))
                .andExpect(status().isBadRequest());

        List<Workorder> workorders = workorderRepository.findAll();
        assertThat(workorders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPlannedEndIsRequired() throws Exception {
        int databaseSizeBeforeTest = workorderRepository.findAll().size();
        // set the field null
        workorder.setPlannedEnd(null);

        // Create the Workorder, which fails.

        restWorkorderMockMvc.perform(post("/api/workorders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workorder)))
                .andExpect(status().isBadRequest());

        List<Workorder> workorders = workorderRepository.findAll();
        assertThat(workorders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWorkorders() throws Exception {
        // Initialize the database
        workorderRepository.saveAndFlush(workorder);

        // Get all the workorders
        restWorkorderMockMvc.perform(get("/api/workorders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(workorder.getId().intValue())))
                .andExpect(jsonPath("$.[*].plannedStart").value(hasItem(DEFAULT_PLANNED_START_STR)))
                .andExpect(jsonPath("$.[*].plannedEnd").value(hasItem(DEFAULT_PLANNED_END_STR)))
                .andExpect(jsonPath("$.[*].actualStart").value(hasItem(DEFAULT_ACTUAL_START_STR)))
                .andExpect(jsonPath("$.[*].actualEnd").value(hasItem(DEFAULT_ACTUAL_END_STR)));
    }

    @Test
    @Transactional
    public void getWorkorder() throws Exception {
        // Initialize the database
        workorderRepository.saveAndFlush(workorder);

        // Get the workorder
        restWorkorderMockMvc.perform(get("/api/workorders/{id}", workorder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(workorder.getId().intValue()))
            .andExpect(jsonPath("$.plannedStart").value(DEFAULT_PLANNED_START_STR))
            .andExpect(jsonPath("$.plannedEnd").value(DEFAULT_PLANNED_END_STR))
            .andExpect(jsonPath("$.actualStart").value(DEFAULT_ACTUAL_START_STR))
            .andExpect(jsonPath("$.actualEnd").value(DEFAULT_ACTUAL_END_STR));
    }

    @Test
    @Transactional
    public void getNonExistingWorkorder() throws Exception {
        // Get the workorder
        restWorkorderMockMvc.perform(get("/api/workorders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkorder() throws Exception {
        // Initialize the database
        workorderRepository.saveAndFlush(workorder);

		int databaseSizeBeforeUpdate = workorderRepository.findAll().size();

        // Update the workorder
        workorder.setPlannedStart(UPDATED_PLANNED_START);
        workorder.setPlannedEnd(UPDATED_PLANNED_END);
        workorder.setActualStart(UPDATED_ACTUAL_START);
        workorder.setActualEnd(UPDATED_ACTUAL_END);

        restWorkorderMockMvc.perform(put("/api/workorders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workorder)))
                .andExpect(status().isOk());

        // Validate the Workorder in the database
        List<Workorder> workorders = workorderRepository.findAll();
        assertThat(workorders).hasSize(databaseSizeBeforeUpdate);
        Workorder testWorkorder = workorders.get(workorders.size() - 1);
        assertThat(testWorkorder.getPlannedStart().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_PLANNED_START);
        assertThat(testWorkorder.getPlannedEnd().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_PLANNED_END);
        assertThat(testWorkorder.getActualStart().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_ACTUAL_START);
        assertThat(testWorkorder.getActualEnd().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_ACTUAL_END);
    }

    @Test
    @Transactional
    public void deleteWorkorder() throws Exception {
        // Initialize the database
        workorderRepository.saveAndFlush(workorder);

		int databaseSizeBeforeDelete = workorderRepository.findAll().size();

        // Get the workorder
        restWorkorderMockMvc.perform(delete("/api/workorders/{id}", workorder.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Workorder> workorders = workorderRepository.findAll();
        assertThat(workorders).hasSize(databaseSizeBeforeDelete - 1);
    }
}
