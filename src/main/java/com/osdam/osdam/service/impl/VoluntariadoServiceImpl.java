package com.osdam.osdam.service.impl;

import com.osdam.osdam.domain.Voluntariado;
import com.osdam.osdam.repository.VoluntariadoRepository;
import com.osdam.osdam.service.VoluntariadoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.osdam.osdam.domain.Voluntariado}.
 */
@Service
@Transactional
public class VoluntariadoServiceImpl implements VoluntariadoService {

    private static final Logger LOG = LoggerFactory.getLogger(VoluntariadoServiceImpl.class);

    private final VoluntariadoRepository voluntariadoRepository;

    public VoluntariadoServiceImpl(VoluntariadoRepository voluntariadoRepository) {
        this.voluntariadoRepository = voluntariadoRepository;
    }

    @Override
    public Voluntariado save(Voluntariado voluntariado) {
        LOG.debug("Request to save Voluntariado : {}", voluntariado);
        return voluntariadoRepository.save(voluntariado);
    }

    @Override
    public Voluntariado update(Voluntariado voluntariado) {
        LOG.debug("Request to update Voluntariado : {}", voluntariado);
        return voluntariadoRepository.save(voluntariado);
    }

    @Override
    public Optional<Voluntariado> partialUpdate(Voluntariado voluntariado) {
        LOG.debug("Request to partially update Voluntariado : {}", voluntariado);

        return voluntariadoRepository
            .findById(voluntariado.getId())
            .map(existingVoluntariado -> {
                if (voluntariado.getName() != null) {
                    existingVoluntariado.setName(voluntariado.getName());
                }
                if (voluntariado.getFechaInicio() != null) {
                    existingVoluntariado.setFechaInicio(voluntariado.getFechaInicio());
                }
                if (voluntariado.getFechaFin() != null) {
                    existingVoluntariado.setFechaFin(voluntariado.getFechaFin());
                }

                return existingVoluntariado;
            })
            .map(voluntariadoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Voluntariado> findAll(Pageable pageable) {
        LOG.debug("Request to get all Voluntariados");
        return voluntariadoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Voluntariado> findOne(Long id) {
        LOG.debug("Request to get Voluntariado : {}", id);
        return voluntariadoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Voluntariado : {}", id);
        voluntariadoRepository.deleteById(id);
    }
}
