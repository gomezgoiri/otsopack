package otsopack.full.java.network.communication;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
	 *   - Node A signs every graph as http://ts.morelab.deusto.es/users/u/nodeA
	 *   - Node B signs every graph as http://ts.morelab.deusto.es/users/u/nodeB
	 *   - Node C does not sign any graph
	 * 
	 */
	
	private static final String HTTP_SPACE1 = "http://space1/";

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
		return manager;
	}
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		this.nodeA = createAndStartOtsoServer(OTSO_TESTING_PORT_NODE_A);
		this.nodeB = createAndStartOtsoServer(OTSO_TESTING_PORT_NODE_B);
		this.nodeC = createAndStartOtsoServer(OTSO_TESTING_PORT_NODE_C);
		
		final IRegistry registry = new SimpleRegistry(HTTP_SPACE1, getNodeAurl(), getNodeBurl(), getNodeCurl());
		final RestMulticastCommunication multicastProvider = new RestMulticastCommunication(registry);
		
		this.proxyP = createAndStartOtsoServer(OTSO_TESTING_PORT_PROXY_P, multicastProvider, false);
		
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
	public void testMulticast() throws Exception {
		
	}
	
	public String getNodeAurl(){
		return "http://127.0.0.1:" + OTSO_TESTING_PORT_NODE_A;
	}
	
	public String getNodeBurl(){
		return "http://127.0.0.1:" + OTSO_TESTING_PORT_NODE_B;
	}
	
	public String getNodeCurl(){
		return "http://127.0.0.1:" + OTSO_TESTING_PORT_NODE_C;
	}
	
	public String getProxyUrl(){
		return "http://127.0.0.1:" + OTSO_TESTING_PORT_PROXY_P;
	}
}
