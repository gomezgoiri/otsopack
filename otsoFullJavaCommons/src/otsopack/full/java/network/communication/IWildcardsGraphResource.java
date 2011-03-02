package otsopack.full.java.network.communication;

import org.restlet.resource.Get;

public interface IWildcardsGraphResource {

	@Get("json")
	public String toJson();

	@Get("html")
	public String toHtml();

}
