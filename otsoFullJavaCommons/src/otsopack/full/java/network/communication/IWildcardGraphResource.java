package otsopack.full.java.network.communication;

import org.restlet.resource.Get;

public interface IWildcardGraphResource {
	
	@Get("json")
	public String toJson();

}
