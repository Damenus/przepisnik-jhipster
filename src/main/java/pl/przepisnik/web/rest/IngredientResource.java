package pl.przepisnik.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.przepisnik.domain.Ingredient;

import pl.przepisnik.repository.IngredientRepository;
import pl.przepisnik.repository.search.IngredientSearchRepository;
import pl.przepisnik.web.rest.errors.BadRequestAlertException;
import pl.przepisnik.web.rest.util.HeaderUtil;
import pl.przepisnik.web.rest.util.PaginationUtil;
import pl.przepisnik.service.dto.IngredientDTO;
import pl.przepisnik.service.mapper.IngredientMapper;
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
 * REST controller for managing Ingredient.
 */
@RestController
@RequestMapping("/api")
public class IngredientResource {

    private final Logger log = LoggerFactory.getLogger(IngredientResource.class);

    private static final String ENTITY_NAME = "ingredient";

    private final IngredientRepository ingredientRepository;

    private final IngredientMapper ingredientMapper;

    private final IngredientSearchRepository ingredientSearchRepository;

    public IngredientResource(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper, IngredientSearchRepository ingredientSearchRepository) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
        this.ingredientSearchRepository = ingredientSearchRepository;
    }

    /**
     * POST  /ingredients : Create a new ingredient.
     *
     * @param ingredientDTO the ingredientDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ingredientDTO, or with status 400 (Bad Request) if the ingredient has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ingredients")
    @Timed
    public ResponseEntity<IngredientDTO> createIngredient(@Valid @RequestBody IngredientDTO ingredientDTO) throws URISyntaxException {
        log.debug("REST request to save Ingredient : {}", ingredientDTO);
        if (ingredientDTO.getId() != null) {
            throw new BadRequestAlertException("A new ingredient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ingredient ingredient = ingredientMapper.toEntity(ingredientDTO);
        ingredient = ingredientRepository.save(ingredient);
        IngredientDTO result = ingredientMapper.toDto(ingredient);
        ingredientSearchRepository.save(ingredient);
        return ResponseEntity.created(new URI("/api/ingredients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ingredients : Updates an existing ingredient.
     *
     * @param ingredientDTO the ingredientDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ingredientDTO,
     * or with status 400 (Bad Request) if the ingredientDTO is not valid,
     * or with status 500 (Internal Server Error) if the ingredientDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ingredients")
    @Timed
    public ResponseEntity<IngredientDTO> updateIngredient(@Valid @RequestBody IngredientDTO ingredientDTO) throws URISyntaxException {
        log.debug("REST request to update Ingredient : {}", ingredientDTO);
        if (ingredientDTO.getId() == null) {
            return createIngredient(ingredientDTO);
        }
        Ingredient ingredient = ingredientMapper.toEntity(ingredientDTO);
        ingredient = ingredientRepository.save(ingredient);
        IngredientDTO result = ingredientMapper.toDto(ingredient);
        ingredientSearchRepository.save(ingredient);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ingredientDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ingredients : get all the ingredients.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ingredients in body
     */
    @GetMapping("/ingredients")
    @Timed
    public ResponseEntity<List<IngredientDTO>> getAllIngredients(Pageable pageable) {
        log.debug("REST request to get a page of Ingredients");
        Page<Ingredient> page = ingredientRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ingredients");
        return new ResponseEntity<>(ingredientMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /ingredients/:id : get the "id" ingredient.
     *
     * @param id the id of the ingredientDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ingredientDTO, or with status 404 (Not Found)
     */
    @GetMapping("/ingredients/{id}")
    @Timed
    public ResponseEntity<IngredientDTO> getIngredient(@PathVariable Long id) {
        log.debug("REST request to get Ingredient : {}", id);
        Ingredient ingredient = ingredientRepository.findOne(id);
        IngredientDTO ingredientDTO = ingredientMapper.toDto(ingredient);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ingredientDTO));
    }

    /**
     * DELETE  /ingredients/:id : delete the "id" ingredient.
     *
     * @param id the id of the ingredientDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ingredients/{id}")
    @Timed
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        log.debug("REST request to delete Ingredient : {}", id);
        ingredientRepository.delete(id);
        ingredientSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/ingredients?query=:query : search for the ingredient corresponding
     * to the query.
     *
     * @param query the query of the ingredient search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/ingredients")
    @Timed
    public ResponseEntity<List<IngredientDTO>> searchIngredients(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Ingredients for query {}", query);
        Page<Ingredient> page = ingredientSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/ingredients");
        return new ResponseEntity<>(ingredientMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

}
