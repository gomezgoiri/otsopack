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
 * 		   Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */
package otsopack.commons.data;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Deserializes those templates that can be deserialized
 */
public class SerializableTemplateFactory {
	protected static final ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
	
	public static SerializableTemplate create(String serializedTemplate) throws TemplateDeserializingException {
		LinkedHashMap<String, Object> obj;
		try {
			obj = mapper.readValue(serializedTemplate, LinkedHashMap.class);
			if(obj.get("type").equals(WildcardTemplate.code))
				return WildcardTemplate.create(obj);
			throw new TemplateDeserializingException("Could not find proper deserializer for code: " + obj.get("type"));
		} catch (JsonParseException e) {
			throw new TemplateDeserializingException("Could not deserialize template: " + e.getMessage());
		} catch (JsonMappingException e) {
			throw new TemplateDeserializingException("Could not deserialize template: " + e.getMessage());
		} catch (IOException e) {
			throw new TemplateDeserializingException("Could not deserialize template: " + e.getMessage());
		}
	}
}
