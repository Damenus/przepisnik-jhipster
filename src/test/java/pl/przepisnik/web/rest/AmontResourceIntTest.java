package pl.przepisnik.web.rest;

import pl.przepisnik.PrzepisnikApp;

import pl.przepisnik.domain.Amont;
import pl.przepisnik.repository.AmontRepository;
import pl.przepisnik.repository.search.AmontSearchRepository;
import pl.przepisnik.service.dto.AmontDTO;
import pl.przepisnik.service.mapper.AmontMapper;
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
 * Test class for the AmontResource REST controller.
 *
 * @see AmontResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrzepisnikApp.class)
public class AmontResourceIntTest {

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    @Autowired
    private AmontRepository amontRepository;

    @Autowired
    private AmontMapper amontMapper;

    @Autowired
    private AmontSearchRepository amontSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAmontMockMvc;

    private Amont amont;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AmontResource amontResource = new AmontResource(amontRepository, amontMapper, amontSearchRepository);
        this.restAmontMockMvc = MockMvcBuilders.standaloneSetup(amontResource)
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
    public static Amont createEntity(EntityManager em) {
        Amont amont = new Amont()
            .number(DEFAULT_NUMBER);
        return amont;
    }

    @Before
    public void initTest() {
        amontSearchRepository.deleteAll();
        amont = createEntity(em);
    }

    @Test
    @Transactional
    public void createAmont() throws Exception {
        int databaseSizeBeforeCreate = amontRepository.findAll().size();

        // Create the Amont
        AmontDTO amontDTO = amontMapper.toDto(amont);
        restAmontMockMvc.perform(post("/api/amonts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amontDTO)))
            .andExpect(status().isCreated());

        // Validate the Amont in the database
        List<Amont> amontList = amontRepository.findAll();
        assertThat(amontList).hasSize(databaseSizeBeforeCreate + 1);
        Amont testAmont = amontList.get(amontList.size() - 1);
        assertThat(testAmont.getNumber()).isEqualTo(DEFAULT_NUMBER);

        // Validate the Amont in Elasticsearch
        Amont amontEs = amontSearchRepository.findOne(testAmont.getId());
        assertThat(amontEs).isEqualToIgnoringGivenFields(testAmont);
    }

    @Test
    @Transactional
    public void createAmontWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = amontRepository.findAll().size();

        // Create the Amont with an existing ID
        amont.setId(1L);
        AmontDTO amontDTO = amontMapper.toDto(amont);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAmontMockMvc.perform(post("/api/amonts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amontDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Amont in the database
        List<Amont> amontList = amontRepository.findAll();
        assertThat(amontList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAmonts() throws Exception {
        // Initialize the database
        amontRepository.saveAndFlush(amont);

        // Get all the amontList
        restAmontMockMvc.perform(get("/api/amonts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amont.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)));
    }

    @Test
    @Transactional
    public void getAmont() throws Exception {
        // Initialize the database
        amontRepository.saveAndFlush(amont);

        // Get the amont
        restAmontMockMvc.perform(get("/api/amonts/{id}", amont.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(amont.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingAmont() throws Exception {
        // Get the amont
        restAmontMockMvc.perform(get("/api/amonts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAmont() throws Exception {
        // Initialize the database
        amontRepository.saveAndFlush(amont);
        amontSearchRepository.save(amont);
        int databaseSizeBeforeUpdate = amontRepository.findAll().size();

        // Update the amont
        Amont updatedAmont = amontRepository.findOne(amont.getId());
        // Disconnect from session so that the updates on updatedAmont are not directly saved in db
        em.detach(updatedAmont);
        updatedAmont
            .number(UPDATED_NUMBER);
        AmontDTO amontDTO = amontMapper.toDto(updatedAmont);

        restAmontMockMvc.perform(put("/api/amonts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amontDTO)))
            .andExpect(status().isOk());

        // Validate the Amont in the database
        List<Amont> amontList = amontRepository.findAll();
        assertThat(amontList).hasSize(databaseSizeBeforeUpdate);
        Amont testAmont = amontList.get(amontList.size() - 1);
        assertThat(testAmont.getNumber()).isEqualTo(UPDATED_NUMBER);

        // Validate the Amont in Elasticsearch
        Amont amontEs = amontSearchRepository.findOne(testAmont.getId());
        assertThat(amontEs).isEqualToIgnoringGivenFields(testAmont);
    }

    @Test
    @Transactional
    public void updateNonExistingAmont() throws Exception {
        int databaseSizeBeforeUpdate = amontRepository.findAll().size();

        // Create the Amont
        AmontDTO amontDTO = amontMapper.toDto(amont);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAmontMockMvc.perform(put("/api/amonts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amontDTO)))
            .andExpect(status().isCreated());

        // Validate the Amont in the database
        List<Amont> amontList = amontRepository.findAll();
        assertThat(amontList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAmont() throws Exception {
        // Initialize the database
        amontRepository.saveAndFlush(amont);
        amontSearchRepository.save(amont);
        int databaseSizeBeforeDelete = amontRepository.findAll().size();

        // Get the amont
        restAmontMockMvc.perform(delete("/api/amonts/{id}", amont.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean amontExistsInEs = amontSearchRepository.exists(amont.getId());
        assertThat(amontExistsInEs).isFalse();

        // Validate the database is empty
        List<Amont> amontList = amontRepository.findAll();
        assertThat(amontList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAmont() throws Exception {
        // Initialize the database
        amontRepository.saveAndFlush(amont);
        amontSearchRepository.save(amont);

        // Search the amont
        restAmontMockMvc.perform(get("/api/_search/amonts?query=id:" + amont.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amont.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Amont.class);
        Amont amont1 = new Amont();
        amont1.setId(1L);
        Amont amont2 = new Amont();
        amont2.setId(amont1.getId());
        assertThat(amont1).isEqualTo(amont2);
        amont2.setId(2L);
        assertThat(amont1).isNotEqualTo(amont2);
        amont1.setId(null);
        assertThat(amont1).isNotEqualTo(amont2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AmontDTO.class);
        AmontDTO amontDTO1 = new AmontDTO();
        amontDTO1.setId(1L);
        AmontDTO amontDTO2 = new AmontDTO();
        assertThat(amontDTO1).isNotEqualTo(amontDTO2);
        amontDTO2.setId(amontDTO1.getId());
        assertThat(amontDTO1).isEqualTo(amontDTO2);
        amontDTO2.setId(2L);
        assertThat(amontDTO1).isNotEqualTo(amontDTO2);
        amontDTO1.setId(null);
        assertThat(amontDTO1).isNotEqualTo(amontDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(amontMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(amontMapper.fromId(null)).isNull();
    }
}
