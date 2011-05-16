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
package otsopack.full.java.network.coordination.discovery.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import otsopack.full.java.network.communication.util.JSONDecoder;
import otsopack.full.java.network.coordination.IDiscovery;
import otsopack.full.java.network.coordination.SpaceManager;
import otsopack.full.java.network.coordination.discovery.DiscoveryException;
import otsopack.full.java.network.coordination.discovery.http.server.resources.DiscoveryResource;
import otsopack.full.java.network.coordination.spacemanager.HttpSpaceManager;

public class HttpDiscoveryClient implements IDiscovery {

	private final String [] uris;
	
	public HttpDiscoveryClient(String ... uris){
		this.uris = uris;
	}
	
	@Override
	public SpaceManager[] getSpaceManagers(String spaceURI) throws DiscoveryException {
		final Set<SpaceManager> spaceManagers = new HashSet<SpaceManager>();
		for(String uri : this.uris){
			String encodedSpace;
			try {
				encodedSpace = URLEncoder.encode(spaceURI, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new DiscoveryException("Could not encode space URI: " + e.getMessage(), e);
			}
			final ClientResource client = new ClientResource(uri + "?"+ DiscoveryResource.SPACEURI_ARGUMENT + "=" + encodedSpace);
			String serializedSpaceManagers;
			try{
				final Representation repr = client.get(MediaType.APPLICATION_JSON);
				try {
					serializedSpaceManagers = IOUtils.toString(repr.getStream());
				} catch (IOException e) {
					throw new DiscoveryException("Could not read stream from discovery server: " + e.getMessage(), e);
				}
			}finally{
				client.release();
			}
			final String [] spaceManagerURIs = JSONDecoder.decode(serializedSpaceManagers, String[].class);
			for(String spaceManagerURI : spaceManagerURIs)
				spaceManagers.add(new HttpSpaceManager(spaceManagerURI));
		}
		return spaceManagers.toArray(new SpaceManager[]{});
	}

}
