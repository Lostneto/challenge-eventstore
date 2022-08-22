package net.intelie.challenges;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import net.intelie.challenges.dao.EventIterator;
import net.intelie.challenges.dao.EventStore;
import net.intelie.challenges.dao.EventStoreImpl;
import net.intelie.challenges.entities.Event;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EventStoreTest {

	Event event;
	Event event2;

	@Before
	public void setUp() throws Exception {

		event = new Event("some_type", 1660166932000L);
		event2 = new Event("another_type", 1660858132000L);
	}

	@Test
	public void insertAndQueryTest() {
		EventStore store = new EventStoreImpl();
		store.insert(event);
		store.insert(event2);
		store.insert(event2);
		store.insert(event);
		store.insert(event);
		
		EventIterator iterator = store.query("some_type", 1655558932000L, 1687123732000L);
		//It has to be three some_type events on the returned iterator
		iterator.moveNext();
		extracted(iterator);
		iterator.moveNext();
		extracted(iterator);
		iterator.moveNext();
		extracted(iterator);		
		
	}

	private void extracted(EventIterator iterator) {
		assertEquals(event, iterator.current());
	}

	@Test
	public void removeAllAnotherTest() {
		EventStore store = new EventStoreImpl();
		store.removeAll("another_type");

		EventIterator iterator = store.query("some_type", 1655558932000L, 1687123732000L);
		assertTrue(iterator.moveNext());
	}

	@Test
	public void removeAllSomeTest() {
		EventStore store = new EventStoreImpl();
		store.removeAll("some_type");

		EventIterator iterator = store.query("some_type", 1655558932000L, 1687123732000L);
		assertFalse(iterator.moveNext());
	}

}
