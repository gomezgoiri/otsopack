package otsopack.full.java.network.communication;

import org.restlet.resource.Get;
import org.restlet.resource.Post;

public interface IPrefixesResource {
	@Get
	public abstract Prefix[] retrieve();
	
	@Get("json")
    public String retrieveJson();

	@Post
	public abstract void create(Prefix prefix);
}