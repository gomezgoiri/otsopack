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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.full.java.network.coordination.spacemanager.http;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import otsopack.full.java.network.coordination.ISpaceManager;
import otsopack.full.java.network.coordination.SpaceManager;
import otsopack.full.java.network.coordination.spacemanager.SimpleSpaceManager;
import otsopack.full.java.network.coordination.spacemanager.http.server.ISpaceManagerController;
import otsopack.full.java.network.coordination.spacemanager.http.server.RestServer;
import otsopack.full.java.network.coordination.spacemanager.http.server.SpaceManagerController;
import otsopack.full.java.network.coordination.spacemanager.http.server.resources.NodesResource;

public class SpaceManagerServerHttpTest {
	private static final String NODE1 = "http://node1/";
	private static final String NODE2 = "http://node2/";

	private int PORT = 18086;
	private RestServer server;
	private ISpaceManager client;

	@Before
	public void setUp() throws Exception {
		ISpaceManager spaceManager = new SimpleSpaceManager(NODE1, NODE2);
		
		final ISpaceManagerController controller = new SpaceManagerController(spaceManager);
		this.server = new RestServer(this.PORT, controller);
		this.server.startup();
		
		this.client = new SpaceManagerClient(new SpaceManager("http://127.0.0.1:" + this.PORT + NodesResource.ROOT));
	}
	
	@After
	public void tearDown() throws Exception {
		this.server.shutdown();
	}
	
	@Test
	public void testServer() throws Exception {
		final List<String> nodes = Arrays.asList(this.client.getNodes());
		assertThat(nodes, hasItem(NODE1));
		assertThat(nodes, hasItem(NODE2));
	}
}
