package com.osdam.osdam.service.impl;

import com.osdam.osdam.domain.Inscripciones;
import com.osdam.osdam.repository.InscripcionesRepository;
import com.osdam.osdam.service.InscripcionesService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.osdam.osdam.domain.Inscripciones}.
 */
@Service
@Transactional
public class InscripcionesServiceImpl implements InscripcionesService {

    private static final Logger LOG = LoggerFactory.getLogger(InscripcionesServiceImpl.class);

    private final InscripcionesRepository inscripcionesRepository;

    public InscripcionesServiceImpl(InscripcionesRepository inscripcionesRepository) {
        this.inscripcionesRepository = inscripcionesRepository;
    }

    @Override
    public Inscripciones save(Inscripciones inscripciones) {
        LOG.debug("Request to save Inscripciones : {}", inscripciones);
        return inscripcionesRepository.save(inscripciones);
    }

    @Override
    public Inscripciones update(Inscripciones inscripciones) {
        LOG.debug("Request to update Inscripciones : {}", inscripciones);
        return inscripcionesRepository.save(inscripciones);
    }

    @Override
    public Optional<Inscripciones> partialUpdate(Inscripciones inscripciones) {
        LOG.debug("Request to partially update Inscripciones : {}", inscripciones);

        return inscripcionesRepository
            .findById(inscripciones.getId())
            .map(existingInscripciones -> {
                if (inscripciones.getName() != null) {
                    existingInscripciones.setName(inscripciones.getName());
                }

                return existingInscripciones;
            })
            .map(inscripcionesRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Inscripciones> findAll(Pageable pageable) {
        LOG.debug("Request to get all Inscripciones");
        return inscripcionesRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Inscripciones> findOne(Long id) {
        LOG.debug("Request to get Inscripciones : {}", id);
        return inscripcionesRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Inscripciones : {}", id);
        inscripcionesRepository.deleteById(id);
    }
}
