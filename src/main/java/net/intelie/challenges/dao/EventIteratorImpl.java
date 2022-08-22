package net.intelie.challenges.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import net.intelie.challenges.entities.Event;

public class EventIteratorImpl implements EventIterator {
	
	//simple way to perform simple mutations effects without having to resort to synchronizing all access.
	private AtomicInteger counter = new AtomicInteger(-1);	
	
	private final List<Event> events;
	
	public EventIteratorImpl() {
		super();
		this.events = new ArrayList<>();		
	}
	
	public EventIteratorImpl(List<Event> events) {
		super();
		this.events = events;
	}

	@Override
	public void close() throws Exception {		
		events.clear();		
	}

	@Override
	public boolean moveNext() {		
		if (hasNext()) {
			counter.incrementAndGet();
			return true;
		}
		return false;
	}

	@Override
	public Event current() {		
		validadeCurrentOrRemove();
		return events.get(counter.get());
	}

	@Override
	public void remove() {		
		validadeCurrentOrRemove();
		events.remove(counter.get());		
	}	
	
	//logic for atomic control
	private boolean hasNext() {
		return events.size() != 0 && events.size() > counter.get();
	}
	
	private void validadeCurrentOrRemove() {
		if(counter.get() == -1) {
			throw new IllegalStateException("movenext() was never called");
		}
		if(!hasNext()) {
			throw new IllegalStateException("The array has reached its end");
		}
	}
}
