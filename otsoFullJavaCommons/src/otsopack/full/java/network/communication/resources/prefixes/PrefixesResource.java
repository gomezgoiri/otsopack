package otsopack.full.java.network.communication.resources.prefixes;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import otsopack.full.java.network.communication.util.HTMLEncoder;
import otsopack.full.java.network.communication.util.JSONEncoder;

public class PrefixesResource extends ServerResource implements IPrefixesResource {
	final ObjectMapper mapper = new ObjectMapper();
	
	public static final ConcurrentHashMap<String,String> prefixesByURI  = new ConcurrentHashMap<String,String>();
	
	public static final String ROOT = "/prefixes";
	
	public static Map<String, Class<?>> getRoots(){
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, PrefixesResource.class);
		graphsRoots.putAll(PrefixResource.getRoots());
		return graphsRoots;
	}
	
	public static void clear() {
		prefixesByURI.clear();
	}
	
	public String getPrefix(URI prefixUri) {
		return prefixesByURI.get(prefixUri.toString());
	}
	
	@Override
    public String retrieveJson() throws ResourceException {
		final ConcurrentHashMap<String, String> ret = prefixesByURI;
		return JSONEncoder.encode(ret);
    }
	
	public static void create(String name, String uri) {
		prefixesByURI.put(uri, name);
	}

	@Override
	public String retrieveHtml() {
		return HTMLEncoder.encodeSortedURIs(getRoots().keySet());
	}
}