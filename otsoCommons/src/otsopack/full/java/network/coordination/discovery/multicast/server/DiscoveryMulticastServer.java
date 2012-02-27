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
package otsopack.full.java.network.coordination.discovery.multicast.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import otsopack.full.java.network.coordination.IDiscovery;
import otsopack.full.java.network.coordination.ISpaceManager;
import otsopack.full.java.network.coordination.discovery.DiscoveryException;

public class DiscoveryMulticastServer implements Runnable {
	
	public static final String DISCOVERY_REQUEST_TOKEN = "otsopack:discovery:spacemanager::"; // + spaceURI
	public static final String DISCOVERY_RESPONSE_TOKEN = "otsopack:discovery:response::"; // + spaceURI + :: + spaceManagerURIs with ';'
	
	public static final String DEFAULT_GROUP = "225.4.5.6";
	public static final int DEFAULT_PORT  = 12345;
	
	private final int port;
	private final String group;
	private IDiscovery discovery;
	private MulticastSocket socket;
	
	private volatile Thread currentThread; 
	
	public DiscoveryMulticastServer(IDiscovery discovery) {
		this(discovery, DEFAULT_PORT, DEFAULT_GROUP);
	}
	
	public DiscoveryMulticastServer(IDiscovery discovery, int port) {
		this(discovery, port, DEFAULT_GROUP);
	}
	
	public DiscoveryMulticastServer(IDiscovery discovery, String group) {
		this(discovery, DEFAULT_PORT, group);
	}
	
	public DiscoveryMulticastServer(IDiscovery discovery, int port, String group) {
		this.discovery = discovery;
		this.port = port;
		this.group = group;
	}
	
	public void setDiscovery(IDiscovery discovery) {
		this.discovery = discovery;
	}
	
	@Override
	public void run() {
		
		final byte buf [] = new byte[1024];
		
		while(!Thread.currentThread().isInterrupted()) {
			final DatagramPacket pack = new DatagramPacket(buf, buf.length);
			
			try {
				this.socket.receive(pack);
			} catch (IOException e) {
				continue;
			}
			
			if(pack.getLength() > 0) {
				final String receivedData = new String(pack.getData());
				if(receivedData.startsWith(DISCOVERY_REQUEST_TOKEN)) {
					final String spaceURI = receivedData.substring(DISCOVERY_REQUEST_TOKEN.length());
					final ISpaceManager [] spaceManagers;
					try {
						spaceManagers = this.discovery.getSpaceManagers(spaceURI);
					} catch (DiscoveryException e) {
						e.printStackTrace();
						continue;
					}
					
					for(ISpaceManager spaceManager : spaceManagers) {
						for(String externalReference : spaceManager.getExternalReferences()) {
							final byte [] responseBuf = (DISCOVERY_RESPONSE_TOKEN + spaceURI + "::" + externalReference).getBytes(); 
							
							try {
								final DatagramPacket response = new DatagramPacket(responseBuf, responseBuf.length, InetAddress.getByName(this.group), this.port);
								this.socket.send(response);
							} catch (UnknownHostException e) {
								e.printStackTrace();
								continue;
							} catch (IOException e) {
								e.printStackTrace();
								continue;
							}
						}
					}
				}
			}
		}
	}
	
	public void startup() throws DiscoveryException {
		if(this.currentThread != null)
			shutdown();

		
		try {
			this.socket = new MulticastSocket(this.port);
			this.socket.joinGroup(InetAddress.getByName(this.group));
			this.socket.setSoTimeout(500);
		} catch (UnknownHostException e) {
			throw new DiscoveryException("Could not startup " + DiscoveryMulticastServer.class.getName() + ": " + e.getMessage(), e);
		} catch (IOException e) {
			throw new DiscoveryException("Could not startup " + DiscoveryMulticastServer.class.getName() + ": " + e.getMessage(), e);
		}
		
		this.currentThread = new Thread(this);
		this.currentThread.start();
	}
	
	public void shutdown() throws DiscoveryException {
		if(this.currentThread != null) {
			this.currentThread.interrupt();
			try {
				this.currentThread.join(3000);
				this.socket.leaveGroup(InetAddress.getByName(this.group));
				this.socket.close();
			} catch (InterruptedException e) {
				throw new DiscoveryException("Could not shutdown " + DiscoveryMulticastServer.class.getName() + ": " + e.getMessage(), e);
			} catch (UnknownHostException e) {
				throw new DiscoveryException("Could not shutdown " + DiscoveryMulticastServer.class.getName() + ": " + e.getMessage(), e);
			} catch (IOException e) {
				throw new DiscoveryException("Could not shutdown " + DiscoveryMulticastServer.class.getName() + ": " + e.getMessage(), e);
			}
		}
	}
}
