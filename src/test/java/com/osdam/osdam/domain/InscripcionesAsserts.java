package com.osdam.osdam.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class InscripcionesAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInscripcionesAllPropertiesEquals(Inscripciones expected, Inscripciones actual) {
        assertInscripcionesAutoGeneratedPropertiesEquals(expected, actual);
        assertInscripcionesAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInscripcionesAllUpdatablePropertiesEquals(Inscripciones expected, Inscripciones actual) {
        assertInscripcionesUpdatableFieldsEquals(expected, actual);
        assertInscripcionesUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInscripcionesAutoGeneratedPropertiesEquals(Inscripciones expected, Inscripciones actual) {
        assertThat(expected)
            .as("Verify Inscripciones auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInscripcionesUpdatableFieldsEquals(Inscripciones expected, Inscripciones actual) {
        assertThat(expected)
            .as("Verify Inscripciones relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInscripcionesUpdatableRelationshipsEquals(Inscripciones expected, Inscripciones actual) {
        assertThat(expected)
            .as("Verify Inscripciones relationships")
            .satisfies(e -> assertThat(e.getVoluntariado()).as("check voluntariado").isEqualTo(actual.getVoluntariado()));
    }
}
