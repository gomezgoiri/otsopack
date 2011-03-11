package otsopack.full.java.network.communication.representations;

import java.io.IOException;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormats;

public class TurtleRepresentation extends SemanticFormatRepresentation {

	public final static MediaType TEXT_RDF_TURTLE = MediaType.register("text/turtle", "Turtle");

	public TurtleRepresentation(Graph graph) {
		super(TEXT_RDF_TURTLE, graph);
	}

	public TurtleRepresentation(Representation representation) throws IOException {
		super(TEXT_RDF_TURTLE, representation);
	}

	public TurtleRepresentation(String data) {
		super(TEXT_RDF_TURTLE, data);
	}

	@Override
	protected String getSemanticFormat() {
		return SemanticFormats.TURTLE;
	}
}
