package otsopack.full.java.network.communication.resources.graphs;

import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.full.java.network.communication.resources.prefixes.PrefixesResource;

public class WildcardConverter {
	
	public static ITemplate createTemplateFromURL(String subject, String predicate, String object) throws Exception {
		ISemanticFactory sf = new SemanticFactory();
		return sf.createTemplate(
					adaptFieldFormat(subject,'s') + " " +
					adaptFieldFormat(predicate,'p') + " " +
					adaptFieldFormat(object,'o') +" ."
				);
	}
	
	protected static String adaptFieldFormat(String field, char c) throws Exception {
		if( field.equals("*") ) {
			return "?"+c;
		} else if( field.startsWith("http://") ) {
			return "<" + field + ">";
		} else {
			final String[] split = field.split(":");
			String uri = PrefixesResource.getPrefixByName(split[0]);
			if(uri==null) {
				throw new Exception("This prefix does not exist.");
			}
			if( split.length>1 ) {
				uri = uri + split[1];
			}
			return "<" + uri + ">";
		}
	}
}
