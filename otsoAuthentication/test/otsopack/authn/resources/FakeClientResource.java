package otsopack.authn.resources;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

final class FakeClientResource extends ClientResource {
	
	Object obtainedRepresentation;
	Representation returnedRepresentation;
	
	@Override
	public Representation post(Object repr){
		this.obtainedRepresentation = repr;
		return this.returnedRepresentation;
	}
}