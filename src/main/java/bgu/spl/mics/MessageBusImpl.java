package bgu.spl.mics;


import bgu.spl.mics.application.passiveObjects.MessageWrap;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private Map<MicroService, LinkedBlockingQueue<Message>> microServiceMap;
	private Map<Class<? extends Message>, MessageWrap> messageTypeMap;
	private Map<Event<?>, Future<?>> eventToFutureMap;

	private static class SingletonHolder{
		private static final MessageBusImpl busInstance = new MessageBusImpl();
	}

	public static MessageBusImpl getBusInstance(){
		return SingletonHolder.busInstance;
	}

	private MessageBusImpl(){
		microServiceMap = new HashMap<>();
		messageTypeMap = new HashMap<>();
		eventToFutureMap = new HashMap<>();
	}


	/**
	 * using {@link #subscribeMicroServiceToMessage(Class, MicroService, int)} to avoid duplication
	 * @param type The type to subscribe to,
	 * @param m    The subscribing micro-service.
	 * @param <T>
	 */
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		subscribeMicroServiceToMessage(type, m, 0);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		subscribeMicroServiceToMessage(type, m, -1);
	}


	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		Future futureToUpdate = eventToFutureMap.get(e);
		futureToUpdate.resolve(result);
		eventToFutureMap.remove(e);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		MessageWrap wrap = messageTypeMap.get(b.getClass());
		if (wrap == null)
			throw new IllegalArgumentException("Such broadcast do not exist in the current messageTypeMap");
/*		for (MicroService ms : wrap.getSubscribedMS()){ //Broadcast 'b' is sent to all subscribed MicroServices
			if (ms!=null)
				microServiceMap.get(ms).add(b); // adding broadcast b to ms's queue
		}*/
		for (Iterator<MicroService> it = wrap.getSubscribedMS().iterator(); it.hasNext();){
			MicroService tempMS = it.next();
			microServiceMap.get(tempMS).add(b);
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		MessageWrap wrap = messageTypeMap.get(e.getClass());
		if (wrap == null)
			throw new IllegalArgumentException("Such event do not exist in the current messageTypeMap");
		MicroService toAdd = wrap.getCurrMicroService(); // current MS in the round-robin manner
		microServiceMap.get(toAdd).add(e); // adding event e to toAdd queue
		Future<T> future = new Future<T>();
		eventToFutureMap.put(e, future); // future will be eventually resolved in Complete function
		return future;
	}

	@Override
	public void register(MicroService m) {
		microServiceMap.put(m, new LinkedBlockingQueue<>());
	}

	/**
	 *
	 * @param m the micro-service to unregister.
	 * removes the queue from {@link #microServiceMap} and each appearence of MicroService m in the Messages
	 * he subscribed to
	 */
	@Override
	public void unregister(MicroService m) {
		microServiceMap.remove(m); // removes the queue of this MS in message-bus
		messageTypeMap.forEach((messageType, messageWrap) -> messageWrap.removeMS(m)); // remove each message type this MS subscribed to
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		LinkedBlockingQueue<Message> tempQueue = microServiceMap.get(m);
		Message toReturn = tempQueue.take();
		return toReturn;
	}

	/**
	 * We check here whether or not such class that extends Message exists in the map, if so, we just add
	 * MS m to this messageType. Otherwise, we create a new entry for the message class and then add MS.
	 * @param type
	 * @param m
	 * @param counter
	 */
	private void subscribeMicroServiceToMessage(Class <? extends Message> type, MicroService m, int counter) {
		synchronized (type) {
			MessageWrap wrap = messageTypeMap.get(type); // return value for type in the map, or null if does not exist
			if (wrap == null) { // if no such entry in the map, we need to constructor one
				wrap = new MessageWrap(type, counter);
				messageTypeMap.put(type, wrap);
				wrap.addMS(m);
			} else { // such entry of type class exists in the map, thus we just add m to the list of this wrapper
				wrap.addMS(m);
			}
		}
	}
}

