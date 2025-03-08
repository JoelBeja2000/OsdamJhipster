package com.osdam.osdam.service.impl;

import com.osdam.osdam.domain.Departamento;
import com.osdam.osdam.repository.DepartamentoRepository;
import com.osdam.osdam.service.DepartamentoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.osdam.osdam.domain.Departamento}.
 */
@Service
@Transactional
public class DepartamentoServiceImpl implements DepartamentoService {

    private static final Logger LOG = LoggerFactory.getLogger(DepartamentoServiceImpl.class);

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoServiceImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public Departamento save(Departamento departamento) {
        LOG.debug("Request to save Departamento : {}", departamento);
        return departamentoRepository.save(departamento);
    }

    @Override
    public Departamento update(Departamento departamento) {
        LOG.debug("Request to update Departamento : {}", departamento);
        return departamentoRepository.save(departamento);
    }

    @Override
    public Optional<Departamento> partialUpdate(Departamento departamento) {
        LOG.debug("Request to partially update Departamento : {}", departamento);

        return departamentoRepository
            .findById(departamento.getId())
            .map(existingDepartamento -> {
                if (departamento.getName() != null) {
                    existingDepartamento.setName(departamento.getName());
                }

                return existingDepartamento;
            })
            .map(departamentoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Departamento> findAll(Pageable pageable) {
        LOG.debug("Request to get all Departamentos");
        return departamentoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Departamento> findOne(Long id) {
        LOG.debug("Request to get Departamento : {}", id);
        return departamentoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Departamento : {}", id);
        departamentoRepository.deleteById(id);
    }
}
