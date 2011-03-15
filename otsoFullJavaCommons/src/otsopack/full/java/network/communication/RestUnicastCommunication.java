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

package otsopack.full.java.network.communication;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.restlet.resource.ClientResource;

import otsopack.commons.data.Graph;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.network.ICommunication;
import otsopack.commons.network.communication.demand.local.ISuggestionCallback;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.full.java.network.communication.resources.graphs.IGraphResource;


public class RestUnicastCommunication implements ICommunication {
	
	public RestUnicastCommunication() {
		/*Router router = new Router(svr.getContext());
		router.attach("/user", UserResource.class);*/
	}
	
	String getBaseURI(String spaceuri) {
		String ret = "http://127.0.0.1:"+RestServer.DEFAULT_PORT+"/";
		try {
			ret = URLEncoder.encode(spaceuri, "utf-8")+"/";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public void startup() throws TSException {
		
	}

	@Override
	public void shutdown() throws TSException {
	}

	@Override
	public Graph read(String spaceURI, String graphURI, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException {
		Graph ret = null;
		try {
			ClientResource cr = new ClientResource( getBaseURI(spaceURI)+"graphs/"+URLEncoder.encode(graphURI, "utf-8") );
			IGraphResource res = cr.wrap(IGraphResource.class);
			res.toN3();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//final IGraphResource prefrsc = cr.wrap(IGraphResource.class);
		return null;
	}

	@Override
	public Graph read(String spaceURI, ITemplate template, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Graph take(String spaceURI, ITemplate template, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Graph query(String spaceURI, ITemplate template, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String subscribe(String spaceURI, ITemplate template,
			INotificationListener listener) throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unsubscribe(String spaceURI, String subscriptionURI)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub

	}

	@Override
	public String advertise(String spaceURI, ITemplate template)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unadvertise(String spaceURI, String advertisementURI)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub

	}

	@Override
	public void demand(String spaceURI, ITemplate template, long leaseTime,
			ISuggestionCallback callback) throws TSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void suggest(String spaceURI, Graph graph) throws TSException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean callbackIfIHaveResponsabilityOverThisKnowlege(
			String spaceURI, Graph triples) throws TSException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAnyPeerResponsabilityOverThisKnowlege(String spaceURI,
			Graph triples) throws TSException {
		// TODO Auto-generated method stub
		return false;
	}

}
