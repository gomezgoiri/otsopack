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

import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.otsoME.network.communication.notifications.IAdvertisement;
import otsopack.otsoME.network.communication.notifications.NotificationsFactory;
import jmunit.framework.cldc11.TestCase;

public class AdvertisementTest extends TestCase {
	
	public AdvertisementTest() {
		super(3, "AdvertisementTest");
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
			hashCode1Test();
			break;
		case 1:
			equals1Test();
			break;
		case 2:
			clone1Test();
			break;
		}
	}


	public void hashCode1Test() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		final IAdvertisement adv1 = NotificationsFactory.createAdvertisement("http://spaceuri1", sf.createTemplate("<http://arvak> <http://es> <http://caballo> ."));
		final IAdvertisement adv2 = NotificationsFactory.createAdvertisement("http://spaceuri1", sf.createTemplate("<http://arvak> <http://es> <http://caballo> ."));
		final IAdvertisement adv3 = NotificationsFactory.createAdvertisement("http://spaceuri2", sf.createTemplate("<http://garfield> ?p <http://gato> ."));
		final IAdvertisement adv4 = NotificationsFactory.createAdvertisement("http://spaceuri2", sf.createTemplate("<http://garfield> ?p <http://gato> ."));
		final IAdvertisement adv5 = NotificationsFactory.createAdvertisement("http://spaceuri3", sf.createTemplate("<http://lassie> ?p ?o ."));
		final IAdvertisement adv6 = NotificationsFactory.createAdvertisement("http://spaceuri3", sf.createTemplate("<http://lassie> ?p ?o ."));
		
		assertEquals(adv1.hashCode(),adv2.hashCode());
		assertEquals(adv3.hashCode(),adv4.hashCode());
		assertEquals(adv5.hashCode(),adv6.hashCode());
	}
	
	public void equals1Test() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		final IAdvertisement adv1 = NotificationsFactory.createAdvertisement("http://spaceuri1", sf.createTemplate("<http://arvak> <http://es> <http://caballo> ."));
		final IAdvertisement adv2 = NotificationsFactory.createAdvertisement("http://spaceuri1", sf.createTemplate("<http://arvak> <http://es> <http://caballo> ."));
		final IAdvertisement adv3 = NotificationsFactory.createAdvertisement("http://spaceuri2", sf.createTemplate("<http://garfield> ?p <http://gato> ."));
		final IAdvertisement adv4 = NotificationsFactory.createAdvertisement("http://spaceuri2", sf.createTemplate("<http://garfield> ?p <http://gato> ."));
		final IAdvertisement adv5 = NotificationsFactory.createAdvertisement("http://spaceuri3", sf.createTemplate("<http://lassie> ?p ?o ."));
		final IAdvertisement adv6 = NotificationsFactory.createAdvertisement("http://spaceuri3", sf.createTemplate("<http://lassie> ?p ?o ."));
		
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
	
	public void clone1Test() throws MalformedTemplateException {
		final IAdvertisement adv = NotificationsFactory.createAdvertisement("http://spaceuri1", new SemanticFactory().createTemplate("<http://arvak> <http://es> <http://caballo> ."));
		final IAdvertisement clonedAdv = (IAdvertisement) adv.clone();
		
		assertFalse( adv==clonedAdv );
		assertEquals( adv, clonedAdv );
		assertFalse( adv.getURI()==clonedAdv.getURI() );
		assertEquals( adv.getURI(), clonedAdv.getURI() );
		assertFalse( adv.getTemplate()==clonedAdv.getTemplate() );
		assertEquals( adv.getTemplate(), clonedAdv.getTemplate() );
	}
}
