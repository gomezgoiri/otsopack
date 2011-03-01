package otsopack.full.java.network.communication;

import org.restlet.resource.Get;

public interface GraphsResource {
	@Get
	public abstract String [] retrieve();

}
