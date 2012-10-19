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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.commons.network.communication.comet;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import otsopack.commons.data.Graph;
import otsopack.commons.exceptions.AuthorizationException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;
import otsopack.commons.exceptions.UnsupportedTemplateException;
import otsopack.commons.network.ICommunication;
import otsopack.commons.network.communication.comet.event.Event;
import otsopack.commons.network.communication.comet.event.requests.QueryRequest;
import otsopack.commons.network.communication.comet.event.requests.QueryWithFiltersRequest;
import otsopack.commons.network.communication.comet.event.requests.ReadRequest;
import otsopack.commons.network.communication.comet.event.requests.ReadTemplateRequest;
import otsopack.commons.network.communication.comet.event.requests.ReadTemplateWithFiltersRequest;
import otsopack.commons.network.communication.comet.event.requests.ReadUriRequest;
import otsopack.commons.network.communication.comet.event.requests.ReadUriWithFiltersRequest;
import otsopack.commons.network.communication.comet.event.requests.TakeRequest;
import otsopack.commons.network.communication.comet.event.requests.TakeTemplateRequest;
import otsopack.commons.network.communication.comet.event.requests.TakeTemplateWithFiltersRequest;
import otsopack.commons.network.communication.comet.event.requests.TakeUriRequest;
import otsopack.commons.network.communication.comet.event.requests.TakeUriWithFiltersRequest;
import otsopack.commons.network.communication.comet.event.responses.ErrorResponse;
import otsopack.commons.network.communication.comet.event.responses.GraphResponse;
import otsopack.commons.network.communication.util.JSONDecoder;
import otsopack.commons.network.communication.util.JSONEncoder;

public class EventExecutor {
	
	public final ICommunication comm;
	
	public EventExecutor(ICommunication comm){
		this.comm = comm;
	}

	public Event executeEvent(Event event){
		
		final String payload = event.getPayload();
		ErrorResponse errorResponse;
		try{
			final String response;
			
			if(event.getOperation() == null)
				
				throw new CometException("Null operation provided");
			
			else if(event.getOperation().startsWith(CometEvents.READ)){
				
				final ReadRequest request = buildReadRequest(event, payload);
				final Graph graph = request.read(event.getSpaceURI(), this.comm);
				final GraphResponse graphResponse = GraphResponse.create(graph);
				response = JSONEncoder.encode(graphResponse);
				
			}else if(event.getOperation().startsWith(CometEvents.TAKE)){
				
				final TakeRequest request = buildTakeRequest(event, payload);
				final Graph graph = request.take(event.getSpaceURI(), this.comm);
				final GraphResponse graphResponse = GraphResponse.create(graph);
				response = JSONEncoder.encode(graphResponse);
				
			}else if(event.getOperation().startsWith(CometEvents.QUERY)){
				
				final QueryRequest request = buildQueryRequest(event, payload);
				final Graph [] graphs = request.query(event.getSpaceURI(), this.comm);
				final GraphResponse [] graphResponses = new GraphResponse[graphs.length];
				for(int i = 0; i < graphs.length; ++i)
					graphResponses[i] = GraphResponse.create(graphs[i]);

				response = JSONEncoder.encode(graphResponses);
				
			}else
				throw new CometException("Could not understand operation: " + event.getOperation());
			
			return new Event(Event.TYPE_SUCCESSFUL_RESPONSE, event.getEventId(), event.getOperation(), response, event.getSpaceURI());
			
		}catch(ResourceException e){
			if(e.getStatus().equals(Status.CLIENT_ERROR_NOT_FOUND))
				errorResponse = new ErrorResponse(ErrorResponse.CODE_RESOURCE_NOT_FOUND, e.getMessage());
			else if(e.getStatus().equals(Status.CLIENT_ERROR_FORBIDDEN))
				errorResponse = new ErrorResponse(ErrorResponse.CODE_RESOURCE_FORBIDDEN, e.getMessage());
			else if(e.getStatus().getCode() >= 400 && e.getStatus().getCode() < 500)
				errorResponse = new ErrorResponse(ErrorResponse.CODE_RESOURCE_CLIENT, e.getMessage());
			else if(e.getStatus().getCode() >= 500 && e.getStatus().getCode() < 600)
				errorResponse = new ErrorResponse(ErrorResponse.CODE_RESOURCE_SERVER, e.getMessage());
			else
				errorResponse = new ErrorResponse(ErrorResponse.CODE_RESOURCE, e.getMessage());
		}catch(CometException e){
			errorResponse = new ErrorResponse(ErrorResponse.CODE_COMET, e.getMessage());
		}catch(SpaceNotExistsException e){
			errorResponse = new ErrorResponse(ErrorResponse.CODE_TS_SPACE_NOT_EXISTS, e.getMessage());
		}catch(AuthorizationException e){
			errorResponse = new ErrorResponse(ErrorResponse.CODE_TS_AUTHZ, e.getMessage());
		}catch(UnsupportedSemanticFormatException e){
			errorResponse = new ErrorResponse(ErrorResponse.CODE_TS_UNSUPPORTED_FORMAT, e.getMessage());
		}catch(UnsupportedTemplateException e){
			errorResponse = new ErrorResponse(ErrorResponse.CODE_TS_UNSUPPORTED_TEMPLATE, e.getMessage());
		}catch(TSException e){
			errorResponse = new ErrorResponse(ErrorResponse.CODE_TS, e.getMessage());
		}
		
		final String message;
		try{
			message = JSONEncoder.encode(errorResponse);
		}catch(ResourceException re){
			re.printStackTrace();
			return new Event(Event.TYPE_ERROR_RESPONSE, event.getEventId(), event.getOperation(), "Could not even serialize a simple error response!", event.getSpaceURI());
		}
		
		return new Event(Event.TYPE_ERROR_RESPONSE, event.getEventId(), event.getOperation(), message, event.getSpaceURI());
	}

