package pl.przepisnik.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.przepisnik.domain.Recipe;

import pl.przepisnik.repository.RecipeRepository;
import pl.przepisnik.repository.search.RecipeSearchRepository;
import pl.przepisnik.web.rest.errors.BadRequestAlertException;
import pl.przepisnik.web.rest.util.HeaderUtil;
import pl.przepisnik.web.rest.util.PaginationUtil;
import pl.przepisnik.service.dto.RecipeDTO;
import pl.przepisnik.service.mapper.RecipeMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
 * REST controller for managing Recipe.
 */
@RestController
@RequestMapping("/api")
public class RecipeResource {

    private final Logger log = LoggerFactory.getLogger(RecipeResource.class);

    private static final String ENTITY_NAME = "recipe";

    private final RecipeRepository recipeRepository;

    private final RecipeMapper recipeMapper;

    private final RecipeSearchRepository recipeSearchRepository;

    public RecipeResource(RecipeRepository recipeRepository, RecipeMapper recipeMapper, RecipeSearchRepository recipeSearchRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.recipeSearchRepository = recipeSearchRepository;
    }

    /**
     * POST  /recipes : Create a new recipe.
     *
     * @param recipeDTO the recipeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new recipeDTO, or with status 400 (Bad Request) if the recipe has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/recipes")
    @Timed
    public ResponseEntity<RecipeDTO> createRecipe(@RequestBody RecipeDTO recipeDTO) throws URISyntaxException {
        log.debug("REST request to save Recipe : {}", recipeDTO);
        if (recipeDTO.getId() != null) {
            throw new BadRequestAlertException("A new recipe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Recipe recipe = recipeMapper.toEntity(recipeDTO);
        recipe = recipeRepository.save(recipe);
        RecipeDTO result = recipeMapper.toDto(recipe);
        recipeSearchRepository.save(recipe);
        return ResponseEntity.created(new URI("/api/recipes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /recipes : Updates an existing recipe.
     *
     * @param recipeDTO the recipeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated recipeDTO,
     * or with status 400 (Bad Request) if the recipeDTO is not valid,
     * or with status 500 (Internal Server Error) if the recipeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/recipes")
    @Timed
    public ResponseEntity<RecipeDTO> updateRecipe(@RequestBody RecipeDTO recipeDTO) throws URISyntaxException {
        log.debug("REST request to update Recipe : {}", recipeDTO);
        if (recipeDTO.getId() == null) {
            return createRecipe(recipeDTO);
        }
        Recipe recipe = recipeMapper.toEntity(recipeDTO);
        recipe = recipeRepository.save(recipe);
        RecipeDTO result = recipeMapper.toDto(recipe);
        recipeSearchRepository.save(recipe);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, recipeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /recipes : get all the recipes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of recipes in body
     */
    @GetMapping("/recipes")
    @Timed
    public ResponseEntity<List<RecipeDTO>> getAllRecipes(Pageable pageable) {
        log.debug("REST request to get a page of Recipes");
        Page<Recipe> page = recipeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/recipes");
        return new ResponseEntity<>(recipeMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /recipes/:id : get the "id" recipe.
     *
     * @param id the id of the recipeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the recipeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/recipes/{id}")
    @Timed
    public ResponseEntity<RecipeDTO> getRecipe(@PathVariable Long id) {
        log.debug("REST request to get Recipe : {}", id);
        Recipe recipe = recipeRepository.findOneWithEagerRelationships(id);
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(recipeDTO));
    }

    /**
     * DELETE  /recipes/:id : delete the "id" recipe.
     *
     * @param id the id of the recipeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/recipes/{id}")
    @Timed
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        log.debug("REST request to delete Recipe : {}", id);
        recipeRepository.delete(id);
        recipeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/recipes?query=:query : search for the recipe corresponding
     * to the query.
     *
     * @param query the query of the recipe search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/recipes")
    @Timed
    public ResponseEntity<List<RecipeDTO>> searchRecipes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Recipes for query {}", query);
        Page<Recipe> page = recipeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/recipes");
        return new ResponseEntity<>(recipeMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

}
