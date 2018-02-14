package pl.przepisnik.repository.search;

import pl.przepisnik.domain.Stage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Stage entity.
 */
public interface StageSearchRepository extends ElasticsearchRepository<Stage, Long> {
}
