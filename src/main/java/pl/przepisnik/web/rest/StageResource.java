package pl.przepisnik.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.przepisnik.domain.Stage;

import pl.przepisnik.repository.StageRepository;
import pl.przepisnik.repository.search.StageSearchRepository;
import pl.przepisnik.web.rest.errors.BadRequestAlertException;
import pl.przepisnik.web.rest.util.HeaderUtil;
import pl.przepisnik.service.dto.StageDTO;
import pl.przepisnik.service.mapper.StageMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Stage.
 */
@RestController
@RequestMapping("/api")
public class StageResource {

    private final Logger log = LoggerFactory.getLogger(StageResource.class);

    private static final String ENTITY_NAME = "stage";

    private final StageRepository stageRepository;

    private final StageMapper stageMapper;

    private final StageSearchRepository stageSearchRepository;

    public StageResource(StageRepository stageRepository, StageMapper stageMapper, StageSearchRepository stageSearchRepository) {
        this.stageRepository = stageRepository;
        this.stageMapper = stageMapper;
        this.stageSearchRepository = stageSearchRepository;
    }

    /**
     * POST  /stages : Create a new stage.
     *
     * @param stageDTO the stageDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stageDTO, or with status 400 (Bad Request) if the stage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stages")
    @Timed
    public ResponseEntity<StageDTO> createStage(@RequestBody StageDTO stageDTO) throws URISyntaxException {
        log.debug("REST request to save Stage : {}", stageDTO);
        if (stageDTO.getId() != null) {
            throw new BadRequestAlertException("A new stage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Stage stage = stageMapper.toEntity(stageDTO);
        stage = stageRepository.save(stage);
        StageDTO result = stageMapper.toDto(stage);
        stageSearchRepository.save(stage);
        return ResponseEntity.created(new URI("/api/stages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stages : Updates an existing stage.
     *
     * @param stageDTO the stageDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stageDTO,
     * or with status 400 (Bad Request) if the stageDTO is not valid,
     * or with status 500 (Internal Server Error) if the stageDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stages")
    @Timed
    public ResponseEntity<StageDTO> updateStage(@RequestBody StageDTO stageDTO) throws URISyntaxException {
        log.debug("REST request to update Stage : {}", stageDTO);
        if (stageDTO.getId() == null) {
            return createStage(stageDTO);
        }
        Stage stage = stageMapper.toEntity(stageDTO);
        stage = stageRepository.save(stage);
        StageDTO result = stageMapper.toDto(stage);
        stageSearchRepository.save(stage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stageDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stages : get all the stages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of stages in body
     */
    @GetMapping("/stages")
    @Timed
    public List<StageDTO> getAllStages() {
        log.debug("REST request to get all Stages");
        List<Stage> stages = stageRepository.findAll();
        return stageMapper.toDto(stages);
        }

    /**
     * GET  /stages/:id : get the "id" stage.
     *
     * @param id the id of the stageDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stageDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stages/{id}")
    @Timed
    public ResponseEntity<StageDTO> getStage(@PathVariable Long id) {
        log.debug("REST request to get Stage : {}", id);
        Stage stage = stageRepository.findOne(id);
        StageDTO stageDTO = stageMapper.toDto(stage);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stageDTO));
    }

    /**
     * DELETE  /stages/:id : delete the "id" stage.
     *
     * @param id the id of the stageDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stages/{id}")
    @Timed
    public ResponseEntity<Void> deleteStage(@PathVariable Long id) {
        log.debug("REST request to delete Stage : {}", id);
        stageRepository.delete(id);
        stageSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/stages?query=:query : search for the stage corresponding
     * to the query.
     *
     * @param query the query of the stage search
     * @return the result of the search
     */
    @GetMapping("/_search/stages")
    @Timed
    public List<StageDTO> searchStages(@RequestParam String query) {
        log.debug("REST request to search Stages for query {}", query);
        return StreamSupport
            .stream(stageSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(stageMapper::toDto)
            .collect(Collectors.toList());
    }

}
