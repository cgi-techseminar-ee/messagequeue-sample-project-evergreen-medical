package com.cgi.seminar.web.rest;

import com.cgi.seminar.Application;
import com.cgi.seminar.domain.Questionnaire;
import com.cgi.seminar.repository.QuestionnaireRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the QuestionnaireResource REST controller.
 *
 * @see QuestionnaireResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class QuestionnaireResourceTest {

    private static final String DEFAULT_NAME = "AAA";
    private static final String UPDATED_NAME = "BBB";
    private static final String DEFAULT_QUESTIONS = "AA";
    private static final String UPDATED_QUESTIONS = "BB";
    private static final String DEFAULT_EXTERNAL_ID = "AAAAA";
    private static final String UPDATED_EXTERNAL_ID = "BBBBB";

    @Inject
    private QuestionnaireRepository questionnaireRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restQuestionnaireMockMvc;

    private Questionnaire questionnaire;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        QuestionnaireResource questionnaireResource = new QuestionnaireResource();
        ReflectionTestUtils.setField(questionnaireResource, "questionnaireRepository", questionnaireRepository);
        this.restQuestionnaireMockMvc = MockMvcBuilders.standaloneSetup(questionnaireResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        questionnaire = new Questionnaire();
        questionnaire.setName(DEFAULT_NAME);
        questionnaire.setQuestions(DEFAULT_QUESTIONS);
        questionnaire.setExternalId(DEFAULT_EXTERNAL_ID);
    }

    @Test
    @Transactional
    public void createQuestionnaire() throws Exception {
        int databaseSizeBeforeCreate = questionnaireRepository.findAll().size();

        // Create the Questionnaire

        restQuestionnaireMockMvc.perform(post("/api/questionnaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(questionnaire)))
                .andExpect(status().isCreated());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaires = questionnaireRepository.findAll();
        assertThat(questionnaires).hasSize(databaseSizeBeforeCreate + 1);
        Questionnaire testQuestionnaire = questionnaires.get(questionnaires.size() - 1);
        assertThat(testQuestionnaire.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQuestionnaire.getQuestions()).isEqualTo(DEFAULT_QUESTIONS);
        assertThat(testQuestionnaire.getExternalId()).isEqualTo(DEFAULT_EXTERNAL_ID);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionnaireRepository.findAll().size();
        // set the field null
        questionnaire.setName(null);

        // Create the Questionnaire, which fails.

        restQuestionnaireMockMvc.perform(post("/api/questionnaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(questionnaire)))
                .andExpect(status().isBadRequest());

        List<Questionnaire> questionnaires = questionnaireRepository.findAll();
        assertThat(questionnaires).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuestionsIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionnaireRepository.findAll().size();
        // set the field null
        questionnaire.setQuestions(null);

        // Create the Questionnaire, which fails.

        restQuestionnaireMockMvc.perform(post("/api/questionnaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(questionnaire)))
                .andExpect(status().isBadRequest());

        List<Questionnaire> questionnaires = questionnaireRepository.findAll();
        assertThat(questionnaires).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuestionnaires() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaires
        restQuestionnaireMockMvc.perform(get("/api/questionnaires"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(questionnaire.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].questions").value(hasItem(DEFAULT_QUESTIONS.toString())))
                .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID.toString())));
    }

    @Test
    @Transactional
    public void getQuestionnaire() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get the questionnaire
        restQuestionnaireMockMvc.perform(get("/api/questionnaires/{id}", questionnaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(questionnaire.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.questions").value(DEFAULT_QUESTIONS.toString()))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingQuestionnaire() throws Exception {
        // Get the questionnaire
        restQuestionnaireMockMvc.perform(get("/api/questionnaires/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestionnaire() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

		int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();

        // Update the questionnaire
        questionnaire.setName(UPDATED_NAME);
        questionnaire.setQuestions(UPDATED_QUESTIONS);
        questionnaire.setExternalId(UPDATED_EXTERNAL_ID);

        restQuestionnaireMockMvc.perform(put("/api/questionnaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(questionnaire)))
                .andExpect(status().isOk());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaires = questionnaireRepository.findAll();
        assertThat(questionnaires).hasSize(databaseSizeBeforeUpdate);
        Questionnaire testQuestionnaire = questionnaires.get(questionnaires.size() - 1);
        assertThat(testQuestionnaire.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQuestionnaire.getQuestions()).isEqualTo(UPDATED_QUESTIONS);
        assertThat(testQuestionnaire.getExternalId()).isEqualTo(UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    public void deleteQuestionnaire() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

		int databaseSizeBeforeDelete = questionnaireRepository.findAll().size();

        // Get the questionnaire
        restQuestionnaireMockMvc.perform(delete("/api/questionnaires/{id}", questionnaire.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Questionnaire> questionnaires = questionnaireRepository.findAll();
        assertThat(questionnaires).hasSize(databaseSizeBeforeDelete - 1);
    }
}
