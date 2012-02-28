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
package otsopack.commons.data;

import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Deserializes those templates that can be deserialized
 */
public class SerializableTemplateFactory {
	public static SerializableTemplate create(String serializedTemplate) throws TemplateDeserializingException {
		try{
			final JSONObject obj = new JSONObject(serializedTemplate);
			if(obj.get("type").equals(WildcardTemplate.code))
				return WildcardTemplate.create(obj);
			
			throw new TemplateDeserializingException("Could not find proper deserializer for code: " + obj.get("type"));
		}catch(JSONException e){
			e.printStackTrace();
			throw new TemplateDeserializingException("Could not deserialize template: " + e.getMessage());
		}
		
	}
}
