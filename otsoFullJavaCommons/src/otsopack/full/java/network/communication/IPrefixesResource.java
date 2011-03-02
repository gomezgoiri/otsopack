package otsopack.full.java.network.communication;

import java.util.HashMap;

import org.restlet.resource.Get;

public interface IPrefixesResource {
	@Get
	public abstract HashMap<String, String> retrieve();
	
	@Get("json")
    public String retrieveJson();
	
	@Get("html")
    public String retrieveHtml();
}