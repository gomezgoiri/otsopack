/*
 * Copyright (C) 2008-2011 University of Deusto
 * 
 * All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 */

package otsopack.full.java.network.communication.util;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Set;

public class HTMLEncoder {
	public static String encodeURIs(Set<Entry<String,String>> properties, Set<String> uris, String body){
		final StringBuilder builder = new StringBuilder("<html>\n");
		builder.append("<body>\n");
		
		appendProperties(builder,properties);
		appendRoots(builder, uris);
		builder.append(body);
		
		builder.append("</body>\n");
		builder.append("</html>\n");
		
		return builder.toString();
	}
	
	private static void appendProperties(StringBuilder builder,	Set<Entry<String, String>> properties) {
		if( properties!=null ) {
			builder.append("\t<p>Properties:</p>\n");
			builder.append("\t<ul>\n");
			
			// Add other systems
			for(Entry<String, String> property : properties) {
				builder.append("\t\t<li><span style=\"font-weight: bold;\">");
				builder.append( property.getKey() );
				builder.append(":</span> ");
				builder.append( property.getValue() );
				builder.append("</li>\n");
			}
			
			builder.append("\t</ul>\n");
		} else builder.append("\t<p>No properties shown.</p>\n");
	}
	
		private static void addChild(StringBuilder builder, String root){
			builder.append("\t\t<li><a href=\"");
			builder.append(root);
			builder.append("\">");
			builder.append(root);
			builder.append("</a></li>\n");
		}

	private static void appendRoots(final StringBuilder builder, final Set<String> uris) {
		if( uris!=null ) {
			final String [] rootURIs = uris.toArray(new String[]{});
			Arrays.sort(rootURIs);
			
			builder.append("\t<p>Roots:</p>\n");
			builder.append("\t<ul>\n");
			
			// Add other systems
			for(String root : uris)
				addChild(builder, root);
			
			builder.append("\t</ul>\n");
		}
	}
	
	public static String encodeURIs(Set<String> uris, String body){
		return encodeURIs(null,uris,body);
	}
	
	public static String encodeURIs(Set<Entry<String,String>> properties, Set<String> uris){
		return encodeURIs(properties,uris, "");
	}
	
	public static String encodeURIs(Set<String> uris){
		return encodeURIs(null,uris);
	}
}