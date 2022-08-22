package net.intelie.challenges.dao;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import net.intelie.challenges.entities.Event;

public class EventStoreImpl implements EventStore {
	
	/*
	 * volatile is guaranteed that the updated value will always be available to other threads
	 * 
	 * synchronized block guarantee only one thread running inside them at a time
	 * 
	 * ConcurrentLinkedQueue an unbounded thread-safe queue based on linked nodes. This queue orders elements FIFO,  
	 * is an appropriate choice when many threads will share access to a common collection
	 */
	private static volatile Queue<Event> events;

	private static Queue<Event> getInstanceEvent() {

		if (events == null) { 
			synchronized (EventStoreImpl.class) {
				if (events == null)
					events = new ConcurrentLinkedQueue<Event>();
				}
		}

		return events;
	}

	@Override
	public synchronized void insert(Event event) {
		getInstanceEvent().add(event);
	}

	@Override
	public synchronized void removeAll(String type) {
		getInstanceEvent().removeIf((Event event) -> event.type() == type);		
	}

	@Override
	public synchronized EventIterator query(String type, long startTime, long endTime) {

		return new EventIteratorImpl(getInstanceEvent().parallelStream()
				.filter((Event event) -> (event.type() == type	&& (event.timestamp() >= startTime && event.timestamp() < endTime)))
				.collect(Collectors.toList()));
	}

}
