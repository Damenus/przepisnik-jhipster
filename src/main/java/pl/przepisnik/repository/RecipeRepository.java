package pl.przepisnik.repository;

import pl.przepisnik.domain.Recipe;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Recipe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query("select distinct recipe from Recipe recipe left join fetch recipe.categories")
    List<Recipe> findAllWithEagerRelationships();

    @Query("select recipe from Recipe recipe left join fetch recipe.categories where recipe.id =:id")
    Recipe findOneWithEagerRelationships(@Param("id") Long id);

}
