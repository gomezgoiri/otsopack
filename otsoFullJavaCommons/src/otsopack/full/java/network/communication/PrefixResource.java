package otsopack.full.java.network.communication;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;

import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
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
    public String retrieveJson() throws ResourceException {
		final String prefname = this.getRequest().getAttributes().get("prefixname").toString();
		final URI uri;
		try {
			String uriStr = URLDecoder.decode(prefname, "utf-8");
			uri = new URI(uriStr);
		} catch (UnsupportedEncodingException e1) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Prefix must be an UTF-8 encoded URI");
		} catch (URISyntaxException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Prefix must be a valid URI");		
		}
		
		final String name = this.pr.getPrefix(uri);
		if( name == null )
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Can't find uri");  

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			this.mapper.writeValue(baos, name);
		} catch (Exception e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Couldn't serialize result!");
		}
    	return baos.toString();
    }
}
