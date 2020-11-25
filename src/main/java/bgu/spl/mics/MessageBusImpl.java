package bgu.spl.mics;

import com.sun.jmx.remote.internal.ArrayQueue;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private ConcurrentHashMap<String, ConcurrentLinkedQueue<Message>> map;

	private static class SingletonHolder{
		private static MessageBusImpl busInstance = new MessageBusImpl();
	}

	private MessageBusImpl(){
		map = new ConcurrentHashMap<String, ConcurrentLinkedQueue<Message>>();
	}

	public static MessageBusImpl getBusInstance(){
		return SingletonHolder.busInstance;
	}
	
	
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		
    }

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		
        return null;
	}

	@Override
	public void register(MicroService m) {
		map.put(m.getName(), new ConcurrentLinkedQueue<Message>());
	}

	@Override
	public void unregister(MicroService m) {
		map.remove(m.getName());
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		
		return null;
	}
}
