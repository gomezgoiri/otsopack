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
package otsopack.full.java.network.coordination.discovery.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import otsopack.full.java.network.coordination.IDiscovery;
import otsopack.full.java.network.coordination.ISpaceManager;
import otsopack.full.java.network.coordination.discovery.SimpleDiscovery;
import otsopack.full.java.network.coordination.discovery.http.server.DiscoveryController;
import otsopack.full.java.network.coordination.discovery.http.server.DiscoveryRestServer;
import otsopack.full.java.network.coordination.discovery.http.server.IDiscoveryController;
import otsopack.full.java.network.coordination.discovery.http.server.resources.DiscoveryResource;
import otsopack.full.java.network.coordination.spacemanager.HttpSpaceManager;

public class DiscoveryHttpTest extends Object {
	
	private static final String SPACE_MANAGER1 = "http://space.manager1/";
	private static final String SPACE_MANAGER2 = "http://space.manager2/";
	private static final String SPACE_MANAGER3 = "http://space.manager3/";
	private static final String SPACE_MANAGER4 = "http://space.manager4/";
	private static final String SPACE_MANAGER5 = "http://space.manager5/";
	
	private static final String SPACE2 = "http://space2";
	private static final String SPACE1 = "http://space1";
	
	private int PORT = 18085;
	private DiscoveryRestServer server;
	private IDiscovery discovery;
	private IDiscovery client;
	
	private ISpaceManager sp1;
	private ISpaceManager sp2;
	private ISpaceManager sp3;
	private ISpaceManager sp4;
	private ISpaceManager sp5;
	
	private void initialize(boolean withDefault) throws Exception {
		final Map<String, ISpaceManager[]> managers = new HashMap<String, ISpaceManager[]>();
		
		this.sp1 = new HttpSpaceManager(SPACE_MANAGER1);
		this.sp2 = new HttpSpaceManager(SPACE_MANAGER2);
		this.sp3 = new HttpSpaceManager(SPACE_MANAGER3);
		this.sp4 = new HttpSpaceManager(SPACE_MANAGER4);
		this.sp5 = new HttpSpaceManager(SPACE_MANAGER5);
		
		managers.put(SPACE1, new ISpaceManager[]{this.sp1, this.sp2});
		managers.put(SPACE2, new ISpaceManager[]{this.sp3, this.sp4});
		if(withDefault)
			managers.put("", new ISpaceManager[]{ this.sp5 });
		
		this.discovery = new SimpleDiscovery(managers);
		final IDiscoveryController controller = new DiscoveryController(this.discovery);
		this.server = new DiscoveryRestServer(this.PORT, controller);
		this.server.startup();
		
		this.client = new HttpDiscoveryClient("http://127.0.0.1:" + this.PORT + DiscoveryResource.ROOT);
	}
	
	@After
	public void tearDown() throws Exception{
		this.server.shutdown();
	}
	
	@Test
	public void testDiscoveryResourceSpace1() throws Exception {
		initialize(false);
		
		final List<ISpaceManager> spaceManagers = Arrays.asList(this.client.getSpaceManagers(SPACE1));
		assertEquals(2, spaceManagers.size());
		assertThat(spaceManagers, hasItem( this.sp1 ));
		assertThat(spaceManagers, hasItem( this.sp2 ));
	}
	
	@Test
	public void testDiscoveryResourceSpace2() throws Exception {
		initialize(true);
		
		final List<ISpaceManager> spaceManagers = Arrays.asList(this.client.getSpaceManagers(SPACE2));
		assertEquals(2, spaceManagers.size());
		assertThat(spaceManagers, hasItem( this.sp3 ));
		assertThat(spaceManagers, hasItem( this.sp4 ));
	}
	
	@Test
	public void testDiscoveryResourceWithDefault() throws Exception {
		initialize(true);
		
		final List<ISpaceManager> spaceManagers = Arrays.asList(this.client.getSpaceManagers("http://foo"));
		assertEquals(1, spaceManagers.size());
		
		assertThat(spaceManagers, hasItem( (ISpaceManager)new HttpSpaceManager(SPACE_MANAGER5) ));
	}
	
	@Test
	public void testDiscoveryResourceWithoutDefault() throws Exception {
		initialize(false);
		try{
			this.client.getSpaceManagers("http://foo");
		}catch(ResourceException re){
			assertEquals(Status.CLIENT_ERROR_NOT_FOUND, re.getStatus());
		}
	}
}
