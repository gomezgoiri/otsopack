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
import otsopack.commons.data.Graph;
import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.full.java.network.communication.resources.AbstractServerResource;

public class WildcardGraphResource extends AbstractServerResource implements IWildcardGraphResource {

	public static final String ROOT = WildcardsGraphResource.ROOT + "/{subject}/{predicate}/{object}";
	private final ISemanticFactory sf = new SemanticFactory();
	

	protected Graph getGraphByWildcard(SemanticFormat semanticFormat) {
		final String space    = getArgument("space");
		final String subject   = getArgument("subject");
		final String predicate = getArgument("predicate");
		final String object    = getArgument("object");
		Graph ret = null;
		
		try {
			ITemplate tpl = WildcardConverter.createTemplateFromURL(subject,predicate,object, getOtsopackApplication().getPrefixesStorage());
			
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
		final Graph graph = getGraphByWildcard(SemanticFormat.RDF_JSON);
		// TODO convert IGraph to Json format
		return "JsonGraph";
	}
	
	@Override
	public String toNTriples(){
		if(!this.sf.isInputSupported(SemanticFormat.NTRIPLES))
			throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE, "Server does not support NTriples");
		
		final Graph graph = getGraphByWildcard(SemanticFormat.NTRIPLES);
		// TODO convert IGraph to N-Triples format
		return "set of ntriples";
	}
}