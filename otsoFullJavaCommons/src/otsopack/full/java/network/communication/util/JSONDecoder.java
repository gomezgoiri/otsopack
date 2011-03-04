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

package otsopack.full.java.network.communication.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

public class JSONDecoder {
	public static <T> T decode(String encoded, Class<T> type, String errorMessage){
		final ObjectMapper mapper = new ObjectMapper();
		
		try {
			return mapper.readValue(encoded, type);
		} catch (Exception e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, errorMessage, e);
		} 
	}
	
	public static <T> T decode(String encoded, Class<T> type){
		return decode(encoded, type, "Couldn't decode request");
	}
}
