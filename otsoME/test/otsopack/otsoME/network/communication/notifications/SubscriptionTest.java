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

import jmunit.framework.cldc11.AssertionFailedException;
import jmunit.framework.cldc11.TestCase;
import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.WildcardTemplate;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.exceptions.MalformedTemplateException;

public class SubscriptionTest extends TestCase {
	public SubscriptionTest() {
		super(4, "SubscriptionTest");
	}
	
	public void setUp()	throws Throwable {
		super.setUp();
		SemanticFactory.initialize(new MicrojenaFactory());
	}
	
	public void tearDown() {
	}
	
	public void test(int testNumber) throws Throwable {
		switch (testNumber) {
		case 0:
			testHashCode1();
			break;
		case 1:
			testEquals1();
			break;
		case 2:
			testClone1();
			break;
		case 3:
			testMatches1();
			break;
		}
	}
	
	public void testHashCode1() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		final ISubscription adv1 = NotificationsFactory.createSubscription("http://spaceuri1", new WildcardTemplate("http://arvak <http://es> <http://caballo> ."),null);
		final ISubscription adv2 = NotificationsFactory.createSubscription("http://spaceuri1", new WildcardTemplate("<http://arvak> <http://es> <http://caballo> ."),null);
		final ISubscription adv3 = NotificationsFactory.createSubscription("http://spaceuri2", new WildcardTemplate("<http://garfield> ?p <http://gato> ."),null);
		final ISubscription adv4 = NotificationsFactory.createSubscription("http://spaceuri2", new WildcardTemplate("<http://garfield> ?p <http://gato> ."),null);
		final ISubscription adv5 = NotificationsFactory.createSubscription("http://spaceuri3", new WildcardTemplate("<http://lassie> ?p ?o ."),null);
		final ISubscription adv6 = NotificationsFactory.createSubscription("http://spaceuri3", new WildcardTemplate("<http://lassie> ?p ?o ."),null);
		
