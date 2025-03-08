package com.osdam.osdam.web.rest;

import static com.osdam.osdam.domain.InscripcionesAsserts.*;
import static com.osdam.osdam.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.osdam.osdam.IntegrationTest;
import com.osdam.osdam.domain.Inscripciones;
import com.osdam.osdam.repository.InscripcionesRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link InscripcionesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InscripcionesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/inscripciones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InscripcionesRepository inscripcionesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInscripcionesMockMvc;

    private Inscripciones inscripciones;

    private Inscripciones insertedInscripciones;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inscripciones createEntity() {
        return new Inscripciones().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inscripciones createUpdatedEntity() {
        return new Inscripciones().name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        inscripciones = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedInscripciones != null) {
            inscripcionesRepository.delete(insertedInscripciones);
            insertedInscripciones = null;
        }
    }

    @Test
    @Transactional
    void createInscripciones() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Inscripciones
        var returnedInscripciones = om.readValue(
            restInscripcionesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inscripciones)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Inscripciones.class
        );

        // Validate the Inscripciones in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInscripcionesUpdatableFieldsEquals(returnedInscripciones, getPersistedInscripciones(returnedInscripciones));

        insertedInscripciones = returnedInscripciones;
    }

    @Test
    @Transactional
    void createInscripcionesWithExistingId() throws Exception {
        // Create the Inscripciones with an existing ID
        inscripciones.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInscripcionesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inscripciones)))
            .andExpect(status().isBadRequest());

        // Validate the Inscripciones in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInscripciones() throws Exception {
        // Initialize the database
        insertedInscripciones = inscripcionesRepository.saveAndFlush(inscripciones);

        // Get all the inscripcionesList
        restInscripcionesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscripciones.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getInscripciones() throws Exception {
        // Initialize the database
        insertedInscripciones = inscripcionesRepository.saveAndFlush(inscripciones);

        // Get the inscripciones
        restInscripcionesMockMvc
            .perform(get(ENTITY_API_URL_ID, inscripciones.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inscripciones.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingInscripciones() throws Exception {
        // Get the inscripciones
        restInscripcionesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInscripciones() throws Exception {
        // Initialize the database
        insertedInscripciones = inscripcionesRepository.saveAndFlush(inscripciones);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inscripciones
        Inscripciones updatedInscripciones = inscripcionesRepository.findById(inscripciones.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInscripciones are not directly saved in db
        em.detach(updatedInscripciones);
        updatedInscripciones.name(UPDATED_NAME);

        restInscripcionesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInscripciones.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInscripciones))
            )
            .andExpect(status().isOk());

        // Validate the Inscripciones in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInscripcionesToMatchAllProperties(updatedInscripciones);
    }

    @Test
    @Transactional
    void putNonExistingInscripciones() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscripciones.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscripcionesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inscripciones.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inscripciones))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscripciones in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInscripciones() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscripciones.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscripcionesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inscripciones))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscripciones in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInscripciones() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscripciones.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscripcionesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inscripciones)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inscripciones in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInscripcionesWithPatch() throws Exception {
        // Initialize the database
        insertedInscripciones = inscripcionesRepository.saveAndFlush(inscripciones);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inscripciones using partial update
        Inscripciones partialUpdatedInscripciones = new Inscripciones();
        partialUpdatedInscripciones.setId(inscripciones.getId());

        restInscripcionesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscripciones.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInscripciones))
            )
            .andExpect(status().isOk());

        // Validate the Inscripciones in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInscripcionesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInscripciones, inscripciones),
            getPersistedInscripciones(inscripciones)
        );
    }

    @Test
    @Transactional
    void fullUpdateInscripcionesWithPatch() throws Exception {
        // Initialize the database
        insertedInscripciones = inscripcionesRepository.saveAndFlush(inscripciones);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inscripciones using partial update
        Inscripciones partialUpdatedInscripciones = new Inscripciones();
        partialUpdatedInscripciones.setId(inscripciones.getId());

        partialUpdatedInscripciones.name(UPDATED_NAME);

        restInscripcionesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscripciones.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInscripciones))
            )
            .andExpect(status().isOk());

        // Validate the Inscripciones in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInscripcionesUpdatableFieldsEquals(partialUpdatedInscripciones, getPersistedInscripciones(partialUpdatedInscripciones));
    }

    @Test
    @Transactional
    void patchNonExistingInscripciones() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscripciones.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscripcionesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inscripciones.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inscripciones))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscripciones in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInscripciones() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscripciones.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscripcionesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inscripciones))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscripciones in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInscripciones() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscripciones.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscripcionesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(inscripciones)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inscripciones in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInscripciones() throws Exception {
        // Initialize the database
        insertedInscripciones = inscripcionesRepository.saveAndFlush(inscripciones);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the inscripciones
        restInscripcionesMockMvc
            .perform(delete(ENTITY_API_URL_ID, inscripciones.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return inscripcionesRepository.count();
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

    protected Inscripciones getPersistedInscripciones(Inscripciones inscripciones) {
        return inscripcionesRepository.findById(inscripciones.getId()).orElseThrow();
    }

    protected void assertPersistedInscripcionesToMatchAllProperties(Inscripciones expectedInscripciones) {
        assertInscripcionesAllPropertiesEquals(expectedInscripciones, getPersistedInscripciones(expectedInscripciones));
    }

    protected void assertPersistedInscripcionesToMatchUpdatableProperties(Inscripciones expectedInscripciones) {
        assertInscripcionesAllUpdatablePropertiesEquals(expectedInscripciones, getPersistedInscripciones(expectedInscripciones));
    }
}
