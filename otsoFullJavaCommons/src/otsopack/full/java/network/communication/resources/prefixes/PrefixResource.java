package otsopack.full.java.network.communication.resources.prefixes;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import otsopack.full.java.network.communication.resources.AbstractServerResource;
import otsopack.full.java.network.communication.util.JSONEncoder;

public class PrefixResource extends AbstractServerResource implements IPrefixResource {
	
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
		final URI uri;
		try {
			uri = new URI(getArgument("prefixname"));
		} catch (URISyntaxException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Prefix must be a valid URI", e);		
		}
		
		final String name = this.pr.getPrefix(uri);
		if( name == null )
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Can't find uri");  

		return JSONEncoder.encode(name);
    }
}
