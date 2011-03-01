package otsopack.full.java.network.communication;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;

public class WildcardsGraphManager extends ServerResource implements WildcardGraphResource {

	public static final String ROOT = GraphsManager.ROOT + "/wildcards";
	
	@Override
	public Representation retrieve() {
		return new StringRepresentation("foo");
	}
}
