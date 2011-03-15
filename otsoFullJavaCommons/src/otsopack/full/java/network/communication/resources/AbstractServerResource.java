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

import org.restlet.data.MediaType;
import org.restlet.data.Preference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import otsopack.commons.IController;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.impl.SemanticFormatsManager;
import otsopack.full.java.network.communication.OtsopackApplication;
import otsopack.full.java.network.communication.representations.SemanticFormatRepresentationFactory;
import otsopack.full.java.network.communication.representations.SemanticFormatRepresentationRegistry;

public class AbstractServerResource extends ServerResource {
	
	protected final static SemanticFormatsManager semanticFormatsManager = new SemanticFormatsManager();
	protected final static SemanticFormatRepresentationFactory semanticFormatRepresentationFactory = new SemanticFormatRepresentationFactory();
	
	protected String getArgument(String argumentName){
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
	
	protected Representation serializeGraph(Graph graph){
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

	protected void checkInputSemanticFormat() {
		final MediaType contentType = this.getRequestEntity().getMediaType();
		final SemanticFormat semanticFormat = SemanticFormatRepresentationRegistry.getSemanticFormat(contentType);
		
		if(semanticFormat == null)
			throw new ResourceException(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE, "Invalid semantic format: " + contentType.getName());
		
		if(semanticFormatsManager.isInputSupported(semanticFormat))
			throw new ResourceException(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE, "Could not read " + semanticFormat + " format!");
	}
}