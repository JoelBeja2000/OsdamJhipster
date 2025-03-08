package com.osdam.osdam.web.rest;

import static com.osdam.osdam.domain.VoluntariadoAsserts.*;
import static com.osdam.osdam.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.osdam.osdam.IntegrationTest;
import com.osdam.osdam.domain.Voluntariado;
import com.osdam.osdam.repository.VoluntariadoRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VoluntariadoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VoluntariadoResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/voluntariados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VoluntariadoRepository voluntariadoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVoluntariadoMockMvc;

    private Voluntariado voluntariado;

    private Voluntariado insertedVoluntariado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Voluntariado createEntity() {
        return new Voluntariado().name(DEFAULT_NAME).fechaInicio(DEFAULT_FECHA_INICIO).fechaFin(DEFAULT_FECHA_FIN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Voluntariado createUpdatedEntity() {
        return new Voluntariado().name(UPDATED_NAME).fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN);
    }

    @BeforeEach
    public void initTest() {
        voluntariado = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedVoluntariado != null) {
            voluntariadoRepository.delete(insertedVoluntariado);
            insertedVoluntariado = null;
        }
    }

    @Test
    @Transactional
    void createVoluntariado() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Voluntariado
        var returnedVoluntariado = om.readValue(
            restVoluntariadoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(voluntariado)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Voluntariado.class
        );

        // Validate the Voluntariado in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVoluntariadoUpdatableFieldsEquals(returnedVoluntariado, getPersistedVoluntariado(returnedVoluntariado));

        insertedVoluntariado = returnedVoluntariado;
    }

    @Test
    @Transactional
    void createVoluntariadoWithExistingId() throws Exception {
        // Create the Voluntariado with an existing ID
        voluntariado.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoluntariadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(voluntariado)))
            .andExpect(status().isBadRequest());

        // Validate the Voluntariado in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVoluntariados() throws Exception {
        // Initialize the database
        insertedVoluntariado = voluntariadoRepository.saveAndFlush(voluntariado);

        // Get all the voluntariadoList
        restVoluntariadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voluntariado.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())));
    }

    @Test
    @Transactional
    void getVoluntariado() throws Exception {
        // Initialize the database
        insertedVoluntariado = voluntariadoRepository.saveAndFlush(voluntariado);

        // Get the voluntariado
        restVoluntariadoMockMvc
            .perform(get(ENTITY_API_URL_ID, voluntariado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(voluntariado.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVoluntariado() throws Exception {
        // Get the voluntariado
        restVoluntariadoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVoluntariado() throws Exception {
        // Initialize the database
        insertedVoluntariado = voluntariadoRepository.saveAndFlush(voluntariado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the voluntariado
        Voluntariado updatedVoluntariado = voluntariadoRepository.findById(voluntariado.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVoluntariado are not directly saved in db
        em.detach(updatedVoluntariado);
        updatedVoluntariado.name(UPDATED_NAME).fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN);

        restVoluntariadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVoluntariado.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedVoluntariado))
            )
            .andExpect(status().isOk());

        // Validate the Voluntariado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVoluntariadoToMatchAllProperties(updatedVoluntariado);
    }

    @Test
    @Transactional
    void putNonExistingVoluntariado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        voluntariado.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoluntariadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, voluntariado.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(voluntariado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voluntariado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVoluntariado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        voluntariado.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoluntariadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(voluntariado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voluntariado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVoluntariado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        voluntariado.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoluntariadoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(voluntariado)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Voluntariado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVoluntariadoWithPatch() throws Exception {
        // Initialize the database
        insertedVoluntariado = voluntariadoRepository.saveAndFlush(voluntariado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the voluntariado using partial update
        Voluntariado partialUpdatedVoluntariado = new Voluntariado();
        partialUpdatedVoluntariado.setId(voluntariado.getId());

        partialUpdatedVoluntariado.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN);

        restVoluntariadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoluntariado.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVoluntariado))
            )
            .andExpect(status().isOk());

        // Validate the Voluntariado in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVoluntariadoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVoluntariado, voluntariado),
            getPersistedVoluntariado(voluntariado)
        );
    }

    @Test
    @Transactional
    void fullUpdateVoluntariadoWithPatch() throws Exception {
        // Initialize the database
        insertedVoluntariado = voluntariadoRepository.saveAndFlush(voluntariado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the voluntariado using partial update
        Voluntariado partialUpdatedVoluntariado = new Voluntariado();
        partialUpdatedVoluntariado.setId(voluntariado.getId());

        partialUpdatedVoluntariado.name(UPDATED_NAME).fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN);

        restVoluntariadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoluntariado.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVoluntariado))
            )
            .andExpect(status().isOk());

        // Validate the Voluntariado in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVoluntariadoUpdatableFieldsEquals(partialUpdatedVoluntariado, getPersistedVoluntariado(partialUpdatedVoluntariado));
    }

    @Test
    @Transactional
    void patchNonExistingVoluntariado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        voluntariado.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoluntariadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, voluntariado.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(voluntariado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voluntariado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVoluntariado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        voluntariado.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoluntariadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(voluntariado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voluntariado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVoluntariado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        voluntariado.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoluntariadoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(voluntariado)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Voluntariado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVoluntariado() throws Exception {
        // Initialize the database
        insertedVoluntariado = voluntariadoRepository.saveAndFlush(voluntariado);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the voluntariado
        restVoluntariadoMockMvc
            .perform(delete(ENTITY_API_URL_ID, voluntariado.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return voluntariadoRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Voluntariado getPersistedVoluntariado(Voluntariado voluntariado) {
        return voluntariadoRepository.findById(voluntariado.getId()).orElseThrow();
    }

    protected void assertPersistedVoluntariadoToMatchAllProperties(Voluntariado expectedVoluntariado) {
        assertVoluntariadoAllPropertiesEquals(expectedVoluntariado, getPersistedVoluntariado(expectedVoluntariado));
    }

    protected void assertPersistedVoluntariadoToMatchUpdatableProperties(Voluntariado expectedVoluntariado) {
        assertVoluntariadoAllUpdatablePropertiesEquals(expectedVoluntariado, getPersistedVoluntariado(expectedVoluntariado));
    }
}
