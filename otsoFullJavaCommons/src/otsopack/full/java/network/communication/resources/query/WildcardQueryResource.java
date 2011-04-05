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

package otsopack.full.java.network.communication.resources.query;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import otsopack.commons.IController;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.Template;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.full.java.network.communication.resources.AbstractServerResource;
import otsopack.full.java.network.communication.resources.graphs.WildcardConverter;
import otsopack.full.java.network.communication.util.HTMLEncoder;

public class WildcardQueryResource extends AbstractServerResource implements IWildcardQueryResource {

	public static final String [] ROOTS = {
		WildcardsQueryResource.ROOT + "/{subject}/{predicate}/{object-type}/{object-value}",
		WildcardsQueryResource.ROOT + "/{subject}/{predicate}/*",
		WildcardsQueryResource.ROOT + "/{subject}/{predicate}/{object-uri}"
	};
	
	protected Graph getWildcard(SemanticFormat semanticFormat) {
		final String space       = getArgument("space");
		final String subject     = getArgument("subject");
		final String predicate   = getArgument("predicate");
		final String objectUri   = getArgument("object-uri");
		final String objectValue = getArgument("object-value");
		final String objectType  = getArgument("object-type");
		final Graph ret;
		
		try {
			final Template tpl = WildcardConverter.createTemplateFromURL(subject,predicate, objectUri, objectValue, objectType, getOtsopackApplication().getPrefixesStorage());
			final IController controller = getController();
			ret = controller.getDataAccessService().query(space,tpl, semanticFormat);
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
	public String toHtml() {
		final StringBuilder bodyHtml = new StringBuilder("<br />\n");
		bodyHtml.append("\t<fieldset>\n\t<legend>Triples</legend>\n");
		bodyHtml.append("\t\t<textarea rows=\"10\" cols=\"50\">");
		bodyHtml.append("triple1, triple2,...");
		bodyHtml.append("\t\t</textarea>\n");
		bodyHtml.append("\t</fieldset>\n");
		
		return HTMLEncoder.encodeURIs(
					super.getArguments(ROOTS[0]).entrySet(),
					null,
					bodyHtml.toString()); // TODO print NTriples
	}
	
	@Override
	public String toJson(){
		final Graph graph = getWildcard(SemanticFormat.RDF_JSON);
		// TODO convert IGraph to Json format
		return "JsonGraph";
	}
	
	@Override
	public String toNTriples(){
		final Graph graph = getWildcard(SemanticFormat.NTRIPLES);
		// TODO convert IGraph to N-Triples format
		return "set of ntriples";
	}
}
