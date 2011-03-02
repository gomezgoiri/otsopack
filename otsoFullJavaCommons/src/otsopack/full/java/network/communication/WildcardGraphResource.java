package otsopack.full.java.network.communication;

import org.restlet.resource.ServerResource;

public class WildcardGraphResource extends ServerResource implements IWildcardGraphResource {

	public static final String PATTERN = WildcardsGraphResource.ROOT + "/{subject}/{predicate}/{object}";

	@Override
	public String toJson(){
		return "bar";
	}
}
