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

package otsopack.commons.network.communication.resources.query;

import java.util.HashSet;
import java.util.Set;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

import otsopack.commons.Arguments;
import otsopack.commons.IController;
import otsopack.commons.authz.entities.User;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.Template;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;
import otsopack.commons.exceptions.UnsupportedTemplateException;
import otsopack.commons.network.communication.representations.RepresentationException;
import otsopack.commons.network.communication.resources.AbstractServerResource;
import otsopack.commons.network.communication.resources.graphs.WildcardConverter;
import otsopack.commons.network.communication.util.HTMLEncoder;

public class WildcardQueryResource extends AbstractServerResource implements IWildcardQueryResource {

	public static final String [] ROOTS = {
		// {object-uri} can be "*" also ( /{subject}/{predicate}/* )
		WildcardsQueryResource.ROOT + "/{subject}/{predicate}/{object-uri}",
		WildcardsQueryResource.ROOT + "/{subject}/{predicate}/{object-type}/{object-value}"
	};
	
	private Template getWildcard() {
		final String subject     = getArgument("subject");
		final String predicate   = getArgument("predicate");
		final String objectUri   = getArgument("object-uri");
		final String objectValue = getArgument("object-value");
		final String objectType  = getArgument("object-type");
		
		try {
			return WildcardConverter.createTemplateFromURL(subject, predicate, objectUri, objectValue, objectType, getOtsopackApplication().getPrefixesStorage());
		} catch (MalformedTemplateException e) {
			//TODO never thrown!
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
			//throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Could not serialize retrieved data", e);
		} catch (Exception e) {
			//TODO is this a internal error or a bad request?
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "The given prefix used in the template does not exist");
		}
	}
	
	protected Graph [] getTriplesByWildcard(SemanticFormat outputFormat) {
		final String space = getArgument("space");
		final Template tpl = getWildcard();
		final IController controller = getController();
		final User currentClient = getCurrentClient();
		
		try {
			Graph ret;
			
			if( controller != null ){
				if( currentClient==null )
					ret = controller.getDataAccessService().query(space,tpl, outputFormat);
				else
					ret = controller.getDataAccessService().query(space,tpl, outputFormat, currentClient);
			} else ret=null;
			
			if(ret != null && getSigner() != null){
				ret = ret.sign(getSigner());
			}
			
			Graph [] graphs = new Graph[]{};
			
			if( isMulticastProvider() ){
				final Graph [] queried = getMulticastProvider().query(space, tpl, new Arguments().setOutputFormat(outputFormat));
				if(queried != null)
					graphs = queried;
			}
			
			if( ret==null && graphs.length == 0)
				throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "No graph found with the requested arguments");
			
			
			if( ret==null )
				return graphs;
			
			final Graph [] setOfGraphs = new Graph[graphs.length + 1]; // graphs + ret
			setOfGraphs[0] = ret;
			for(int i = 0; i < graphs.length; ++i)
				setOfGraphs[i + 1] = graphs[i];
			
			return setOfGraphs;
		} catch (SpaceNotExistsException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, SpaceNotExistsException.HTTPMSG);
		} catch (UnsupportedSemanticFormatException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE, "Unsupported output format: " + outputFormat);
		} catch (UnsupportedTemplateException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}
	}
	
	@Override
	public Representation query(){
		final SemanticFormat semanticFormat = checkOutputSemanticFormats();
		final Graph [] graph = getTriplesByWildcard(semanticFormat);
		try {
			return serializeGraphs(graph);
		} catch (RepresentationException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Could not serialize result: " + e.getMessage());
		}
	}
	
	
	@Override
	public Representation toHtml() {
		final HTMLEncoder encoder = new HTMLEncoder();
		
		final Set<String> sets = new HashSet<String>();
		for(String root: ROOTS) sets.add(root);
		encoder.appendRoots(sets);
		
		if (getArgument("object-value")==null) {
			encoder.appendProperties(super.getArguments(ROOTS[0]).entrySet());
		} else {
			encoder.appendProperties(super.getArguments(ROOTS[1]).entrySet());
		}
		
		// TODO display the rest of received graphs!
		encoder.appendGraph( getTriplesByWildcard(SemanticFormat.NTRIPLES)[0] );
		
		return encoder.getHtmlRepresentation();
	}
}