		assertEquals(adv1.hashCode(),adv2.hashCode());
		assertEquals(adv3.hashCode(),adv4.hashCode());
		assertEquals(adv5.hashCode(),adv6.hashCode());
	}
	
	public void testEquals1() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		final ISubscription adv1 = NotificationsFactory.createSubscription("http://spaceuri1", new WildcardTemplate("<http://arvak> <http://es> <http://caballo> ."),null);
		final ISubscription adv2 = NotificationsFactory.createSubscription("http://spaceuri1", new WildcardTemplate("<http://arvak> <http://es> <http://caballo> ."),null);
		final ISubscription adv3 = NotificationsFactory.createSubscription("http://spaceuri2", new WildcardTemplate("<http://garfield> ?p <http://gato> ."),null);
		final ISubscription adv4 = NotificationsFactory.createSubscription("http://spaceuri2", new WildcardTemplate("<http://garfield> ?p <http://gato> ."),null);
		final ISubscription adv5 = NotificationsFactory.createSubscription("http://spaceuri3", new WildcardTemplate("<http://lassie> ?p ?o ."),null);
		final ISubscription adv6 = NotificationsFactory.createSubscription("http://spaceuri3", new WildcardTemplate("<http://lassie> ?p ?o ."),null);
		
		assertEquals(adv1,adv2);
		assertEquals(adv1,adv2);
		assertNotEquals(adv1,adv3);
		assertNotEquals(adv1,adv4);
		assertNotEquals(adv1,adv5);
		assertNotEquals(adv1,adv6);
		assertEquals(adv2,adv2);
		assertEquals(adv2,adv1);
		assertNotEquals(adv2,adv3);
		assertNotEquals(adv2,adv4);
		assertNotEquals(adv2,adv5);
		assertNotEquals(adv2,adv6);
		assertEquals(adv3,adv3);
		assertEquals(adv3,adv4);
		assertNotEquals(adv3,adv1);
		assertNotEquals(adv3,adv2);
		assertNotEquals(adv3,adv5);
		assertNotEquals(adv3,adv6);
		assertEquals(adv4,adv4);
		assertEquals(adv4,adv3);
		assertNotEquals(adv4,adv1);
		assertNotEquals(adv4,adv2);
		assertNotEquals(adv4,adv5);
		assertNotEquals(adv4,adv6);
		assertEquals(adv5,adv5);
		assertEquals(adv5,adv6);
		assertNotEquals(adv5,adv1);
		assertNotEquals(adv5,adv2);
		assertNotEquals(adv5,adv3);
		assertNotEquals(adv5,adv4);
		assertEquals(adv6,adv6);
		assertEquals(adv6,adv5);
		assertNotEquals(adv6,adv1);
		assertNotEquals(adv6,adv2);
		assertNotEquals(adv6,adv3);
		assertNotEquals(adv6,adv4);
	}
	
	public void testClone1() throws MalformedTemplateException {
		final ISubscription subs = NotificationsFactory.createSubscription("http://spaceuri1", new WildcardTemplate("<http://arvak> <http://es> <http://caballo> ."),null);
		final ISubscription clonedSubs = (ISubscription) subs.clone();
		
		assertFalse( subs==clonedSubs );
		assertEquals( subs, clonedSubs );
		assertFalse( subs.getURI()==clonedSubs.getURI() );
		assertEquals( subs.getURI(), clonedSubs.getURI() );
		assertFalse( subs.getTemplate()==clonedSubs.getTemplate() );
		assertEquals( subs.getTemplate(), clonedSubs.getTemplate() );
		assertTrue( subs.getListener()==clonedSubs.getListener() );
	}

	public void testMatches1() throws AssertionFailedException, MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		final ISubscription adv1 = NotificationsFactory.createSubscription("http://spaceuri1", new WildcardTemplate("<http://arvak> <http://es> <http://caballo> ."),null);
		final ISubscription adv2 = NotificationsFactory.createSubscription("http://spaceuri1", new WildcardTemplate("<http://garfield> ?p <http://gato> ."),null);
		final ISubscription adv3 = NotificationsFactory.createSubscription("http://spaceuri1", new WildcardTemplate("<http://lassie> ?p ?o ."),null);
		
		assertTrue( adv1.matches(new WildcardTemplate("<http://arvak> <http://es> <http://caballo> .")) );
		assertFalse( adv1.matches(new WildcardTemplate("<http://arvak> ?p <http://caballo> .")) );
		assertFalse( adv1.matches(new WildcardTemplate("<http://arvak> <http://es> ?o .")) );
		assertFalse( adv1.matches(new WildcardTemplate("<http://arvak> ?p ?o .")) );
		assertFalse( adv1.matches(new WildcardTemplate("?s <http://es> <http://caballo> .")) );
		assertFalse( adv1.matches(new WildcardTemplate("?s ?p <http://caballo> .")) );
		assertFalse( adv1.matches(new WildcardTemplate("?s <http://es> ?o .")) );
		assertFalse( adv1.matches(new WildcardTemplate("<http://garfield> ?p <http://gato> .")) );
		assertFalse( adv1.matches(new WildcardTemplate("<http://lassie> ?p ?o .")) );
		
		assertTrue( adv2.matches(new WildcardTemplate("<http://garfield> <http://es> <http://gato> .")) );
		assertTrue( adv2.matches(new WildcardTemplate("<http://garfield> ?p <http://gato> .")) );
		assertFalse( adv2.matches(new WildcardTemplate("<http://garfield> ?p ?o .")) );
		assertFalse( adv2.matches(new WildcardTemplate("?s ?p <http://gato> .")) );
		assertFalse( adv2.matches(new WildcardTemplate("?s ?p ?o .")) );
		assertFalse( adv2.matches(new WildcardTemplate("<http://lassie> ?p ?o .")) );
		assertFalse( adv2.matches(new WildcardTemplate("<http://arvak> <http://es> <http://caballo> .")) );
		
		assertTrue( adv3.matches(new WildcardTemplate("<http://lassie> <http://es> <http://perro> .")) );
		assertTrue( adv3.matches(new WildcardTemplate("<http://lassie> <http://es> ?o .")) );
		assertTrue( adv3.matches(new WildcardTemplate("<http://lassie> ?p <http://perro> .")) );
		assertTrue( adv3.matches(new WildcardTemplate("<http://lassie> ?p ?o .")) );
		assertFalse( adv3.matches(new WildcardTemplate("?s ?p ?o .")) );
		assertFalse( adv3.matches(new WildcardTemplate("?s <http://es> ?o .")) );
		assertFalse( adv3.matches(new WildcardTemplate("<http://garfield> ?p <http://gato> .")) );
		assertFalse( adv3.matches(new WildcardTemplate("<http://arvak> <http://es> <http://caballo> .")) );
	}
}