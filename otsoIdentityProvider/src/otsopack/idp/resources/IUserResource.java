package otsopack.idp.resources;

import org.restlet.representation.Representation;
import org.restlet.resource.Post;

public interface IUserResource {
	
	@Post
	public Representation postUserResource(Representation entity);
}
