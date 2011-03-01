package otsopack.full.java.network.communication;

import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;

public class WildcardGraphManager extends ServerResource implements WildcardGraphResource {

	public static final String PATTERN = WildcardsGraphManager.ROOT + "/{subject}/{predicate}/{object}";
	
	@Override
	public Representation retrieve() {
		return null;
	}
}
