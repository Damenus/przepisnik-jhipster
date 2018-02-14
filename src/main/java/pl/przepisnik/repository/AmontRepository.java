package pl.przepisnik.repository;

import pl.przepisnik.domain.Amont;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Amont entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AmontRepository extends JpaRepository<Amont, Long> {

}
