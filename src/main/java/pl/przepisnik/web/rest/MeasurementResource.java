package pl.przepisnik.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.przepisnik.domain.Measurement;

import pl.przepisnik.repository.MeasurementRepository;
import pl.przepisnik.repository.search.MeasurementSearchRepository;
import pl.przepisnik.web.rest.errors.BadRequestAlertException;
import pl.przepisnik.web.rest.util.HeaderUtil;
import pl.przepisnik.web.rest.util.PaginationUtil;
import pl.przepisnik.service.dto.MeasurementDTO;
import pl.przepisnik.service.mapper.MeasurementMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Measurement.
 */
@RestController
@RequestMapping("/api")
public class MeasurementResource {

    private final Logger log = LoggerFactory.getLogger(MeasurementResource.class);

    private static final String ENTITY_NAME = "measurement";

    private final MeasurementRepository measurementRepository;

    private final MeasurementMapper measurementMapper;

    private final MeasurementSearchRepository measurementSearchRepository;

    public MeasurementResource(MeasurementRepository measurementRepository, MeasurementMapper measurementMapper, MeasurementSearchRepository measurementSearchRepository) {
        this.measurementRepository = measurementRepository;
        this.measurementMapper = measurementMapper;
        this.measurementSearchRepository = measurementSearchRepository;
    }

    /**
     * POST  /measurements : Create a new measurement.
     *
     * @param measurementDTO the measurementDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new measurementDTO, or with status 400 (Bad Request) if the measurement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/measurements")
    @Timed
    public ResponseEntity<MeasurementDTO> createMeasurement(@Valid @RequestBody MeasurementDTO measurementDTO) throws URISyntaxException {
        log.debug("REST request to save Measurement : {}", measurementDTO);
        if (measurementDTO.getId() != null) {
            throw new BadRequestAlertException("A new measurement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Measurement measurement = measurementMapper.toEntity(measurementDTO);
        measurement = measurementRepository.save(measurement);
        MeasurementDTO result = measurementMapper.toDto(measurement);
        measurementSearchRepository.save(measurement);
        return ResponseEntity.created(new URI("/api/measurements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /measurements : Updates an existing measurement.
     *
     * @param measurementDTO the measurementDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated measurementDTO,
     * or with status 400 (Bad Request) if the measurementDTO is not valid,
     * or with status 500 (Internal Server Error) if the measurementDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/measurements")
    @Timed
    public ResponseEntity<MeasurementDTO> updateMeasurement(@Valid @RequestBody MeasurementDTO measurementDTO) throws URISyntaxException {
        log.debug("REST request to update Measurement : {}", measurementDTO);
        if (measurementDTO.getId() == null) {
            return createMeasurement(measurementDTO);
        }
        Measurement measurement = measurementMapper.toEntity(measurementDTO);
        measurement = measurementRepository.save(measurement);
        MeasurementDTO result = measurementMapper.toDto(measurement);
        measurementSearchRepository.save(measurement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, measurementDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /measurements : get all the measurements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of measurements in body
     */
    @GetMapping("/measurements")
    @Timed
    public ResponseEntity<List<MeasurementDTO>> getAllMeasurements(Pageable pageable) {
        log.debug("REST request to get a page of Measurements");
        Page<Measurement> page = measurementRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/measurements");
        return new ResponseEntity<>(measurementMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /measurements/:id : get the "id" measurement.
     *
     * @param id the id of the measurementDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the measurementDTO, or with status 404 (Not Found)
     */
    @GetMapping("/measurements/{id}")
    @Timed
    public ResponseEntity<MeasurementDTO> getMeasurement(@PathVariable Long id) {
        log.debug("REST request to get Measurement : {}", id);
        Measurement measurement = measurementRepository.findOne(id);
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(measurementDTO));
    }

    /**
     * DELETE  /measurements/:id : delete the "id" measurement.
     *
     * @param id the id of the measurementDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/measurements/{id}")
    @Timed
    public ResponseEntity<Void> deleteMeasurement(@PathVariable Long id) {
        log.debug("REST request to delete Measurement : {}", id);
        measurementRepository.delete(id);
        measurementSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/measurements?query=:query : search for the measurement corresponding
     * to the query.
     *
     * @param query the query of the measurement search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/measurements")
    @Timed
    public ResponseEntity<List<MeasurementDTO>> searchMeasurements(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Measurements for query {}", query);
        Page<Measurement> page = measurementSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/measurements");
        return new ResponseEntity<>(measurementMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

}
