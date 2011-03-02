package otsopack.full.java.network.communication;

import org.restlet.resource.ServerResource;

import otsopack.full.java.network.communication.util.JSONEncoder;

public class GraphsManager extends ServerResource implements GraphsResource {

	public static final String ROOT = "/graphs";
	
	private static final String [] roots = new String[]{
		WildcardsGraphManager.ROOT
	};
	
	private static final String html;
	
	static{
		final StringBuilder builder = new StringBuilder("<html>\n");
		builder.append("<body>\n");
		builder.append("\t<ul>\n");
		
		// Add other systems
		for(String root : roots)
			addChild(builder, root);
		
		builder.append("\t</ul>\n");
		builder.append("</body>\n");
		builder.append("</html>\n");
		
		html = builder.toString();
	}
	
	private static void addChild(StringBuilder builder, String root){
		builder.append("\t\t<li><a href=\"");
		builder.append(root);
		builder.append("\">");
		builder.append(root);
		builder.append("</a></li>\n");
	}
	
	@Override
	public String toHtml() {
		return html;
	}
	
	@Override
	public String toJson() {
		return JSONEncoder.encode(roots);
	}
	
}
