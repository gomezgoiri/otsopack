package otsopack.full.java.network.communication.resources.graphs;

import org.restlet.resource.Get;

public interface IWildcardsGraphResource {

	@Get("json")
	public String toJson();

	@Get("html")
	public String toHtml();

}
