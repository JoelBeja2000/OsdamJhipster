package com.osdam.osdam.service;

import com.osdam.osdam.domain.Voluntariado;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.osdam.osdam.domain.Voluntariado}.
 */
public interface VoluntariadoService {
    /**
     * Save a voluntariado.
     *
     * @param voluntariado the entity to save.
     * @return the persisted entity.
     */
    Voluntariado save(Voluntariado voluntariado);

    /**
     * Updates a voluntariado.
     *
     * @param voluntariado the entity to update.
     * @return the persisted entity.
     */
    Voluntariado update(Voluntariado voluntariado);

    /**
     * Partially updates a voluntariado.
     *
     * @param voluntariado the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Voluntariado> partialUpdate(Voluntariado voluntariado);

    /**
     * Get all the voluntariados.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Voluntariado> findAll(Pageable pageable);

    /**
     * Get the "id" voluntariado.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Voluntariado> findOne(Long id);

    /**
     * Delete the "id" voluntariado.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
