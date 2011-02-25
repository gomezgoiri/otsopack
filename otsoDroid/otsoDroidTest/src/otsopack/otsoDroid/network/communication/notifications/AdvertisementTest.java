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
package otsopack.otsoDroid.network.communication.notifications;

import junit.framework.TestCase;
import otsopack.otsoCommons.data.ISemanticFactory;
import otsopack.otsoCommons.data.impl.SemanticFactory;
import otsopack.otsoCommons.data.impl.microjena.MicrojenaFactory;
import otsopack.otsoCommons.exceptions.MalformedTemplateException;

public class AdvertisementTest extends TestCase {
	
	public void setUp() throws Exception {
		super.setUp();
		SemanticFactory.initialize(new MicrojenaFactory());
	}
	
	public void tearDown() {
	}

	public void testHashCode() throws MalformedTemplateException {
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
	
	public void testEquals() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		final IAdvertisement adv1 = NotificationsFactory.createAdvertisement("http://spaceuri1", sf.createTemplate("<http://arvak> <http://es> <http://caballo> ."));
		final IAdvertisement adv2 = NotificationsFactory.createAdvertisement("http://spaceuri1", sf.createTemplate("<http://arvak> <http://es> <http://caballo> ."));
		final IAdvertisement adv3 = NotificationsFactory.createAdvertisement("http://spaceuri2", sf.createTemplate("<http://garfield> ?p <http://gato> ."));
		final IAdvertisement adv4 = NotificationsFactory.createAdvertisement("http://spaceuri2", sf.createTemplate("<http://garfield> ?p <http://gato> ."));
		final IAdvertisement adv5 = NotificationsFactory.createAdvertisement("http://spaceuri3", sf.createTemplate("<http://lassie> ?p ?o ."));
		final IAdvertisement adv6 = NotificationsFactory.createAdvertisement("http://spaceuri3", sf.createTemplate("<http://lassie> ?p ?o ."));
		
		assertEquals(adv1,adv2);
		assertEquals(adv1,adv2);
		assertNotSame(adv1,adv3);
		assertNotSame(adv1,adv4);
		assertNotSame(adv1,adv5);
		assertNotSame(adv1,adv6);
		assertEquals(adv2,adv2);
		assertEquals(adv2,adv1);
		assertNotSame(adv2,adv3);
		assertNotSame(adv2,adv4);
		assertNotSame(adv2,adv5);
		assertNotSame(adv2,adv6);
		assertEquals(adv3,adv3);
		assertEquals(adv3,adv4);
		assertNotSame(adv3,adv1);
		assertNotSame(adv3,adv2);
		assertNotSame(adv3,adv5);
		assertNotSame(adv3,adv6);
		assertEquals(adv4,adv4);
		assertEquals(adv4,adv3);
		assertNotSame(adv4,adv1);
		assertNotSame(adv4,adv2);
		assertNotSame(adv4,adv5);
		assertNotSame(adv4,adv6);
		assertEquals(adv5,adv5);
		assertEquals(adv5,adv6);
		assertNotSame(adv5,adv1);
		assertNotSame(adv5,adv2);
		assertNotSame(adv5,adv3);
		assertNotSame(adv5,adv4);
		assertEquals(adv6,adv6);
		assertEquals(adv6,adv5);
		assertNotSame(adv6,adv1);
		assertNotSame(adv6,adv2);
		assertNotSame(adv6,adv3);
		assertNotSame(adv6,adv4);
	}
	
	public void testClone() throws MalformedTemplateException {
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
