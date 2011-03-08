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
package otsopack.droid.network.communication.notifications;

import java.util.Enumeration;

import junit.framework.TestCase;
import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.commons.util.collections.HashSet;
import otsopack.commons.util.collections.Vector;
import otsopack.droid.network.communication.notifications.INotificationElement;
import otsopack.droid.network.communication.notifications.NotificationContainer;
import otsopack.droid.network.communication.notifications.NotificationsFactory;

public class NotificationContainerTest extends TestCase {
	NotificationContainer container;
	
	public void setUp() throws Exception {
		super.setUp();
		SemanticFactory.initialize(new MicrojenaFactory());
		container = new NotificationContainer();
	}
	
	public void tearDown() {
	}
	
	public void testAdd() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		boolean added = container.add(NotificationsFactory.createAdvertisement("http://uri1", sf.createTemplate("?s1 ?p1 ?o1 .")));
		assertTrue(added);
		assertEquals(container.size(),1);
		
		added = container.add(NotificationsFactory.createSubscription("http://uri1", sf.createTemplate("?s ?p ?o ."), null));
		assertFalse(added);
		assertEquals(container.size(),1);
		
		added = container.add(NotificationsFactory.createSubscription("http://uri2", sf.createTemplate("?s2 ?p2 ?o2 ."), null));
		assertFalse(added);
		assertEquals(container.size(),1);
		
		added = container.add(NotificationsFactory.createSubscription("http://uri1", sf.createTemplate("<http://s1> ?p ?o ."), null));
		assertFalse(added);
		assertEquals(container.size(),1);
		
		added = container.add(NotificationsFactory.createSubscription("http://uri2",sf.createTemplate("<http://s1> ?p ?o ."), null));
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

	public void testAddAll() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		boolean added = false;
		
