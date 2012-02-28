/*
 * Copyright (C) 2008 onwards University of Deusto
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
package otsopack.droid.network.communication.incoming;

import java.util.Hashtable;

import otsopack.droid.network.communication.incoming.response.ModelResponse;
import otsopack.droid.network.communication.incoming.response.Response;
import otsopack.droid.network.communication.incoming.response.URIResponse;

public class IncomingList {
	final Hashtable<Object,URIResponse> uriResponses;
	//Hashtable/*<[URI,Selector],ModelResponse>*/ modelResponses;
	final Hashtable<Object,ModelResponse> modelResponses;
	
	public IncomingList() {
		uriResponses = new Hashtable<Object, URIResponse>();
		modelResponses = new Hashtable<Object, ModelResponse>();
	}
	
	public void add(URIResponse r) throws ArrayStoreException {
		if(uriResponses.get(r.getKey())!=null) throw new ArrayStoreException("It cannot query the same thing twice at the same time");
		uriResponses.put(r.getKey(),r);
	}
	
	public void add(ModelResponse r) throws ArrayStoreException {
		if(modelResponses.get(r.getKey())!=null) throw new ArrayStoreException("It cannot query the same thing twice at the same time");
		modelResponses.put(r.getKey(),r);
	}
	
	public Response get(Object key, boolean waitingForURI) {
		if( waitingForURI ) 
			return uriResponses.get(key);
		
		return modelResponses.get(key);
	}
	
	public URIResponse remove(URIResponse r) {
		return uriResponses.remove(r.getKey());
	}
		
	public ModelResponse remove(ModelResponse r) {
		return modelResponses.remove(r.getKey());
	}
	
	public void clear() {
		uriResponses.clear();
		modelResponses.clear();
	}	
}