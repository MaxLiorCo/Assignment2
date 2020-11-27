package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.DummyBroadcast;
import bgu.spl.mics.application.services.C3POMicroservice;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import bgu.spl.mics.application.services.LandoMicroservice;
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

    /**
     * Done through {@link #sendEvent()}
     */
    @Test
    void subscribeEvent() {
        //no need to test
    }

    /**
     * Done through {@link #sendBroadcast()} ()}
     */
    @Test
    void subscribeBroadcast() {
        //no need to test
    }

    @Test
    void complete() {
    }

    /**
     * Lando sends broadcast of type TerminateBroadcast.
     * We make sure that ms2 and ms3 that are subscribed to this type of broadcast will indeed
     * receive the broadcast.
     * In addition to {@link #sendBroadcast()}, we check {@link #register()}, {@link #subscribeBroadcast()}.
     * */
    @Test
    void sendBroadcast() {
        TerminateBroadcast b1 = new TerminateBroadcast();
        Message b2 = new DummyBroadcast();
        Message b3 = new DummyBroadcast();
        C3POMicroservice ms1 = new C3POMicroservice();
        HanSoloMicroservice ms2 = new HanSoloMicroservice();
        LandoMicroservice ms3 = new LandoMicroservice(0);
        MessageBusImpl bus = MessageBusImpl.getBusInstance();
        bus.register(ms1);
        bus.register(ms2);
        bus.register(ms3);
        ms1.subscribeBroadcast(b1.getClass(), (c) -> {});
        ms2.subscribeBroadcast(b1.getClass(), (c) -> {});
        ms1.sendBroadcast(b1);
        try {
            b2 = MessageBusImpl.getBusInstance().awaitMessage(ms1);
            b3 = MessageBusImpl.getBusInstance().awaitMessage(ms2);
        }
        catch (InterruptedException e){ }
        assertTrue(b1.equals(b2)); //check if b2 was indeed changed to b1
        assertTrue(b1.equals(b3)); //check if b3 was indeed changed to b1

    }

    /**
     * In this test we check bus.sendEvent(Microservice) method.
     * In addition, we assume here that {@link #subscribeEvent()}, {@link #register()} works properly.
     * Thus, there is no need to test {@link #subscribeEvent()}, {@link #register()} explicitly.
     */

    @Test
    void sendEvent() {
        AttackEvent e1 = new AttackEvent();
        C3POMicroservice ms1 = new C3POMicroservice();
        HanSoloMicroservice ms2 = new HanSoloMicroservice();
        MessageBusImpl bus = MessageBusImpl.getBusInstance();
        bus.register(ms1);
        bus.register(ms2);
        ms2.subscribeEvent(e1.getClass(), (c) -> {});
        ms1.sendEvent(e1);
        Message e2 = new DummyAttack();
        try {
            e2 = MessageBusImpl.getBusInstance().awaitMessage(ms2);
        }
        catch (InterruptedException e){ }
        assertTrue(e1.equals(e2)); //check if e2 was indeed changed to e1
    }

    /**
     * Tested in {@link #sendEvent()}
     */
    @Test
    void register() {
        //no need to test
    }

    @Test
    void unregister() {
        //can't check this method cause can not access map (it is private)
    }

    @Test
    void awaitMessage() {
        C3POMicroservice ms = new C3POMicroservice();
        MessageBusImpl bus = MessageBusImpl.getBusInstance();
        bus.register(ms);
        ms.subscribeEvent(new AttackEvent().getClass(), (c) -> {});
        Future<Boolean> f1 = bus.sendEvent(new AttackEvent());
        try{
            Thread.sleep(2000);
        }
        catch(InterruptedException e){ }
        assertTrue(f1.get());
    }
}