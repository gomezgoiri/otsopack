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
package otsopack.otsoDroid.network.jxme;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Enumeration;

import net.jxta.discovery.DiscoveryService;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupFactory;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.platform.ConfigurationFactory;
import net.jxta.platform.Module;
import net.jxta.protocol.ModuleImplAdvertisement;
import net.jxta.protocol.PeerGroupAdvertisement;
import net.jxta.rendezvous.RendezVousService;
import net.jxta.rendezvous.RendezvousListener;

import org.apache.log4j.Logger;

import otsopack.otsoDroid.configuration.JxmeConfiguration;
import otsopack.otsoCommons.exceptions.TSException;

public class JxmePeerBase {
	private final static Logger LOG = Logger.getLogger(JxmePeerBase.class.getName());
	
	/**
	 * Configuration parameters for the JXTA instance
	 */
	private JxmeConfiguration configurationParameters = JxmeConfiguration.getConfiguration();

	/**
	 * Every jxta peer has to be part of a PeerGroup.
	 */
	private PeerGroup netPeerGroup = null;

	public JxmePeerBase() {
		super();
	}

	public JxmeConfiguration getConfigurationParameters() {
		return configurationParameters;
	}

	/**
	 * This method gathers all the needed configuration of the JXTA.
	 * 
	 * @throws IOException
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 * 
	 */
	private void configureJxta() {

		// XXX: this method might need to be reimplemented to support NetworkManager
		
		// XXX: peerdroid doesn't support clean method in ConfigurationFactory
		// ConfigurationFactory.clean();
		ConfigurationFactory.setRelaySeedingURI(null);
		ConfigurationFactory.setRdvSeedingURI(null);
		
		ConfigurationFactory.setName(getConfigurationParameters().getPeerName());
		ConfigurationFactory.setDescription(getConfigurationParameters().getDescription());

		// Setting TCP configuration
		ConfigurationFactory.setTcpPort(getConfigurationParameters().getTcpPort());
		ConfigurationFactory.setTcpIncoming(getConfigurationParameters().isTcpIncomingEnabled());
		ConfigurationFactory.setTcpOutgoing(getConfigurationParameters().isTcpOutgoingEnabled());
		ConfigurationFactory.setTCPPortRange(getConfigurationParameters().getTcpStartPort(), getConfigurationParameters().getTcpEndPort());
		
		// XXX: peerdroid doesn't support "setTcpInterface" method in ConfigurationFactory
		// ConfigurationFactory.setTcpInterface(getConfigurationParameters().getTcpInterface());
		
		// No multicast use - It is not implemented anyway
		ConfigurationFactory.setUseMulticast(getConfigurationParameters().isUseMulticast());

		// Setting the Peer ID
		ConfigurationFactory.setPeerID(getConfigurationParameters().getPID());
				
		// Configure RDV and RELAY settings
		ConfigurationFactory.setRelayed(getConfigurationParameters().getRelayed());

		// Do not use the default JXTA seeds to find RDV and Relay peers.

		// Add custom RDV and Relay paramaters
		// To add a custom seed
		// networkConfigurator.addSeedRendezvous(URI seedURI);
		// networkConfigurator.addSeedRelay(URI seedURI);
		// e.g. seedURI = http://192.168.1.1:9700
		// if you want to add your own seeding peers:
		// networkConfigurator.addRdvSeedingURI(URI seedURI);
		// networkConfigurator.addRelaySeedingURI(URI seedURI);
		// e.g. seedURI = http://rdv.jxtahosts.net/cgi-bin/relays.cgi?3		

		if (this.getConfigurationParameters().getRendezvousURI() != null) {
			ConfigurationFactory.addSeedRendezvous(this.getConfigurationParameters().getRendezvousURI());

			//PeerID of the rendezvous is created based on the defaultNetPeerGroup
			PeerID rdvPeerID = IDFactory.newPeerID(
					PeerGroupID.defaultNetPeerGroupID, 
					this.getConfigurationParameters().getRendezvousName().getBytes());
			System.out.println(rdvPeerID.getClass().getName());
			System.out.println(rdvPeerID);
			System.out.println(rdvPeerID.toURI());
			// XXX: peerdroid doesn't support "addSeedRendezvousID" method in ConfigurationFactory
			ConfigurationFactory.addSeedRendezvousID(rdvPeerID);
		}

		// There are many other parameters still to be explored!! But their
		// default values will do for now
	}

	/**
	 * Starts the JXTA Platform
	 * 
	 * @throws PeerGroupException
	 * @throws IOException
	 * 
	 * @throws IOException
	 * @throws PeerGroupException
	 */
	public void startJXTA() throws PeerGroupException {
		this.configureJxta();

		System.out.println("Start the JXTA network");

		// Starting the platform returns the netPeerGroup that was created based
		// on the configuration
		// By default there is a WorldPeerGroup and a NetPeerGroup.
		netPeerGroup = PeerGroupFactory.newNetPeerGroup();
	}

	public void stopJXTA() {		
		getNetPeerGroup().stopApp();
		netPeerGroup = null;
	}

	/**
	 * Adds a RendezvousEvent Listener to the default peerGroup of the
	 * rendezvous peer
	 * 
	 * @param listener
	 */
	public void addListener(RendezvousListener listener) {
		if (getNetPeerGroup() != null) {
			RendezVousService rdvService = this.getNetPeerGroup()
					.getRendezVousService();
			rdvService.addListener(listener);
			System.out.println("Added listener to RendezVousService");
		}
	}

	public PeerGroup getNetPeerGroup() {
		return netPeerGroup;
	}

