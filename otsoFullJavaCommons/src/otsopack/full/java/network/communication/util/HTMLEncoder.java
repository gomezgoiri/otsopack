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
 * Author:	Aitor Gómez Goiri <aitor.gomez@deusto.es>
 * 			Pablo Orduña <pablo.orduna@deusto.es>
 */

package otsopack.full.java.network.communication.util;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Set;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import otsopack.commons.data.Graph;

public class HTMLEncoder {
	String properties = null;
	String roots = null;
	String graph = null;
	String otherContent = null;
	
	public void appendProperties(Set<Entry<String,String>> props) {
		final StringBuilder builder = new StringBuilder();
		if (props!=null) {
			builder.append("\t<p>Properties:</p>\n");
			builder.append("\t<ul>\n");
			
			// Add other systems
			for(Entry<String, String> property : props) {
				builder.append("\t\t<li><span style=\"font-weight: bold;\">");
				builder.append( property.getKey() );
				builder.append(":</span> ");
				builder.append( property.getValue() );
				builder.append("</li>\n");
			}
			
			builder.append("\t</ul>\n");
		} else builder.append("\t<p>No properties shown.</p>\n");
		this.properties = builder.toString();
	}
	
	private void addChild(StringBuilder builder, String root){
		builder.append("\t\t<li><a href=\"");
		builder.append(root);
		builder.append("\">");
		builder.append(root);
		builder.append("</a></li>\n");
	}
	
	public void appendRoots(Set<String> uris) {
		final StringBuilder builder = new StringBuilder();
		if (uris!=null) {
			final String [] rootURIs = uris.toArray(new String[]{});
			Arrays.sort(rootURIs);
			
			builder.append("\t<p>Roots:</p>\n");
			builder.append("\t<ul>\n");
			
			// Add other systems
			for(String root : uris)
				addChild(builder, root);
			
			builder.append("\t</ul>\n");
			
			this.roots = builder.toString();
		}
	}
	
	public void appendOtherContent(String other) {
		if (other!=null) {
			this.otherContent = other;
		}
	}
	
	public void appendGraph(Graph g) {
		if (g!=null) {
			final StringBuilder builder = new StringBuilder("<br />\n");
			builder.append("\t<fieldset>\n\t<legend>Format: ");
			builder.append(g.getFormat().getName()).append("</legend>\n");
			builder.append("\t\t<textarea rows=\"10\" style=\"width:100%;\">");
			builder.append(g.getData());
			builder.append("</textarea>\n");
			builder.append("\t</fieldset>\n");
			this.graph = builder.toString();
		}
	}
	
	public Representation getHtmlRepresentation() {
		final StringBuilder builder = new StringBuilder();
		builder.append("<html>\n").append("<body>\n");
		
		if( this.properties!=null ) builder.append(this.properties);
		if( this.roots!=null ) builder.append(this.roots);
		if( this.graph!=null ) builder.append(this.graph);
		if( this.otherContent!=null ) builder.append(this.otherContent);
		
		builder.append("</body>\n");
		builder.append("</html>\n");
		
		final Representation rep = new StringRepresentation(builder.toString());
		rep.setMediaType(MediaType.TEXT_HTML);
		return rep;
	}
}