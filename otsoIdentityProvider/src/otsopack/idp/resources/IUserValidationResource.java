package otsopack.idp.resources;

import org.restlet.representation.Representation;
import org.restlet.resource.Post;

public interface IUserValidationResource {
	
	@Post()
	public Representation post(Representation entity);
	
}
