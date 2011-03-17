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
package otsopack.otsoME.configuration;

import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.util.java.net.URI;

public class JxmeConfiguration 
{
	public static final long FORCE_CONNECTION_RENDEZVOUS = 0;
	public static final int MAX_ATTEMPS_DISCOVER = 10;
	public static final long TIMEOUT_DISCOVER = 5000;

	/**
	 * Human readable name of the peer (in this case the rendezvous peer).
	 * This will be used to generate the PID, which has to be unique in the network
	 */
	private String peerName = "unkown";

	/**
	 * PeerID of the peer. This is a unique identifier used by JXTA.
	 * It can be created using different methods, IDFactory provides one to create PID using
	 * the peergroup and the name of the peer.
	 */
	private PeerID PID = IDFactory.newPeerID(PeerGroupID.defaultNetPeerGroupID, peerName.getBytes());
	
	/**
	 * TCP incoming interface.
	 */
	private String tcpInterface = "localhost";
	
	/**
	 * TCP Listening port for incoming messages related to the rendezvous
	 */
	private int tcpPort = 9701;
	
	/**
	 * JXTA Option to use dynamic port allocation for TCP connections
	 */
	private int tcpDynamicPort = 0;
	
	/**
	 * TCP start port for dynamic connections
	 */
	private int tcpStartPort = 8000;
	
	/**
	 * TCP end port for dynamic connections
	 */
	private int tcpEndPort = 8999;
	
	/**
	 * HTTP Listening port
	 */
	private int httpPort = 8085;
	
	/**
	 * JXTA saves configuration in a file. Used too to save adv, routing ...etc
	 */
	//private File configurationFile = new File("."+ System.getProperty("file.separator") + peerName);

	//private ConfigMode networkManagerConfigMode = NetworkManager.ConfigMode.RENDEZVOUS;

	private boolean tcpEnabled = true;

	private boolean tcpIncomingEnabled = true;

	private boolean tcpOutgoingEnabled = true;

	private boolean httpEnabled  = false;

	private boolean httpIncomingEnabled = false;

	private boolean httpOutgoingEnabled = false;

	private boolean useDefaultSeeds = true;
	
	private boolean rdvAutoStart = true;
	
	private URI rendezvousURI = null;
	
	private boolean useMulticast = false;

	private String description = "unknown";

	private String rendezvousName = "unknown";

	private long rendezvousConnectionTimeout = FORCE_CONNECTION_RENDEZVOUS;
	
	private boolean relayed = true;
	
	private static JxmeConfiguration configuration = null;
	
	public static JxmeConfiguration getConfiguration()
	{
		if (configuration == null)
		{
			configuration = new JxmeConfiguration();
		}
		
		return configuration;
	}
	
	public String getPeerName() 
	{
		return peerName;
	}

	public void setPeerName(String peerName) 
	{
		this.peerName = "mobile:" + peerName;
		this.PID = IDFactory.newPeerID(PeerGroupID.defaultNetPeerGroupID, peerName.getBytes());
		//configurationFile = new File("."+ System.getProperty("file.separator") + peerName);
	}

	public String getTcpInterface() {
		return tcpInterface;
	}

	public void setTcpInterface(String tcpInterface) {
		this.tcpInterface = tcpInterface;
	}

	public PeerID getPID() 
	{
		return PID;
	}

	public int getTcpPort() 
	{
		return tcpPort;
	}

	public void setTcpPort(int tcpPort) 
	{
		this.tcpPort = tcpPort;
	}

	public int getTcpDynamicPort() 
	{
		return tcpDynamicPort;
	}

	public int getTcpStartPort() 
	{
		return tcpStartPort;
	}

	public void setTcpStartPort(int tcpStartPort) 
	{
		this.tcpStartPort = tcpStartPort;
	}

	public int getTcpEndPort() 
	{
		return tcpEndPort;
	}

	public void setTcpEndPort(int tcpEndPort) 
	{
		this.tcpEndPort = tcpEndPort;
	}

	public int getHttpPort() 
	{
		return httpPort;
	}

	public void setHttpPort(int httpPort) 
	{
		this.httpPort = httpPort;
	}

//	public File getConfigurationFile() 
//	{
//		return configurationFile;
//	}

//	public ConfigMode getNetworkManagerConfigMode() 
//	{
//		return networkManagerConfigMode ;
//	}
	
//	public void setNetworkManagerConfigMode(ConfigMode configMode)
//	{
//		this.networkManagerConfigMode = configMode;
//	}

	public boolean isTcpEnabled() 
	{
		return tcpEnabled;
	}

	public void setTcpEnabled(boolean enabled)
	{
		this.tcpEnabled = enabled;
	}
	
	public boolean isTcpIncomingEnabled() 
	{
		return tcpIncomingEnabled;
	}

	public void setTcpIncomingEnabled(boolean enabled)
	{
		this.tcpIncomingEnabled = enabled;
	}
	
	public boolean isTcpOutgoingEnabled() 
	{
		return tcpOutgoingEnabled;
	}
	
	public void setTcpOutgoingEnabled(boolean enabled)
	{
		this.tcpOutgoingEnabled = enabled;
	}
			
	public boolean isHttpEnabled() 
	{
		return httpEnabled;
	}

	public void setHttpEnabled(boolean enabled)
	{
		this.httpEnabled = enabled;
	}
	
	public boolean isHttpIncomingEnabled() 
	{
		return httpIncomingEnabled;
	}

	public void setHttpIncomingEnabled(boolean enabled)
	{
		this.httpIncomingEnabled = enabled;
	}
	
	public boolean isHttpOutgoingEnabled() 
	{
		return httpOutgoingEnabled;
	}
	
	public void setHttpOutgoingEnabled(boolean enabled)
	{
		this.httpOutgoingEnabled = enabled;
	}

	public void setUseDefaultSeeds(boolean useDefaultSeeds) 
	{
		this.useDefaultSeeds = useDefaultSeeds;
	}

	public boolean isUseDefaultSeeds() 
	{
		return useDefaultSeeds;
	}

	public void setRdvAutoStart(boolean rdvAutoStart) {
		this.rdvAutoStart = rdvAutoStart;
	}

	public boolean isRdvAutoStart() {
		return rdvAutoStart;
	}

	public void setRendezvousURI(net.jxta.util.java.net.URI rendezvousURI) {
		this.rendezvousURI = rendezvousURI;
	}

	public URI getRendezvousURI() {
		return rendezvousURI;
	}

	public void setUseMulticast(boolean useMulticast) {
		this.useMulticast = useMulticast;
	}

	public boolean isUseMulticast() {
		return useMulticast;
	}

	public String getDescription() 
	{
		return description;
	}
	
	public void setDescription(String description) 
	{
		this.description = description;
	}

	public void setRendezvousName(String rendezvousName) 
	{
		this.rendezvousName = rendezvousName;
	}
	
	public String getRendezvousName()
	{
		return this.rendezvousName;
	}

	public long getRendezvousConnectionTimeout() {
		return this.rendezvousConnectionTimeout;
	}
	
	public void setRendezvousConnectionTimeout(long timeout) {
		this.rendezvousConnectionTimeout = timeout;
	}

	public boolean getRelayed() {
		return relayed;
	}	
	
	public void setRelayed(boolean relayed)
	{
		this.relayed = relayed;
	}
}