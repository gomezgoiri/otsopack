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
 * 			Pablo Orduña <pablo.orduna@deusto.es>
 */
package otsopack.commons.network.communication;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.routing.Router;
import org.restlet.service.MetadataService;

import otsopack.commons.IController;
import otsopack.commons.authz.entities.IEntity;
import otsopack.commons.network.ICommunication;
import otsopack.commons.network.communication.representations.OtsopackConverter;
import otsopack.commons.network.communication.resources.authn.LoginResource;
import otsopack.commons.network.communication.resources.prefixes.PrefixesResource;
import otsopack.commons.network.communication.resources.prefixes.PrefixesStorage;
import otsopack.commons.network.communication.resources.spaces.SpacesResource;
import otsopack.commons.network.communication.session.UserSession;
import otsopack.restlet.commons.sessions.ISessionManager;
import otsopack.restlet.commons.sessions.memory.MemorySessionManager;

public class OtsopackApplication extends Application {
	
	public static final String OTSOPACK_USER = "X-OTSOPACK-User";
	
	private final Map<String, Class<?>> resources;
	private final ConcurrentMap<String, Object> attributes = new ConcurrentHashMap<String, Object>();
	private final PrefixesStorage prefixesStorage = new PrefixesStorage();
	private final ISessionManager<UserSession> sessionManager = new MemorySessionManager<UserSession>();
	private final ICommunication multicastProvider;
	private final IEntity signer;
	
	private static final String CONTROLLER_PROPERTY_NAME = "controller";
	private static final String TIMEOUT_PROPERTY_NAME    = "timeout";
	private static final Integer DEFAULT_TIMEOUT         = Integer.valueOf(5 * 1000);

	private static final Map<String, Class<?>> PATHS = new HashMap<String, Class<?>>();
	
	static{
		addPaths(PrefixesResource.getRoots());
		addPaths(LoginResource.getRoots());
		addPaths(SpacesResource.getRoots());
	}
	
	private static void addPaths(Map<String, Class<?>> roots){
		for(String uri : roots.keySet()) {
			PATHS.put(uri, roots.get(uri));
			PATHS.put(uri + "/", roots.get(uri));
		}
	}
	
	public OtsopackApplication(){
		this(null, null);
	}
	
	public OtsopackApplication(ICommunication multicastProvider){
		this(multicastProvider, null);
	}
	
	public OtsopackApplication(IEntity signer){
		this(null, signer);
	}
	
	public OtsopackApplication(ICommunication multicastProvider, IEntity signer){
		this.resources = PATHS;
		this.multicastProvider = multicastProvider;
		this.signer = signer;
		this.attributes.put(TIMEOUT_PROPERTY_NAME, DEFAULT_TIMEOUT);
	}
	
	public boolean isMulticastProvider(){
		return this.multicastProvider != null;
	}
	
	public ICommunication getMulticastProvider(){
		return this.multicastProvider;
	}
	
	public IEntity getSigner(){
		return this.signer;
	}
	
	public static void registerExtensions(MetadataService metadataService){
		metadataService.addExtension(OtsopackConverter.MEDIA_TYPE_TURTLE, MediaType.APPLICATION_RDF_TURTLE);
		metadataService.addExtension(OtsopackConverter.MEDIA_TYPE_RDF_XML, MediaType.APPLICATION_RDF_XML);
		metadataService.addExtension(OtsopackConverter.MEDIA_TYPE_ACROSS_MULTIPART, OtsopackConverter.ACROSS_MULTIPART_MEDIA_TYPE);
		// For some reason by default nt is registered in Restlet as TEXT_PLAIN
		metadataService.addExtension(OtsopackConverter.MEDIA_TYPE_NTRIPLES, MediaType.TEXT_RDF_NTRIPLES, true); 
		// n3 is already registered
	}
	
	@Override
	public Restlet createInboundRoot(){
		registerExtensions(getMetadataService());
        final Router router = new Router(getContext());
        
	    for(String pattern : this.resources.keySet())
	    	router.attach(pattern, this.resources.get(pattern));
        
        return router;
	}
	
	public ConcurrentMap<String, Object> getAttributes(){
		return this.attributes;
	}
	
	public IController getController(){
		return (IController)this.attributes.get(CONTROLLER_PROPERTY_NAME);
	}
	
	public void setController(IController controller){
		this.attributes.put(CONTROLLER_PROPERTY_NAME, controller);
	}
	
	public int getTimeout(){
		return ((Integer)this.attributes.get(TIMEOUT_PROPERTY_NAME)).intValue();
	}
	
	public void setTimeout(int timeout){
		this.attributes.put(TIMEOUT_PROPERTY_NAME, Integer.valueOf(timeout));
	}
	
	public PrefixesStorage getPrefixesStorage(){
		return this.prefixesStorage;
	}
	
	public ISessionManager<UserSession> getSessionManager() {
		return this.sessionManager;
	}
}
