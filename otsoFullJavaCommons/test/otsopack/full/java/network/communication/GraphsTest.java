package otsopack.full.java.network.communication;

import org.junit.Test;
import static org.junit.Assert.*;
import org.restlet.resource.ClientResource;

import otsopack.full.java.network.communication.util.JSONDecoder;

public class GraphsTest extends AbstractRestServerTesting {
	@Test
	public void testCreatePrefix() throws Exception {
		final ClientResource cr = new ClientResource("http://localhost:8182/graphs");
		final IGraphsResource prefrsc = cr.wrap(IGraphsResource.class);
		
		final String prefixes = prefrsc.toJson();
		
		final String [] results = JSONDecoder.decode(prefixes, String[].class);
		
		assertEquals(2, results.length);
		assertEquals("/graphs", results[0]);
		assertEquals("/graphs/wildcards", results[1]);
	}

}
