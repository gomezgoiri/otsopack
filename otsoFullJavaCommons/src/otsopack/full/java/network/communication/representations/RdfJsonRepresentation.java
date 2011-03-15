package otsopack.full.java.network.communication.representations;

import java.io.IOException;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

import otsopack.commons.data.Graph;

public class RdfJsonRepresentation extends SemanticFormatRepresentation {
	
	public RdfJsonRepresentation(Graph graph) {
		super(MediaType.APPLICATION_JSON, graph);
	}

	public RdfJsonRepresentation(Representation representation)
			throws IOException {
		super(MediaType.APPLICATION_JSON, representation);
	}

	public RdfJsonRepresentation(String data) {
		super(MediaType.APPLICATION_JSON, data);
	}
}
