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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import otsopack.commons.authz.Filter;
import otsopack.commons.authz.entities.User;
import otsopack.commons.data.Graph;
import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.Template;
import otsopack.commons.data.WildcardTemplate;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.network.ICommunication;
import otsopack.commons.network.communication.demand.local.ISuggestionCallback;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.full.java.network.communication.resources.graphs.WildcardConverter;

public class RestUnicastCommunication implements ICommunication {
	private String baseRESTServer;
	private User entity; // TODO it should be obtained from the RESTServer
	
	public RestUnicastCommunication() {
		this("http://127.0.0.1:"+RestServer.DEFAULT_PORT+"/");
	}
	
	public RestUnicastCommunication(String restserver) {
		this.baseRESTServer = restserver;
		this.entity = new User("http://myid.com/aitor");
	}
	
	String getBaseURI(String spaceuri) {
		String ret = "";
		try {
			ret = URLEncoder.encode(spaceuri, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return this.baseRESTServer + "spaces/" + ret + "/";
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
		/*
		 * 	final MediaType [] clientMediaTypes = SemanticFormatRepresentationRegistry.getMediaTypes(SemanticFormat.NTRIPLES, SemanticFormat.TURTLE);  
		 *	OtsopackConverter.setEnabledVariants(clientMediaTypes);
		 */
		try {
			final ClientResource cr = new ClientResource( getBaseURI(spaceURI)+"graphs/"+URLEncoder.encode(graphURI, "utf-8") );
			final Representation rep = cr.get(MediaType.TEXT_RDF_NTRIPLES);
			return new Graph( rep.getText(), SemanticFormat.NTRIPLES);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Graph read(String spaceURI, Template template, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException {
		if( template instanceof WildcardTemplate ) {
			try {
				final String relativeURI = WildcardConverter.createURLFromTemplate( (WildcardTemplate)template );
				final ClientResource cr = new ClientResource( getBaseURI(spaceURI)+"graphs/wildcards/"+relativeURI );
				final Representation rep = cr.get(MediaType.TEXT_RDF_NTRIPLES);
				return new Graph( rep.getText(), SemanticFormat.NTRIPLES);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException {
		/*
		 * 	final MediaType [] clientMediaTypes = SemanticFormatRepresentationRegistry.getMediaTypes(SemanticFormat.NTRIPLES, SemanticFormat.TURTLE);  
		 *	OtsopackConverter.setEnabledVariants(clientMediaTypes);
		 */
		try {
			final ClientResource cr = new ClientResource( getBaseURI(spaceURI)+"graphs/"+URLEncoder.encode(graphURI, "utf-8") );
			final Representation rep = cr.delete(MediaType.TEXT_RDF_NTRIPLES);
			return new Graph( rep.getText(), SemanticFormat.NTRIPLES);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Graph take(String spaceURI, Template template, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException {
		if( template instanceof WildcardTemplate ) {
			try {
				final String relativeURI = WildcardConverter.createURLFromTemplate( (WildcardTemplate)template );
				final ClientResource cr = new ClientResource( getBaseURI(spaceURI)+"graphs/wildcards/"+relativeURI );
				final Representation rep = cr.delete(MediaType.TEXT_RDF_NTRIPLES);
				return new Graph( rep.getText(), SemanticFormat.NTRIPLES);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	// TODO test and put into ITripleSpace
	public Graph query(String spaceURI, Template template, SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException {
		final Graph graph = query(spaceURI, template, outputFormat, timeout);
		for(Filter filter: filters) {
			if( filter.getAssert().evaluate(graph) ) {
				if( !filter.getEntity().equals(this.entity) )
					return null;
			}
		}
		return graph;
	}

	@Override
	public Graph query(String spaceURI, Template template, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException {
		if( template instanceof WildcardTemplate ) {
			try {
				final String relativeURI = WildcardConverter.createURLFromTemplate( (WildcardTemplate)template );
				final ClientResource cr = new ClientResource( getBaseURI(spaceURI)+"query/wildcards/"+relativeURI );
				final Representation rep = cr.get(MediaType.TEXT_RDF_NTRIPLES);
				return new Graph( rep.getText(), SemanticFormat.NTRIPLES);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String subscribe(String spaceURI, NotificableTemplate template,
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
	public String advertise(String spaceURI, NotificableTemplate template)
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
	public void demand(String spaceURI, Template template, long leaseTime,
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
