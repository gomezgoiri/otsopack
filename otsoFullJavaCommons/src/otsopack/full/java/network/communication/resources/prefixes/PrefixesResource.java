package otsopack.full.java.network.communication.resources.prefixes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import otsopack.full.java.network.communication.util.HTMLEncoder;
import otsopack.full.java.network.communication.util.JSONEncoder;

public class PrefixesResource extends ServerResource implements IPrefixesResource {
	final ObjectMapper mapper = new ObjectMapper();
	
	public static final ConcurrentHashMap<String,String> prefixesByURI  = new ConcurrentHashMap<String,String>();
	public static final ConcurrentHashMap<String,String> prefixesByName = new ConcurrentHashMap<String,String>();
	
	public static final String ROOT = "/prefixes";
	
	public static Map<String, Class<?>> getRoots(){
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, PrefixesResource.class);
		graphsRoots.putAll(PrefixResource.getRoots());
		return graphsRoots;
	}
	
	public static void clear() {
		prefixesByURI.clear();
		prefixesByName.clear();
	}
	
	public static String getPrefixByName(String prefixName) {
		return prefixesByName.get(prefixName);
	}
	
	public static String getPrefixByURI(String prefixUri) {
		return prefixesByURI.get(prefixUri);
	}
	
	@Override
    public String retrieveJson() throws ResourceException {
		final ConcurrentHashMap<String, String> ret = prefixesByURI;
		return JSONEncoder.encode(ret);
    }
	
	public static void create(String name, String uri) {
		prefixesByURI.put(uri, name);
		prefixesByName.put(name, uri);
	}

	@Override
	public String retrieveHtml() {
		final StringBuilder bodyHtml = new StringBuilder("<br>Available prefixes:<br>\n<ul>\n");
		for(Entry<String, String> entry : prefixesByURI.entrySet()){
			bodyHtml.append("\t<li><b>");
			bodyHtml.append(entry.getValue());
			
			String encoded;
			try {
				encoded = URLEncoder.encode(entry.getKey(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				encoded = null;
			}
			bodyHtml.append(":</b> <a href=\"");
			bodyHtml.append(ROOT);
			bodyHtml.append("/");
			bodyHtml.append(encoded);
			bodyHtml.append("\">");
			bodyHtml.append(entry.getKey());
			bodyHtml.append("</a>");
			bodyHtml.append("</li>\n");
		}
		bodyHtml.append("</ul>\n");
		System.out.println(bodyHtml.toString());
		return HTMLEncoder.encodeSortedURIs(getRoots().keySet(), bodyHtml.toString());
	}
}