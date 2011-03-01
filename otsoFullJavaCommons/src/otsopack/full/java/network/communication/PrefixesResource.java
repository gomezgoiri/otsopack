package otsopack.full.java.network.communication;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.ServerResource;

public class PrefixesResource extends ServerResource implements IPrefixesResource {
	static public Map<String,Prefix> prefixesByURI;
	static public Map<String,Prefix> prefixesByName;
	
	static {
		prefixesByURI = new HashMap<String,Prefix>();
		prefixesByName = new HashMap<String,Prefix>();
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