package otsopack.commons.network.communication.representations;

import java.io.IOException;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

import otsopack.commons.data.Graph;

public class RdfXmlRepresentation extends SemanticFormatRepresentation {
	
	public RdfXmlRepresentation(Graph graph) {
		super(MediaType.APPLICATION_RDF_XML, graph);
	}

	public RdfXmlRepresentation(Representation representation)
			throws IOException {
		super(MediaType.APPLICATION_RDF_XML, representation);
	}

	public RdfXmlRepresentation(String data) {
		super(MediaType.APPLICATION_RDF_XML, data);
	}
}
