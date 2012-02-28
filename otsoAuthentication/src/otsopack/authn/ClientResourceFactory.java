/*
 * Copyright (C) 2011 onwards University of Deusto
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
 */
package otsopack.authn;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.resource.ClientResource;

public class ClientResourceFactory implements IClientResourceFactory {
	
	// TODO: this is not clean at all
	public ClientResource createResource(String url){
		return new ClientResource(url){
			@Override
			public Context getContext(){
				return null;
			}
			
			@Override
			public Application getApplication(){
				return new Application(Context.getCurrent()){
					@Override
					public Restlet getOutboundRoot(){
						return null;
					}
				};
			}
		};
	}
	
}