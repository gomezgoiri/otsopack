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
package otsopack.full.java.network.communication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static otsopack.full.java.network.communication.GraphAssert.assertGraphEquals;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import otsopack.commons.IController;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.WildcardTemplate;
import otsopack.commons.dataaccess.memory.MemoryDataAccess;
import otsopack.full.java.network.coordination.IRegistry;
import otsopack.full.java.network.coordination.Node;
import otsopack.full.java.network.coordination.discovery.SimpleDiscovery;
import otsopack.full.java.network.coordination.registry.SimpleRegistry;
import otsopack.full.java.network.coordination.spacemanager.SimpleSpaceManager;
import otsopack.full.java.network.coordination.spacemanager.SpaceManager;

public class RestMulticastCommunicationTest {

	private final String spaceURI = "http://space1/";
	
	private static final int STARTING_PORT = 9050;
	private static final int SERVERS = 10;
	
	private int currentStartingPort = STARTING_PORT;
	
	private OtsoRestServer [] restServers;
	private IController [] controllers;
	private Node [] nodes;
	private SpaceManager spaceManager;
	private IRegistry registry;
	
	private RestMulticastCommunication comm;
	
	public static final String DEPICTION = "http://xmlns.com/foaf/0.1/depiction";
	
	public static final String PABLO_DEPICTION = "http://paginaspersonales.deusto.es/porduna/images/porduna.png";
	
	public static final Graph PABLO_GRAPH = new Graph(
			"<http://pablo.ordunya.com/me> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> . \n" +
			"<http://pablo.ordunya.com/me> <http://xmlns.com/foaf/0.1/name> \"Pablo Orduna\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
			"<http://pablo.ordunya.com/me> <http://xmlns.com/foaf/0.1/title> \"Sr\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
			"<http://pablo.ordunya.com/me> <http://xmlns.com/foaf/0.1/givenname> \"Pablo\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
			"<http://pablo.ordunya.com/me> <http://xmlns.com/foaf/0.1/family_name> \"Orduna\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
			"<http://pablo.ordunya.com/me> <http://xmlns.com/foaf/0.1/homepage> <http://pablo.ordunya.com> . \n" +
			"<http://pablo.ordunya.com/me> <" + DEPICTION + "> <" + PABLO_DEPICTION + "> . \n",
			SemanticFormat.NTRIPLES);
	
	public static final Graph PABLO_DEPICTION_GRAPH = new Graph(
			"<http://pablo.ordunya.com/me> <" + DEPICTION + "> <" + PABLO_DEPICTION + "> . \n",
			SemanticFormat.NTRIPLES);
	
	public static final String AITOR_DEPICTION = "http://aitor.gomezgoiri.net/profile.jpg";
	
	public static final Graph AITOR_GRAPH = new Graph(
			"<http://aitor.gomezgoiri.net/me> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> . \n" +
			"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/name> \"Aitor Gomez-Goiri\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
			"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/title> \"Sr\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
			"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/givenname> \"Aitor\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
			"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/family_name> \"Gomez-Goiri\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
			"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/homepage> <http://aitor.gomezgoiri.net> . \n" +
			"<http://aitor.gomezgoiri.net/me> <" + DEPICTION + "> <" + AITOR_DEPICTION + "> . \n",
			SemanticFormat.NTRIPLES);
	
	public static final Graph AITOR_DEPICTION_GRAPH = new Graph(
			"<http://aitor.gomezgoiri.net/me> <" + DEPICTION + "> <" + AITOR_DEPICTION + "> . \n",
			SemanticFormat.NTRIPLES);
	
	@Before
	public void setUp() throws Exception {
		assertTrue(SERVERS * 2 >= RestMulticastCommunication.MULTICAST_THREADS);
		
		this.restServers = new OtsoRestServer[SERVERS];
		this.controllers = new IController[SERVERS];
		this.nodes = new Node[SERVERS]; 
		
		for(int i = 0; i < SERVERS; ++i){
			this.controllers[i] = EasyMock.createMock(IController.class);
			EasyMock.expect(this.controllers[i].getDataAccessService()).andReturn(new MemoryDataAccess()).anyTimes();
			EasyMock.replay(this.controllers[i]);
			
			this.restServers[i] = new OtsoRestServer(this.currentStartingPort + i, this.controllers[i], null, null);
			this.restServers[i].startup();
			
			this.nodes[i] = new Node("http://localhost:" + (this.currentStartingPort + i) + "/", "node" + i, false, false);
		}

		this.spaceManager = new SimpleSpaceManager(this.nodes);
		this.spaceManager.startup();
		
		this.registry = new SimpleRegistry(this.spaceURI, new SimpleDiscovery(this.spaceManager));
		
		this.comm = new RestMulticastCommunication(this.registry);
		this.comm.startup();
	}
	
	@After
	public void tearDown() throws Exception {
		this.comm.shutdown();
		
		this.spaceManager.shutdown();
		
		for(int i = 0; i < SERVERS; ++i)
			this.restServers[i].shutdown();
		
		this.currentStartingPort += SERVERS;
	}
	
