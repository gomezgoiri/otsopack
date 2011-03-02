package otsopack.full.java.network.communication;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import otsopack.full.java.network.communication.util.JSONEncoder;

public class PrefixResource extends ServerResource implements IPrefixResource {
	
	public static final String ROOT = PrefixesResource.ROOT + "/{prefixname}";
	
	static Map<String, Class<?>> getRoots(){
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, PrefixResource.class);
		return graphsRoots;
	}
	
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
		} catch (UnsupportedEncodingException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Prefix must be an UTF-8 encoded URI", e);
		} catch (URISyntaxException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Prefix must be a valid URI", e);		
		}
		
		final String name = this.pr.getPrefix(uri);
		if( name == null )
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Can't find uri");  

		return JSONEncoder.encode(name);
    }
}
