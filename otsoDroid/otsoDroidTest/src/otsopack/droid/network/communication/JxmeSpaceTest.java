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
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */
package otsopack.droid.network.communication;

import java.io.IOException;
import java.util.Iterator;

import otsopack.droid.network.communication.JxmeSpace;

import junit.framework.TestCase;
import net.jxta.access.AccessService;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.document.Element;
import net.jxta.endpoint.EndpointService;
import net.jxta.exception.PeerGroupException;
import net.jxta.exception.ProtocolNotSupportedException;
import net.jxta.exception.ServiceNotFoundException;
import net.jxta.id.ID;
import net.jxta.membership.MembershipService;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.pipe.PipeService;
import net.jxta.platform.JxtaLoader;
import net.jxta.platform.Module;
import net.jxta.platform.ModuleSpecID;
import net.jxta.protocol.ConfigParams;
import net.jxta.protocol.ModuleImplAdvertisement;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PeerGroupAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.rendezvous.RendezVousService;
import net.jxta.resolver.ResolverService;
import net.jxta.service.Service;

public class JxmeSpaceTest extends TestCase {
	PeerGroup netPeerGroup;
	
	public void setUp() throws Exception {
		/*//It is mandatory to clean the configuration for multiple platforms to be created in the tests
		 * String RDV_ADDRESS = "tcp://localhost:9701";
		 * ConfigurationFactory.clean();
		 * ConfigurationFactory.setName("TestPeer");
		 * ConfigurationFactory.setTCPPortRange(9900, 9910);
		 * ConfigurationFactory.setTcpPort(0);
		 * ConfigurationFactory.addSeedRendezvous(new URI(RDV_ADDRESS));
		 * ConfigurationFactory.setTcpIncoming(true);
		 * ConfigurationFactory.setTcpOutgoing(true);
		 * 
		 * PeerID rdvPeerID = (PeerID) IDFactory.fromURI(new URI("urn:jxta:uuid-59616261646162614E5047205032503349736D656452456EA465FA766F757303"));
		 * ConfigurationFactory.addSeedRendezvousID(rdvPeerID);
		 * 
		 * netPeerGroup = PeerGroupFactory.newNetPeerGroup();
		 */
		netPeerGroup = new FakePeerGroup();
	}
	
	public void tearDown() {		
		netPeerGroup.stopApp();
		netPeerGroup = null;
	}
	
	/**
	 * Test whether given the same URI and the same group id, the generated id is also the same.
	 */
	public void testGetPipeIDEquals() {		
		String spaceURI = "ts://eooo/miratu/";
		JxmeSpace space = new JxmeSpace(netPeerGroup, spaceURI);
		PipeAdvertisement adv = space.createPipeAdvertisement(netPeerGroup,spaceURI);
	   
		JxmeSpace space2 = new JxmeSpace(netPeerGroup, spaceURI);
		PipeAdvertisement adv2 = space2.createPipeAdvertisement(netPeerGroup,spaceURI);
	   
		assertEquals( adv.getID(), adv2.getID() );
	}	
	
	public void testGetPipeAdvertisementUsingMD5() {
		String spaceURI = "tsc://www.morelab.deusto.es/lab1/test";
		JxmeSpace space = new JxmeSpace(netPeerGroup, spaceURI);
		PipeAdvertisement adv = space.createPipeAdvertisement(netPeerGroup,spaceURI);

		spaceURI = "tsc://www.morelab.deusto.es/lab2/test";
		JxmeSpace space2 = new JxmeSpace(netPeerGroup, spaceURI);
		PipeAdvertisement adv2 = space2.createPipeAdvertisement(netPeerGroup,spaceURI);
		
		assertEquals( adv.getPipeID().toString(), "urn:jxta:uuid-59616261646162614E50472050325033C64E2285C12E4AC5ABE4A0394FE5519C04");
		assertEquals( adv2.getPipeID().toString(), "urn:jxta:uuid-59616261646162614E50472050325033B2FB582554854B388E14A88E3320F62404");
	}
}

class FakePeerGroup implements PeerGroup {
	PeerGroupID gid;
	
	FakePeerGroup() {
		gid = PeerGroupID.defaultNetPeerGroupID; //(PeerGroupID) IDFactory.newPeerGroupID();
	}
	
	public boolean compatible(Element compat) {
		return false;
	}

	public AccessService getAccessService() {
		return null;
	}

	public ModuleImplAdvertisement getAllPurposePeerGroupImplAdvertisement()
			throws Exception {
		return null;
	}

	public ConfigParams getConfigAdvertisement() {
		return null;
	}

	public DiscoveryService getDiscoveryService() {
		return null;
	}

	public EndpointService getEndpointService() {
		return null;
	}

	public MembershipService getMembershipService() {
		return null;
	}

	public PeerGroup getParentGroup() {
		return null;
	}

	public PeerAdvertisement getPeerAdvertisement() {
		return null;
	}

	public PeerGroupAdvertisement getPeerGroupAdvertisement() {
		return null;
	}

	public PeerGroupID getPeerGroupID() {
		return gid;
	}

	public String getPeerGroupName() {
		return null;
	}

	public PeerID getPeerID() {
		return null;
	}

	public String getPeerName() {
		return null;
	}

	public PipeService getPipeService() {
		return null;
	}

	public RendezVousService getRendezVousService() {
		return null;
	}

	public ResolverService getResolverService() {
		return null;
	}

	public Iterator getRoleMap(ID name) {
		return null;
	}

	public PeerGroup getWeakInterface() {
		return null;
	}

	public boolean isRendezvous() {
		return false;
	}

	public Module loadModule(ID assignedID, Advertisement impl)
			throws ProtocolNotSupportedException, PeerGroupException {
		return null;
	}

	public Module loadModule(ID assignedID, ModuleSpecID specID, int where) {
		return null;
	}

	public Service lookupService(ID name) throws ServiceNotFoundException {
		return null;
	}

	public Service lookupService(ID name, int roleIndex)
			throws ServiceNotFoundException {
		return null;
	}

	public PeerGroup newGroup(Advertisement pgAdv) throws PeerGroupException {
		return null;
	}

	public PeerGroup newGroup(PeerGroupID gid, Advertisement impl, String name,
			String description) throws PeerGroupException {
		return null;
	}

	public PeerGroup newGroup(PeerGroupID gid) throws PeerGroupException {
		return null;
	}

	public void publishGroup(String name, String description)
			throws IOException {
		
	}

	public void unref() {
		
	}

	public Advertisement getImplAdvertisement() {
		return null;
	}

	public Service getInterface() {
		return null;
	}

	public void init(PeerGroup group, ID assignedID, Advertisement implAdv)
			throws PeerGroupException {
		
	}

	public int startApp(String[] args) {
		return 0;
	}

	public void stopApp() {
		
	}

	@Override
	public ThreadGroup getHomeThreadGroup() {
		return null;
	}

	@Override
	public JxtaLoader getLoader() {
		return null;
	}	
}