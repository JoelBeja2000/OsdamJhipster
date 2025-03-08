package com.osdam.osdam.domain;

import static com.osdam.osdam.domain.DepartamentoTestSamples.*;
import static com.osdam.osdam.domain.InscripcionesTestSamples.*;
import static com.osdam.osdam.domain.VoluntariadoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.osdam.osdam.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VoluntariadoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Voluntariado.class);
        Voluntariado voluntariado1 = getVoluntariadoSample1();
        Voluntariado voluntariado2 = new Voluntariado();
        assertThat(voluntariado1).isNotEqualTo(voluntariado2);

        voluntariado2.setId(voluntariado1.getId());
        assertThat(voluntariado1).isEqualTo(voluntariado2);

        voluntariado2 = getVoluntariadoSample2();
        assertThat(voluntariado1).isNotEqualTo(voluntariado2);
    }

    @Test
    void inscripcionesTest() {
        Voluntariado voluntariado = getVoluntariadoRandomSampleGenerator();
        Inscripciones inscripcionesBack = getInscripcionesRandomSampleGenerator();

        voluntariado.addInscripciones(inscripcionesBack);
        assertThat(voluntariado.getInscripciones()).containsOnly(inscripcionesBack);
        assertThat(inscripcionesBack.getVoluntariado()).isEqualTo(voluntariado);

        voluntariado.removeInscripciones(inscripcionesBack);
        assertThat(voluntariado.getInscripciones()).doesNotContain(inscripcionesBack);
        assertThat(inscripcionesBack.getVoluntariado()).isNull();

        voluntariado.inscripciones(new HashSet<>(Set.of(inscripcionesBack)));
        assertThat(voluntariado.getInscripciones()).containsOnly(inscripcionesBack);
        assertThat(inscripcionesBack.getVoluntariado()).isEqualTo(voluntariado);

        voluntariado.setInscripciones(new HashSet<>());
        assertThat(voluntariado.getInscripciones()).doesNotContain(inscripcionesBack);
        assertThat(inscripcionesBack.getVoluntariado()).isNull();
    }

    @Test
    void departamentoTest() {
        Voluntariado voluntariado = getVoluntariadoRandomSampleGenerator();
        Departamento departamentoBack = getDepartamentoRandomSampleGenerator();

        voluntariado.setDepartamento(departamentoBack);
        assertThat(voluntariado.getDepartamento()).isEqualTo(departamentoBack);

        voluntariado.departamento(null);
        assertThat(voluntariado.getDepartamento()).isNull();
    }
}
