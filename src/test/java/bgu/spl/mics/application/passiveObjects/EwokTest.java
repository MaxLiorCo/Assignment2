package bgu.spl.mics.application.passiveObjects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EwokTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void acquire() {
        Ewok e = new Ewok(123);
        e.acquire();
        assertFalse(e.available);
    }

    @Test
    void release() {
        Ewok e = new Ewok(123);
        e.acquire();
        assertFalse(e.available);
        e.release();
        assertTrue(e.available);
    }
}