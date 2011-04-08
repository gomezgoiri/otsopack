package otsopack.authn.resources;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class FakeClientResource extends ClientResource {
	
	public Object obtainedRepresentation;
	public Representation returnedRepresentation;
	
	@Override
	public Representation post(Object repr){
		this.obtainedRepresentation = repr;
		return this.returnedRepresentation;
	}
}