package pl.przepisnik.repository.search;

import pl.przepisnik.domain.Amont;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Amont entity.
 */
public interface AmontSearchRepository extends ElasticsearchRepository<Amont, Long> {
}
