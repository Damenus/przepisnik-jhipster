package pl.przepisnik.web.rest;

import pl.przepisnik.PrzepisnikApp;

import pl.przepisnik.domain.Stage;
import pl.przepisnik.repository.StageRepository;
import pl.przepisnik.repository.search.StageSearchRepository;
import pl.przepisnik.service.dto.StageDTO;
import pl.przepisnik.service.mapper.StageMapper;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static pl.przepisnik.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StageResource REST controller.
 *
 * @see StageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrzepisnikApp.class)
public class StageResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private StageMapper stageMapper;

    @Autowired
    private StageSearchRepository stageSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStageMockMvc;

    private Stage stage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StageResource stageResource = new StageResource(stageRepository, stageMapper, stageSearchRepository);
        this.restStageMockMvc = MockMvcBuilders.standaloneSetup(stageResource)
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
    public static Stage createEntity(EntityManager em) {
        Stage stage = new Stage()
            .title(DEFAULT_TITLE)
            .text(DEFAULT_TEXT);
        return stage;
    }

    @Before
    public void initTest() {
        stageSearchRepository.deleteAll();
        stage = createEntity(em);
    }

    @Test
    @Transactional
    public void createStage() throws Exception {
        int databaseSizeBeforeCreate = stageRepository.findAll().size();

        // Create the Stage
        StageDTO stageDTO = stageMapper.toDto(stage);
        restStageMockMvc.perform(post("/api/stages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stageDTO)))
            .andExpect(status().isCreated());

        // Validate the Stage in the database
        List<Stage> stageList = stageRepository.findAll();
        assertThat(stageList).hasSize(databaseSizeBeforeCreate + 1);
        Stage testStage = stageList.get(stageList.size() - 1);
        assertThat(testStage.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testStage.getText()).isEqualTo(DEFAULT_TEXT);

        // Validate the Stage in Elasticsearch
        Stage stageEs = stageSearchRepository.findOne(testStage.getId());
        assertThat(stageEs).isEqualToIgnoringGivenFields(testStage);
    }

    @Test
    @Transactional
    public void createStageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stageRepository.findAll().size();

        // Create the Stage with an existing ID
        stage.setId(1L);
        StageDTO stageDTO = stageMapper.toDto(stage);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStageMockMvc.perform(post("/api/stages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Stage in the database
        List<Stage> stageList = stageRepository.findAll();
        assertThat(stageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStages() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList
        restStageMockMvc.perform(get("/api/stages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stage.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    @Test
    @Transactional
    public void getStage() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get the stage
        restStageMockMvc.perform(get("/api/stages/{id}", stage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stage.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStage() throws Exception {
        // Get the stage
        restStageMockMvc.perform(get("/api/stages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStage() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);
        stageSearchRepository.save(stage);
        int databaseSizeBeforeUpdate = stageRepository.findAll().size();

        // Update the stage
        Stage updatedStage = stageRepository.findOne(stage.getId());
        // Disconnect from session so that the updates on updatedStage are not directly saved in db
        em.detach(updatedStage);
        updatedStage
            .title(UPDATED_TITLE)
            .text(UPDATED_TEXT);
        StageDTO stageDTO = stageMapper.toDto(updatedStage);

        restStageMockMvc.perform(put("/api/stages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stageDTO)))
            .andExpect(status().isOk());

        // Validate the Stage in the database
        List<Stage> stageList = stageRepository.findAll();
        assertThat(stageList).hasSize(databaseSizeBeforeUpdate);
        Stage testStage = stageList.get(stageList.size() - 1);
        assertThat(testStage.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testStage.getText()).isEqualTo(UPDATED_TEXT);

        // Validate the Stage in Elasticsearch
        Stage stageEs = stageSearchRepository.findOne(testStage.getId());
        assertThat(stageEs).isEqualToIgnoringGivenFields(testStage);
    }

    @Test
    @Transactional
    public void updateNonExistingStage() throws Exception {
        int databaseSizeBeforeUpdate = stageRepository.findAll().size();

        // Create the Stage
        StageDTO stageDTO = stageMapper.toDto(stage);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStageMockMvc.perform(put("/api/stages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stageDTO)))
            .andExpect(status().isCreated());

        // Validate the Stage in the database
        List<Stage> stageList = stageRepository.findAll();
        assertThat(stageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStage() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);
        stageSearchRepository.save(stage);
        int databaseSizeBeforeDelete = stageRepository.findAll().size();

        // Get the stage
        restStageMockMvc.perform(delete("/api/stages/{id}", stage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean stageExistsInEs = stageSearchRepository.exists(stage.getId());
        assertThat(stageExistsInEs).isFalse();

        // Validate the database is empty
        List<Stage> stageList = stageRepository.findAll();
        assertThat(stageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStage() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);
        stageSearchRepository.save(stage);

        // Search the stage
        restStageMockMvc.perform(get("/api/_search/stages?query=id:" + stage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stage.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Stage.class);
        Stage stage1 = new Stage();
        stage1.setId(1L);
        Stage stage2 = new Stage();
        stage2.setId(stage1.getId());
        assertThat(stage1).isEqualTo(stage2);
        stage2.setId(2L);
        assertThat(stage1).isNotEqualTo(stage2);
        stage1.setId(null);
        assertThat(stage1).isNotEqualTo(stage2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StageDTO.class);
        StageDTO stageDTO1 = new StageDTO();
        stageDTO1.setId(1L);
        StageDTO stageDTO2 = new StageDTO();
        assertThat(stageDTO1).isNotEqualTo(stageDTO2);
        stageDTO2.setId(stageDTO1.getId());
        assertThat(stageDTO1).isEqualTo(stageDTO2);
        stageDTO2.setId(2L);
        assertThat(stageDTO1).isNotEqualTo(stageDTO2);
        stageDTO1.setId(null);
        assertThat(stageDTO1).isNotEqualTo(stageDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(stageMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(stageMapper.fromId(null)).isNull();
    }
}
