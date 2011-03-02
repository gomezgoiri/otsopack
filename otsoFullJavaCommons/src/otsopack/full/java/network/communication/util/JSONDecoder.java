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
