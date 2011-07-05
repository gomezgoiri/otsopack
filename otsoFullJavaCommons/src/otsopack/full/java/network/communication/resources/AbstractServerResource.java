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
 * Author:  Pablo Orduña <pablo.orduna@deusto.es>
 *			Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */

package otsopack.full.java.network.communication.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Preference;
import org.restlet.data.Status;
import org.restlet.engine.http.header.HeaderConstants;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import otsopack.commons.IController;
import otsopack.commons.authz.entities.IEntity;
import otsopack.commons.authz.entities.User;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.impl.SemanticFormatsManager;
import otsopack.commons.network.ICommunication;
import otsopack.full.java.network.communication.OtsopackApplication;
import otsopack.full.java.network.communication.representations.RepresentationException;
import otsopack.full.java.network.communication.representations.SemanticFormatRepresentationFactory;
import otsopack.full.java.network.communication.representations.SemanticFormatRepresentationRegistry;
import otsopack.full.java.network.communication.session.UserSession;

public class AbstractServerResource extends ServerResource {
	
	protected final static SemanticFormatsManager semanticFormatsManager = new SemanticFormatsManager();
	protected final static SemanticFormatRepresentationFactory semanticFormatRepresentationFactory = new SemanticFormatRepresentationFactory();
	
	protected String getArgument(String argumentName){
		if( !this.getRequest().getAttributes().containsKey(argumentName) ) return null;
		
		final String prefname = this.getRequest().getAttributes().get(argumentName).toString();
		try {
			return URLDecoder.decode(prefname, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Prefix must be an UTF-8 encoded value", e);
		}		
	}
		
	protected Set<String> getArgumentNamesFromURI(String rootURI) {
		final Set<String> ret = new HashSet<String>();
		
		Pattern pattern = Pattern.compile("\\{(\\w+)\\}");
		Matcher matcher = pattern.matcher(rootURI);
		while (matcher.find()) {
			final String argn = matcher.group();
			ret.add(argn.substring(1, argn.length()-1));
		}
		return ret;
	}
	
	protected IEntity getSigner(){
		return getOtsopackApplication().getSigner();
	}
	
	protected int getTimeout(){
		return getOtsopackApplication().getTimeout();
	}
	
	protected IController getController() {
		return getOtsopackApplication().getController();
	}
	
	protected OtsopackApplication getOtsopackApplication(){
		return (OtsopackApplication)this.getApplication();
	}
		
	protected Map<String,String> getArguments(String rootURI) {
		final Map<String,String> properties = new HashMap<String,String>();
		final Set<String> argnames = getArgumentNamesFromURI(rootURI);
		for(String argname: argnames) {
			properties.put(argname, getArgument(argname));
		}
		return properties;
	}
	
	
	protected ConcurrentHashMap<String, String> getPrefixesByURI(){
		return getOtsopackApplication().getPrefixesStorage().getPrefixesByURI();
	}
	
	protected ConcurrentHashMap<String, String> getPrefixesByName(){
		return getOtsopackApplication().getPrefixesStorage().getPrefixesByName();
	}
	
	protected String getPrefixByName(String prefixName) {
		return getOtsopackApplication().getPrefixesStorage().getPrefixByName(prefixName);
	}
	
	protected String getPrefixByURI(String prefixUri) {
		return getOtsopackApplication().getPrefixesStorage().getPrefixByURI(prefixUri);
	}

	protected SemanticFormat [] getAcceptedSemanticFormats(){
		final List<SemanticFormat> acceptedSemanticFormats = new Vector<SemanticFormat>();
		for(Preference<MediaType> acceptedMediaType : getRequest().getClientInfo().getAcceptedMediaTypes()){
			final SemanticFormat acceptedSemanticFormat = SemanticFormatRepresentationRegistry.getSemanticFormat(acceptedMediaType.getMetadata());
			if(acceptedSemanticFormat != null)
				acceptedSemanticFormats.add(acceptedSemanticFormat);
		}
		
		return acceptedSemanticFormats.toArray(new SemanticFormat[]{});
	}
	
	protected Representation serializeGraphs(Graph [] graphs) throws RepresentationException{
		if(graphs.length == 1)
			addSignatureHttpHeader(graphs[0]);
		return semanticFormatRepresentationFactory.create(graphs);
	}
	
	private void addSignatureHttpHeader(Graph graph){
		if(graph.getEntity() instanceof User){
			final User user = (User)graph.getEntity();
			Form httpHeaders = (Form)getResponse().getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
			if(httpHeaders == null){
				httpHeaders = new Form();
				getResponse().getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS, httpHeaders);
			}
			httpHeaders.add(OtsopackApplication.OTSOPACK_USER, user.getId());
		}
	}
	
	protected Representation serializeGraph(Graph graph){
		addSignatureHttpHeader(graph);
		return semanticFormatRepresentationFactory.create(graph);
	}
	
	protected  SemanticFormat checkInputOutputSemanticFormats() {
		checkInputSemanticFormat();
		final SemanticFormat outputFormat = checkOutputSemanticFormats();
		return outputFormat;
	}

	protected SemanticFormat checkOutputSemanticFormats() {
		final SemanticFormat outputFormat = semanticFormatsManager.retrieveProperOutput(getAcceptedSemanticFormats());
		if(outputFormat == null)
			throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE, "Can't accept requested formats");
		return outputFormat;
	}
	
	protected SemanticFormat getInputSemanticFormat(){
		final MediaType contentType = this.getRequestEntity().getMediaType();
		final SemanticFormat semanticFormat = SemanticFormatRepresentationRegistry.getSemanticFormat(contentType);
		return semanticFormat;
	}

	protected void checkInputSemanticFormat() {
		final MediaType contentType = this.getRequestEntity().getMediaType();
		final SemanticFormat semanticFormat = SemanticFormatRepresentationRegistry.getSemanticFormat(contentType);
		
		if(semanticFormat == null)
			throw new ResourceException(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE, "Invalid semantic format: " + contentType.getName());
		
		if(!semanticFormatsManager.isInputSupported(semanticFormat))
			throw new ResourceException(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE, "Could not read " + semanticFormat + " format!");
	}
	
	private String obtainSessionIdFromCookies() {
		return this.getRequest().getCookies().getFirstValue("sessionID");
	}
	
	private String obtainSessionIdFromGETParameter() {
		return this.getRequest().getResourceRef().getQueryAsForm().getFirstValue("sessionID");
	}
	
	//XXX test!
	protected User getCurrentClient() {//throws ClientNotAuthenticatedException {
		String sessionId = obtainSessionIdFromCookies();
		if( sessionId==null ) 
			sessionId = obtainSessionIdFromGETParameter();
		if ( sessionId == null)
			return null;
		final UserSession us = ((OtsopackApplication)getApplication()).getSessionManager().getSession(sessionId);
		if(us==null) 
			return null;
		return new User(us.getUserIdentifier());
	}
	
	protected boolean isMulticastProvider(){
		return getOtsopackApplication().isMulticastProvider();
	}
	
	protected ICommunication getMulticastProvider(){
		return getOtsopackApplication().getMulticastProvider();
	}	
}