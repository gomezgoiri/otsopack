package otsopack.full.java.network.communication.util;

import java.util.Arrays;
import java.util.Set;

public class HTMLEncoder {
	public static String encodeURIs(String [] uris, String body){
		final StringBuilder builder = new StringBuilder("<html>\n");
		builder.append("<body>\n");
		builder.append("\t<ul>\n");
		
		// Add other systems
		for(String root : uris)
			addChild(builder, root);
		
		builder.append("\t</ul>\n");
		
		builder.append(body);
		
		builder.append("</body>\n");
		builder.append("</html>\n");
		
		return builder.toString();
	}
	
	public static String encodeURIs(String [] uris){
		return encodeURIs(uris, "");
	}
	
	public static String encodeSortedURIs(Set<String> uris, String body){
		final String [] rootURIs = uris.toArray(new String[]{});
		Arrays.sort(rootURIs);
		return encodeURIs(rootURIs, body);
	}
	
	public static String encodeSortedURIs(Set<String> uris){
		return encodeSortedURIs(uris, "");
	}
	
	private static void addChild(StringBuilder builder, String root){
		builder.append("\t\t<li><a href=\"");
		builder.append(root);
		builder.append("\">");
		builder.append(root);
		builder.append("</a></li>\n");
	}
}
