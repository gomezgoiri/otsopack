package otsopack.full.java.network.communication.resources.spaces;

import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.ServerResource;

import otsopack.full.java.network.communication.util.HTMLEncoder;
import otsopack.full.java.network.communication.util.JSONEncoder;

public class SpacesResource extends ServerResource implements ISpaceResource {

	public static final String ROOT = "/spaces";
	
	static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, SpacesResource.class);
		graphsRoots.putAll(SpaceResource.getRoots());
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
