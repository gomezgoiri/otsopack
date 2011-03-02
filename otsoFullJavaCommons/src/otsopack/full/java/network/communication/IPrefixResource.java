package otsopack.full.java.network.communication;

import org.restlet.resource.Get;

public interface IPrefixResource {
	@Get("json")
	public abstract String retrieveJson();	
}