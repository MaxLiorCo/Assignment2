package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

/**
 * This type of event will be sent by th leader of the rebels to
 * {@link bgu.spl.mics.application.services.R2D2Microservice}. It will execute the given event
 * by sleeping for the given amount in the input file.
 */

public class DeactivationEvent implements Event<Boolean> {
}