		Vector collection = new Vector();
		final Object[] elements = new Object[10];
		collection.addElement( elements[0] = NotificationsFactory.createAdvertisement("http://uri1", sf.createTemplate("?s1 ?p1 ?o1 .")) );
		collection.addElement( elements[1] = NotificationsFactory.createSubscription("http://uri2", sf.createTemplate("?s ?p <http://o1> ."),null) );
		collection.addElement( elements[2] = NotificationsFactory.createSubscription("http://uri3", sf.createTemplate("?s <http://p1> <http://o1> ."),null) );
		collection.addElement( elements[3] = NotificationsFactory.createSubscription("http://uri4", sf.createTemplate("?s <http://p1> ?o ."),null) );
		collection.addElement( elements[4] = NotificationsFactory.createSubscription("http://uri5", sf.createTemplate("<http://s1> ?p ?o ."),null) );
		collection.addElement( elements[5] = NotificationsFactory.createSubscription("http://uri6", sf.createTemplate("<http://s1> <http://p1> ?o ."),null) );
		collection.addElement( elements[6] = NotificationsFactory.createSubscription("http://uri7", sf.createTemplate("<http://s1> <http://p1> <http://o1> ."),null) );
		collection.addElement( elements[7] = NotificationsFactory.createSubscription("http://uri8", sf.createTemplate("<http://s1> <http://p1> <http://o1> ."),null) ); //repe
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
		collection.add( elements2[2] = NotificationsFactory.createSubscription("http://uri9", sf.createTemplate("?s <http://p9> <http://o9> ."),null) );
		collection.add( elements2[3] = NotificationsFactory.createSubscription("http://uri10", sf.createTemplate("?s <http://p10> ?o ."),null) );
		assertTrue( container.addAll(collection) );
		assertEquals( container.size(), 9 );
	}

	public void testClear() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		boolean added = container.add(NotificationsFactory.createSubscription("http://uri1", sf.createTemplate("?s ?p ?o ."),null));
		assertTrue(added);
		added = container.add(NotificationsFactory.createSubscription("http://uri2", sf.createTemplate("?s <http://p2> ?o ."),null));
		assertTrue(added);
		added = container.add(NotificationsFactory.createSubscription("http://uri3", sf.createTemplate("?s <http://p3> ?o ."),null));
		assertTrue(added);
		added = container.add(NotificationsFactory.createSubscription("http://uri4", sf.createTemplate("?s <http://p4> ?o ."),null));
		assertTrue(added);
		assertEquals(container.size(),4);
		
		container.clear();
		assertEquals(container.size(),0);
		assertTrue(container.notificationsBySelector.isEmpty());
		assertTrue(container.notificationsByURI.isEmpty());
		assertTrue(container.isEmpty());
		
	}

	public void testContains() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		
		final INotificationElement[] elements = new INotificationElement[4];
		boolean added = container.add(elements[0]=NotificationsFactory.createSubscription("http://uri1", sf.createTemplate("?s ?p ?o ."),null));
		assertTrue(added);
		added = container.add(elements[1]=NotificationsFactory.createSubscription("http://uri2", sf.createTemplate("?s <http://p2> ?o ."),null));
		assertTrue(added);
		added = container.add(elements[2]=NotificationsFactory.createSubscription("http://uri3", sf.createTemplate("?s <http://p3> ?o ."),null));
		assertTrue(added);
		added = container.add(elements[3]=NotificationsFactory.createSubscription("http://uri4", sf.createTemplate("?s <http://p4> ?o ."),null));
		assertTrue(added);
		assertEquals(container.size(),elements.length);
		
		assertTrue( container.contains(elements[0]) );
		assertTrue( container.contains(elements[1]) );
		assertTrue( container.contains(elements[2]) );
		assertTrue( container.contains(elements[3]) );
		assertFalse( container.contains(NotificationsFactory.createSubscription("http://uri5", sf.createTemplate("?s <http://p5> ?o ."),null)) );
	}

	public void testContainsAll() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		HashSet elements = new HashSet(4);
		elements.add( NotificationsFactory.createSubscription("http://uri1", sf.createTemplate("?s ?p ?o ."),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri2", sf.createTemplate("?s <http://p2> ?o ."),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri3", sf.createTemplate("?s <http://p3> ?o ."),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri4", sf.createTemplate("?s <http://p4> ?o ."),null) );
		boolean added = container.addAll(elements);
		assertTrue(added);
		assertEquals(container.size(),elements.size());
		assertTrue( container.containsAll(elements) );

		elements = new HashSet(2);
		elements.add( NotificationsFactory.createSubscription("http://uri1", sf.createTemplate("?s ?p ?o ."),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri2", sf.createTemplate("?s <http://p2> ?o ."),null) );
		assertTrue( container.containsAll(elements) );
		
		elements = new HashSet(2);
		elements.add( NotificationsFactory.createSubscription("http://uri3", sf.createTemplate("?s ?p ?o ."),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri2", sf.createTemplate("?s <http://p2> ?o ."),null) );
		assertFalse( container.containsAll(elements) );
	}

	public void testIsEmpty() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		assertTrue(container.isEmpty());
		
		boolean added = container.add(NotificationsFactory.createSubscription("http://uri1", sf.createTemplate("?s ?p ?o ."),null));
		assertTrue(added);
		added = container.add(NotificationsFactory.createSubscription("http://uri2", sf.createTemplate("?s <http://p2> ?o ."),null));
		assertTrue(added);
		added = container.add(NotificationsFactory.createSubscription("http://uri3", sf.createTemplate("?s <http://p3> ?o ."),null));
		assertTrue(added);
		added = container.add(NotificationsFactory.createSubscription("http://uri4", sf.createTemplate("?s <http://p4> ?o ."),null));
		assertTrue(added);
		assertEquals(container.size(),4);
		assertFalse(container.isEmpty());
		
		container.clear();
		assertTrue(container.isEmpty());
	}

	public void testIterator() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		boolean added = container.add(NotificationsFactory.createSubscription("http://uri1", sf.createTemplate("?s ?p ?o ."),null));
		assertTrue(added);
		added = container.add(NotificationsFactory.createSubscription("http://uri2", sf.createTemplate("?s <http://p2> ?o ."),null));
		assertTrue(added);
		added = container.add(NotificationsFactory.createSubscription("http://uri3", sf.createTemplate("?s <http://p3> ?o ."),null));
		assertTrue(added);
		added = container.add(NotificationsFactory.createSubscription("http://uri4", sf.createTemplate("?s <http://p4> ?o ."),null));
		assertTrue(added);
		assertEquals(container.size(),4);
		assertFalse(container.isEmpty());
		
		int i = 0;
		Enumeration<?> it = container.elements();
		while( it.hasMoreElements() ) {
			it.nextElement();
			i++;
		}
		assertEquals(i,4);
	}

	public void testRemove() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		
		final INotificationElement[] elements = new INotificationElement[4];
		assertTrue( container.add(elements[0]=NotificationsFactory.createSubscription("http://uri1", sf.createTemplate("?s ?p ?o ."),null)) );
		assertTrue( container.add(elements[1]=NotificationsFactory.createSubscription("http://uri2", sf.createTemplate("?s <http://p2> ?o ."),null)) );
		assertTrue( container.add(elements[2]=NotificationsFactory.createSubscription("http://uri3", sf.createTemplate("?s <http://p3> ?o ."),null)) );
		assertTrue( container.add(elements[3]=NotificationsFactory.createSubscription("http://uri4", sf.createTemplate("?s <http://p4> ?o ."),null)) );
		
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

	public void testRemoveAll() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final INotificationElement[] els = new INotificationElement[8];
		
		HashSet elements = new HashSet(4);
		elements.add( els[0] = NotificationsFactory.createSubscription("http://uri1", sf.createTemplate("?s ?p ?o ."),null) );
		elements.add( els[1] = NotificationsFactory.createSubscription("http://uri2", sf.createTemplate("?s <http://p2> ?o ."),null) );
		elements.add( els[2] = NotificationsFactory.createSubscription("http://uri3", sf.createTemplate("?s <http://p3> ?o ."),null) );
		elements.add( els[3] = NotificationsFactory.createSubscription("http://uri4", sf.createTemplate("?s <http://p4> ?o ."),null) );
		assertTrue( container.addAll(elements) );
		assertEquals(container.size(),elements.size());
		
		
		final HashSet elements2 = new HashSet(4);
		elements2.add( els[4] = NotificationsFactory.createSubscription("http://uri5", sf.createTemplate("<http://s5> ?p ?o ."),null) );
		elements2.add( els[5] = NotificationsFactory.createSubscription("http://uri6", sf.createTemplate("?s <http://p6> ?o ."),null) );
		elements2.add( els[6] = NotificationsFactory.createSubscription("http://uri7", sf.createTemplate("?s <http://p7> ?o ."),null) );
		elements2.add( els[7] = NotificationsFactory.createSubscription("http://uri8", sf.createTemplate("?s <http://p8> ?o ."),null) );
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
		elements.add( NotificationsFactory.createSubscription("http://uri9", sf.createTemplate("?s <http://p9> ?o ."),null) );
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

	public void testRetainAll() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final INotificationElement[] els = new INotificationElement[8];
		
		final HashSet elements = new HashSet(8);
		elements.add( els[0] = NotificationsFactory.createSubscription("http://uri1", sf.createTemplate("?s ?p ?o ."),null) );
		elements.add( els[1] = NotificationsFactory.createSubscription("http://uri2", sf.createTemplate("?s <http://p2> ?o ."),null) );
		elements.add( els[2] = NotificationsFactory.createSubscription("http://uri3", sf.createTemplate("?s <http://p3> ?o ."),null) );
		elements.add( els[3] = NotificationsFactory.createSubscription("http://uri4", sf.createTemplate("?s <http://p4> ?o ."),null) );
		elements.add( els[4] = NotificationsFactory.createSubscription("http://uri5", sf.createTemplate("<http://s5> ?p ?o ."),null) );
		elements.add( els[5] = NotificationsFactory.createSubscription("http://uri6", sf.createTemplate("?s <http://p6> ?o ."),null) );
		elements.add( els[6] = NotificationsFactory.createSubscription("http://uri7", sf.createTemplate("?s <http://p7> ?o ."),null) );
		elements.add( els[7] = NotificationsFactory.createSubscription("http://uri8", sf.createTemplate("?s <http://p8> ?o ."),null) );
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

	public void testSize() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		assertEquals(container.size(),0);
		assertTrue( container.add(NotificationsFactory.createSubscription("http://uri1", sf.createTemplate("?s ?p ?o ."),null)) );
		assertEquals(container.size(),1);
		assertTrue( container.add(NotificationsFactory.createSubscription("http://uri2", sf.createTemplate("?s <http://p2> ?o ."),null)) );
		assertEquals(container.size(),2);
		assertTrue( container.add(NotificationsFactory.createSubscription("http://uri3", sf.createTemplate("?s <http://p3> ?o ."),null)) );
		assertEquals(container.size(),3);
		assertTrue( container.add(NotificationsFactory.createSubscription("http://uri4", sf.createTemplate("?s <http://p4> ?o ."),null)) );
		assertEquals(container.size(),4);
	}

	public void testToArray1() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final Vector elements = new Vector(8);
		elements.add( NotificationsFactory.createSubscription("http://uri1", sf.createTemplate("?s ?p ?o ."),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri2", sf.createTemplate("?s <http://p2> ?o ."),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri3", sf.createTemplate("?s <http://p3> ?o ."),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri4", sf.createTemplate("?s <http://p4> ?o ."),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri5", sf.createTemplate("<http://s5> ?p ?o ."),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri6", sf.createTemplate("?s <http://p6> ?o ."),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri7", sf.createTemplate("?s <http://p7> ?o ."),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri8", sf.createTemplate("?s <http://p8> ?o ."),null) );
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
		elements.add( NotificationsFactory.createSubscription("http://uri1", sf.createTemplate("?s ?p ?o ."),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri2", sf.createTemplate("?s <http://p2> ?o ."),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri3", sf.createTemplate("?s <http://p3> ?o ."),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri4", sf.createTemplate("?s <http://p4> ?o ."),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri5", sf.createTemplate("<http://s5> ?p ?o ."),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri6", sf.createTemplate("?s <http://p6> ?o ."),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri7", sf.createTemplate("?s <http://p7> ?o ."),null) );
		elements.add( NotificationsFactory.createSubscription("http://uri8", sf.createTemplate("?s <http://p8> ?o ."),null) );
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
		elements.add( els[0] = NotificationsFactory.createSubscription("http://uri1", sf.createTemplate("?s ?p ?o ."),null) );
		elements.add( els[1] = NotificationsFactory.createSubscription("http://uri2", sf.createTemplate("?s <http://p2> ?o ."),null) );
		elements.add( els[2] = NotificationsFactory.createSubscription("http://uri3", sf.createTemplate("?s <http://p3> ?o ."),null) );
		elements.add( els[3] = NotificationsFactory.createSubscription("http://uri4", sf.createTemplate("?s <http://p4> ?o ."),null) );
		elements.add( els[4] = NotificationsFactory.createSubscription("http://uri5", sf.createTemplate("<http://s5> ?p ?o ."),null) );
		elements.add( els[5] = NotificationsFactory.createSubscription("http://uri6", sf.createTemplate("?s <http://p6> ?o ."),null) );
		elements.add( els[6] = NotificationsFactory.createSubscription("http://uri7", sf.createTemplate("?s <http://p7> ?o ."),null) );
		elements.add( els[7] = NotificationsFactory.createSubscription("http://uri8", sf.createTemplate("?s <http://p8> ?o ."),null) );
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

	public void testGet2() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final INotificationElement[] els = new INotificationElement[8];
		final HashSet elements = new HashSet(8);
		elements.add( els[0] = NotificationsFactory.createSubscription("http://uri1", sf.createTemplate("?s ?p ?o ."),null) );
		elements.add( els[1] = NotificationsFactory.createSubscription("http://uri2", sf.createTemplate("?s <http://p2> ?o ."),null) );
		elements.add( els[2] = NotificationsFactory.createSubscription("http://uri3", sf.createTemplate("?s <http://p3> ?o ."),null) );
		elements.add( els[3] = NotificationsFactory.createSubscription("http://uri4", sf.createTemplate("?s <http://p4> ?o ."),null) );
		elements.add( els[4] = NotificationsFactory.createSubscription("http://uri5", sf.createTemplate("<http://s5> ?p ?o ."),null) );
		elements.add( els[5] = NotificationsFactory.createSubscription("http://uri6", sf.createTemplate("?s <http://p6> ?o ."),null) );
		elements.add( els[6] = NotificationsFactory.createSubscription("http://uri7", sf.createTemplate("?s <http://p7> ?o ."),null) );
		elements.add( els[7] = NotificationsFactory.createSubscription("http://uri8", sf.createTemplate("?s <http://p8> ?o ."),null) );
		assertTrue( container.addAll(elements) );
		assertEquals(container.size(),elements.size());
		
		assertEquals(container.get(sf.createTemplate("?s ?p ?o .")),els[0]);
		assertEquals(container.get(sf.createTemplate("?s <http://p8> ?o .")),els[7]);
		assertEquals(container.get(sf.createTemplate("?s <http://p7> ?o .")),els[6]);
		assertEquals(container.get(sf.createTemplate("?s <http://p2> ?o .")),els[1]);
		assertEquals(container.get(sf.createTemplate("?s <http://p3> ?o .")),els[2]);
		assertEquals(container.get(sf.createTemplate("?s <http://p4> ?o .")),els[3]);
		assertEquals(container.get(sf.createTemplate("?s <http://p6> ?o .")),els[5]);
		assertEquals(container.get(sf.createTemplate("<http://s5> ?p ?o .")),els[4]);
		assertNull(container.get(sf.createTemplate("<http://imposible> <http://is> <http://nothing> .")));
	}

	public void testGetThoseWhichMatch() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final INotificationElement[] els = new INotificationElement[8];
		final HashSet elements = new HashSet(8);
		elements.add( els[0] = NotificationsFactory.createSubscription("http://uri1", sf.createTemplate("?s ?p ?o ."),null) );
		elements.add( els[1] = NotificationsFactory.createSubscription("http://uri2", sf.createTemplate("?s <http://p2> ?o ."),null) );
		elements.add( els[2] = NotificationsFactory.createSubscription("http://uri3", sf.createTemplate("<http://s2> <http://p2> ?o ."),null) );
		elements.add( els[3] = NotificationsFactory.createSubscription("http://uri4", sf.createTemplate("?s <http://p2> <http://o2> ."),null) );
		elements.add( els[4] = NotificationsFactory.createSubscription("http://uri5", sf.createTemplate("<http://s5> ?p ?o ."),null) );
		elements.add( els[5] = NotificationsFactory.createSubscription("http://uri6", sf.createTemplate("?s <http://p6> ?o ."),null) );
		elements.add( els[6] = NotificationsFactory.createSubscription("http://uri7", sf.createTemplate("?s <http://p7> ?o ."),null) );
		elements.add( els[7] = NotificationsFactory.createSubscription("http://uri8", sf.createTemplate("?s <http://p8> ?o ."),null) );
		assertTrue( container.addAll(elements) );
		assertEquals(container.size(),elements.size());
		
		HashSet those = container.getThoseWhichMatch(sf.createTemplate("?s ?p ?o ."));
		assertEquals(those.size(),elements.size());
		assertTrue( those.contains(els[0]) );
		assertTrue( those.contains(els[1]) );
		assertTrue( those.contains(els[2]) );
		assertTrue( those.contains(els[3]) );
		assertTrue( those.contains(els[4]) );
		assertTrue( those.contains(els[5]) );
		assertTrue( those.contains(els[6]) );
		assertTrue( those.contains(els[7]) );
		
		those = container.getThoseWhichMatch(sf.createTemplate("?s <http://p2> ?o ."));
		assertEquals(those.size(),3);
		assertFalse( those.contains(els[0]) );
		assertTrue( those.contains(els[1]) );
		assertTrue( those.contains(els[2]) );
		assertTrue( those.contains(els[3]) );
		assertFalse( those.contains(els[4]) );
		assertFalse( those.contains(els[5]) );
		assertFalse( those.contains(els[6]) );
		assertFalse( those.contains(els[7]) );
		
		those = container.getThoseWhichMatch(sf.createTemplate("<http://imposible> <http://is> <http://nothing> ."));
		assertEquals(those.size(),0);
	}
}