	private QueryRequest buildQueryRequest(Event event, String payload) throws CometException, ResourceException {
		if(event.getOperation().equals(CometEvents.QUERY_TEMPLATE))
			return JSONDecoder.decode(payload, QueryRequest.class);
		
		if(event.getOperation().equals(CometEvents.QUERY_TEMPLATE_FILTERS))
			return JSONDecoder.decode(payload, QueryWithFiltersRequest.class);
		
		throw new CometException("Could not understand take operation: " + event.getOperation());
	}

	private ReadRequest buildReadRequest(Event event, final String payload) throws CometException, ResourceException {
		if(event.getOperation().equals(CometEvents.READ_URI))
			return JSONDecoder.decode(payload, ReadUriRequest.class);
		
		if(event.getOperation().equals(CometEvents.READ_URI_FILTERS))
			return JSONDecoder.decode(payload, ReadUriWithFiltersRequest.class);
		
		if(event.getOperation().equals(CometEvents.READ_TEMPLATE))
			return JSONDecoder.decode(payload, ReadTemplateRequest.class);
		
		if(event.getOperation().equals(CometEvents.READ_TEMPLATE_FILTERS))
			return JSONDecoder.decode(payload, ReadTemplateWithFiltersRequest.class);
		
		throw new CometException("Could not understand read operation: " + event.getOperation());
	}

	private TakeRequest buildTakeRequest(Event event, final String payload) throws CometException, ResourceException {
		if(event.getOperation().equals(CometEvents.TAKE_URI))
			return JSONDecoder.decode(payload, TakeUriRequest.class);
		
		if(event.getOperation().equals(CometEvents.TAKE_URI_FILTERS))
			return JSONDecoder.decode(payload, TakeUriWithFiltersRequest.class);
		
		if(event.getOperation().equals(CometEvents.TAKE_TEMPLATE))
			return JSONDecoder.decode(payload, TakeTemplateRequest.class);
		
		if(event.getOperation().equals(CometEvents.TAKE_TEMPLATE_FILTERS))
			return JSONDecoder.decode(payload, TakeTemplateWithFiltersRequest.class);
	
		throw new CometException("Could not understand take operation: " + event.getOperation());
	}
}