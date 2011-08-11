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

import otsopack.commons.authz.Filter;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.network.ICommunication;
import otsopack.full.java.network.communication.comet.event.Event;
import otsopack.full.java.network.communication.comet.event.ReadUriRequest;
import otsopack.full.java.network.communication.comet.event.ReadUriWithFiltersRequest;
import otsopack.full.java.network.communication.util.JSONDecoder;
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
			if(event.getOperation().equals(CometEvents.READ_URI)){
				
				final ReadUriRequest request = JSONDecoder.decode(payload, ReadUriRequest.class);
				this.comm.read(event.getSpaceURI(), request.getUri(), request.getOutputFormat(), request.getTimeout());
				
			}else if(event.getOperation().equals(CometEvents.READ_URI_FILTERS)){
				
				final ReadUriWithFiltersRequest request = JSONDecoder.decode(payload, ReadUriWithFiltersRequest.class);
				this.comm.read(event.getSpaceURI(), request.getUri(), request.getOutputFormat(), request.getFilters(), request.getTimeout());
				
			}
		}catch(TSException e){
			
		}
	}
}
