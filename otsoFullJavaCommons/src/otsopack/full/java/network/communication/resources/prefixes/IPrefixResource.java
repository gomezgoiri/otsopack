package otsopack.full.java.network.communication.resources.prefixes;

import org.restlet.resource.Get;

public interface IPrefixResource {
	@Get("json")
	public abstract String retrieveJson();	
}