package otsopack.authn.resources;

import org.restlet.representation.Representation;
import org.restlet.resource.Post;

public interface ISessionRequestResource {
	@Post
	public Representation postRequest(Representation representation); 
}
