package pl.przepisnik.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Amont.
 */
@Entity
@Table(name = "amont")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "amont")
public class Amont implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_number")
    private Integer number;

    @ManyToOne
    private Ingredient ingredient;

    @ManyToOne
    private Measurement measurement;

    @ManyToOne
    private Stage stage;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public Amont number(Integer number) {
        this.number = number;
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public Amont ingredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public Amont measurement(Measurement measurement) {
        this.measurement = measurement;
        return this;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    public Stage getStage() {
        return stage;
    }

    public Amont stage(Stage stage) {
        this.stage = stage;
        return this;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
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
        Amont amont = (Amont) o;
        if (amont.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), amont.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Amont{" +
            "id=" + getId() +
            ", number=" + getNumber() +
            "}";
    }
}
