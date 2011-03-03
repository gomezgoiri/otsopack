package otsopack.full.java.network.communication.resources.graphs;

import org.restlet.resource.Get;

public interface IWildcardGraphResource {
	
	@Get("json")
	public String toJson();
	
	@Get("nt")
	public String toNTriples();

}
