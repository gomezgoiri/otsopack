package otsopack.full.java.network.communication;

import java.util.Collection;

import org.restlet.resource.Get;
import org.restlet.resource.Post;

public interface IPrefixesResource {
	@Get
	public abstract Collection<Prefix> retrieve();

	@Post
	public abstract void create(Prefix prefix);
}