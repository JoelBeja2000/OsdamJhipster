// src/main/java/com/osdam/osdam/domain/Departamento.java
package com.osdam.osdam.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Departamento.
 */
@Entity
@Table(name = "departamento")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Departamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "departamento")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "inscripciones", "departamento" }, allowSetters = true)
    private Set<Voluntariado> voluntariados = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Departamento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Departamento name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Voluntariado> getVoluntariados() {
        return this.voluntariados;
    }

    public void setVoluntariados(Set<Voluntariado> voluntariados) {
        if (this.voluntariados != null) {
            this.voluntariados.forEach(i -> i.setDepartamento(null));
        }
        if (voluntariados != null) {
            voluntariados.forEach(i -> i.setDepartamento(this));
        }
        this.voluntariados = voluntariados;
    }

    public Departamento voluntariados(Set<Voluntariado> voluntariados) {
        this.setVoluntariados(voluntariados);
        return this;
    }

    public Departamento addVoluntariado(Voluntariado voluntariado) {
        this.voluntariados.add(voluntariado);
        voluntariado.setDepartamento(this);
        return this;
    }

    public Departamento removeVoluntariado(Voluntariado voluntariado) {
        this.voluntariados.remove(voluntariado);
        voluntariado.setDepartamento(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Departamento)) {
            return false;
        }
        return getId() != null && getId().equals(((Departamento) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Departamento{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
