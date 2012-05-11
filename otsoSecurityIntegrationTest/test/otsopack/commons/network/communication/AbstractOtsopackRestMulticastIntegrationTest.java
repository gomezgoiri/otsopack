/*
 * Copyright (C) 2011 onwards University of Deusto
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
package otsopack.commons.network.communication;

import org.junit.After;
import org.junit.Before;

import otsopack.commons.AbstractRestServerIntegrationTesting;
import otsopack.commons.OtsoServerManager;
import otsopack.commons.authz.entities.IEntity;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.network.ICommunication;
import otsopack.commons.network.communication.RestMulticastCommunication;
import otsopack.commons.network.communication.RestUnicastCommunication;
import otsopack.commons.network.coordination.IRegistry;
import otsopack.commons.network.coordination.IRegistryManager;
import otsopack.commons.network.coordination.Node;
import otsopack.commons.network.coordination.registry.SimpleRegistry;

public abstract class AbstractOtsopackRestMulticastIntegrationTest extends AbstractRestServerIntegrationTesting {

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
	
	protected OtsoServerManager nodeA;
	protected OtsoServerManager nodeB;
	protected OtsoServerManager nodeC;
	
	protected OtsoServerManager proxyP;
	
	protected RestUnicastCommunication ruc;
	
	public AbstractOtsopackRestMulticastIntegrationTest() {
		super(OTSO_IDP_TESTING_PORT);
	}

	private OtsoServerManager createAndStartOtsoServer(int port, IEntity signer) throws Exception{
		return createAndStartOtsoServer(port, signer, null, true);
	}
	
	private OtsoServerManager createAndStartOtsoServer(int port, IEntity signer, ICommunication multicastProvider, boolean provideController) throws Exception{
		final OtsoServerManager manager = new OtsoServerManager(port, signer, multicastProvider, provideController);
		manager.start();
		if(provideController)
			manager.prepareSemanticRepository();
		return manager;
	}
	
	protected IEntity getNodeASigner(){
		return this.AITOR;
	}
	
	protected IEntity getNodeBSigner(){
		return this.PABLO;
	}
	
	protected IEntity getNodeCSigner(){
		return null;
	}
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		SemanticFactory.initialize(new MicrojenaFactory());
		
		this.nodeA = createAndStartOtsoServer(OTSO_TESTING_PORT_NODE_A, getNodeASigner());
		this.nodeA.addGraph(OtsoServerManager.AITOR_GRAPH);
		this.nodeB = createAndStartOtsoServer(OTSO_TESTING_PORT_NODE_B, getNodeBSigner());
		this.nodeB.addGraph(OtsoServerManager.PABLO_GRAPH);
		this.nodeC = createAndStartOtsoServer(OTSO_TESTING_PORT_NODE_C, getNodeCSigner());
		this.nodeC.addGraph(OtsoServerManager.YODA_GRAPH);
		
		final IRegistryManager registry = new SimpleRegistry(getNodeA(), getNodeB(), getNodeC());
		registry.joinSpace(OtsoServerManager.SPACE);
		registry.startup();
		final RestMulticastCommunication multicastProvider = new RestMulticastCommunication(registry);
		
		this.proxyP = createAndStartOtsoServer(OTSO_TESTING_PORT_PROXY_P, null, multicastProvider, true);
		
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
	
	public Node getNodeA(){
		return new Node("http://127.0.0.1:" + OTSO_TESTING_PORT_NODE_A + "/", "nodeA");
	}
	
	public Node getNodeB(){
		return new Node("http://127.0.0.1:" + OTSO_TESTING_PORT_NODE_B + "/", "nodeB");
	}
	
	public Node getNodeC(){
		return new Node("http://127.0.0.1:" + OTSO_TESTING_PORT_NODE_C + "/", "nodeC");
	}
	
	public String getProxyUrl(){
		return "http://127.0.0.1:" + OTSO_TESTING_PORT_PROXY_P + "/";
	}
}
