package pl.przepisnik.service.mapper;

import pl.przepisnik.domain.*;
import pl.przepisnik.service.dto.RecipeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Recipe and its DTO RecipeDTO.
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface RecipeMapper extends EntityMapper<RecipeDTO, Recipe> {


    @Mapping(target = "stages", ignore = true)
    Recipe toEntity(RecipeDTO recipeDTO);

    default Recipe fromId(Long id) {
        if (id == null) {
            return null;
        }
        Recipe recipe = new Recipe();
        recipe.setId(id);
        return recipe;
    }
}
