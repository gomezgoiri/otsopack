package otsopack.full.java.network.communication;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;

public class WildcardsGraphResource extends ServerResource implements IWildcardGraphResource {

	public static final String ROOT = GraphsResource.ROOT + "/wildcards";
	
	@Override
	public Representation retrieve() {
		return new StringRepresentation("foo");
	}
}
