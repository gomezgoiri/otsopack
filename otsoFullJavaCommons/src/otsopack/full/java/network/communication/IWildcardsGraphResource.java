package otsopack.full.java.network.communication;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public interface IWildcardsGraphResource {
	
	@Get
	public abstract Representation retrieve();

}
