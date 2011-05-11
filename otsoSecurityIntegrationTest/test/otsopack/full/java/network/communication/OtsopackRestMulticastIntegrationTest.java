package otsopack.full.java.network.communication;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import otsopack.full.java.AbstractRestServerIntegrationTesting;
import otsopack.full.java.OtsoServerManager;
import otsopack.full.java.network.coordination.IDiscovery;
import otsopack.full.java.network.coordination.IRegistry;
import otsopack.full.java.network.coordination.ISpaceManager;
import otsopack.full.java.network.coordination.discovery.SimpleDiscovery;
import otsopack.full.java.network.coordination.registry.SimpleRegistry;
import otsopack.full.java.network.coordination.spacemanager.MemorySpaceManager;
import otsopack.full.java.network.coordination.spacemanager.SimpleSpaceManager;

public class OtsopackRestMulticastIntegrationTest extends AbstractRestServerIntegrationTesting {

	/*
	 * Test scenario:
	 * 
	 * There are three data provider nodes (Node A, Node B and Node C), 
	 * a proxy node P which consumes these nodes, and a client with 
	 * uses the IdP to confirm its identification. There is also a 
	 * Space Manager SM which is used by the Proxy P to know where the
	 * Nodes are. 
	 * 
	 *             ----------     ----------     ---------- 
	 *             | Node A |     | Node B |     | Node C |
	 *             ----------     ----------     ----------
	 *                   \            |             /
	 *                    \-----\     |     /------/
	 *                           \    |    /
	 *  --------------------     -----------          -------
	 *  | Space Manager SM |-----| Proxy P |----------| IdP |
	 *  --------------------     -----------          -------
	 *                                |                 /
	 *                                |                /
	 *                                |               /
	 *                           ----------          /
	 *                           | Client |---------/
	 *                           ----------
	 * 
	 * - Node A signs every graph as http://ts.morelab.deusto.es/users/u/nodeA
	 * - Node B signs every graph as http://ts.morelab.deusto.es/users/u/nodeB
	 * - Node C does not sign any graph
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
	
	public OtsopackRestMulticastIntegrationTest() {
		super(OTSO_IDP_TESTING_PORT);
	}

	private OtsoServerManager createAndStartOtsoServer(int port) throws Exception{
		final OtsoServerManager manager = new OtsoServerManager(port);
		manager.start();
		return manager;
	}
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		this.nodeA = createAndStartOtsoServer(OTSO_TESTING_PORT_NODE_A);
		this.nodeB = createAndStartOtsoServer(OTSO_TESTING_PORT_NODE_B);
		this.nodeC = createAndStartOtsoServer(OTSO_TESTING_PORT_NODE_C);
		
		this.proxyP = createAndStartOtsoServer(OTSO_TESTING_PORT_PROXY_P);
		
		final ISpaceManager spManager = new SimpleSpaceManager(getNodeAurl(), getNodeBurl(), getNodeCurl());
		final IDiscovery discovery = new SimpleDiscovery(new MemorySpaceManager(spManager));
		final IRegistry registry = new SimpleRegistry("http://space1/", discovery);
		final RestMulticastCommunication comm = new RestMulticastCommunication(registry);
		
	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
		
		this.nodeA.stop();
		this.nodeB.stop();
		this.nodeC.stop();
		
		this.proxyP.stop();
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
	
	@Test
	public void testMulticast() throws Exception {
		
	}
}