	/**
	 * Waits for a Rendezvous connection to be made. 0 - forces the connection
	 * other value ignored
	 * @throws TSException
	 */
	public void waitForRendezvous(long timeout) throws TSException {
		RendezVousService rendezvous = netPeerGroup.getRendezVousService();
		
		if (timeout == JxmeConfiguration.FORCE_CONNECTION_RENDEZVOUS) {
			while (!rendezvous.isConnectedToRendezVous()) 
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			long time = System.currentTimeMillis() + timeout;
			while( time < System.currentTimeMillis() && !rendezvous.isConnectedToRendezVous()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		if (rendezvous.isConnectedToRendezVous()) {
			System.out.println("Connected to Rendezvous of " + netPeerGroup.getPeerGroupID().toString());
		} else throw new TSException("Could not connect to rendezvous peer");
	}

	/**
	 * Creates a PeerGroup. It must not exist
	 * 
	 * @param groupName
	 * @return
	 * @throws Exception
	 */
	public PeerGroup createPeerGroup(String groupName) throws Exception {		
		LOG.debug(this.getClass().getName() + " BEGIN createPeerGroup" + "\n" + "groupURI: " + groupName + "\n");
			
		PeerGroupAdvertisement peerGroupAdvertisement = null;
		
		ModuleImplAdvertisement moduleImplAdvertisement = this.getNetPeerGroup().getAllPurposePeerGroupImplAdvertisement();
		PeerGroupID groupID = IDFactory.newPeerGroupID(groupName.toString().getBytes());
		
		LOG.info(this.getClass().getName() + " " + moduleImplAdvertisement.toString());
		
		PeerGroup peerGroup = this.getNetPeerGroup().newGroup(groupID, moduleImplAdvertisement, groupName.toString(), "");
		
		if (Module.START_OK != peerGroup.startApp(new String[0])) {                
			throw new Exception("Could not start new PeerGroup");
        }
		
		peerGroupAdvertisement = peerGroup.getPeerGroupAdvertisement();
				
		//TODO load this properties from the configuration file or class
		long lifetime = Long.MAX_VALUE;
		long expiration = 7200000;			

		DiscoveryService discoveryService = this.getNetPeerGroup().getDiscoveryService();		

		discoveryService.publish(peerGroupAdvertisement, lifetime, expiration);
		discoveryService.remotePublish(moduleImplAdvertisement, expiration);
		discoveryService.remotePublish(peerGroupAdvertisement, expiration);

		LOG.debug(this.getClass().getName() + " END createPeerGroup" + "\n");
		
		return peerGroup;
	}

	/**
	 * Searches for a concrete PeerGroup in the Jxta network.
	 * 
	 * @param groupName
	 * @return
	 */
	protected PeerGroup discoverPeerGroup(String groupName) {			
		PeerGroup peerGroup = null;		
			
		String attribute = "Name";
		String value = groupName.toString();
		int threshold = 1;
			
		//Once the remote search is done, the local method should retrieve the group if found
		DiscoveryService discoveryService = this.getNetPeerGroup().getDiscoveryService();
		discoveryService.getRemoteAdvertisements(null, DiscoveryService.GROUP, attribute, value, threshold);
		
		try {
			Enumeration advs = discoveryService.getLocalAdvertisements(DiscoveryService.GROUP, attribute, value);
			if (advs.hasMoreElements()) {
				PeerGroupAdvertisement peerGroupAdvertisement = (PeerGroupAdvertisement) advs.nextElement();
				peerGroup = this.getNetPeerGroup().newGroup(peerGroupAdvertisement);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PeerGroupException e) {
			e.printStackTrace();
		}	
				
		return peerGroup;
			
	//		try 
	//		{
	//
	//			long attemps = new Long("10");
	//			long timeout = 5000;
	//			
	//			try {
	//				attemps = Long.parseLong(Props.Jxta.MAX_ATTEMPS_DISCOVER.getProperty());
	//				timeout = Long.parseLong(Props.Jxta.TIMEOUT_DISCOVER.getProperty());
	//			} catch (PropertiesException e) {
	//				e.printStackTrace();
	//			} catch (NumberFormatException e) {
	//				PropertiesException exception = new PropertiesException("jxta.properties non valid values for MAX_ATTEMPS_DISCOVER or TIMEOUT_DISCOVER using default values 10 5000", e);
	//				exception.printStackTrace();
	//				attemps = new Long("10");
	//				timeout = 5000;
	//			}	
	
	//			Enumeration ae = null;
	
	//			// search for advertisements, local/remote
	//			while (attemps-- > 0) {
	//				netPeerGroup.getDiscoveryService().getRemoteAdvertisements(null, DiscoveryService.GROUP, attribute, value, threshold);
	//				ae = netPeerGroup.getDiscoveryService().getLocalAdvertisements(DiscoveryService.GROUP, attribute, value);
	//
	//				if ((ae != null) && ae.hasMoreElements()) {
	//					break;
	//				}
	//
	//				//sleep to allow time for peers to respond to the discovery request
	//				Thread.sleep(timeout);
	//			}
	//
	////			// no advertisements found, create and advertise
	//			if (ae == null || !ae.hasMoreElements()) {
	//				//throw new SpaceNotFoundException(); 
	//			}
	////			// advertisements found, use
	//			else {
	//				PeerGroupAdvertisement peerGroupAdvertisement = (PeerGroupAdvertisement) ae.nextElement();
	//				peerGroup = netPeerGroup.newGroup(peerGroupAdvertisement);
	//			}
	//
	//			logger.debug(this.getClass().getName() + " BEGIN discoverPeerGroup" + "\n");
	//		} catch (PeerGroupException e) {
	//			e.printStackTrace();
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		} catch (InterruptedException e) {
	//			e.printStackTrace();
	//		}
	//		
			
		}
}