	@Test
	public void testRead() throws Exception {
		// Long test, given that it takes several seconds to start all the servers
		
		assertTrue(SERVERS > 8);
		
		// Check when it is wrong
		final Graph emptyByUriGraph = this.comm.read(this.spaceURI, "http://foo/bar", SemanticFormat.NTRIPLES, 100);
		assertEquals(null, emptyByUriGraph);
		
		final Graph emptyByTemplateGraph = this.comm.read(this.spaceURI, WildcardTemplate.createWithURI(null, null, "http://this.does.not.exist"), SemanticFormat.NTRIPLES, 100);
		assertEquals(null, emptyByTemplateGraph);
		
		// Add information to 2 stores
		
		this.controllers[3].getDataAccessService().startup();
		this.controllers[3].getDataAccessService().createSpace(this.spaceURI);
		this.controllers[3].getDataAccessService().joinSpace(this.spaceURI);
		final String graphUriPablo = this.controllers[3].getDataAccessService().write(this.spaceURI, PABLO_GRAPH);
		
		this.controllers[8].getDataAccessService().startup();
		this.controllers[8].getDataAccessService().createSpace(this.spaceURI);
		this.controllers[8].getDataAccessService().joinSpace(this.spaceURI);
		final String graphUriAitor = this.controllers[8].getDataAccessService().write(this.spaceURI, AITOR_GRAPH);
		
		// Access by graph
		final Graph pabloByUriGraph = this.comm.read(this.spaceURI, graphUriPablo, SemanticFormat.NTRIPLES, 100);
		assertGraphEquals(PABLO_GRAPH, pabloByUriGraph);
		
		final Graph aitorByUriGraph = this.comm.read(this.spaceURI, graphUriAitor, SemanticFormat.NTRIPLES, 100);
		assertGraphEquals(AITOR_GRAPH, aitorByUriGraph);
		
		// Access by template
		final Graph pabloByTemplateGraph = this.comm.read(this.spaceURI, WildcardTemplate.createWithURI(null, null, PABLO_DEPICTION), SemanticFormat.NTRIPLES, 100);
		assertGraphEquals(PABLO_GRAPH, pabloByTemplateGraph);
		
		final Graph aitorByTemplateGraph = this.comm.read(this.spaceURI, WildcardTemplate.createWithURI(null, null, AITOR_DEPICTION), SemanticFormat.NTRIPLES, 100);
		assertGraphEquals(AITOR_GRAPH, aitorByTemplateGraph);
		
		// Access any of them by template
		final Graph anyByTemplateGraph = this.comm.read(this.spaceURI, WildcardTemplate.createWithNull(null, null), SemanticFormat.NTRIPLES, 100);
		assertNotNull(anyByTemplateGraph);
		// It is Pablo...
		try{
			assertGraphEquals(PABLO_GRAPH, anyByTemplateGraph);
		}catch(AssertionError ae){
			// ...or Aitor
			assertGraphEquals(AITOR_GRAPH, anyByTemplateGraph);
		}
	}
	
