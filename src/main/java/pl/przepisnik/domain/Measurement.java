package pl.przepisnik.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Measurement.
 */
@Entity
@Table(name = "measurement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "measurement")
public class Measurement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "measurement")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Amont> amonts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Measurement name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Amont> getAmonts() {
        return amonts;
    }

    public Measurement amonts(Set<Amont> amonts) {
        this.amonts = amonts;
        return this;
    }

    public Measurement addAmont(Amont amont) {
        this.amonts.add(amont);
        amont.setMeasurement(this);
        return this;
    }

    public Measurement removeAmont(Amont amont) {
        this.amonts.remove(amont);
        amont.setMeasurement(null);
        return this;
    }

    public void setAmonts(Set<Amont> amonts) {
        this.amonts = amonts;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Measurement measurement = (Measurement) o;
        if (measurement.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), measurement.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Measurement{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
