package otsopack.full.java.network.communication;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.resource.ServerResource;

public class PrefixesResource extends ServerResource implements IPrefixesResource {
	ObjectMapper mapper;
	static public Map<String,Prefix> prefixesByURI;
	static public Map<String,Prefix> prefixesByName;
	
	static {
		prefixesByURI = new HashMap<String,Prefix>();
		prefixesByName = new HashMap<String,Prefix>();
	}
	
	public PrefixesResource() {
		this.mapper = new ObjectMapper();
	}
	
	synchronized public static void clear() {
		prefixesByName.clear();
		prefixesByURI.clear();
	}
	
	synchronized public Prefix getPrefix(String prefixName) {
		return prefixesByName.get(prefixName);
	}
	
	synchronized public Prefix getPrefix(URI prefixUri) {
		return prefixesByURI.get(prefixUri);
	}
	
	@Override
    public String retrieveJson() {
		Prefix[] ret = this.retrieve();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			this.mapper.writeValue(baos,ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return baos.toString();
    }
	
	@Override
	synchronized public Prefix[] retrieve() {
		Prefix[] ret = new Prefix[prefixesByURI.size()];
		return prefixesByURI.values().toArray(ret);
	}

	@Override
	synchronized public void create(Prefix prefix) {
		prefixesByName.put(prefix.getName(),prefix);
		prefixesByURI.put(prefix.getUri(),prefix);
	}
}