package otsopack.full.java.network.communication.resources.spaces;

import org.restlet.resource.Get;

public interface ISpacesResource {
	@Get("html")
	public abstract String toHtml();

	@Get("json")
	public abstract String toJson();
}
