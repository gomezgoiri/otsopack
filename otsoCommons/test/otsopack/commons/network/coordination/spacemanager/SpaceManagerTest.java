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
package otsopack.commons.network.coordination.spacemanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import otsopack.commons.network.coordination.Node;
import otsopack.commons.network.coordination.spacemanager.SecretNotFoundException;
import otsopack.commons.network.coordination.spacemanager.SpaceManager;
import otsopack.commons.network.coordination.spacemanager.SpaceManagerException;

public class SpaceManagerTest {
	
	public static final int TIMEOUT = 50;
	public static int DEAD_TIMEOUT = 300;
	private DummySpaceManager spaceManager;
	
	@Before
	public void setUp(){
		this.spaceManager = new DummySpaceManager();
		this.spaceManager.start();
	}
	
	@After
	public void tearDown() throws Exception {
		this.spaceManager.shutdown();
		this.spaceManager.join();
	}
	
	@Test
	public void testRegistered() throws Exception {
		Node [] nodes = this.spaceManager.getNodes();
		assertEquals(0, nodes.length);
		
		this.spaceManager.setNodes(new Node[]{ new Node("http://foo", "bar", false, false)});
		
		sleepTimeout();
		nodes = this.spaceManager.getNodes();
		assertEquals(1, nodes.length);
		assertEquals("bar", nodes[0].getUuid());
		
		this.spaceManager.setNodes(new Node[]{ new Node("http://foo", "bar2", false, false)});
		
		sleepTimeout();
		nodes = this.spaceManager.getNodes();
		assertEquals(1, nodes.length);
		assertEquals("bar2", nodes[0].getUuid());
		
		this.spaceManager.setNodes(new Node[]{});
		sleepTimeout();
		nodes = this.spaceManager.getNodes();
		assertEquals(0, nodes.length);
	}

	@Test
	public void testPoll() throws Exception {
		Node [] nodes = this.spaceManager.getNodes();
		assertEquals(0, nodes.length);

		String secret = this.spaceManager.join(new Node("http://foo", "bar", false, true));
		
		// Once joined, it is active
		sleepTimeout();
		nodes = this.spaceManager.getNodes();
		assertEquals(1, nodes.length);
		
		// But if time elapses and no poll is performed, it is not in the current nodes
		sleepTimeout();
		nodes = this.spaceManager.getNodes();
		assertEquals(0, nodes.length);
		
		// However, whenever it calls poll, the node is active again
		this.spaceManager.poll(secret);
		sleepTimeout();
		nodes = this.spaceManager.getNodes();
		assertEquals(1, nodes.length);
		
		// But whenever the node actively leaves, it can not poll again
		this.spaceManager.leave(secret);
		try{
			this.spaceManager.poll(secret);
			fail("A " + SecretNotFoundException.class.getName() + " was expected");
		}catch(SecretNotFoundException snfe){
			// ok
		}
	}
	
	@Test
	public void testDeadPoll() throws Exception {
		Node [] nodes = this.spaceManager.getNodes();
		assertEquals(0, nodes.length);

		String secret = this.spaceManager.join(new Node("http://foo", "bar", false, true));
		
		sleepDeadTimeout();
		try{
			this.spaceManager.poll(secret);
			fail("A " + SecretNotFoundException.class.getName() + " was expected");
		}catch(SecretNotFoundException snfe){
			// ok
		}
	}

	@Test
	public void testDeadCheck() throws Exception {
		Node [] nodes = this.spaceManager.getNodes();
		assertEquals(0, nodes.length);

		this.spaceManager.setClientResource(this.badClientResource);

		String secret = this.spaceManager.join(new Node("http://foo", "bar", true, false));
		
		sleepDeadTimeout();
		try{
			this.spaceManager.poll(secret);
			fail("A " + SecretNotFoundException.class.getName() + " was expected");
		}catch(SecretNotFoundException snfe){
			// ok
		}
	}

	
	private int iterations = 0;

	final ClientResource goodClientResource = new ClientResource(){
		@Override
		public Representation get(){
			SpaceManagerTest.this.iterations++;
			return new StringRepresentation("");
		}
	};
	
	final ClientResource badClientResource = new ClientResource(){
		@Override
		public Representation get(){
			SpaceManagerTest.this.iterations++;
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL){
				private static final long serialVersionUID = 1339150641633327350L;

				@Override
				public void printStackTrace(){
					// Don't be so verbose
				}
			};
		}
	};
	
	@Test
	public void testCheck() throws Exception {
		Node [] nodes = this.spaceManager.getNodes();
		assertEquals(0, nodes.length);
		
		this.spaceManager.setClientResource(this.goodClientResource);
		String secret = this.spaceManager.join(new Node("http://foo", "bar", true, false));
		
		// Once joined, and with the client being checked, it is available
		sleepTimeout();
		nodes = this.spaceManager.getNodes();
		assertEquals(1, nodes.length);
		
		// But if time elapses and it becomes unreachable or there is some kind of error, it is not in the current nodes anymore
		this.spaceManager.setClientResource(this.badClientResource);
		sleepTimeout();
		sleepTimeout();
		sleepTimeout();
		sleepTimeout();
		nodes = this.spaceManager.getNodes();
		assertEquals(0, nodes.length);
		
		// However, whenever it is back again, the node appears
		this.spaceManager.setClientResource(this.goodClientResource);
		sleepTimeout();
		sleepTimeout();
		sleepTimeout();
		sleepTimeout();
		nodes = this.spaceManager.getNodes();
		assertEquals(1, nodes.length);
		
		// But whenever the node actively leaves, it will not be checked again
		this.spaceManager.leave(secret);
		this.iterations = 0;
		sleepTimeout();			

		assertEquals(0, this.iterations);
	}
	
	private void sleepTimeout() throws InterruptedException {
		Thread.sleep(TIMEOUT);
	}

	private void sleepDeadTimeout() throws InterruptedException {
		Thread.sleep(DEAD_TIMEOUT + DEAD_TIMEOUT / 2);
	}

	private static class DummySpaceManager extends SpaceManager{

		private volatile Node [] nodes;
		private volatile ClientResource clientResource;
		
		public DummySpaceManager(Node [] nodes){
			super(TIMEOUT, DEAD_TIMEOUT); // milliseconds for timeout
			this.nodes = nodes;
		}
		
		public DummySpaceManager(){
			this(new Node[]{});
		}
		public void setNodes(Node [] nodes){
			this.nodes = nodes;
		}
		
		@Override
		public int getStepTime(){
			return 1; // milliseconds
		}
		
		public void setClientResource(ClientResource clientResource){
			this.clientResource = clientResource;
		}
		
		@Override
		protected ClientResource createClientResource(String uri){
			return this.clientResource;
		}
		
		@Override
		public String[] getExternalReferences() {
			return new String[]{};
		}

		@Override
		protected Node[] getRegisteredNodes() throws SpaceManagerException {
			return this.nodes;
		}
	}
}
