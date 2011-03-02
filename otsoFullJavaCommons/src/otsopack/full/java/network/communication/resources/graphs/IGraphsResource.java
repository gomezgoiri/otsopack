package otsopack.full.java.network.communication.resources.graphs;

import org.restlet.resource.Get;

public interface IGraphsResource {
	@Get("html")
	public abstract String toHtml();

	@Get("json")
	public abstract String toJson();
}
