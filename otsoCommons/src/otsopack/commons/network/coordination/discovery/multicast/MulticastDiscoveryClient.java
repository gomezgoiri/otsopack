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
package otsopack.commons.network.coordination.discovery.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Vector;

import otsopack.commons.network.coordination.IDiscovery;
import otsopack.commons.network.coordination.ISpaceManager;
import otsopack.commons.network.coordination.discovery.DiscoveryException;
import otsopack.commons.network.coordination.discovery.multicast.server.DiscoveryMulticastServer;
import otsopack.commons.network.coordination.spacemanager.SpaceManagerFactory;

public class MulticastDiscoveryClient implements IDiscovery{

	private MulticastSocket socket;
	
	private final int port;
	private final String group;

	public MulticastDiscoveryClient() {
		this(DiscoveryMulticastServer.DEFAULT_PORT, DiscoveryMulticastServer.DEFAULT_GROUP);
	}
	
	public MulticastDiscoveryClient(int port) {
		this(port, DiscoveryMulticastServer.DEFAULT_GROUP);
	}
	
	public MulticastDiscoveryClient(String group) {
		this(DiscoveryMulticastServer.DEFAULT_PORT, group);
	}
	
	public MulticastDiscoveryClient(int port, String group) {
		this.port = port;
		this.group = group;
	}
	
	@Override
	public void startup() throws DiscoveryException {
		try {
			this.socket = new MulticastSocket(this.port);
			this.socket.joinGroup(InetAddress.getByName(this.group));
			this.socket.setSoTimeout(500);
		} catch (UnknownHostException e) {
			throw new DiscoveryException("Could not startup " + DiscoveryMulticastServer.class.getName() + ": " + e.getMessage(), e);
		} catch (IOException e) {
			throw new DiscoveryException("Could not startup " + DiscoveryMulticastServer.class.getName() + ": " + e.getMessage(), e);
		}
	}

	@Override
	public void shutdown() throws DiscoveryException {
		try {
			this.socket.leaveGroup(InetAddress.getByName(this.group));
			this.socket.close();
		} catch (UnknownHostException e) {
			throw new DiscoveryException("Could not shutdown " + MulticastDiscoveryClient.class.getName() + ": " + e.getMessage(), e);
		} catch (IOException e) {
			throw new DiscoveryException("Could not shutdown " + MulticastDiscoveryClient.class.getName() + ": " + e.getMessage(), e);
		}
	}
	
	@Override
	public ISpaceManager[] getSpaceManagers(String spaceURI) throws DiscoveryException {
		
		final String requestMessage = DiscoveryMulticastServer.DISCOVERY_REQUEST_TOKEN + spaceURI;
		final byte [] request = requestMessage.getBytes();
		
		try {
			final DatagramPacket response = new DatagramPacket(request, request.length, InetAddress.getByName(this.group), this.port);
			this.socket.send(response);
		} catch (UnknownHostException e1) {
			throw new DiscoveryException("Could not perform discovery request: " + e1.getMessage(), e1);
		} catch (IOException e1) {
			throw new DiscoveryException("Could not perform discovery request: " + e1.getMessage(), e1);
		}

		final byte buf [] = new byte[1024];
		
		final String expectedToken = DiscoveryMulticastServer.DISCOVERY_RESPONSE_TOKEN + spaceURI + "::";
		
		final List<ISpaceManager> spaceManagers = new Vector<ISpaceManager>();
		
		while(true) {
			final DatagramPacket pack = new DatagramPacket(buf, buf.length);
			
			try {
				this.socket.receive(pack);
			} catch (IOException e) {
				break;
			}
			
			if(pack.getLength() <= 0) {
				System.err.println("Discarding empty message at client");
				break;
			}
			
			final String responseMessage = new String(pack.getData());
			if(responseMessage.startsWith(expectedToken)) {
				final String spaceManagerURI = responseMessage.substring(expectedToken.length());
				try{
					spaceManagers.add(SpaceManagerFactory.create(spaceManagerURI));
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return spaceManagers.toArray(new ISpaceManager[]{});
	}
}
