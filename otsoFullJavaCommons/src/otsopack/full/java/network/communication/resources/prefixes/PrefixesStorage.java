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
package otsopack.full.java.network.communication.resources.prefixes;

import java.util.concurrent.ConcurrentHashMap;

public class PrefixesStorage {
	public final ConcurrentHashMap<String,String> prefixesByURI  = new ConcurrentHashMap<String,String>();
	public final ConcurrentHashMap<String,String> prefixesByName = new ConcurrentHashMap<String,String>();

	public ConcurrentHashMap<String,String> getPrefixesByURI(){
		return this.prefixesByURI;
	}
	
	public ConcurrentHashMap<String,String> getPrefixesByName(){
		return this.prefixesByName;
	}

	public void create(String name, String uri) {
		this.prefixesByURI.put(uri, name);
		this.prefixesByName.put(name, uri);
	}
	
	public String getPrefixByName(String prefixName) {
		return this.prefixesByName.get(prefixName);
	}
	
	public String getPrefixByURI(String prefixURI) {
		return this.prefixesByURI.get(prefixURI);
	}
}
