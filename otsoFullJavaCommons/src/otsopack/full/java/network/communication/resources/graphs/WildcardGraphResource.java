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
 * Author:	Aitor Gómez Goiri <aitor.gomez@deusto.es>
 * 			Pablo Orduña <pablo.orduna@deusto.es>
 */

package otsopack.full.java.network.communication.resources.graphs;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

import otsopack.commons.IController;
import otsopack.commons.authz.entities.User;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.Template;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;
import otsopack.commons.exceptions.UnsupportedTemplateException;
import otsopack.full.java.network.communication.resources.AbstractServerResource;

public class WildcardGraphResource extends AbstractServerResource implements IWildcardGraphResource {

	public static final String [] ROOTS = {
		// {object-uri} can be "*" also ( /{subject}/{predicate}/* )
		WildcardsGraphResource.ROOT + "/{subject}/{predicate}/{object-uri}",
		WildcardsGraphResource.ROOT + "/{subject}/{predicate}/{object-type}/{object-value}"
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
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "The given prefix used in the template does not exist", e);
		}
	}

	protected Graph readGraphByWildcard(SemanticFormat outputFormat) {
		final String space = getArgument("space");
		final Template tpl = getWildcard();
		final IController controller = getController();
		final User currentClient = getCurrentClient();
		
		try {
			final Graph ret;
			if( currentClient==null )
				ret = controller.getDataAccessService().read(space, tpl, outputFormat);
			else
				ret = controller.getDataAccessService().read(space, tpl, outputFormat, currentClient);
			
			if( ret!=null ) 
				return ret;
			
			if(isMulticastProvider()){
				final Graph multicastGraph = getMulticastProvider().read(space, tpl, outputFormat, getTimeout());
				if(multicastGraph != null)
					return multicastGraph;
			}
		} catch (SpaceNotExistsException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, SpaceNotExistsException.HTTPMSG, e);
		} catch (UnsupportedSemanticFormatException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE, "Unsupported output format: " + outputFormat, e);
		} catch (UnsupportedTemplateException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}
		throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Graph not found");
	}
	
	protected Graph takeGraphByWildcard(SemanticFormat outputFormat) {
		final String space = getArgument("space");
		final Template tpl = getWildcard();
		final IController controller = getController();
		final User currentClient = getCurrentClient();
		
		try {
			final Graph ret;
			if( currentClient==null )
				ret = controller.getDataAccessService().take(space, tpl, outputFormat);
			else
				ret = controller.getDataAccessService().take(space, tpl, outputFormat, currentClient);
			
			if( ret!=null ) 
				return ret;

			if(isMulticastProvider()){
				final Graph multicastGraph = getMulticastProvider().take(space, tpl, outputFormat, getTimeout());
				if(multicastGraph != null)
					return multicastGraph;
			}
		} catch (SpaceNotExistsException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, SpaceNotExistsException.HTTPMSG, e);
		} catch (UnsupportedSemanticFormatException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE, "Unsupported output format: " + outputFormat, e);
		} catch (UnsupportedTemplateException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}
		throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Graph not found");
	}
	
	@Override
	public Representation read(){
		SemanticFormat semanticFormat = checkOutputSemanticFormats();
		final Graph graph = readGraphByWildcard(semanticFormat);
		return serializeGraph(graph);
	}
	
	@Override
	public Representation take(){
		SemanticFormat semanticFormat = checkOutputSemanticFormats();
		final Graph graph = takeGraphByWildcard(semanticFormat);
		return serializeGraph(graph);
	}
}