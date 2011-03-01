package otsopack.full.java.network.communication;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.ServerResource;

public class PrefixesResource extends ServerResource implements IPrefixesResource {
	static public Map<URI,Prefix> prefixesByURI;
	static public Map<String,Prefix> prefixesByName;
	
	static {
		prefixesByURI = new HashMap<URI,Prefix>();
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
	synchronized public Collection<Prefix> retrieve() {
		return prefixesByURI.values();
	}

	@Override
	synchronized public void create(Prefix prefix) {
		prefixesByName.put(prefix.getName(),prefix);
		prefixesByURI.put(prefix.getUri(),prefix);
	}
}