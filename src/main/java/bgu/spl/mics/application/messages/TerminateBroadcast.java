package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

/**
 * This class marker class used to terminate all running threads.
 * When {@link bgu.spl.mics.application.services.LeiaMicroservice} comes to understanding
 * that all of the events were executed by the group members, she sends to all of the
 * this {@link TerminateBroadcast} via the {@link bgu.spl.mics.MessageBus}, to notify them that the
 * mission is over.
 * In addition, she terminates herself.
 */

public class TerminateBroadcast implements Broadcast {

}
