package otsopack.authn.resources;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public interface IValidatedSessionResource {

	@Get
	public Representation getValidatedSession();
	
}
