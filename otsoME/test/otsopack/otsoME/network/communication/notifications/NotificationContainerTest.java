/*
 * Copyright (C) 2008-2011 University of Deusto
 * 
 * All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */
package otsopack.otsoME.network.communication.notifications;

import java.util.Enumeration;

import jmunit.framework.cldc11.AssertionFailedException;
import jmunit.framework.cldc11.TestCase;
import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.WildcardTemplate;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.commons.util.collections.HashSet;
import otsopack.commons.util.collections.Vector;

public class NotificationContainerTest extends TestCase {
	NotificationContainer container;
	
	public NotificationContainerTest() {
		super(16, "NotificationContainerTest");
	}
	
	public void setUp() throws Throwable {
		super.setUp();
		SemanticFactory.initialize(new MicrojenaFactory());
		container = new NotificationContainer();
	}
	
	public void tearDown() {
	}
	
	public void test(int testNumber) throws Throwable {
		switch (testNumber) {
		case 0:
			testAdd1();
			break;
		case 1:
			testAddAll1();
			break;
		case 2:
			testClear1();
			break;
		case 3:
			testContains1();
			break;
		case 4:
			testContainsAll1();
			break;
		case 5:
			testIsEmpty1();
			break;
		case 6:
			testIterator1();
			break;
		case 7:
			testRemove1();
			break;
		case 8:
			testRemoveAll1();
			break;
		case 9:
			testRetainAll1();
			break;
		case 10:
			testSize1();
			break;
		case 11:
			testToArray1();
			break;
		case 12:
			testToArray2();
			break;
		case 13:
			testGet1();
			break;
		case 14:
			testGet2();
			break;
		case 15:
			testGetThoseWhichMatch1();
			break;
		}
	}

	public void testAdd1() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		boolean added = container.add(NotificationsFactory.createAdvertisement("http://uri1", WildcardTemplate.createWithNull(null, null)));
		assertTrue(added);
		assertEquals(container.size(),1);
		
		added = container.add(NotificationsFactory.createSubscription("http://uri1", WildcardTemplate.createWithNull(null, null), null));
		assertFalse(added);
		assertEquals(container.size(),1);
		
		added = container.add(NotificationsFactory.createSubscription("http://uri2", WildcardTemplate.createWithNull(null, null), null));
		assertFalse(added);
		assertEquals(container.size(),1);
		
		added = container.add(NotificationsFactory.createSubscription("http://uri1", WildcardTemplate.createWithNull("http://s1", null), null));
		assertFalse(added);
		assertEquals(container.size(),1);
		
		added = container.add(NotificationsFactory.createSubscription("http://uri2",WildcardTemplate.createWithNull("http://s1", null), null));
		assertTrue(added);
		assertEquals(container.size(),2);

		try {
			added = container.add(new Integer(1));
			assertTrue(false);
		} catch(ClassCastException e) {
			//it's OK
		} finally {
			assertEquals(container.size(),2);
		}

