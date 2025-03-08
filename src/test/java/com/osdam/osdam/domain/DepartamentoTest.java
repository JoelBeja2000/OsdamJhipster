package com.osdam.osdam.domain;

import static com.osdam.osdam.domain.DepartamentoTestSamples.*;
import static com.osdam.osdam.domain.VoluntariadoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.osdam.osdam.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DepartamentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Departamento.class);
        Departamento departamento1 = getDepartamentoSample1();
        Departamento departamento2 = new Departamento();
        assertThat(departamento1).isNotEqualTo(departamento2);

        departamento2.setId(departamento1.getId());
        assertThat(departamento1).isEqualTo(departamento2);

        departamento2 = getDepartamentoSample2();
        assertThat(departamento1).isNotEqualTo(departamento2);
    }

    @Test
    void voluntariadoTest() {
        Departamento departamento = getDepartamentoRandomSampleGenerator();
        Voluntariado voluntariadoBack = getVoluntariadoRandomSampleGenerator();

        departamento.addVoluntariado(voluntariadoBack);
        assertThat(departamento.getVoluntariados()).containsOnly(voluntariadoBack);
        assertThat(voluntariadoBack.getDepartamento()).isEqualTo(departamento);

        departamento.removeVoluntariado(voluntariadoBack);
        assertThat(departamento.getVoluntariados()).doesNotContain(voluntariadoBack);
        assertThat(voluntariadoBack.getDepartamento()).isNull();

        departamento.voluntariados(new HashSet<>(Set.of(voluntariadoBack)));
        assertThat(departamento.getVoluntariados()).containsOnly(voluntariadoBack);
        assertThat(voluntariadoBack.getDepartamento()).isEqualTo(departamento);

        departamento.setVoluntariados(new HashSet<>());
        assertThat(departamento.getVoluntariados()).doesNotContain(voluntariadoBack);
        assertThat(voluntariadoBack.getDepartamento()).isNull();
    }
}
