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
package otsopack.full.java.network.coordination.spacemanager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import otsopack.full.java.network.communication.util.JSONDecoder;
import otsopack.full.java.network.communication.util.JSONEncoder;
import otsopack.full.java.network.coordination.ISpaceManager;
import otsopack.full.java.network.coordination.Node;
import otsopack.full.java.network.coordination.spacemanager.http.server.resources.NodesResource;
import otsopack.full.java.network.coordination.spacemanager.http.server.resources.StatesResource;

public class HttpSpaceManager implements ISpaceManager {
	
	private final String uri;
	private final String [] references; 
	
	public HttpSpaceManager(String uri){
		this.uri = uri;
		this.references = new String[]{"[http]" + uri};
	}

	@Override
	public Node[] getNodes() throws SpaceManagerException {
		final ClientResource client = new ClientResource(this.uri + NodesResource.ROOT);
		String serializedSpaceManagers;
		try{
			final Representation repr;
			try{
				repr = client.get(MediaType.APPLICATION_JSON);
			}catch(ResourceException e){
				throw new SpaceManagerException("Could not get nodes from " + this.uri + ": " + e.getMessage(), e);
			}
			try {
				serializedSpaceManagers = IOUtils.toString(repr.getStream());
			} catch (IOException e) {
				throw new SpaceManagerException("Could not read stream from space manager: " + e.getMessage(), e);
			}
		}finally{
			client.release();
		}
		return JSONDecoder.decode(serializedSpaceManagers, Node[].class);
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.uri == null) ? 0 : this.uri.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HttpSpaceManager other = (HttpSpaceManager) obj;
		if (this.uri == null) {
			if (other.uri != null)
				return false;
		} else if (!this.uri.equals(other.uri))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "HttpSpaceManagerClient [uri=" + this.uri + "]";
	}

	@Override
	public String [] getExternalReferences() {
		return this.references;
	}

	/* (non-Javadoc)
	 * @see otsopack.full.java.network.coordination.ISpaceManager#join(otsopack.full.java.network.coordination.Node)
	 */
	@Override
	public String join(Node node) throws SpaceManagerException {
		final ClientResource client = new ClientResource(this.uri + StatesResource.ROOT);
		final String encodedNode = JSONEncoder.encode(node);
		final String serializedSecret;
		try{
			final Representation repr;
			try{
				repr = client.post(encodedNode, MediaType.APPLICATION_JSON);
			}catch(ResourceException e){
				throw new SpaceManagerException("Could not join to space manager " + this.uri + ": " + e.getMessage(), e);
			}
			try {
				serializedSecret = repr.getText();
			} catch (IOException e) {
				throw new SpaceManagerException("Could not read stream from space manager: " + e.getMessage(), e);
			}
		}finally{
			client.release();
		}
		return JSONDecoder.decode(serializedSecret, String.class);
	}
	
	public static String getIpAddress() {
		try{
			final Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
	        for (NetworkInterface netIf : Collections.list(nets)) {
	        	// If the network is active
	            if(netIf.isUp()){
	                Enumeration<InetAddress> addresses = netIf.getInetAddresses();
	                for(InetAddress addr : Collections.list(addresses)) 
	                	// If the IP address is IPv4 and it's not the local address, store it
	                    if(addr.getAddress().length == 4 && !addr.isLoopbackAddress())
	                    	return addr.getHostAddress();
	            }
	        }
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String generateBaseUrl(int port, String basePath) throws SpaceManagerException {
		final String myIpAddress = getIpAddress();
		if(myIpAddress == null)
			throw new SpaceManagerException("No ip address obtained: use join(Node)");
		return "http://" + myIpAddress + ":" + port + basePath;
	}
	
	public String selfJoin(int port) throws SpaceManagerException {
		return selfJoin(port, true, false);
	}

	public String selfJoin(int port, String uuid) throws SpaceManagerException {
		return selfJoin(port, uuid, true, false);
	}
	
	public String selfJoin(int port, boolean reachable, boolean mustPoll) throws SpaceManagerException {
		return selfJoin(port, UUID.randomUUID().toString(), reachable, mustPoll);
	}

	public String selfJoin(int port, String uuid, boolean reachable, boolean mustPoll) throws SpaceManagerException {
		return selfJoin(port, "/", uuid, reachable, mustPoll);
	}

	public String selfJoin(int port, String basePath, String uuid, boolean reachable, boolean mustPoll) throws SpaceManagerException {
		final Node node = new Node(generateBaseUrl(port, basePath), uuid, reachable, mustPoll);
		return join(node);
	}

	/* (non-Javadoc)
	 * @see otsopack.full.java.network.coordination.ISpaceManager#poll(java.lang.String)
	 */
	@Override
	public void poll(String secret) throws SpaceManagerException {
		final ClientResource client = new ClientResource(this.uri + StatesResource.ROOT + "/" + secret);
		try{
			try{
				client.put("", MediaType.APPLICATION_JSON);
			}catch(ResourceException e){
				throw new SpaceManagerException("Could not poll space manager " + this.uri + ": " + e.getMessage(), e);
			}
		}finally{
			client.release();
		}
	}

	/* (non-Javadoc)
	 * @see otsopack.full.java.network.coordination.ISpaceManager#leave(java.lang.String)
	 */
	@Override
	public void leave(String secret) throws SpaceManagerException {
		final ClientResource client = new ClientResource(this.uri + StatesResource.ROOT + "/" + secret);
		try{
			try{
				client.delete(MediaType.APPLICATION_JSON);
			}catch(ResourceException e){
				throw new SpaceManagerException("Could not leave from space manager " + this.uri + ": " + e.getMessage(), e);
			}
		}finally{
			client.release();
		}
	}

	@Override
	public void startup() throws SpaceManagerException {
	}

	@Override
	public void shutdown() throws SpaceManagerException {
	}
}
