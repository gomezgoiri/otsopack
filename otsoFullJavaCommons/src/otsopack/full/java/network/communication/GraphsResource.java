package otsopack.full.java.network.communication;

import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.ServerResource;

import otsopack.full.java.network.communication.util.HTMLEncoder;
import otsopack.full.java.network.communication.util.JSONEncoder;

public class GraphsResource extends ServerResource implements IGraphsResource {

	public static final String ROOT = "/graphs";
	
	static Map<String, Class<?>> getRoots(){
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, GraphsResource.class);
		graphsRoots.putAll(WildcardsGraphResource.getRoots());
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
