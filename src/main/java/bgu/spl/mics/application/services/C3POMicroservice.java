package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.FinishedAttacksBroadcast;
import bgu.spl.mics.application.messages.IsReadyBroadcast;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewok;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.ArrayList;
import java.util.List;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {
	
    public C3POMicroservice() {
        super("C3PO");
    }

    @Override
    protected void initialize() {

        //---subscribe to AttackEvents
        subscribeEvent(AttackEvent.class,
                (AttackEvent att) -> {
                    ArrayList<Ewok> ewoks = Ewoks.getInstance();
                    List<Integer> serials = att.getSerials();
                    for(Integer i : serials) { //example[1,2]
                        ewoks.get(i).acquire();
                    }
                    try {
                        Thread.sleep(att.getDuration());
                    }
                    catch (InterruptedException e) {
                        System.out.println("Someone interrupted C3PO in his sleep, not supposed to happen");
                    }
                    //release
                    for(Integer i : serials) { //[2,1]
                        ewoks.get(i).release();
                    }
                    MessageBusImpl bus = MessageBusImpl.getBusInstance();
                    bus.complete(att, true); // finished attack
                    Diary.incrementTotalAttacks();
                });

        //-----subscribe to TerminateBroadcast
        subscribeBroadcast(TerminateBroadcast.class,
                (TerminateBroadcast ter)-> {
                    terminate();
                    Diary.setC3POTerminate(System.currentTimeMillis());
                });

        //-----subscribe to FinishedAttacksBroadcast
        subscribeBroadcast(FinishedAttacksBroadcast.class, (finishedAttacks) -> Diary.setC3POFinish(System.currentTimeMillis()));

        sendBroadcast(new IsReadyBroadcast());
    }
}
