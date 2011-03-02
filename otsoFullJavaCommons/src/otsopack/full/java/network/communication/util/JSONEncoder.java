package otsopack.full.java.network.communication.util;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

public class JSONEncoder {
	
	public static String encode(Serializable serializable, String errorMessage){
		final ObjectMapper mapper = new ObjectMapper();
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			mapper.writeValue(baos, serializable);
		} catch (Exception e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, errorMessage, e);
		}
    	return baos.toString();
	}
	
	public static String encode(Serializable serializable){
		return encode(serializable, "Could not serialize response");
	}
}
