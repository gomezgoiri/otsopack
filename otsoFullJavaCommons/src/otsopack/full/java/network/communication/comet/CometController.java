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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.full.java.network.communication.comet;

import org.restlet.resource.ResourceException;

import otsopack.commons.data.Graph;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.network.ICommunication;
import otsopack.full.java.network.communication.comet.event.Event;
import otsopack.full.java.network.communication.comet.event.requests.QueryRequest;
import otsopack.full.java.network.communication.comet.event.requests.QueryWithFiltersRequest;
import otsopack.full.java.network.communication.comet.event.requests.ReadRequest;
import otsopack.full.java.network.communication.comet.event.requests.ReadTemplateRequest;
import otsopack.full.java.network.communication.comet.event.requests.ReadTemplateWithFiltersRequest;
import otsopack.full.java.network.communication.comet.event.requests.ReadUriRequest;
import otsopack.full.java.network.communication.comet.event.requests.ReadUriWithFiltersRequest;
import otsopack.full.java.network.communication.comet.event.requests.TakeRequest;
import otsopack.full.java.network.communication.comet.event.requests.TakeTemplateRequest;
import otsopack.full.java.network.communication.comet.event.requests.TakeTemplateWithFiltersRequest;
import otsopack.full.java.network.communication.comet.event.requests.TakeUriRequest;
import otsopack.full.java.network.communication.comet.event.requests.TakeUriWithFiltersRequest;
import otsopack.full.java.network.communication.comet.event.responses.GraphResponse;
import otsopack.full.java.network.communication.util.JSONDecoder;
import otsopack.full.java.network.communication.util.JSONEncoder;
import otsopack.restlet.commons.sessions.ISessionManager;
import otsopack.restlet.commons.sessions.memory.MemorySessionManager;

public class CometController implements ICometController {

	private final ISessionManager<CometSession> sessionManager;
	private final ICommunication comm;
	
	public CometController(ISessionManager<CometSession> sessionManager, ICommunication comm){
		this.sessionManager = sessionManager;
		this.comm           = comm;
	}
	
	public CometController(ICommunication comm){
		this(new MemorySessionManager<CometSession>(), comm);
	}
	
	@Override
	public ISessionManager<CometSession> getSessionManager() {
		return this.sessionManager;
	}
	
	public void pushEvent(){
		
	}
	
	public void executeEvent(Event event){
		
		final String payload = event.getPayload();
		
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
			
			System.out.println(response);
			
		}catch(ResourceException e){
			e.printStackTrace();
		}catch(CometException e){
			e.printStackTrace();
		}catch(TSException e){
			e.printStackTrace();
		}
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
