package pl.przepisnik.service.mapper;

import pl.przepisnik.domain.*;
import pl.przepisnik.service.dto.MeasurementDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Measurement and its DTO MeasurementDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MeasurementMapper extends EntityMapper<MeasurementDTO, Measurement> {


    @Mapping(target = "amonts", ignore = true)
    Measurement toEntity(MeasurementDTO measurementDTO);

    default Measurement fromId(Long id) {
        if (id == null) {
            return null;
        }
        Measurement measurement = new Measurement();
        measurement.setId(id);
        return measurement;
    }
}
