package otsopack.full.java.network.communication;

import java.io.ByteArrayOutputStream;

import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

public class PrefixResource extends ServerResource implements IPrefixResource {
	ObjectMapper mapper;
	PrefixesResource pr;
	
	public PrefixResource() {
		this.mapper = new ObjectMapper();
		this.pr = new PrefixesResource();
		/*super.getVariants().add(new Variant(MediaType.TEXT_PLAIN));
		super.getVariants().add(new Variant(MediaType.APPLICATION_JSON));*/
	}
	
	@Get("json")
    public String retrieveJson() {
		Prefix ret = this.retrieve();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			this.mapper.writeValue(baos,ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return baos.toString();
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
