package pl.przepisnik.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Stage.
 */
@Entity
@Table(name = "stage")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "stage")
public class Stage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "text")
    private String text;

    @OneToMany(mappedBy = "stage")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Amont> amonts = new HashSet<>();

    @ManyToOne
    private Recipe recipe;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Stage title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public Stage text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<Amont> getAmonts() {
        return amonts;
    }

    public Stage amonts(Set<Amont> amonts) {
        this.amonts = amonts;
        return this;
    }

    public Stage addAmont(Amont amont) {
        this.amonts.add(amont);
        amont.setStage(this);
        return this;
    }

    public Stage removeAmont(Amont amont) {
        this.amonts.remove(amont);
        amont.setStage(null);
        return this;
    }

    public void setAmonts(Set<Amont> amonts) {
        this.amonts = amonts;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public Stage recipe(Recipe recipe) {
        this.recipe = recipe;
        return this;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
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
        Stage stage = (Stage) o;
        if (stage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Stage{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", text='" + getText() + "'" +
            "}";
    }
}
