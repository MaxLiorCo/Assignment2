package bgu.spl.mics.application.services;


import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Ewok;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {

    public HanSoloMicroservice() {
        super("Han");
    }


    @Override
    protected void initialize() {

        subscribeEvent(AttackEvent.class,
                (AttackEvent att) -> {
                    ArrayList<Ewok> ewoks = Ewoks.getInstance();
                    List<Integer> serials = att.getSerials();
                    for(Integer i : serials) {
                        if(ewoks.get(i).isAvailable()) {
                            //TODO
                        }
                    }
                    try {
                        Thread.sleep(att.getDuration());
                    } catch (InterruptedException e) {
                    }
                    MessageBusImpl bus = MessageBusImpl.getBusInstance();
                    bus.complete(att, true); // finished attack
                });
    }
}