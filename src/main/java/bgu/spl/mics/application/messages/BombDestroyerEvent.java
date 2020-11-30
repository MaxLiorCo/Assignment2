package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

/**
 * This type of Event will be sent by the leader of the rebels to Lando.
 * Then {@link bgu.spl.mics.application.services.LandoMicroservice}
 * will execute this event by sleeping the given amount in the input file.
 */
public class BombDestroyerEvent implements Event<Boolean> {
}
