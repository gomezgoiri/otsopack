package otsopack.full.java.network.communication.resources.spaces;

import java.util.HashMap;
import java.util.Map;

import otsopack.full.java.network.communication.resources.graphs.GraphsResource;
import otsopack.full.java.network.communication.resources.graphs.WildcardGraphResource;
import otsopack.full.java.network.communication.resources.graphs.WildcardsGraphResource;
import otsopack.full.java.network.communication.util.HTMLEncoder;
import otsopack.full.java.network.communication.util.JSONEncoder;

public class ServerResource implements ISpacesResource {

	public static final String ROOT = GraphsResource.ROOT + "/wildcards";
	
	static Map<String, Class<?>> getRoots(){
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, WildcardsGraphResource.class);
		graphsRoots.put(ServerResource.ROOT, WildcardGraphResource.class);
		return graphsRoots;
	}
	
	@Override
	public String toHtml() {
		return HTMLEncoder.encodeSortedURIs(getRoots().keySet());
	}
	
	@Override
	public String toJson() {
		return JSONEncoder.encodeSortedURIs(getRoots().keySet());
	}
}
