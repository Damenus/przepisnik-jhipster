package pl.przepisnik.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.przepisnik.domain.Amont;

import pl.przepisnik.repository.AmontRepository;
import pl.przepisnik.repository.search.AmontSearchRepository;
import pl.przepisnik.web.rest.errors.BadRequestAlertException;
import pl.przepisnik.web.rest.util.HeaderUtil;
import pl.przepisnik.service.dto.AmontDTO;
import pl.przepisnik.service.mapper.AmontMapper;
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
 * REST controller for managing Amont.
 */
@RestController
@RequestMapping("/api")
public class AmontResource {

    private final Logger log = LoggerFactory.getLogger(AmontResource.class);

    private static final String ENTITY_NAME = "amont";

    private final AmontRepository amontRepository;

    private final AmontMapper amontMapper;

    private final AmontSearchRepository amontSearchRepository;

    public AmontResource(AmontRepository amontRepository, AmontMapper amontMapper, AmontSearchRepository amontSearchRepository) {
        this.amontRepository = amontRepository;
        this.amontMapper = amontMapper;
        this.amontSearchRepository = amontSearchRepository;
    }

    /**
     * POST  /amonts : Create a new amont.
     *
     * @param amontDTO the amontDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new amontDTO, or with status 400 (Bad Request) if the amont has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/amonts")
    @Timed
    public ResponseEntity<AmontDTO> createAmont(@RequestBody AmontDTO amontDTO) throws URISyntaxException {
        log.debug("REST request to save Amont : {}", amontDTO);
        if (amontDTO.getId() != null) {
            throw new BadRequestAlertException("A new amont cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Amont amont = amontMapper.toEntity(amontDTO);
        amont = amontRepository.save(amont);
        AmontDTO result = amontMapper.toDto(amont);
        amontSearchRepository.save(amont);
        return ResponseEntity.created(new URI("/api/amonts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /amonts : Updates an existing amont.
     *
     * @param amontDTO the amontDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated amontDTO,
     * or with status 400 (Bad Request) if the amontDTO is not valid,
     * or with status 500 (Internal Server Error) if the amontDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/amonts")
    @Timed
    public ResponseEntity<AmontDTO> updateAmont(@RequestBody AmontDTO amontDTO) throws URISyntaxException {
        log.debug("REST request to update Amont : {}", amontDTO);
        if (amontDTO.getId() == null) {
            return createAmont(amontDTO);
        }
        Amont amont = amontMapper.toEntity(amontDTO);
        amont = amontRepository.save(amont);
        AmontDTO result = amontMapper.toDto(amont);
        amontSearchRepository.save(amont);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, amontDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /amonts : get all the amonts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of amonts in body
     */
    @GetMapping("/amonts")
    @Timed
    public List<AmontDTO> getAllAmonts() {
        log.debug("REST request to get all Amonts");
        List<Amont> amonts = amontRepository.findAll();
        return amontMapper.toDto(amonts);
        }

    /**
     * GET  /amonts/:id : get the "id" amont.
     *
     * @param id the id of the amontDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the amontDTO, or with status 404 (Not Found)
     */
    @GetMapping("/amonts/{id}")
    @Timed
    public ResponseEntity<AmontDTO> getAmont(@PathVariable Long id) {
        log.debug("REST request to get Amont : {}", id);
        Amont amont = amontRepository.findOne(id);
        AmontDTO amontDTO = amontMapper.toDto(amont);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(amontDTO));
    }

    /**
     * DELETE  /amonts/:id : delete the "id" amont.
     *
     * @param id the id of the amontDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/amonts/{id}")
    @Timed
    public ResponseEntity<Void> deleteAmont(@PathVariable Long id) {
        log.debug("REST request to delete Amont : {}", id);
        amontRepository.delete(id);
        amontSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/amonts?query=:query : search for the amont corresponding
     * to the query.
     *
     * @param query the query of the amont search
     * @return the result of the search
     */
    @GetMapping("/_search/amonts")
    @Timed
    public List<AmontDTO> searchAmonts(@RequestParam String query) {
        log.debug("REST request to search Amonts for query {}", query);
        return StreamSupport
            .stream(amontSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(amontMapper::toDto)
            .collect(Collectors.toList());
    }

}
