package com.osdam.osdam.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Voluntariado.
 */
@Entity
@Table(name = "voluntariado")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Voluntariado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "fecha_inicio")
    private Instant fechaInicio;

    @Column(name = "fecha_fin")
    private Instant fechaFin;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "voluntariado")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "voluntariado" }, allowSetters = true)
    private Set<Inscripciones> inscripciones = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "voluntariados" }, allowSetters = true)
    private Departamento departamento;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Voluntariado id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Voluntariado name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getFechaInicio() {
        return this.fechaInicio;
    }

    public Voluntariado fechaInicio(Instant fechaInicio) {
        this.setFechaInicio(fechaInicio);
        return this;
    }

    public void setFechaInicio(Instant fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Instant getFechaFin() {
        return this.fechaFin;
    }

    public Voluntariado fechaFin(Instant fechaFin) {
        this.setFechaFin(fechaFin);
        return this;
    }

    public void setFechaFin(Instant fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Set<Inscripciones> getInscripciones() {
        return this.inscripciones;
    }

    public void setInscripciones(Set<Inscripciones> inscripciones) {
        if (this.inscripciones != null) {
            this.inscripciones.forEach(i -> i.setVoluntariado(null));
        }
        if (inscripciones != null) {
            inscripciones.forEach(i -> i.setVoluntariado(this));
        }
        this.inscripciones = inscripciones;
    }

    public Voluntariado inscripciones(Set<Inscripciones> inscripciones) {
        this.setInscripciones(inscripciones);
        return this;
    }

    public Voluntariado addInscripciones(Inscripciones inscripciones) {
        this.inscripciones.add(inscripciones);
        inscripciones.setVoluntariado(this);
        return this;
    }

    public Voluntariado removeInscripciones(Inscripciones inscripciones) {
        this.inscripciones.remove(inscripciones);
        inscripciones.setVoluntariado(null);
        return this;
    }

    public Departamento getDepartamento() {
        return this.departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Voluntariado departamento(Departamento departamento) {
        this.setDepartamento(departamento);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Voluntariado)) {
            return false;
        }
        return getId() != null && getId().equals(((Voluntariado) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Voluntariado{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            "}";
    }
}
