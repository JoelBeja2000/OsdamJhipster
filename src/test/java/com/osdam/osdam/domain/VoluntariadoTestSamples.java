package com.osdam.osdam.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VoluntariadoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Voluntariado getVoluntariadoSample1() {
        return new Voluntariado().id(1L).name("name1");
    }

    public static Voluntariado getVoluntariadoSample2() {
        return new Voluntariado().id(2L).name("name2");
    }

    public static Voluntariado getVoluntariadoRandomSampleGenerator() {
        return new Voluntariado().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
