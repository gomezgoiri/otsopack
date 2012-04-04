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
 *         Pablo Orduña <pablo.orduna@deusto.es>
 */

package otsopack.commons.network.communication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.restlet.data.CookieSetting;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.engine.http.header.HeaderConstants;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import otsopack.authn.client.AuthenticationClient;
import otsopack.authn.client.credentials.LocalCredentialsManager;
import otsopack.authn.client.exc.AuthenticationException;
import otsopack.authn.resources.SessionRequestResource;
import otsopack.commons.Arguments;
import otsopack.commons.authz.Filter;
import otsopack.commons.authz.entities.User;
import otsopack.commons.data.Graph;
import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.SignedGraph;
import otsopack.commons.data.Template;
import otsopack.commons.data.WildcardTemplate;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.exceptions.AuthorizationException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;
import otsopack.commons.exceptions.UnsupportedTemplateException;
import otsopack.commons.network.ICommunication;
import otsopack.commons.network.OtsoFullJavaNetworkException;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.commons.network.communication.representations.NTriplesRepresentation;
import otsopack.commons.network.communication.representations.RdfMultipartRepresentation;
import otsopack.commons.network.communication.representations.SemanticFormatRepresentation;
import otsopack.commons.network.communication.representations.SemanticFormatRepresentationRegistry;
import otsopack.commons.network.communication.resources.ClientResourceFactory;
import otsopack.commons.network.communication.resources.authn.LoginResource;
import otsopack.commons.network.communication.resources.cookies.CookieStore;
import otsopack.commons.network.communication.resources.graphs.WildcardConverter;

public class RestUnicastCommunication implements ICommunication {

	private final String baseRESTServer;
	protected final AuthenticationClient authenticationClient;
	private final CookieStore cookieStore = new CookieStore();
	private final ClientResourceFactory clientFactory = new ClientResourceFactory(this.cookieStore);
	
	public RestUnicastCommunication(String restserver) {
		this(restserver, new LocalCredentialsManager()); 
	}
	
	public RestUnicastCommunication(String restserver, LocalCredentialsManager credentialsManager) {
		this.baseRESTServer = uniformizeURI(restserver);
		this.authenticationClient = new AuthenticationClient(credentialsManager);
		SemanticFactory.initialize(new MicrojenaFactory());
	}
	
	private String uniformizeURI(String restserver) {
		if( restserver.endsWith("/")) 
			return restserver.substring(0,restserver.length()-1); // last "/" removed
		return restserver;
	}
	
	public LocalCredentialsManager getLocalCredentialsManager(){
		return this.authenticationClient.getLocalCredentialsManager();
	}
	
