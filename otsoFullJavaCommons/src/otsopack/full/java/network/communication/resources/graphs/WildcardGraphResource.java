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
import org.restlet.resource.ResourceException;

import otsopack.commons.IController;
import otsopack.commons.data.IGraph;
import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.ISemanticFormatExchangeable;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.full.java.network.communication.RestServer;
import otsopack.full.java.network.communication.resources.AbstractServerResource;

public class WildcardGraphResource extends AbstractServerResource implements IWildcardGraphResource {

	public static final String ROOT = WildcardsGraphResource.ROOT + "/{subject}/{predicate}/{object}";
	private final ISemanticFactory sf = new SemanticFactory();
	

	protected IGraph getGraphByWildcard(String semanticFormat) {
		final String space    = getArgument("space");
		final String subject   = getArgument("subject");
		final String predicate = getArgument("predicate");
		final String object    = getArgument("object");
		IGraph ret = null;
		
		try {
			ITemplate tpl = WildcardConverter.createTemplateFromURL(subject,predicate,object);
			
			final IController controller = getController();
			ret = controller.getDataAccessService().read(space, tpl, semanticFormat);
		} catch (SpaceNotExistsException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Space not found", e);
		} catch (MalformedTemplateException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
			//throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Could not serialize retrieved data", e);
		} catch (Exception e) {
			//TODO is this a internal error or a bad request?
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "The given prefix used in the template does not exist", e);
		}
		return ret;
	}
	
	@Override
	public String toJson(){
		final IGraph graph = getGraphByWildcard(ISemanticFormatExchangeable.RDF_JSON);
		// TODO convert IGraph to Json format
		return "JsonGraph";
	}
	
	@Override
	public String toNTriples(){
		if(!this.sf.isInputSupported(ISemanticFormatExchangeable.NTRIPLES))
			throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE, "Server does not support NTriples");
		
		final IGraph graph = getGraphByWildcard(ISemanticFormatExchangeable.NTRIPLES);
		// TODO convert IGraph to N-Triples format
		return "set of ntriples";
	}
}