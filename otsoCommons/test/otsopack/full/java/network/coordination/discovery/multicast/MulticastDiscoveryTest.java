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
 * Author: FILLME
 *
 */
package otsopack.full.java.network.coordination.discovery.multicast;

import static org.junit.Assert.assertArrayEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import otsopack.full.java.network.coordination.ISpaceManager;
import otsopack.full.java.network.coordination.discovery.DiscoveryException;
import otsopack.full.java.network.coordination.discovery.SimpleDiscovery;
import otsopack.full.java.network.coordination.discovery.multicast.server.DiscoveryMulticastServer;
import otsopack.full.java.network.coordination.spacemanager.HttpSpaceManager;

public class MulticastDiscoveryTest {
	private final SimpleDiscovery discovery = new SimpleDiscovery(new HttpSpaceManager("http://ts.alimerka.es/discovery/sample01/"), new HttpSpaceManager("http://ts.alimerka.es/discovery/sample02/"));
	
	private DiscoveryMulticastServer server;
	private MulticastDiscoveryClient client;
	
	@Before
	public void startUp() throws DiscoveryException {
		this.server = new DiscoveryMulticastServer(this.discovery);
		this.server.startup();
		this.client = new MulticastDiscoveryClient();
		this.client.startup();
	}
	
	@After
	public void tearDown() throws DiscoveryException {
		try{
			this.server.shutdown();
		}finally{
			this.client.shutdown();
		}
	}
	
	@Test
	public void testDataRetrieved() throws DiscoveryException, InterruptedException {
		
		Thread.sleep(100);
		
		final ISpaceManager [] managers = this.client.getSpaceManagers("");
		
		assertArrayEquals(new ISpaceManager[]{ 
				new HttpSpaceManager("http://ts.alimerka.es/discovery/sample01/"),
				new HttpSpaceManager("http://ts.alimerka.es/discovery/sample02/"),
		}, managers);
	}
}