		try {
			added = container.add(null);
			assertTrue(false);
		} catch(NullPointerException e) {
			//it's OK
		} finally {
			assertEquals(container.size(),2);
		}
	}

	public void testAddAll1() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		boolean added = false;
		
		Vector collection = new Vector();
		final Object[] elements = new Object[10];
		collection.addElement( elements[0] = NotificationsFactory.createAdvertisement("http://uri1", WildcardTemplate.createWithNull(null, null)) );
		collection.addElement( elements[1] = NotificationsFactory.createSubscription("http://uri2", WildcardTemplate.createWithURI(null, null, "http://o1"),null) );
		collection.addElement( elements[2] = NotificationsFactory.createSubscription("http://uri3", WildcardTemplate.createWithURI(null, "http://p1", "http://o1"),null) );
		collection.addElement( elements[3] = NotificationsFactory.createSubscription("http://uri4", WildcardTemplate.createWithNull(null, "http://p1"),null) );
		collection.addElement( elements[4] = NotificationsFactory.createSubscription("http://uri5", WildcardTemplate.createWithNull("http://s1", null),null) );
		collection.addElement( elements[5] = NotificationsFactory.createSubscription("http://uri6", WildcardTemplate.createWithNull("http://s1","http://p1"),null) );
		collection.addElement( elements[6] = NotificationsFactory.createSubscription("http://uri7", WildcardTemplate.createWithURI("http://s1","http://p1","http://o1"),null) );
		collection.addElement( elements[7] = NotificationsFactory.createSubscription("http://uri8", WildcardTemplate.createWithURI("http://s1","http://p1","http://o1"),null) ); //repe
		collection.addElement( elements[8] = new Integer(1) );
		collection.addElement( elements[9] = null );
				
		try {
			added = container.addAll(collection);
			assertTrue(false);
		} catch(ClassCastException e) {
			//it's OK
		}
		container.clear();
		collection.remove( elements[8] );
		
		try {
			added = container.addAll(collection);
			assertTrue(false);
		} catch(NullPointerException e) {
			//it's OK
		}
		container.clear();
		collection.remove( elements[9] );
		
		added = container.addAll(collection);
		assertEquals( collection.size(), 8 );
		assertTrue( added );
		assertTrue( container.contains(elements[0]) );
		assertTrue( container.contains(elements[1]) );
		assertTrue( container.contains(elements[2]) );
		assertTrue( container.contains(elements[3]) );
		assertTrue( container.contains(elements[4]) );
		assertTrue( container.contains(elements[5]) );
		assertTrue( container.contains(elements[6]) );
		assertFalse( container.contains(elements[7]) ); //elements[7].equals(elements[6])
		assertEquals( container.size(), 7 );
		
		//Adding elements already in the list
		collection = new Vector(4);
		Object[] elements2 = new Object[4];
		collection.add( elements2[0] = elements[0] );
		collection.add( elements2[1] = elements[1] );
		collection.add( elements2[2] = NotificationsFactory.createSubscription("http://uri9", WildcardTemplate.createWithURI(null, "http://p9", "http://o9>"),null) );
		collection.add( elements2[3] = NotificationsFactory.createSubscription("http://uri10", WildcardTemplate.createWithNull(null, "http://p10"),null) );
		assertTrue( container.addAll(collection) );
		assertEquals( container.size(), 9 );
	}

	public void testClear1() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		boolean added = container.add(NotificationsFactory.createSubscription("http://uri1", WildcardTemplate.createWithNull(null, null),null));
		assertTrue(added);
		added = container.add(NotificationsFactory.createSubscription("http://uri2", WildcardTemplate.createWithNull(null, "http://p2"),null));
		assertTrue(added);
		added = container.add(NotificationsFactory.createSubscription("http://uri3", WildcardTemplate.createWithNull(null, "http://p3"),null));
		assertTrue(added);
		added = container.add(NotificationsFactory.createSubscription("http://uri4", WildcardTemplate.createWithNull(null, "http://p4"),null));
		assertTrue(added);
		assertEquals(container.size(),4);
		
		container.clear();
		assertEquals(container.size(),0);
		assertTrue(container.notificationsBySelector.isEmpty());
		assertTrue(container.notificationsByURI.isEmpty());
		assertTrue(container.isEmpty());
		
	}

	public void testContains1() throws AssertionFailedException, MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		final INotificationElement[] elements = new INotificationElement[4];
		boolean added = container.add(elements[0]=NotificationsFactory.createSubscription("http://uri1", WildcardTemplate.createWithNull(null, null),null));
		assertTrue(added);
		added = container.add(elements[1]=NotificationsFactory.createSubscription("http://uri2", WildcardTemplate.createWithNull(null, "http://p2"),null));
		assertTrue(added);
		added = container.add(elements[2]=NotificationsFactory.createSubscription("http://uri3", WildcardTemplate.createWithNull(null, "http://p3"),null));
		assertTrue(added);
		added = container.add(elements[3]=NotificationsFactory.createSubscription("http://uri4", WildcardTemplate.createWithNull(null, "http://p4"),null));
		assertTrue(added);
		assertEquals(container.size(),elements.length);
		
		assertTrue( container.contains(elements[0]) );
		assertTrue( container.contains(elements[1]) );
		assertTrue( container.contains(elements[2]) );
		assertTrue( container.contains(elements[3]) );
		assertFalse( container.contains(NotificationsFactory.createSubscription("http://uri5", WildcardTemplate.createWithNull(null, "http://p5"),null)) );
	}

	public void testContainsAll1() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		HashSet elements = new HashSet(4);
		elements.add( NotificationsFactory.createSubscription("http://uri1", WildcardTemplate.createWithNull(null, null),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri2", WildcardTemplate.createWithNull(null, "http://p2"),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri3", WildcardTemplate.createWithNull(null, "http://p3"),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri4", WildcardTemplate.createWithNull(null, "http://p4"),null) );
		boolean added = container.addAll(elements);
		assertTrue(added);
		assertEquals(container.size(),elements.size());
		assertTrue( container.containsAll(elements) );

		elements = new HashSet(2);
		elements.add( NotificationsFactory.createSubscription("http://uri1", WildcardTemplate.createWithNull(null, null),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri2", WildcardTemplate.createWithNull(null, "http://p2"),null) );
		assertTrue( container.containsAll(elements) );
		
		elements = new HashSet(2);
		elements.add( NotificationsFactory.createSubscription("http://uri3", WildcardTemplate.createWithNull(null, null),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri2", WildcardTemplate.createWithNull(null, "http://p2"),null) );
		assertFalse( container.containsAll(elements) );
	}

	public void testIsEmpty1() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		assertTrue(container.isEmpty());
		
		boolean added = container.add(NotificationsFactory.createSubscription("http://uri1", WildcardTemplate.createWithNull(null, null),null));
		assertTrue(added);
		added = container.add(NotificationsFactory.createSubscription("http://uri2", WildcardTemplate.createWithNull(null, "http://p2"),null));
		assertTrue(added);
		added = container.add(NotificationsFactory.createSubscription("http://uri3", WildcardTemplate.createWithNull(null, "http://p3"),null));
		assertTrue(added);
		added = container.add(NotificationsFactory.createSubscription("http://uri4", WildcardTemplate.createWithNull(null, "http://p4"),null));
		assertTrue(added);
		assertEquals(container.size(),4);
		assertFalse(container.isEmpty());
		
		container.clear();
		assertTrue(container.isEmpty());
	}

	public void testIterator1() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		boolean added = container.add(NotificationsFactory.createSubscription("http://uri1", WildcardTemplate.createWithNull(null, null),null));
		assertTrue(added);
		added = container.add(NotificationsFactory.createSubscription("http://uri2", WildcardTemplate.createWithNull(null, "http://p2"),null));
		assertTrue(added);
		added = container.add(NotificationsFactory.createSubscription("http://uri3", WildcardTemplate.createWithNull(null, "http://p3"),null));
		assertTrue(added);
		added = container.add(NotificationsFactory.createSubscription("http://uri4", WildcardTemplate.createWithNull(null, "http://p4"),null));
		assertTrue(added);
		assertEquals(container.size(),4);
		assertFalse(container.isEmpty());
		
		int i = 0;
		Enumeration it = container.elements();
		while( it.hasMoreElements() ) {
			it.nextElement();
			i++;
		}
		assertEquals(i,4);
	}

	public void testRemove1() throws AssertionFailedException, MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		final INotificationElement[] elements = new INotificationElement[4];
		assertTrue( container.add(elements[0]=NotificationsFactory.createSubscription("http://uri1", WildcardTemplate.createWithNull(null, null),null)) );
		assertTrue( container.add(elements[1]=NotificationsFactory.createSubscription("http://uri2", WildcardTemplate.createWithNull(null, "http://p2"),null)) );
		assertTrue( container.add(elements[2]=NotificationsFactory.createSubscription("http://uri3", WildcardTemplate.createWithNull(null, "http://p3"),null)) );
		assertTrue( container.add(elements[3]=NotificationsFactory.createSubscription("http://uri4", WildcardTemplate.createWithNull(null, "http://p4"),null)) );
		
		assertEquals(container.size(),elements.length);
		assertTrue(container.contains(elements[0]));
		assertTrue(container.contains(elements[1]));
		assertTrue(container.contains(elements[2]));
		assertTrue(container.contains(elements[3]));
		
		container.remove(elements[1]);
		assertEquals(container.size(),3);
		assertTrue(container.contains(elements[0]));
		assertFalse(container.contains(elements[1]));
		assertTrue(container.contains(elements[2]));
		assertTrue(container.contains(elements[3]));
		
		container.remove(elements[2]);
		assertEquals(container.size(),2);
		assertTrue(container.contains(elements[0]));
		assertFalse(container.contains(elements[1]));
		assertFalse(container.contains(elements[2]));
		assertTrue(container.contains(elements[3]));
		
		container.remove(elements[0]);
		assertEquals(container.size(),1);
		assertFalse(container.contains(elements[0]));
		assertFalse(container.contains(elements[1]));
		assertFalse(container.contains(elements[2]));
		assertTrue(container.contains(elements[3]));
		
		container.remove(elements[3]);
		assertEquals(container.size(),0);
		assertFalse(container.contains(elements[0]));
		assertFalse(container.contains(elements[1]));
		assertFalse(container.contains(elements[2]));
		assertFalse(container.contains(elements[3]));
	}

	public void testRemoveAll1() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final INotificationElement[] els = new INotificationElement[8];
		
		HashSet elements = new HashSet(4);
		elements.add( els[0] = NotificationsFactory.createSubscription("http://uri1", WildcardTemplate.createWithNull(null, null),null) );
		elements.add( els[1] = NotificationsFactory.createSubscription("http://uri2", WildcardTemplate.createWithNull(null, "http://p2"),null) );
		elements.add( els[2] = NotificationsFactory.createSubscription("http://uri3", WildcardTemplate.createWithNull(null, "http://p3"),null) );
		elements.add( els[3] = NotificationsFactory.createSubscription("http://uri4", WildcardTemplate.createWithNull(null, "http://p4"),null) );
		assertTrue( container.addAll(elements) );
		assertEquals(container.size(),elements.size());
		
		
		final HashSet elements2 = new HashSet(4);
		elements2.add( els[4] = NotificationsFactory.createSubscription("http://uri5", WildcardTemplate.createWithNull("http://s5", null),null) );
		elements2.add( els[5] = NotificationsFactory.createSubscription("http://uri6", WildcardTemplate.createWithNull(null, "http://p6"),null) );
		elements2.add( els[6] = NotificationsFactory.createSubscription("http://uri7", WildcardTemplate.createWithNull(null, "http://p7"),null) );
		elements2.add( els[7] = NotificationsFactory.createSubscription("http://uri8", WildcardTemplate.createWithNull(null, "http://p8"),null) );
		assertTrue( container.addAll(elements2) );
		assertEquals(container.size(),elements.size()+elements2.size());
		
		assertTrue(container.contains(els[0]));
		assertTrue(container.contains(els[1]));
		assertTrue(container.contains(els[2]));
		assertTrue(container.contains(els[3]));
		assertTrue(container.contains(els[4]));
		assertTrue(container.contains(els[5]));
		assertTrue(container.contains(els[6]));
		assertTrue(container.contains(els[7]));
		
		assertTrue( container.removeAll(elements) );
		assertEquals(container.size(),elements2.size());
		assertFalse(container.contains(els[0]));
		assertFalse(container.contains(els[1]));
		assertFalse(container.contains(els[2]));
		assertFalse(container.contains(els[3]));
		assertTrue(container.contains(els[4]));
		assertTrue(container.contains(els[5]));
		assertTrue(container.contains(els[6]));
		assertTrue(container.contains(els[7]));
		
		elements = new HashSet(2);
		elements.add( els[6] );
		elements.add( NotificationsFactory.createSubscription("http://uri9", WildcardTemplate.createWithNull(null, "http://p9"),null) );
		assertTrue( container.removeAll(elements) );
		assertEquals(container.size(),elements2.size()-1);
		assertFalse(container.contains(els[0]));
		assertFalse(container.contains(els[1]));
		assertFalse(container.contains(els[2]));
		assertFalse(container.contains(els[3]));
		assertTrue(container.contains(els[4]));
		assertTrue(container.contains(els[5]));
		assertFalse(container.contains(els[6]));
		assertTrue(container.contains(els[7]));
		
		assertTrue( container.removeAll(elements2) );
		assertEquals(container.size(),0);
		assertFalse(container.contains(els[0]));
		assertFalse(container.contains(els[1]));
		assertFalse(container.contains(els[2]));
		assertFalse(container.contains(els[3]));
		assertFalse(container.contains(els[4]));
		assertFalse(container.contains(els[5]));
		assertFalse(container.contains(els[6]));
		assertFalse(container.contains(els[7]));
	}

	public void testRetainAll1() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final INotificationElement[] els = new INotificationElement[8];
		
		final HashSet elements = new HashSet(8);
		elements.add( els[0] = NotificationsFactory.createSubscription("http://uri1", WildcardTemplate.createWithNull(null, null),null) );
		elements.add( els[1] = NotificationsFactory.createSubscription("http://uri2", WildcardTemplate.createWithNull(null, "http://p2"),null) );
		elements.add( els[2] = NotificationsFactory.createSubscription("http://uri3", WildcardTemplate.createWithNull(null, "http://p3"),null) );
		elements.add( els[3] = NotificationsFactory.createSubscription("http://uri4", WildcardTemplate.createWithNull(null, "http://p4"),null) );
		elements.add( els[4] = NotificationsFactory.createSubscription("http://uri5", WildcardTemplate.createWithNull("http://s5", null),null) );
		elements.add( els[5] = NotificationsFactory.createSubscription("http://uri6", WildcardTemplate.createWithNull(null, "http://p6"),null) );
		elements.add( els[6] = NotificationsFactory.createSubscription("http://uri7", WildcardTemplate.createWithNull(null, "http://p7"),null) );
		elements.add( els[7] = NotificationsFactory.createSubscription("http://uri8", WildcardTemplate.createWithNull(null, "http://p8"),null) );
		assertTrue( container.addAll(elements) );
		assertEquals(container.size(),elements.size());
		
		HashSet elements2 = new HashSet(6);
		elements2.add( els[1].clone() );
		elements2.add( els[2].clone() );
		elements2.add( els[3].clone() );
		elements2.add( els[4].clone() );
		elements2.add( els[5].clone() );
		elements2.add( els[6].clone() );
		assertTrue( container.retainAll(elements2) );
		assertEquals(elements2.size(), container.size());
		assertFalse(container.contains(els[0]));
		assertTrue(container.contains(els[1]));
		assertTrue(container.contains(els[2]));
		assertTrue(container.contains(els[3]));
		assertTrue(container.contains(els[4]));
		assertTrue(container.contains(els[5]));
		assertTrue(container.contains(els[6]));
		assertFalse(container.contains(els[7]));
		
		
		elements2 = new HashSet(4);
		elements2.add( els[2].clone() );
		elements2.add( els[3].clone() );
		elements2.add( els[4].clone() );
		elements2.add( els[5].clone() );
		assertTrue( container.retainAll(elements2) );
		assertEquals(container.size(),elements2.size());
		assertFalse(container.contains(els[0]));
		assertFalse(container.contains(els[1]));
		assertTrue(container.contains(els[2]));
		assertTrue(container.contains(els[3]));
		assertTrue(container.contains(els[4]));
		assertTrue(container.contains(els[5]));
		assertFalse(container.contains(els[6]));
		assertFalse(container.contains(els[7]));
		
		
		elements2 = new HashSet(2);
		elements2.add( els[2].clone() );
		elements2.add( els[3].clone() );
		assertTrue( container.retainAll(elements2) );
		assertEquals(container.size(),elements2.size());
		assertFalse(container.contains(els[0]));
		assertFalse(container.contains(els[1]));
		assertTrue(container.contains(els[2]));
		assertTrue(container.contains(els[3]));
		assertFalse(container.contains(els[4]));
		assertFalse(container.contains(els[5]));
		assertFalse(container.contains(els[6]));
		assertFalse(container.contains(els[7]));
		
		
		elements2 = new HashSet(1);
		elements2.add( els[2].clone() );
		assertTrue( container.retainAll(elements2) );
		assertEquals(container.size(),elements2.size());
		assertFalse(container.contains(els[0]));
		assertFalse(container.contains(els[1]));
		assertTrue(container.contains(els[2]));
		assertFalse(container.contains(els[3]));
		assertFalse(container.contains(els[4]));
		assertFalse(container.contains(els[5]));
		assertFalse(container.contains(els[6]));
		assertFalse(container.contains(els[7]));
		
		elements2 = new HashSet(0);
		assertTrue( container.retainAll(elements2) );
		assertEquals(container.size(),0);
		assertFalse(container.contains(els[0]));
		assertFalse(container.contains(els[1]));
		assertFalse(container.contains(els[2]));
		assertFalse(container.contains(els[3]));
		assertFalse(container.contains(els[4]));
		assertFalse(container.contains(els[5]));
		assertFalse(container.contains(els[6]));
		assertFalse(container.contains(els[7]));
	}

	public void testSize1() throws AssertionFailedException, MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		assertEquals(container.size(),0);
		assertTrue( container.add(NotificationsFactory.createSubscription("http://uri1", WildcardTemplate.createWithNull(null, null),null)) );
		assertEquals(container.size(),1);
		assertTrue( container.add(NotificationsFactory.createSubscription("http://uri2", WildcardTemplate.createWithNull(null, "http://p2"),null)) );
		assertEquals(container.size(),2);
		assertTrue( container.add(NotificationsFactory.createSubscription("http://uri3", WildcardTemplate.createWithNull(null, "http://p3"),null)) );
		assertEquals(container.size(),3);
		assertTrue( container.add(NotificationsFactory.createSubscription("http://uri4", WildcardTemplate.createWithNull(null, "http://p4"),null)) );
		assertEquals(container.size(),4);
	}

	public void testToArray1() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final Vector elements = new Vector(8);
		elements.add( NotificationsFactory.createSubscription("http://uri1", WildcardTemplate.createWithNull(null, null),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri2", WildcardTemplate.createWithNull(null, "http://p2"),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri3", WildcardTemplate.createWithNull(null, "http://p3"),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri4", WildcardTemplate.createWithNull(null, "http://p4"),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri5", WildcardTemplate.createWithNull("http://s5", null),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri6", WildcardTemplate.createWithNull(null, "http://p6"),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri7", WildcardTemplate.createWithNull(null, "http://p7"),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri8", WildcardTemplate.createWithNull(null, "http://p8"),null) );
		assertTrue( container.addAll(elements) );
		assertEquals(container.size(),elements.size());
		
		Object[] toArray = container.toArray();
		assertEquals(toArray.length,container.size());
		for(int i=0; i<toArray.length; i++) {
			assertTrue( elements.contains(toArray[i]) );
		}
	}

	public void testToArray2() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final Vector elements = new Vector(8);
		elements.add( NotificationsFactory.createSubscription("http://uri1", WildcardTemplate.createWithNull(null, null),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri2", WildcardTemplate.createWithNull(null, "http://p2"),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri3", WildcardTemplate.createWithNull(null, "http://p3"),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri4", WildcardTemplate.createWithNull(null, "http://p4"),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri5", WildcardTemplate.createWithNull("http://s5", null),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri6", WildcardTemplate.createWithNull(null, "http://p6"),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri7", WildcardTemplate.createWithNull(null, "http://p7"),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri8", WildcardTemplate.createWithNull(null, "http://p8"),null) );
		assertTrue( container.addAll(elements) );
		assertEquals(container.size(),elements.size());
		
		Object[] toArray = container.toArray(new Object[0]);
		assertEquals(toArray.length, container.size());
		for(int i=0; i<toArray.length; i++) {
			assertTrue( elements.contains(toArray[i]) );
		}
		
		toArray = container.toArray(new Object[10]);
		assertEquals(toArray.length,10);
		for(int i=0; i<container.size(); i++) {
			assertTrue( elements.contains(toArray[i]) );
		}
	}

	public void testGet1() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final INotificationElement[] els = new INotificationElement[8];
		final HashSet elements = new HashSet(8);
		elements.add( els[0] = NotificationsFactory.createSubscription("http://uri1", WildcardTemplate.createWithNull(null, null),null) );
		elements.add( els[1] = NotificationsFactory.createSubscription("http://uri2", WildcardTemplate.createWithNull(null, "http://p2"),null) );
		elements.add( els[2] = NotificationsFactory.createSubscription("http://uri3", WildcardTemplate.createWithNull(null, "http://p3"),null) );
		elements.add( els[3] = NotificationsFactory.createSubscription("http://uri4", WildcardTemplate.createWithNull(null, "http://p4"),null) );
		elements.add( els[4] = NotificationsFactory.createSubscription("http://uri5", WildcardTemplate.createWithNull("http://s5", null),null) );
		elements.add( els[5] = NotificationsFactory.createSubscription("http://uri6", WildcardTemplate.createWithNull(null, "http://p6"),null) );
		elements.add( els[6] = NotificationsFactory.createSubscription("http://uri7", WildcardTemplate.createWithNull(null, "http://p7"),null) );
		elements.add( els[7] = NotificationsFactory.createSubscription("http://uri8", WildcardTemplate.createWithNull(null, "http://p8"),null) );
		assertTrue( container.addAll(elements) );
		assertEquals(container.size(),elements.size());
		
		assertEquals(container.get("http://uri1"),els[0]);
		assertEquals(container.get("http://uri8"),els[7]);
		assertEquals(container.get("http://uri7"),els[6]);
		assertEquals(container.get("http://uri2"),els[1]);
		assertEquals(container.get("http://uri3"),els[2]);
		assertEquals(container.get("http://uri4"),els[3]);
		assertEquals(container.get("http://uri6"),els[5]);
		assertEquals(container.get("http://uri5"),els[4]);
		assertNull(container.get("http://thisUriDoesnotExist"));
	}

	public void testGet2() throws AssertionFailedException, MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final INotificationElement[] els = new INotificationElement[8];
		final HashSet elements = new HashSet(8);
		elements.add( els[0] = NotificationsFactory.createSubscription("http://uri1", WildcardTemplate.createWithNull(null, null),null) );
		elements.add( els[1] = NotificationsFactory.createSubscription("http://uri2", WildcardTemplate.createWithNull(null, "http://p2"),null) );
		elements.add( els[2] = NotificationsFactory.createSubscription("http://uri3", WildcardTemplate.createWithNull(null, "http://p3"),null) );
		elements.add( els[3] = NotificationsFactory.createSubscription("http://uri4", WildcardTemplate.createWithNull(null, "http://p4"),null) );
		elements.add( els[4] = NotificationsFactory.createSubscription("http://uri5", WildcardTemplate.createWithNull("http://s5", null),null) );
		elements.add( els[5] = NotificationsFactory.createSubscription("http://uri6", WildcardTemplate.createWithNull(null, "http://p6"),null) );
		elements.add( els[6] = NotificationsFactory.createSubscription("http://uri7", WildcardTemplate.createWithNull(null, "http://p7"),null) );
		elements.add( els[7] = NotificationsFactory.createSubscription("http://uri8", WildcardTemplate.createWithNull(null, "http://p8"),null) );
		assertTrue( container.addAll(elements) );
		assertEquals(container.size(),elements.size());
		
		assertEquals(container.get(WildcardTemplate.createWithNull(null, null)),els[0]);
		assertEquals(container.get(WildcardTemplate.createWithNull(null, "http://p8")),els[7]);
		assertEquals(container.get(WildcardTemplate.createWithNull(null, "http://p7")),els[6]);
		assertEquals(container.get(WildcardTemplate.createWithNull(null, "http://p2")),els[1]);
		assertEquals(container.get(WildcardTemplate.createWithNull(null, "http://p3")),els[2]);
		assertEquals(container.get(WildcardTemplate.createWithNull(null, "http://p4")),els[3]);
		assertEquals(container.get(WildcardTemplate.createWithNull(null, "http://p6")),els[5]);
		assertEquals(container.get(WildcardTemplate.createWithNull("http://s5", null)),els[4]);
		assertNull(container.get(WildcardTemplate.createWithURI("http://imposible", "http://is", "http://nothing>")));
	}

	public void testGetThoseWhichMatch1() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final INotificationElement[] els = new INotificationElement[8];
		final HashSet elements = new HashSet(8);
		elements.add( els[0] = NotificationsFactory.createSubscription("http://uri1", WildcardTemplate.createWithNull(null, null),null) );
		elements.add( els[1] = NotificationsFactory.createSubscription("http://uri2", WildcardTemplate.createWithNull(null, "http://p2"),null) );
		elements.add( els[2] = NotificationsFactory.createSubscription("http://uri3", WildcardTemplate.createWithNull("http://s2", "http://p2"),null) );
		elements.add( els[3] = NotificationsFactory.createSubscription("http://uri4", WildcardTemplate.createWithURI(null, "http://p2", "http://o2>"),null) );
		elements.add( els[4] = NotificationsFactory.createSubscription("http://uri5", WildcardTemplate.createWithNull("http://s5", null),null) );
		elements.add( els[5] = NotificationsFactory.createSubscription("http://uri6", WildcardTemplate.createWithNull(null, "http://p6"),null) );
		elements.add( els[6] = NotificationsFactory.createSubscription("http://uri7", WildcardTemplate.createWithNull(null, "http://p7"),null) );
		elements.add( els[7] = NotificationsFactory.createSubscription("http://uri8", WildcardTemplate.createWithNull(null, "http://p8"),null) );
		assertTrue( container.addAll(elements) );
		assertEquals(container.size(),elements.size());
		
		HashSet those = container.getThoseWhichMatch(WildcardTemplate.createWithNull(null, null));
		assertEquals(those.size(),elements.size());
		assertTrue( those.contains(els[0]) );
		assertTrue( those.contains(els[1]) );
		assertTrue( those.contains(els[2]) );
		assertTrue( those.contains(els[3]) );
		assertTrue( those.contains(els[4]) );
		assertTrue( those.contains(els[5]) );
		assertTrue( those.contains(els[6]) );
		assertTrue( those.contains(els[7]) );
		
		those = container.getThoseWhichMatch(WildcardTemplate.createWithNull(null, "http://p2"));
		assertEquals(those.size(),3);
		assertFalse( those.contains(els[0]) );
		assertTrue( those.contains(els[1]) );
		assertTrue( those.contains(els[2]) );
		assertTrue( those.contains(els[3]) );
		assertFalse( those.contains(els[4]) );
		assertFalse( those.contains(els[5]) );
		assertFalse( those.contains(els[6]) );
		assertFalse( those.contains(els[7]) );
		
		those = container.getThoseWhichMatch(WildcardTemplate.createWithURI("http://imposible", "http://is", "http://nothing>"));
		assertEquals(those.size(),0);
	}
}