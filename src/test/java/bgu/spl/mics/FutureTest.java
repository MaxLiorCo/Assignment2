package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;


import static org.junit.jupiter.api.Assertions.*;


public class FutureTest {

    private Future<String> future;
    Future<Boolean> future2;

    @BeforeEach
    public void setUp(){
        future = new Future<>();

        future2 = new Future<Boolean>();
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * @pre:
     *     isDone = false
     *     result = null
     * @post:
     *     isDone = true
     *     result != null
     */
    @Test
    public void resolve(){
        String str = "someResult";
        future.resolve(str);
        assertTrue(future.isDone());
        assertTrue(str.equals(future.get()));
    }


    @Test
    void get() { //checks both get functions
        future.get();
        long timeout =1000;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        assertNull(future.get(timeout, unit));
        String str = "someResult";
        future.resolve(str);
        assertTrue(str.equals(future.get(timeout, unit)));
        assertTrue(str.equals(future.get()));
    }

    @Test
    void isDone() {
        assertFalse(future.isDone());
        String str = "someResult";
        future.resolve(str);
        assertTrue(str.equals(future.get()));
    }

}
