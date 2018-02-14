package pl.przepisnik.service.mapper;

import pl.przepisnik.domain.*;
import pl.przepisnik.service.dto.StageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Stage and its DTO StageDTO.
 */
@Mapper(componentModel = "spring", uses = {RecipeMapper.class})
public interface StageMapper extends EntityMapper<StageDTO, Stage> {

    @Mapping(source = "recipe.id", target = "recipeId")
    StageDTO toDto(Stage stage);

    @Mapping(target = "amonts", ignore = true)
    @Mapping(source = "recipeId", target = "recipe")
    Stage toEntity(StageDTO stageDTO);

    default Stage fromId(Long id) {
        if (id == null) {
            return null;
        }
        Stage stage = new Stage();
        stage.setId(id);
        return stage;
    }
}
