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
package otsopack.otsoME.network.communication.incoming.response;

import otsopack.otsoMobile.data.IModel;

public class LockModelResponse extends ModelResponse {
	final Object lock;
	int numberOfResponses;
	
	public LockModelResponse(Object inRespTo, Object lock, int numberOfResponsesExpected) {
		super(inRespTo);
		this.numberOfResponses = numberOfResponsesExpected;
		this.lock = lock;
	}
	
	/**
	 * This method is used for specify model responses
	 * to query, read and take primitives.
	 * @param model
	 * 		The model in response to a primitive.
	 */
	public void addTriples(IModel model) {
		if(model!=null) {
			synchronized(lock) {
				numberOfResponses--;
				super.addTriples(model);
				if(numberOfResponses<=0) {
					lock.notify();
				}
			}
		}
	}
	
	//same as parent
	/*public boolean equals(Object o) {
		return (o instanceof LockModelResponse) && super.equals(o);
	}*/
}