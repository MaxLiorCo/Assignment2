package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.services.C3POMicroservice;
import bgu.spl.mics.application.services.LeiaMicroservice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void subscribeEvent() {
        C3POMicroservice m = new C3POMicroservice();
        MessageBusImpl bus = MessageBusImpl.getBusInstance();
        bus.register(m);
       // bus.subscribeEvent(AttackEvent,m);
    }

    @Test
    void subscribeBroadcast() {
    }

    @Test
    void complete() {
    }

    @Test
    void sendBroadcast() {


    }

    @Test
    void sendEvent() {
    }

    @Test
    void register() {
        //can't check this method cause can not access map (it is private)
    }

    @Test
    void unregister() {
        //can't check this method cause can not access map (it is private)
    }

    @Test
    void awaitMessage() {
    }
}