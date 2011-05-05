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
 *         Pablo Orduña <pablo.orduna@deusto.es>
 */

package otsopack.full.java.network.communication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Vector;

import org.restlet.data.Form;
import org.restlet.engine.http.header.HeaderConstants;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import otsopack.authn.ClientResourceFactory;
import otsopack.commons.authz.Filter;
import otsopack.commons.authz.entities.User;
import otsopack.commons.data.Graph;
import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.SignedGraph;
import otsopack.commons.data.Template;
import otsopack.commons.data.WildcardTemplate;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.network.ICommunication;
import otsopack.commons.network.communication.demand.local.ISuggestionCallback;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.full.java.network.OtsoFullJavaNetworkException;
import otsopack.full.java.network.communication.representations.NTriplesRepresentation;
import otsopack.full.java.network.communication.representations.RdfMultipartRepresentation;
import otsopack.full.java.network.communication.representations.SemanticFormatRepresentation;
import otsopack.full.java.network.communication.representations.SemanticFormatRepresentationRegistry;
import otsopack.full.java.network.communication.resources.graphs.WildcardConverter;

public class RestUnicastCommunication implements ICommunication {

	public static final String OTSOPACK_USER = "X-OTSOPACK-User";
	
	private final String baseRESTServer;
	private final ClientResourceFactory clientFactory = new ClientResourceFactory();
	
