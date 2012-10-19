package otsopack.commons.network.communication.representations;

import java.io.IOException;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

import otsopack.commons.data.Graph;

public class TurtleRepresentation extends SemanticFormatRepresentation {

	public TurtleRepresentation(Graph graph) {
		super(MediaType.APPLICATION_RDF_TURTLE, graph);
	}

	public TurtleRepresentation(Representation representation) throws IOException {
		super(MediaType.APPLICATION_RDF_TURTLE, representation);
	}

	public TurtleRepresentation(String data) {
		super(MediaType.APPLICATION_RDF_TURTLE, data);
	}
}
