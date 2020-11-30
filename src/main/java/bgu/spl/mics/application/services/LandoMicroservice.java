package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.IsReadyBroadcast;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {

    private long duration;

    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration = duration;
    }

    @Override
    protected void initialize() {

        //-----Subscribe to BombDestroyerEvent
        subscribeEvent(BombDestroyerEvent.class, (bombDestroyer) -> {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
            }
            complete(bombDestroyer, true);
        });

        //-----Subscribe to TerminateBroadcast
        subscribeBroadcast(TerminateBroadcast.class, (tr) -> {
            terminate();
            Diary.setLandoTerminate(System.currentTimeMillis());
        });

        //Notify other micro services that you are ready to receive messages
        sendBroadcast(new IsReadyBroadcast());

    }
}
