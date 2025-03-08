package com.osdam.osdam.domain;

import static com.osdam.osdam.domain.InscripcionesTestSamples.*;
import static com.osdam.osdam.domain.VoluntariadoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.osdam.osdam.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InscripcionesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inscripciones.class);
        Inscripciones inscripciones1 = getInscripcionesSample1();
        Inscripciones inscripciones2 = new Inscripciones();
        assertThat(inscripciones1).isNotEqualTo(inscripciones2);

        inscripciones2.setId(inscripciones1.getId());
        assertThat(inscripciones1).isEqualTo(inscripciones2);

        inscripciones2 = getInscripcionesSample2();
        assertThat(inscripciones1).isNotEqualTo(inscripciones2);
    }

    @Test
    void voluntariadoTest() {
        Inscripciones inscripciones = getInscripcionesRandomSampleGenerator();
        Voluntariado voluntariadoBack = getVoluntariadoRandomSampleGenerator();

        inscripciones.setVoluntariado(voluntariadoBack);
        assertThat(inscripciones.getVoluntariado()).isEqualTo(voluntariadoBack);

        inscripciones.voluntariado(null);
        assertThat(inscripciones.getVoluntariado()).isNull();
    }
}
