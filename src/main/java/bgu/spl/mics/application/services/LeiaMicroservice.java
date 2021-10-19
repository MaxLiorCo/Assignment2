package bgu.spl.mics.application.services;

import java.util.HashMap;
import java.util.Map;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;
	private Map<Event<Boolean>, Future<Boolean>> attackMap;
	private int totalMicroServices;
	private int numOfReadyMicroServices;
	private Object lock;
	
    public LeiaMicroservice(Attack[] attacks, int totalMicroServices, Object lock) {
        super("Leia");
		this.attacks = attacks;
		this.totalMicroServices = totalMicroServices;
        numOfReadyMicroServices = 0;
		attackMap = new HashMap<>();
		this.lock = lock;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(IsReadyBroadcast.class , (IsReadyBroadcast rdy) -> act() );
        numOfReadyMicroServices++; //Leia is ready
        synchronized (lock){
            //notify Main-Thread to start other threads/micro-services
            lock.notifyAll();
        }


    }

    private void act(){
        numOfReadyMicroServices++;
        if(numOfReadyMicroServices == totalMicroServices) {
            AttackEvent[] attackEvents = new AttackEvent[attacks.length];
            for (int i = 0; i < attacks.length; i++) {
                attackEvents[i] = new AttackEvent(attacks[i]);
                attackMap.put(attackEvents[i], sendEvent(attackEvents[i]));
            }
            //waits for the future of all attacks to be true/done
            for (AttackEvent attackEvent : attackEvents)
                attackMap.get(attackEvent).get();

            //to inform han solo and cp3o that no more attacks left
            sendBroadcast(new FinishedAttacksBroadcast());

            //send and wait for Deactivation by R2D2
            Future<Boolean> futureDeact = sendEvent(new DeactivationEvent());
            futureDeact.get();

            //send and wait for Lando to finish Distraction
            Future<Boolean> futureBomb = sendEvent(new BombDestroyerEvent());
            futureBomb.get();

            //Terminate all other Micro-Services
            sendBroadcast(new TerminateBroadcast());

            terminate();
            Diary.setLeiaTerminate(System.currentTimeMillis());
        }
    }
}
