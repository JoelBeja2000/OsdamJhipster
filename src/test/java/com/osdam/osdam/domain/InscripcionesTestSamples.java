package com.osdam.osdam.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InscripcionesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Inscripciones getInscripcionesSample1() {
        return new Inscripciones().id(1L).name("name1");
    }

    public static Inscripciones getInscripcionesSample2() {
        return new Inscripciones().id(2L).name("name2");
    }

    public static Inscripciones getInscripcionesRandomSampleGenerator() {
        return new Inscripciones().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
