package otsopack.full.java.network.communication;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;

public class GraphsManager extends ServerResource implements GraphsResource {

	@Override
	public Representation retrieve() {
		return new StringRepresentation("wildcard");
	}
	
}
