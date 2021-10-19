package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.C3POMicroservice;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import bgu.spl.mics.application.services.LandoMicroservice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {

    MessageBusImpl bus;
    LinkedList<MicroService> registered;

    @BeforeEach
    void setUp() {
        bus = MessageBusImpl.getBusInstance();
        registered = new LinkedList<>();
    }

    @AfterEach
    void tearDown() {
        clear();
    }

    private void clear() {
        for (MicroService ms : registered)
            bus.unregister(ms);
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
        //no need to test
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
        bus.register(ms1);
        bus.register(ms2);
        bus.register(ms3);
        registered.add(ms1);
        registered.add(ms2);
        registered.add(ms3);
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
        AttackEvent e1 = new AttackEvent(new Attack(new ArrayList<Integer>(),10));
        C3POMicroservice ms1 = new C3POMicroservice();
        HanSoloMicroservice ms2 = new HanSoloMicroservice();
        bus.register(ms1);
        bus.register(ms2);
        registered.add(ms1);
        registered.add(ms2);
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
        Message message;
        C3POMicroservice ms = new C3POMicroservice();
        bus.register(ms);
        registered.add(ms);
        Attack a = new Attack(new ArrayList<>(), 10);
        AttackEvent aE = new AttackEvent(a);
        ms.subscribeEvent(AttackEvent.class,
                (AttackEvent att) -> {
                    bus.complete(att, true); // finished attack
                });

        Future<Boolean> f1 = bus.sendEvent(aE);
        try {
            message = bus.awaitMessage(ms);
            Callback c = ((att) -> {
                bus.complete(aE, true); // finished attack
            });

            c.call(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(f1.get());
    }
}