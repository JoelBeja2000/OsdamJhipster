package com.osdam.osdam.repository;

import com.osdam.osdam.domain.Voluntariado;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Voluntariado entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoluntariadoRepository extends JpaRepository<Voluntariado, Long> {}
