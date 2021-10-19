package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

/**
 * While implementing the assignment, we encountered a problem where Leia might send
 * {@link AttackEvent} to Lando and C3PO when they haven't registered yet via the
 * {@link bgu.spl.mics.MessageBus}. Even if both of them did register by the time Leia sends them the
 * events, the other memebers of the rebels (R2D2 and Lando) might not yet have registered.
 * <p>
 *     To solve this problem, we decided that Leia will wait until all members of the group
 *     will register, and notify her by sending her {@link IsReadyBroadcast}.
 *     After all have registered and ready for her commands, the mission will start.
 * </p>
 */

public class IsReadyBroadcast implements Broadcast {}
