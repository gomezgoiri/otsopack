package otsopack.full.java.network.communication.resources.prefixes;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import otsopack.full.java.network.communication.util.HTMLEncoder;
import otsopack.full.java.network.communication.util.JSONEncoder;

public class PrefixesResource extends ServerResource implements IPrefixesResource {
	final ObjectMapper mapper = new ObjectMapper();
	
	static public final HashMap<String,String> prefixesByURI = new HashMap<String,String>();
	static public final HashMap<String,String> prefixesByName = new HashMap<String,String>();
	
	public static final String ROOT = "/prefixes";
	
	public static Map<String, Class<?>> getRoots(){
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, PrefixesResource.class);
		graphsRoots.putAll(PrefixResource.getRoots());
		return graphsRoots;
	}
	
	synchronized public static void clear() {
		prefixesByName.clear();
		prefixesByURI.clear();
	}
	
	synchronized public String getPrefix(String prefixName) {
		return prefixesByName.get(prefixName);
	}
	
	synchronized public String getPrefix(URI prefixUri) {
		return prefixesByURI.get(prefixUri.toString());
	}
	
	@Override
    public String retrieveJson() throws ResourceException {
		final HashMap<String, String> ret = this.retrieve();
		return JSONEncoder.encode(ret);
    }
	
	@Override
	synchronized public HashMap<String, String> retrieve() {
		return prefixesByName;
	}

	synchronized public static void create(String name, String uri) {
		prefixesByName.put(name, uri);
		prefixesByURI.put(uri, name);
	}

	@Override
	public String retrieveHtml() {
		return HTMLEncoder.encodeSortedURIs(getRoots().keySet());
	}
}