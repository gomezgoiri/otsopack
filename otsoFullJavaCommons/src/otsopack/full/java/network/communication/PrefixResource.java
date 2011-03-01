package otsopack.full.java.network.communication;

import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface PrefixResource {

	@Get
	public abstract String retrieve();

	@Put
	public abstract void store(Prefix prefix);

}