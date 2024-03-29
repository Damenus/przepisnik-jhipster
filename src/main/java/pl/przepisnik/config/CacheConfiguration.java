package pl.przepisnik.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(pl.przepisnik.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(pl.przepisnik.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(pl.przepisnik.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(pl.przepisnik.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(pl.przepisnik.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(pl.przepisnik.domain.Ingredient.class.getName(), jcacheConfiguration);
            cm.createCache(pl.przepisnik.domain.Ingredient.class.getName() + ".amonts", jcacheConfiguration);
            cm.createCache(pl.przepisnik.domain.Measurement.class.getName(), jcacheConfiguration);
            cm.createCache(pl.przepisnik.domain.Measurement.class.getName() + ".amonts", jcacheConfiguration);
            cm.createCache(pl.przepisnik.domain.Amont.class.getName(), jcacheConfiguration);
            cm.createCache(pl.przepisnik.domain.Stage.class.getName(), jcacheConfiguration);
            cm.createCache(pl.przepisnik.domain.Stage.class.getName() + ".amonts", jcacheConfiguration);
            cm.createCache(pl.przepisnik.domain.Recipe.class.getName(), jcacheConfiguration);
            cm.createCache(pl.przepisnik.domain.Recipe.class.getName() + ".stages", jcacheConfiguration);
            cm.createCache(pl.przepisnik.domain.Recipe.class.getName() + ".categories", jcacheConfiguration);
            cm.createCache(pl.przepisnik.domain.Category.class.getName(), jcacheConfiguration);
            cm.createCache(pl.przepisnik.domain.Category.class.getName() + ".recipes", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
