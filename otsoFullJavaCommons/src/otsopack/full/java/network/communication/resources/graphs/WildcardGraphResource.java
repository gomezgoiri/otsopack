package otsopack.full.java.network.communication.resources.graphs;

import otsopack.commons.IController;
import otsopack.commons.data.IGraph;
import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.full.java.network.communication.RestServer;
import otsopack.full.java.network.communication.resources.AbstractServerResource;


public class WildcardGraphResource extends AbstractServerResource implements IWildcardGraphResource {

	public static final String ROOT = WildcardsGraphResource.ROOT + "/{subject}/{predicate}/{object}";
	private final ISemanticFactory sf = new SemanticFactory();

	@Override
	public String toJson(){
		final String subject   = getArgument("subject");
		final String predicate = getArgument("predicate");
		final String object    = getArgument("object");
		
		try {
			ITemplate tpl = createTemplate(subject,predicate,object);
			
			IController controller = (IController) RestServer.getCurrent().getAttributes().get("controller");
			// TODO pass space
			IGraph gr = controller.getDataAccessService().read(null,tpl);
		} catch (SpaceNotExistsException e) {
			// TODO Status!
			e.printStackTrace();
		} catch (MalformedTemplateException e) {
			// TODO Status!
			e.printStackTrace();
		}
		return "bar";
	}

	protected ITemplate createTemplate(String subject, String predicate, String object) throws MalformedTemplateException {
		return this.sf.createTemplate(
					adaptFieldFormat(subject) + " " +
					adaptFieldFormat(predicate) + " " +
					adaptFieldFormat(object) +" ."
				);
	}
	
	protected String adaptFieldFormat(String field) {
		if( field.equals("*") ) {
			return "?s";
		} else if( field.startsWith("http://") ) {
			return "<" + field + ">";
		} else {
			final String[] split = field.split(":");
			//TODO
			final String uri = "";// = split[0].getPrefix(byName!);
			return "<" + uri + split[1] + ">";
		}
	}
	
	
}
