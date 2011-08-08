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
import otsopack.full.java.network.coordination.Node;

public class SpaceManagerServerHttpTest {
	private int PORT = 18086;
	private ISpaceManager client;
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
	public void testGetNodes() throws Exception {
		final List<Node> nodes = Arrays.asList(this.client.getNodes());
		assertThat(nodes, hasItem(SpaceManagerManager.NODE1));
		assertThat(nodes, hasItem(SpaceManagerManager.NODE2));
	}
}
