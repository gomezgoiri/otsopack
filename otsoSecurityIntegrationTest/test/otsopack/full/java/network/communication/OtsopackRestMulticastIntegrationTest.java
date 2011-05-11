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
 */
package otsopack.full.java.network.communication;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNull;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.network.ICommunication;
import otsopack.full.java.AbstractRestServerIntegrationTesting;
import otsopack.full.java.OtsoServerManager;
import otsopack.full.java.network.coordination.IRegistry;
import otsopack.full.java.network.coordination.registry.SimpleRegistry;

public class OtsopackRestMulticastIntegrationTest extends AbstractRestServerIntegrationTesting {

	/*
	 * TEST SCENARIO:
	 * 
	 *   There are three data provider nodes (Node A, Node B and Node C), 
	 *   a proxy node P which consumes these nodes, and a client with 
	 *   uses the IdP to confirm its identification. There is also a 
	 *   Space Manager SM which is used by the Proxy P to know where the
	 *   Nodes are.
	 *   
	 *               ----------     ----------     ---------- 
	 *               | Node A |     | Node B |     | Node C |
	 *               ----------     ----------     ----------
	 *                     \            |             /
	 *                      \-----\     |     /------/
	 *                             \    |    /
	 *    --------------------     -----------          -------
	 *    | Space Manager SM |-----| Proxy P |----------| IdP |
	 *    --------------------     -----------          -------
	 *                                  |                 /
	 *                                  |                /
	 *                                  |               /
	 *                             ----------          /
	 *                             | Client |---------/
	 *                             ----------
	 *   
	 *   - Node A signs every graph as http://ts.morelab.deusto.es/users/u/aigomez
	 *     + It has a single graph (AITOR_GRAPH)
	 *   - Node B signs every graph as http://ts.morelab.deusto.es/users/u/porduna
	 *     + It has a single graph (PABLO_GRAPH)
	 *   - Node C does not sign any graph
	 *     + It has a single graph (YODA_GRAPH)
	 * 
	 */
	
	private static final int OTSO_IDP_TESTING_PORT = 18082;
	
	private static final int OTSO_TESTING_PORT_NODE_A   = 18083;
	private static final int OTSO_TESTING_PORT_NODE_B   = 18084;
	private static final int OTSO_TESTING_PORT_NODE_C   = 18085;
	private static final int OTSO_TESTING_PORT_PROXY_P  = 18086;
	
	private OtsoServerManager nodeA;
	private OtsoServerManager nodeB;
	private OtsoServerManager nodeC;
	
	private OtsoServerManager proxyP;
	
	private RestUnicastCommunication ruc;
	
	public OtsopackRestMulticastIntegrationTest() {
		super(OTSO_IDP_TESTING_PORT);
	}

	private OtsoServerManager createAndStartOtsoServer(int port) throws Exception{
		return createAndStartOtsoServer(port, null, true);
	}
	
	private OtsoServerManager createAndStartOtsoServer(int port, ICommunication multicastProvider, boolean provideController) throws Exception{
		final OtsoServerManager manager = new OtsoServerManager(port, multicastProvider, provideController);
		manager.start();
		if(provideController)
			manager.prepareSemanticRepository();
		return manager;
	}
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		SemanticFactory.initialize(new MicrojenaFactory());
		
		this.nodeA = createAndStartOtsoServer(OTSO_TESTING_PORT_NODE_A);
		this.nodeA.addGraph(OtsoServerManager.AITOR_GRAPH);
		this.nodeB = createAndStartOtsoServer(OTSO_TESTING_PORT_NODE_B);
		this.nodeB.addGraph(OtsoServerManager.PABLO_GRAPH);
		this.nodeC = createAndStartOtsoServer(OTSO_TESTING_PORT_NODE_C);
		this.nodeC.addGraph(OtsoServerManager.YODA_GRAPH);
		
		final IRegistry registry = new SimpleRegistry(OtsoServerManager.SPACE, getNodeAurl(), getNodeBurl(), getNodeCurl());
		final RestMulticastCommunication multicastProvider = new RestMulticastCommunication(registry);
		
		this.proxyP = createAndStartOtsoServer(OTSO_TESTING_PORT_PROXY_P, multicastProvider, true);
		
		this.ruc = new RestUnicastCommunication(getProxyUrl());
		this.ruc.startup();
	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
		
		this.nodeA.stop();
		this.nodeB.stop();
		this.nodeC.stop();
		
		this.proxyP.stop();
	}
	
	@Test
	public void testMulticastReadWithoutSecurity() throws Exception {
		final Graph graphA = this.ruc.read(OtsoServerManager.SPACE, this.nodeA.getGraphUris().get(0), SemanticFormat.NTRIPLES, 1000);
		assertGraphEquals(OtsoServerManager.AITOR_GRAPH, graphA);
		
		final Graph graphB = this.ruc.read(OtsoServerManager.SPACE, this.nodeB.getGraphUris().get(0), SemanticFormat.NTRIPLES, 1000);
		assertGraphEquals(OtsoServerManager.PABLO_GRAPH, graphB);
		
		final Graph graphC = this.ruc.read(OtsoServerManager.SPACE, this.nodeC.getGraphUris().get(0), SemanticFormat.NTRIPLES, 1000);
		assertGraphEquals(OtsoServerManager.YODA_GRAPH, graphC);
		
		final Graph graphD = this.ruc.read(OtsoServerManager.SPACE, "http://not.existing.uri/", SemanticFormat.NTRIPLES, 1000);
		assertNull(graphD);
	}

	public String getNodeAurl(){
		return "http://127.0.0.1:" + OTSO_TESTING_PORT_NODE_A + "/";
	}
	
	public String getNodeBurl(){
		return "http://127.0.0.1:" + OTSO_TESTING_PORT_NODE_B + "/";
	}
	
	public String getNodeCurl(){
		return "http://127.0.0.1:" + OTSO_TESTING_PORT_NODE_C + "/";
	}
	
	public String getProxyUrl(){
		return "http://127.0.0.1:" + OTSO_TESTING_PORT_PROXY_P + "/";
	}
}
