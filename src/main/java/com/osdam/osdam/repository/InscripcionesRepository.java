package com.osdam.osdam.repository;

import com.osdam.osdam.domain.Inscripciones;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Inscripciones entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InscripcionesRepository extends JpaRepository<Inscripciones, Long> {}
