package otsopack.full.java.network.communication;

import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

public class PrefixResource extends ServerResource implements IPrefixResource {
	PrefixesResource pr;
	
	public PrefixResource() {
		this.pr = new PrefixesResource();
	}
	
	@Get
    public Prefix retrieve() {
		String prefname = this.getRequest().getAttributes().get("prefixname").toString();
		Prefix ret = this.pr.getPrefix(prefname);
		if( ret==null ) {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);  
		}
    	return ret;
    }

	@Put
    public void store(Prefix prefix) {
    	
    }

    @Delete
    public void remove(Prefix prefix) {
    	
    }
}
