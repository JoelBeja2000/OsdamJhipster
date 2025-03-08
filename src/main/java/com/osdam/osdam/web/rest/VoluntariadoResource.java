package com.osdam.osdam.web.rest;

import com.osdam.osdam.domain.Voluntariado;
import com.osdam.osdam.repository.VoluntariadoRepository;
import com.osdam.osdam.service.VoluntariadoService;
import com.osdam.osdam.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.osdam.osdam.domain.Voluntariado}.
 */
@RestController
@RequestMapping("/api/voluntariados")
public class VoluntariadoResource {

    private static final Logger LOG = LoggerFactory.getLogger(VoluntariadoResource.class);

    private static final String ENTITY_NAME = "voluntariado";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VoluntariadoService voluntariadoService;

    private final VoluntariadoRepository voluntariadoRepository;

    public VoluntariadoResource(VoluntariadoService voluntariadoService, VoluntariadoRepository voluntariadoRepository) {
        this.voluntariadoService = voluntariadoService;
        this.voluntariadoRepository = voluntariadoRepository;
    }

    /**
     * {@code POST  /voluntariados} : Create a new voluntariado.
     *
     * @param voluntariado the voluntariado to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new voluntariado, or with status {@code 400 (Bad Request)} if the voluntariado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Voluntariado> createVoluntariado(@RequestBody Voluntariado voluntariado) throws URISyntaxException {
        LOG.debug("REST request to save Voluntariado : {}", voluntariado);
        if (voluntariado.getId() != null) {
            throw new BadRequestAlertException("A new voluntariado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        voluntariado = voluntariadoService.save(voluntariado);
        return ResponseEntity.created(new URI("/api/voluntariados/" + voluntariado.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, voluntariado.getId().toString()))
            .body(voluntariado);
    }

    /**
     * {@code PUT  /voluntariados/:id} : Updates an existing voluntariado.
     *
     * @param id the id of the voluntariado to save.
     * @param voluntariado the voluntariado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voluntariado,
     * or with status {@code 400 (Bad Request)} if the voluntariado is not valid,
     * or with status {@code 500 (Internal Server Error)} if the voluntariado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Voluntariado> updateVoluntariado(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Voluntariado voluntariado
    ) throws URISyntaxException {
        LOG.debug("REST request to update Voluntariado : {}, {}", id, voluntariado);
        if (voluntariado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voluntariado.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voluntariadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        voluntariado = voluntariadoService.update(voluntariado);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, voluntariado.getId().toString()))
            .body(voluntariado);
    }

    /**
     * {@code PATCH  /voluntariados/:id} : Partial updates given fields of an existing voluntariado, field will ignore if it is null
     *
     * @param id the id of the voluntariado to save.
     * @param voluntariado the voluntariado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voluntariado,
     * or with status {@code 400 (Bad Request)} if the voluntariado is not valid,
     * or with status {@code 404 (Not Found)} if the voluntariado is not found,
     * or with status {@code 500 (Internal Server Error)} if the voluntariado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Voluntariado> partialUpdateVoluntariado(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Voluntariado voluntariado
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Voluntariado partially : {}, {}", id, voluntariado);
        if (voluntariado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voluntariado.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voluntariadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Voluntariado> result = voluntariadoService.partialUpdate(voluntariado);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, voluntariado.getId().toString())
        );
    }

    /**
     * {@code GET  /voluntariados} : get all the voluntariados.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of voluntariados in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Voluntariado>> getAllVoluntariados(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Voluntariados");
        Page<Voluntariado> page = voluntariadoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /voluntariados/:id} : get the "id" voluntariado.
     *
     * @param id the id of the voluntariado to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the voluntariado, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Voluntariado> getVoluntariado(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Voluntariado : {}", id);
        Optional<Voluntariado> voluntariado = voluntariadoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(voluntariado);
    }

    /**
     * {@code DELETE  /voluntariados/:id} : delete the "id" voluntariado.
     *
     * @param id the id of the voluntariado to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoluntariado(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Voluntariado : {}", id);
        voluntariadoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
