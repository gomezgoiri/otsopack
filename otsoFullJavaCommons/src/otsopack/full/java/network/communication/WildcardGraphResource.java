package otsopack.full.java.network.communication;

import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;

public class WildcardGraphResource extends ServerResource implements IWildcardGraphResource {

	public static final String PATTERN = WildcardsGraphResource.ROOT + "/{subject}/{predicate}/{object}";
	
	@Override
	public Representation retrieve() {
		return null;
	}
}