	public RestUnicastCommunication(String restserver) {
		this.baseRESTServer = restserver;
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
	
	protected Graph filterResults(Graph graph, Filter[] filters) {
		if( graph !=null ){
			for(Filter filter: filters) 
				if( filter.getAssert().evaluate(graph) ) 
					if( !filter.getEntity().check(graph.getEntity()) )
						return null;
		}
		return graph;
	}
	
	protected Graph [] filterResults(Graph [] graphs, Filter[] filters) {
		final List<Graph> resultingGraphs = new Vector<Graph>(graphs.length);
		for(Graph graph : graphs){
			final Graph filtered = filterResults(graph, filters);
			if(filtered != null)
				resultingGraphs.add(graph);
		}
		return resultingGraphs.toArray(new Graph[]{});
	}
	
	@Override
	public Graph read(String spaceURI, String graphURI, SemanticFormat outputFormat, Filter[] filters, long timeout) throws SpaceNotExistsException {
		final Graph graph = read(spaceURI, graphURI, outputFormat, timeout);
		return filterResults(graph, filters);
	}

	@Override
	public Graph read(String spaceURI, String graphURI, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException {
		/*
		 * 	final MediaType [] clientMediaTypes = SemanticFormatRepresentationRegistry.getMediaTypes(SemanticFormat.NTRIPLES, SemanticFormat.TURTLE);  
		 *	OtsopackConverter.setEnabledVariants(clientMediaTypes);
		 */
		try {
			final ClientResource cr = this.clientFactory.createResource( getBaseURI(spaceURI)+"graphs/"+URLEncoder.encode(graphURI, "utf-8") );
			final Representation rep = cr.get(NTriplesRepresentation.class);
			return createGraph(cr, rep);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OtsoFullJavaNetworkException e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public Graph read(String spaceURI, Template template, SemanticFormat outputFormat, Filter[] filters, long timeout) throws SpaceNotExistsException {
		final Graph graph = read(spaceURI, template, outputFormat, timeout);
		return filterResults(graph, filters);
	}

	@Override
	public Graph read(String spaceURI, Template template, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException {
		if( template instanceof WildcardTemplate ) {
			try {
				final String relativeURI = WildcardConverter.createURLFromTemplate( (WildcardTemplate)template );
				final ClientResource cr = this.clientFactory.createResource( getBaseURI(spaceURI)+"graphs/wildcards/"+relativeURI );
				final Representation rep = cr.get(NTriplesRepresentation.class);
				return createGraph(cr, rep);
			} catch (ResourceException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (OtsoFullJavaNetworkException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@Override
	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat, Filter[] filters, long timeout) throws SpaceNotExistsException {
		final Graph graph = take(spaceURI, graphURI, outputFormat, timeout);
		return filterResults(graph, filters);
	}

	@Override
	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException {
		/*
		 * 	final MediaType [] clientMediaTypes = SemanticFormatRepresentationRegistry.getMediaTypes(SemanticFormat.NTRIPLES, SemanticFormat.TURTLE);  
		 *	OtsopackConverter.setEnabledVariants(clientMediaTypes);
		 */
		try {
			final ClientResource cr = this.clientFactory.createResource( getBaseURI(spaceURI)+"graphs/"+URLEncoder.encode(graphURI, "utf-8") );
			final Representation rep = cr.delete(NTriplesRepresentation.class);
			return createGraph(cr, rep);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OtsoFullJavaNetworkException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Graph take(String spaceURI, Template template, SemanticFormat outputFormat, Filter[] filters, long timeout) throws SpaceNotExistsException {
		final Graph graph = read(spaceURI, template, outputFormat, timeout);
		return filterResults(graph, filters);
	}

	@Override
	public Graph take(String spaceURI, Template template, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException {
		if( template instanceof WildcardTemplate ) {
			try {
				final String relativeURI = WildcardConverter.createURLFromTemplate( (WildcardTemplate)template );
				final ClientResource cr = this.clientFactory.createResource( getBaseURI(spaceURI)+"graphs/wildcards/"+relativeURI );
				final Representation rep = cr.delete(NTriplesRepresentation.class);
				return createGraph(cr, rep);
			} catch (ResourceException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (OtsoFullJavaNetworkException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@Override
	public Graph [] query(String spaceURI, Template template, SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException {
		final Graph [] graphs = query(spaceURI, template, outputFormat, timeout);
		return filterResults(graphs, filters);
	}

	@Override
	public Graph [] query(String spaceURI, Template template, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException {
		if( template instanceof WildcardTemplate ) {
			try {
				final String relativeURI = WildcardConverter.createURLFromTemplate( (WildcardTemplate)template );
				final ClientResource cr = this.clientFactory.createResource( getBaseURI(spaceURI)+"query/wildcards/"+relativeURI );
				final Representation rep = cr.get(NTriplesRepresentation.class);
				
				return createGraphs(cr, rep);
			} catch (ResourceException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (OtsoFullJavaNetworkException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Given a client resource and a representation, generate the proper graph, taking into account signatures.
	 * 
	 * @param clientResource
	 * @param outputRepresentation
	 * @return the graph or null, if the response was some kind of error
	 * @throws IOException
	 */
	private Graph createGraph(final ClientResource clientResource, final Representation outputRepresentation)
			throws IOException, OtsoFullJavaNetworkException {
		if(outputRepresentation == null)
			return null;
		
		if(!(outputRepresentation instanceof SemanticFormatRepresentation))
			throw new OtsoFullJavaNetworkException("Unexpected returned representation (semantic representation expected): " + outputRepresentation.getClass().getName());
		
		// What semantic format was used for the output?
		final SemanticFormat semanticFormat = SemanticFormatRepresentationRegistry.getSemanticFormat(outputRepresentation.getMediaType());
		
		// Is it signed?
		final Form httpAttributes = (Form)clientResource.getResponse().getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
		final String user = httpAttributes.getFirstValue(OTSOPACK_USER, null);
		// TODO: the signature process is still missing
		
		if(user == null) // Not signed
			return new Graph( outputRepresentation.getText(), semanticFormat);
		return new SignedGraph(outputRepresentation.getText(), semanticFormat, new User(user));
	}	
	
	private Graph [] createGraphs(final ClientResource clientResource, final Representation outputRepresentation) throws IOException, OtsoFullJavaNetworkException {
		if(outputRepresentation == null)
			return null;
		
		if(!(outputRepresentation instanceof SemanticFormatRepresentation))
			throw new OtsoFullJavaNetworkException("Unexpected returned representation (semantic representation expected): " + outputRepresentation.getClass().getName());
		
		if(outputRepresentation instanceof RdfMultipartRepresentation)
			return ((RdfMultipartRepresentation) outputRepresentation).getGraphs();
			
		return new Graph[]{ 
			createGraph(clientResource, outputRepresentation)	
		};
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
