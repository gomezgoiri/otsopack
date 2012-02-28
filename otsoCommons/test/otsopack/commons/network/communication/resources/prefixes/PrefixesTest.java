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

package otsopack.commons.network.communication.resources.prefixes;

import static org.junit.Assert.assertEquals;

import java.net.URLEncoder;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;
import org.restlet.data.Status;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import otsopack.commons.network.communication.AbstractRestServerTesting;
import otsopack.commons.network.communication.resources.prefixes.IPrefixResource;
import otsopack.commons.network.communication.resources.prefixes.IPrefixesResource;
import otsopack.commons.network.communication.util.JSONDecoder;

public class PrefixesTest extends AbstractRestServerTesting{
	
	private ConcurrentHashMap<String, String> retrieve(IPrefixesResource prefrsc){
		return decode(prefrsc.retrieveJson());
	}
	
	@SuppressWarnings("unchecked")
	private ConcurrentHashMap<String, String> decode(String jsonData){
		return JSONDecoder.decode(jsonData, ConcurrentHashMap.class);
	}
	
	@Test
	public void testCreatePrefix() throws Exception {
		final ClientResource cr = new ClientResource(getBaseURL() + "prefixes");
		final IPrefixesResource prefrsc = cr.wrap(IPrefixesResource.class);
		
		ConcurrentHashMap<String, String> prefixes = retrieve(prefrsc);
		assertEquals(prefixes.size(), 0);
		
		getPrefixesStorage().create( "rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		getPrefixesStorage().create( "rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		getPrefixesStorage().create( "xsd", "http://www.w3.org/2001/XMLSchema#");
		getPrefixesStorage().create( "owl", "http://www.w3.org/2002/07/owl#");
		
		prefixes = retrieve(prefrsc);
		assertEquals(prefixes.size(), 4);
	}
	
	
	@Test
	public void testGetPrefixes() throws Exception {
		final ClientResource cr = new ClientResource(getBaseURL() + "prefixes");
		final IPrefixesResource prefrsc = cr.wrap(IPrefixesResource.class);
		getPrefixesStorage().create( "rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		getPrefixesStorage().create( "rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		getPrefixesStorage().create( "xsd", "http://www.w3.org/2001/XMLSchema#");
		getPrefixesStorage().create( "owl", "http://www.w3.org/2002/07/owl#");
		
		// Test json retrieval
		final ConcurrentHashMap<String, String> prefixes = retrieve(prefrsc);
		assertEquals(prefixes.size(), 4);
		checkIfItContainsPrefix(prefixes,"http://www.w3.org/1999/02/22-rdf-syntax-ns#","rdf");
		checkIfItContainsPrefix(prefixes,"http://www.w3.org/2000/01/rdf-schema#","rdfs");
		checkIfItContainsPrefix(prefixes,"http://www.w3.org/2001/XMLSchema#","xsd");
		checkIfItContainsPrefix(prefixes,"http://www.w3.org/2002/07/owl#","owl");
	}
	
	private void checkIfItContainsPrefix(ConcurrentHashMap<String, String> prefixes, String name, String uri) {
		assertEquals(uri, prefixes.get(name));
	}

	@Test
	public void testGetPrefix() throws Exception {
		final String RDF_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		getPrefixesStorage().create("rdf", RDF_URI);
		
		ClientResource cr = new ClientResource(getBaseURL() + "prefixes/" + URLEncoder.encode(RDF_URI, "utf-8"));
		IPrefixResource prefixrsc = cr.wrap(IPrefixResource.class);
		
		// Test json retrieval
		assertEquals(prefixrsc.retrieveJson(),"\"rdf\"");
		
		
		// Test non existing prefix
		cr = new ClientResource(getBaseURL() + "prefixes/doesnotexist");
		try {
			prefixrsc.retrieveJson();
		} catch(ResourceException re) {
			assertEquals(re.getStatus(),Status.CLIENT_ERROR_NOT_FOUND);
		}
	}
}
