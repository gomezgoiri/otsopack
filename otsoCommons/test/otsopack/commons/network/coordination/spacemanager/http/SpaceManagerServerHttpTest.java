/*
 * Copyright (C) 2008 onwards University of Deusto
 * 
 * All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.commons.network.coordination.spacemanager.http;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import otsopack.commons.network.coordination.Node;
import otsopack.commons.network.coordination.spacemanager.HttpSpaceManager;
import otsopack.commons.network.coordination.spacemanager.SpaceManagerException;

public class SpaceManagerServerHttpTest {
	private int PORT = 18086;
	private HttpSpaceManager client;
	private SpaceManagerManager manager;

	@Before
	public void setUp() throws Exception {
		this.manager = new SpaceManagerManager(this.PORT);
		this.manager.startSpaceManagerServer();
		
		this.client = this.manager.createClient();
	}

	@After
	public void tearDown() throws Exception {
		this.manager.stopSpaceManagerServer();
	}

	@Test
	public void testJoinLeave() throws Exception {
		final String secret = this.client.selfJoin(12345, "id1", false, true, false);
		this.client.poll(secret);
		this.client.leave(secret);
		
		// If it does not exist anymore, there should be no problem
		this.client.leave(secret);
		
		try{
			this.client.poll(secret);
			fail(SpaceManagerException.class.getName() + " expected");
		}catch(SpaceManagerException e) {
			// ok
		}
	}
	
	@Test
	public void testGetNodes() throws Exception {
		final List<Node> nodes = Arrays.asList(this.client.getNodes());
		assertThat(nodes, hasItem(SpaceManagerManager.NODE1));
		assertThat(nodes, hasItem(SpaceManagerManager.NODE2));
	}
}
