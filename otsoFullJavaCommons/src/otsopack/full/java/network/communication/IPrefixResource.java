package otsopack.full.java.network.communication;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface IPrefixResource {
	@Get
	public abstract Prefix retrieve();
	@Get("json")
	public abstract String retrieveJson();	
	@Put
	public abstract void store(Prefix prefix);
	@Delete
    public abstract void remove(Prefix prefix);
}