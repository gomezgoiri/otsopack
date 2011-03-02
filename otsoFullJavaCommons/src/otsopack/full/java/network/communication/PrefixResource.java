package otsopack.full.java.network.communication;

import java.io.ByteArrayOutputStream;

import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;

public class PrefixResource extends ServerResource implements IPrefixResource {
	ObjectMapper mapper;
	PrefixesResource pr;
	
	public PrefixResource() {
		this.mapper = new ObjectMapper();
		this.pr = new PrefixesResource();
		/*super.getVariants().add(new Variant(MediaType.TEXT_PLAIN));
		super.getVariants().add(new Variant(MediaType.APPLICATION_JSON));*/
		//super.getVariants().add(new Variant(MediaType.APPLICATION_JAVA_OBJECT));
	}
	
	@Override
    public String retrieveJson() {
		String prefname = this.getRequest().getAttributes().get("prefixname").toString();
		final String uri = this.pr.getPrefix(prefname);
		if( uri == null ) {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);  
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			this.mapper.writeValue(baos, uri);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("En JSON esto es...");
    	return baos.toString();
    }
}
