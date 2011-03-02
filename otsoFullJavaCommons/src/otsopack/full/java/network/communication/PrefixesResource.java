package otsopack.full.java.network.communication;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.resource.ServerResource;

public class PrefixesResource extends ServerResource implements IPrefixesResource {
	final ObjectMapper mapper = new ObjectMapper();
	
	static public final HashMap<String,String> prefixesByURI = new HashMap<String,String>();
	static public final HashMap<String,String> prefixesByName = new HashMap<String,String>();
	
	synchronized public static void clear() {
		prefixesByName.clear();
		prefixesByURI.clear();
	}
	
	synchronized public String getPrefix(String prefixName) {
		return prefixesByName.get(prefixName);
	}
	
	synchronized public String getPrefix(URI prefixUri) {
		return prefixesByURI.get(prefixUri);
	}
	
	@Override
    public String retrieveJson() {
		final Map<String, String> ret = this.retrieve();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			this.mapper.writeValue(baos,ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return baos.toString();
    }
	
	@Override
	synchronized public HashMap<String, String> retrieve() {
		return prefixesByName;
	}

	synchronized public static void create(String name, String uri) {
		prefixesByName.put(name, uri);
		prefixesByURI.put(uri, name);
	}
}