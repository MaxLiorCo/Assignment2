package bgu.spl.mics;


import bgu.spl.mics.application.passiveObjects.MessageWrap;

import java.util.*;


/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private Map<MicroService, Queue<Message>> microServiceMap;
	private Map<Class<? extends Message>, MessageWrap> messageTypeMap;

	private static class SingletonHolder{
		private static final MessageBusImpl busInstance = new MessageBusImpl();
	}

	public static MessageBusImpl getBusInstance(){
		return SingletonHolder.busInstance;
	}

	private MessageBusImpl(){
		microServiceMap = new HashMap<>();
		messageTypeMap = new HashMap<>();
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
		
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		MessageWrap wrap = messageTypeMap.get(b.getClass());
		if (wrap == null)
			throw new IllegalArgumentException("Such broadcast do not exist in the current messageTypeMap");
		for (MicroService ms : wrap.getSubscribedMS()){ //Broadcast 'b' is sent to all subscribed MicroServices
			microServiceMap.get(ms).add(b);
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		MessageWrap wrap = messageTypeMap.get(e.getClass());
		if (wrap == null)
			throw new IllegalArgumentException("Such event do not exist in the current messageTypeMap");
		MicroService toAdd = wrap.getCurrMicroService();
		microServiceMap.get(toAdd).add(e);
		return e.;
	}

	@Override
	public void register(MicroService m) {
		microServiceMap.put(m, new LinkedList<>());
	}

	/**
	 *
	 * @param m the micro-service to unregister.
	 * removes the queue from {@link #microServiceMap} and each appearence of MicroService m in the Messages
	 * he subscribed to
	 */
	@Override
	public void unregister(MicroService m) {
		microServiceMap.remove(m);
		messageTypeMap.forEach((messageType, messageWrap) -> messageWrap.removeMS(m));
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		
		return null;
	}

	/**
	 * We check here whether or not such class that extends Message exists in the map, if so, we just add
	 * MS m to this messageType. Otherwise, we create a new entry for the message class and then add MS.
	 * @param type
	 * @param m
	 * @param counter
	 */
	private void subscribeMicroServiceToMessage(Class <? extends Message> type, MicroService m, int counter) {
		MessageWrap wrap = messageTypeMap.get(type); // return value for type in the map, or null if does not exist
		if (wrap == null){ // if no such entry in the map, we need to constructor one
			wrap = new MessageWrap(type, counter);
			messageTypeMap.put(type, wrap);
			wrap.addMS(m);
		}
		else{ // such entry of type class exists in the map, thus we just add m to the list of this wrapper
			wrap.addMS(m);
		}
	}
}