	@Test
	public void testTake() throws Exception {
		// Long test, given that it takes several seconds to start all the servers
		
		assertTrue(SERVERS > 8);
		
		// Check when it is wrong
		final Graph emptyByUriGraph = this.comm.take(this.spaceURI, "http://foo/bar", SemanticFormat.NTRIPLES, 100);
		assertEquals(null, emptyByUriGraph);
		
		final Graph emptyByTemplateGraph = this.comm.take(this.spaceURI, WildcardTemplate.createWithURI(null, null, "http://this.does.not.exist"), SemanticFormat.NTRIPLES, 100);
		assertEquals(null, emptyByTemplateGraph);
		
		// Add information to 2 stores
		
		this.controllers[3].getDataAccessService().startup();
		this.controllers[3].getDataAccessService().createSpace(this.spaceURI);
		this.controllers[3].getDataAccessService().joinSpace(this.spaceURI);
		this.controllers[8].getDataAccessService().startup();
		this.controllers[8].getDataAccessService().createSpace(this.spaceURI);
		this.controllers[8].getDataAccessService().joinSpace(this.spaceURI);
		
		final String graphUriPablo = this.controllers[3].getDataAccessService().write(this.spaceURI, PABLO_GRAPH);
		final String graphUriAitor = this.controllers[8].getDataAccessService().write(this.spaceURI, AITOR_GRAPH);
		
		// Access by graph
		final Graph pabloByUriGraph = this.comm.take(this.spaceURI, graphUriPablo, SemanticFormat.NTRIPLES, 100);
		assertGraphEquals(PABLO_GRAPH, pabloByUriGraph);		
		this.controllers[3].getDataAccessService().write(this.spaceURI, PABLO_GRAPH);
		
		final Graph aitorByUriGraph = this.comm.take(this.spaceURI, graphUriAitor, SemanticFormat.NTRIPLES, 100);
		assertGraphEquals(AITOR_GRAPH, aitorByUriGraph);
		this.controllers[8].getDataAccessService().write(this.spaceURI, AITOR_GRAPH);

		// Access by template
		final Graph pabloByTemplateGraph = this.comm.take(this.spaceURI, WildcardTemplate.createWithURI(null, null, PABLO_DEPICTION), SemanticFormat.NTRIPLES, 100);
		assertGraphEquals(PABLO_GRAPH, pabloByTemplateGraph);
		this.controllers[3].getDataAccessService().write(this.spaceURI, PABLO_GRAPH);
		
		final Graph aitorByTemplateGraph = this.comm.take(this.spaceURI, WildcardTemplate.createWithURI(null, null, AITOR_DEPICTION), SemanticFormat.NTRIPLES, 100);
		assertGraphEquals(AITOR_GRAPH, aitorByTemplateGraph);
		this.controllers[8].getDataAccessService().write(this.spaceURI, AITOR_GRAPH);
		
		// Access any of them by template
		final Graph anyByTemplateGraph = this.comm.take(this.spaceURI, WildcardTemplate.createWithNull(null, null), SemanticFormat.NTRIPLES, 100);
		assertNotNull(anyByTemplateGraph);
		// It is Pablo or Aitor.
		try{
			assertGraphEquals(PABLO_GRAPH, anyByTemplateGraph);
			// If it was pablo, Aitor is still there
			final Graph remainingGraph = this.controllers[8].getDataAccessService().read(this.spaceURI, WildcardTemplate.createWithNull(null, null), SemanticFormat.NTRIPLES);
			assertGraphEquals(AITOR_GRAPH, remainingGraph);
		}catch(AssertionError ae){
			// ...or it was Aitor
			assertGraphEquals(AITOR_GRAPH, anyByTemplateGraph);
			
			// And therefore, Pablo is still there
			final Graph remainingGraph = this.controllers[3].getDataAccessService().read(this.spaceURI, WildcardTemplate.createWithNull(null, null), SemanticFormat.NTRIPLES);
			assertGraphEquals(PABLO_GRAPH, remainingGraph);
		}
	}

	@Test
	public void testQuery() throws Exception {
		// Long test, given that it takes several seconds to start all the servers
		
		assertTrue(SERVERS > 8);
		
		// Check when it is wrong
		final Graph [] emptyByTemplateGraph = this.comm.query(this.spaceURI, WildcardTemplate.createWithURI(null, null, "http://this.does.not.exist"), SemanticFormat.NTRIPLES, 100);
		assertArrayEquals(null, emptyByTemplateGraph);
		
		// Add information to 2 stores
		
		this.controllers[3].getDataAccessService().startup();
		this.controllers[3].getDataAccessService().createSpace(this.spaceURI);
		this.controllers[3].getDataAccessService().joinSpace(this.spaceURI);
		this.controllers[3].getDataAccessService().write(this.spaceURI, PABLO_GRAPH);
		
		this.controllers[8].getDataAccessService().startup();
		this.controllers[8].getDataAccessService().createSpace(this.spaceURI);
		this.controllers[8].getDataAccessService().joinSpace(this.spaceURI);
		this.controllers[8].getDataAccessService().write(this.spaceURI, AITOR_GRAPH);
				
		// Access by template
		final Graph [] pabloByTemplateGraph = this.comm.query(this.spaceURI, WildcardTemplate.createWithURI(null, null, PABLO_DEPICTION), SemanticFormat.NTRIPLES, 100);
		assertNotNull(pabloByTemplateGraph);
		assertEquals(1, pabloByTemplateGraph.length);
		assertGraphEquals(PABLO_DEPICTION_GRAPH, pabloByTemplateGraph[0]);
		
		final Graph [] aitorByTemplateGraph = this.comm.query(this.spaceURI, WildcardTemplate.createWithURI(null, null, AITOR_DEPICTION), SemanticFormat.NTRIPLES, 100);
		assertNotNull(aitorByTemplateGraph);
		assertEquals(1, aitorByTemplateGraph.length);
		assertGraphEquals(AITOR_DEPICTION_GRAPH, aitorByTemplateGraph[0]);
		
		// Access any of them by template
		final Graph [] anyByTemplateGraph = this.comm.query(this.spaceURI, WildcardTemplate.createWithNull(null, DEPICTION), SemanticFormat.NTRIPLES, 100);
		assertNotNull(anyByTemplateGraph);
		assertEquals(2, anyByTemplateGraph.length);
		// It is Pablo and Aitor...
		try{
			assertGraphEquals(PABLO_DEPICTION_GRAPH, anyByTemplateGraph[0]);
			assertGraphEquals(AITOR_DEPICTION_GRAPH, anyByTemplateGraph[1]);
		}catch(AssertionError ae){
			// ...or Aitor and Pablo
			assertGraphEquals(AITOR_DEPICTION_GRAPH, anyByTemplateGraph[0]);
			assertGraphEquals(PABLO_DEPICTION_GRAPH, anyByTemplateGraph[1]);
		}
	}
}
