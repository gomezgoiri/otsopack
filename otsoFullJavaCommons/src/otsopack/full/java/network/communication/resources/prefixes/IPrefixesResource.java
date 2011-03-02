package otsopack.full.java.network.communication.resources.prefixes;

import org.restlet.resource.Get;

public interface IPrefixesResource {
	@Get("json")
    public String retrieveJson();
	
	@Get("html")
    public String retrieveHtml();
}