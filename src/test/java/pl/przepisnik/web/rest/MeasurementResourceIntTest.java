package pl.przepisnik.web.rest;

import pl.przepisnik.PrzepisnikApp;

import pl.przepisnik.domain.Measurement;
import pl.przepisnik.repository.MeasurementRepository;
import pl.przepisnik.repository.search.MeasurementSearchRepository;
import pl.przepisnik.service.dto.MeasurementDTO;
import pl.przepisnik.service.mapper.MeasurementMapper;
import pl.przepisnik.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static pl.przepisnik.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MeasurementResource REST controller.
 *
 * @see MeasurementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrzepisnikApp.class)
public class MeasurementResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private MeasurementMapper measurementMapper;

    @Autowired
    private MeasurementSearchRepository measurementSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMeasurementMockMvc;

    private Measurement measurement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MeasurementResource measurementResource = new MeasurementResource(measurementRepository, measurementMapper, measurementSearchRepository);
        this.restMeasurementMockMvc = MockMvcBuilders.standaloneSetup(measurementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Measurement createEntity(EntityManager em) {
        Measurement measurement = new Measurement()
            .name(DEFAULT_NAME);
        return measurement;
    }

    @Before
    public void initTest() {
        measurementSearchRepository.deleteAll();
        measurement = createEntity(em);
    }

    @Test
    @Transactional
    public void createMeasurement() throws Exception {
        int databaseSizeBeforeCreate = measurementRepository.findAll().size();

        // Create the Measurement
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);
        restMeasurementMockMvc.perform(post("/api/measurements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measurementDTO)))
            .andExpect(status().isCreated());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeCreate + 1);
        Measurement testMeasurement = measurementList.get(measurementList.size() - 1);
        assertThat(testMeasurement.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Measurement in Elasticsearch
        Measurement measurementEs = measurementSearchRepository.findOne(testMeasurement.getId());
        assertThat(measurementEs).isEqualToIgnoringGivenFields(testMeasurement);
    }

    @Test
    @Transactional
    public void createMeasurementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = measurementRepository.findAll().size();

        // Create the Measurement with an existing ID
        measurement.setId(1L);
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeasurementMockMvc.perform(post("/api/measurements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measurementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = measurementRepository.findAll().size();
        // set the field null
        measurement.setName(null);

        // Create the Measurement, which fails.
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);

        restMeasurementMockMvc.perform(post("/api/measurements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measurementDTO)))
            .andExpect(status().isBadRequest());

        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMeasurements() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        // Get all the measurementList
        restMeasurementMockMvc.perform(get("/api/measurements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measurement.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getMeasurement() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        // Get the measurement
        restMeasurementMockMvc.perform(get("/api/measurements/{id}", measurement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(measurement.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMeasurement() throws Exception {
        // Get the measurement
        restMeasurementMockMvc.perform(get("/api/measurements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMeasurement() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);
        measurementSearchRepository.save(measurement);
        int databaseSizeBeforeUpdate = measurementRepository.findAll().size();

        // Update the measurement
        Measurement updatedMeasurement = measurementRepository.findOne(measurement.getId());
        // Disconnect from session so that the updates on updatedMeasurement are not directly saved in db
        em.detach(updatedMeasurement);
        updatedMeasurement
            .name(UPDATED_NAME);
        MeasurementDTO measurementDTO = measurementMapper.toDto(updatedMeasurement);

        restMeasurementMockMvc.perform(put("/api/measurements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measurementDTO)))
            .andExpect(status().isOk());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeUpdate);
        Measurement testMeasurement = measurementList.get(measurementList.size() - 1);
        assertThat(testMeasurement.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Measurement in Elasticsearch
        Measurement measurementEs = measurementSearchRepository.findOne(testMeasurement.getId());
        assertThat(measurementEs).isEqualToIgnoringGivenFields(testMeasurement);
    }

    @Test
    @Transactional
    public void updateNonExistingMeasurement() throws Exception {
        int databaseSizeBeforeUpdate = measurementRepository.findAll().size();

        // Create the Measurement
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMeasurementMockMvc.perform(put("/api/measurements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measurementDTO)))
            .andExpect(status().isCreated());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMeasurement() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);
        measurementSearchRepository.save(measurement);
        int databaseSizeBeforeDelete = measurementRepository.findAll().size();

        // Get the measurement
        restMeasurementMockMvc.perform(delete("/api/measurements/{id}", measurement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean measurementExistsInEs = measurementSearchRepository.exists(measurement.getId());
        assertThat(measurementExistsInEs).isFalse();

        // Validate the database is empty
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMeasurement() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);
        measurementSearchRepository.save(measurement);

        // Search the measurement
        restMeasurementMockMvc.perform(get("/api/_search/measurements?query=id:" + measurement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measurement.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Measurement.class);
        Measurement measurement1 = new Measurement();
        measurement1.setId(1L);
        Measurement measurement2 = new Measurement();
        measurement2.setId(measurement1.getId());
        assertThat(measurement1).isEqualTo(measurement2);
        measurement2.setId(2L);
        assertThat(measurement1).isNotEqualTo(measurement2);
        measurement1.setId(null);
        assertThat(measurement1).isNotEqualTo(measurement2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MeasurementDTO.class);
        MeasurementDTO measurementDTO1 = new MeasurementDTO();
        measurementDTO1.setId(1L);
        MeasurementDTO measurementDTO2 = new MeasurementDTO();
        assertThat(measurementDTO1).isNotEqualTo(measurementDTO2);
        measurementDTO2.setId(measurementDTO1.getId());
        assertThat(measurementDTO1).isEqualTo(measurementDTO2);
        measurementDTO2.setId(2L);
        assertThat(measurementDTO1).isNotEqualTo(measurementDTO2);
        measurementDTO1.setId(null);
        assertThat(measurementDTO1).isNotEqualTo(measurementDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(measurementMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(measurementMapper.fromId(null)).isNull();
    }
}