	String getBaseURI(String spaceuri) {
		String ret = "";
		try {
			ret = URLEncoder.encode(spaceuri, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return this.baseRESTServer + "/spaces/" + ret;
	}

	@Override
	public void startup() throws TSException {
		
	}

	@Override
	public void shutdown() throws TSException {
	}
	
	/**
	 * It calls to authenticate storing the cookies returned.
	 */
	private String authenticateStoringCookies(final String originalURL,
			final String dataProviderAuthenticationURL)
			throws AuthorizationException {
		try {
			final String ret = this.authenticationClient.authenticate(dataProviderAuthenticationURL, originalURL);
			// we register session id as a cookie
			for(CookieSetting cookie: this.authenticationClient.getCookies()) {
				this.cookieStore.addCookie(cookie);
			}
			return ret;
		} catch (AuthenticationException e1) {
			throw new AuthorizationException(e1.getMessage());
		}
	}
	
	public String login() throws TSException, AuthorizationException {
		final String originalURL = this.baseRESTServer+ LoginResource.ROOT;
		final ClientResource cr = this.clientFactory.createStatefulClientResource( originalURL );
		try {
			cr.get(NTriplesRepresentation.class); // if no exception is thrown, the user is logged
		} catch (ResourceException e) {
			if(e.getStatus().equals(Status.CLIENT_ERROR_UNAUTHORIZED)) {
				final String dataProviderAuthenticationURL = this.baseRESTServer + SessionRequestResource.PUBLIC_ROOT;
				return authenticateStoringCookies(originalURL,
						dataProviderAuthenticationURL);
			}
			// TODO: maybe we would need a more concrete exception, such as "UnexpectedLoginException or so"
			throw new TSException("Unexpected log-in exception: " + e.getStatus() + "; " + e.getMessage());
		}finally{
			cr.release();
		}
		return originalURL;
	}
	
	protected Graph filterResults(Graph graph, Set<Filter> filters) {
		if( graph !=null ){
			for(Filter filter: filters) 
				if( filter.getAssert().evaluate(graph) ) 
					if( !filter.getEntity().check(graph.getEntity()) )
						return null;
		}
		return graph;
	}
	
	protected Graph [] filterResults(Graph [] graphs, Set<Filter> filters) {
		if(graphs == null)
			return null;
		final List<Graph> resultingGraphs = new Vector<Graph>(graphs.length);
		for(Graph graph : graphs){
			final Graph filtered = filterResults(graph, filters);
			if(filtered != null)
				resultingGraphs.add(graph);
		}
		return resultingGraphs.toArray(new Graph[]{});
	}
	
	@Override
	public Graph read(String spaceURI, String graphURI, Arguments configuration)
			throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException {
		Graph ret = null;
		
		/*
		 * 	final MediaType [] clientMediaTypes = SemanticFormatRepresentationRegistry.getMediaTypes(SemanticFormat.NTRIPLES, SemanticFormat.TURTLE);  
		 *	OtsopackConverter.setEnabledVariants(clientMediaTypes);
		 */
		try {
			final String originalURL = getBaseURI(spaceURI)+"/graphs/"+URLEncoder.encode(graphURI, "utf-8");
			try {
				ret = tryGet(originalURL, configuration.getTimeout());
			} catch (ResourceException e) {
				if(e.getStatus().equals(Status.CLIENT_ERROR_UNAUTHORIZED)) {
					final String dataProviderAuthenticationURL = this.baseRESTServer + SessionRequestResource.PUBLIC_ROOT;
					final String redirectionURL = authenticateStoringCookies(originalURL, dataProviderAuthenticationURL);
					ret = tryGet(redirectionURL, configuration.getTimeout()); //retry
				} else throw e;
			}
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		return filterResults(ret, configuration.getFilters());
	}
	
		private Graph tryGet(String originalURL, long timeout) throws UnsupportedSemanticFormatException, SpaceNotExistsException, AuthorizationException {
			try {
				final ClientResource cr = this.clientFactory.createStatefulClientResource( originalURL, timeout );
				try{
					final Representation rep = cr.get(NTriplesRepresentation.class);
					return createGraph(cr, rep);
				}finally{
					cr.release();
				}
			} catch (ResourceException e) {
				if(e.getStatus().equals(Status.CLIENT_ERROR_FORBIDDEN)) {
					throw new AuthorizationException(e.getMessage());
				} else if(e.getStatus().equals(Status.CLIENT_ERROR_NOT_FOUND)) {
					if(e.getMessage().startsWith(SpaceNotExistsException.HTTPMSG)) {
						throw new SpaceNotExistsException(e.getMessage());
					}
					return null; // Graph not found, it returns nothing
				} else if(e.getStatus().equals(Status.CLIENT_ERROR_NOT_ACCEPTABLE)) {
					throw new UnsupportedSemanticFormatException(e.getMessage());
				}
				throw e;
			}catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (OtsoFullJavaNetworkException e) {
				e.printStackTrace();
			}
			return null;
		}

	@Override
	public Graph read(String spaceURI, Template template, Arguments configuration)
			throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		Graph ret = null;
		if( template instanceof WildcardTemplate ) {
			try {
				final String relativeURI = WildcardConverter.createURLFromTemplate( (WildcardTemplate)template );
				final String originalURL = getBaseURI(spaceURI)+"/graphs/wildcards/"+relativeURI;
				final ClientResource cr = this.clientFactory.createStatefulClientResource( originalURL, configuration.getTimeout() );
				try {
					final Representation rep = cr.get(NTriplesRepresentation.class);
					ret = createGraph(cr, rep);
				} catch (ResourceException e) {
					if(e.getStatus().equals(Status.CLIENT_ERROR_NOT_FOUND)) {
						if(e.getMessage().startsWith(SpaceNotExistsException.HTTPMSG)) {
							throw new SpaceNotExistsException(e.getMessage());
						}
					} else if(e.getStatus().equals(Status.CLIENT_ERROR_BAD_REQUEST) ||
							e.getStatus().equals(Status.SERVER_ERROR_INTERNAL)) {
						throw new UnsupportedTemplateException(e.getMessage());
					} else if(e.getStatus().equals(Status.CLIENT_ERROR_NOT_ACCEPTABLE)) {
						throw new UnsupportedSemanticFormatException(e.getMessage());
					}
				} finally{
					cr.release();
				}
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
		return filterResults(ret, configuration.getFilters());
	}

	@Override
	public Graph take(String spaceURI, String graphURI, Arguments configuration)
			throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException {
		/*
		 * 	final MediaType [] clientMediaTypes = SemanticFormatRepresentationRegistry.getMediaTypes(SemanticFormat.NTRIPLES, SemanticFormat.TURTLE);  
		 *	OtsopackConverter.setEnabledVariants(clientMediaTypes);
		 */
		Graph ret = null;
		try {
			final String originalURL = getBaseURI(spaceURI)+"/graphs/"+URLEncoder.encode(graphURI, "utf-8");
			try {
				ret = tryDelete(originalURL);
			} catch (ResourceException e) {
				if(e.getStatus().equals(Status.CLIENT_ERROR_UNAUTHORIZED)) {
					final String dataProviderAuthenticationURL = this.baseRESTServer + SessionRequestResource.PUBLIC_ROOT;
					final String redirectionURL = authenticateStoringCookies(originalURL, dataProviderAuthenticationURL);
					ret = tryDelete(redirectionURL); //retry
				} else throw e;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return filterResults(ret, configuration.getFilters());
	}	
	
		private Graph tryDelete(String originalURL) throws UnsupportedSemanticFormatException, SpaceNotExistsException, AuthorizationException {
			try {
				final ClientResource cr = this.clientFactory.createStatefulClientResource( originalURL );
				try{
					final Representation rep = cr.delete(NTriplesRepresentation.class);
					return createGraph(cr, rep);
				}finally{
					cr.release();
				}
			} catch (ResourceException e) {
				if(e.getStatus().equals(Status.CLIENT_ERROR_FORBIDDEN)) {
					throw new AuthorizationException(e.getMessage());
				} else if(e.getStatus().equals(Status.CLIENT_ERROR_NOT_FOUND)) {
					if(e.getMessage().startsWith(SpaceNotExistsException.HTTPMSG)) {
						throw new SpaceNotExistsException(e.getMessage());
					}
					return null; // Graph not found, it returns nothing
				} else if(e.getStatus().equals(Status.CLIENT_ERROR_NOT_ACCEPTABLE)) {
					throw new UnsupportedSemanticFormatException(e.getMessage());
				}
				throw e;
			}catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (OtsoFullJavaNetworkException e) {
				e.printStackTrace();
			}
			return null;
		}

	@Override
	public Graph take(String spaceURI, Template template, Arguments configuration)
			throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		Graph ret = null;
		if( template instanceof WildcardTemplate ) {
			try {
				final String relativeURI = WildcardConverter.createURLFromTemplate( (WildcardTemplate)template );
				final ClientResource cr = this.clientFactory.createStatefulClientResource( getBaseURI(spaceURI)+"/graphs/wildcards/"+relativeURI, configuration.getTimeout() );
				
				try {
					final Representation rep = cr.delete(NTriplesRepresentation.class);
					ret = createGraph(cr, rep);
				} catch (ResourceException e) {
					if(e.getStatus().equals(Status.CLIENT_ERROR_NOT_FOUND)) {
						if(e.getMessage().startsWith(SpaceNotExistsException.HTTPMSG)) {
							throw new SpaceNotExistsException(e.getMessage());
						}
						// Graph not found, it returns nothing
					} else if(e.getStatus().equals(Status.CLIENT_ERROR_BAD_REQUEST) ||
							e.getStatus().equals(Status.SERVER_ERROR_INTERNAL)) {
						throw new UnsupportedTemplateException(e.getMessage());
					} else if(e.getStatus().equals(Status.CLIENT_ERROR_NOT_ACCEPTABLE)) {
						throw new UnsupportedSemanticFormatException(e.getMessage());
					}
				} finally{
					cr.release();
				}
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
		return filterResults(ret, configuration.getFilters());
	}

	@Override
	public Graph[] query(String spaceURI, Template template, Arguments configuration)
			throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		Graph[] ret = null;
		
		if( template instanceof WildcardTemplate ) {
			try {
				final String relativeURI = WildcardConverter.createURLFromTemplate( (WildcardTemplate)template );
				final ClientResource cr = this.clientFactory.createStatefulClientResource( getBaseURI(spaceURI)+"/query/wildcards/"+relativeURI, configuration.getTimeout() );
				try {
					final Representation rep = cr.get(NTriplesRepresentation.class);
					ret = createGraphs(cr, rep);
				} catch (ResourceException e) {
					if(e.getStatus().equals(Status.CLIENT_ERROR_NOT_FOUND)) {
						if(e.getMessage().startsWith(SpaceNotExistsException.HTTPMSG)) {
							throw new SpaceNotExistsException(e.getMessage());
						}
						// Graph not found, it returns nothing
					} else if(e.getStatus().equals(Status.CLIENT_ERROR_BAD_REQUEST)) {
						throw new UnsupportedTemplateException(e.getMessage());
					} else if(e.getStatus().equals(Status.CLIENT_ERROR_NOT_ACCEPTABLE)) {
						throw new UnsupportedSemanticFormatException(e.getMessage());
					}
				} finally {
					cr.release();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (OtsoFullJavaNetworkException e) {
				e.printStackTrace();
			}
		}
		return filterResults(ret, configuration.getFilters());
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
		final String user = httpAttributes.getFirstValue(OtsopackApplication.OTSOPACK_USER, null);
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
	public void notify(String spaceURI, NotificableTemplate template)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub
	}
}