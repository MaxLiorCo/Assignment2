package bgu.spl.mics.application.passiveObjects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EwokTest {
    Ewok e;

    @BeforeEach
    void setUp() {
        e = new Ewok(123);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void acquire() {
        e.acquire();
        assertFalse(e.available);
    }

    @Test
    void release() {
        e.acquire();
        assertFalse(e.available);
        e.release();
        assertTrue(e.available);
    }
}