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
package otsopack.full.java.network.coordination.registry;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import otsopack.full.java.network.coordination.IDiscovery;
import otsopack.full.java.network.coordination.SpaceManager;
import otsopack.full.java.network.coordination.discovery.SimpleDiscovery;
import otsopack.full.java.network.coordination.spacemanager.http.SpaceManagerManager;

public class SimpleRegistryTest {
	
	private int port = 18087;
	private SpaceManagerManager manager;
	
	@Before
	public void setUp() throws Exception {
		this.manager = new SpaceManagerManager(this.port);
		this.manager.startSpaceManagerServer();
		
		
	}
	
	@After
	public void tearDown() throws Exception {
		this.manager.stopSpaceManagerServer();
	}
	
	@Test
	public void testRegistry() throws Exception {
		
		final String spaceURI = "http://space1/";
		
		final IDiscovery discovery = new SimpleDiscovery(new SpaceManager(this.manager.createClientAddress()));
		final SimpleRegistry registry = new SimpleRegistry(spaceURI, discovery){
			@Override
			public int getInterval(){
				return 10;
			}
		};
		registry.start();
		
		final Set<String> nodes = registry.getNodesBaseURLs();
		assertThat(nodes, hasItem(SpaceManagerManager.NODE1));
		assertThat(nodes, hasItem(SpaceManagerManager.NODE2));
		
		registry.interrupt();
		for(int i = 0; i < 100; ++i){
			if(!registry.isAlive())
				break;
			Thread.sleep(10);
		}
		assertFalse(registry.isAlive());
	}
}
