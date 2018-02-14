package pl.przepisnik.service.mapper;

import pl.przepisnik.domain.*;
import pl.przepisnik.service.dto.AmontDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Amont and its DTO AmontDTO.
 */
@Mapper(componentModel = "spring", uses = {IngredientMapper.class, MeasurementMapper.class, StageMapper.class})
public interface AmontMapper extends EntityMapper<AmontDTO, Amont> {

    @Mapping(source = "ingredient.id", target = "ingredientId")
    @Mapping(source = "measurement.id", target = "measurementId")
    @Mapping(source = "stage.id", target = "stageId")
    AmontDTO toDto(Amont amont);

    @Mapping(source = "ingredientId", target = "ingredient")
    @Mapping(source = "measurementId", target = "measurement")
    @Mapping(source = "stageId", target = "stage")
    Amont toEntity(AmontDTO amontDTO);

    default Amont fromId(Long id) {
        if (id == null) {
            return null;
        }
        Amont amont = new Amont();
        amont.setId(id);
        return amont;
    }
}
