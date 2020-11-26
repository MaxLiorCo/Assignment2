package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.services.C3POMicroservice;
import bgu.spl.mics.application.services.HanSoloMicroservice;
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
        C3POMicroservice ms1 = new C3POMicroservice();
        HanSoloMicroservice ms2 = new HanSoloMicroservice();
        MessageBusImpl bus = MessageBusImpl.getBusInstance();
        bus.register(ms1);
        bus.register(ms2);
        bus.subscribeEvent(AttackEvent.class,ms1);
        bus.subscribeEvent(AttackEvent.class,ms2);
        Future<Boolean> f1 = bus.sendEvent(new AttackEvent());

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