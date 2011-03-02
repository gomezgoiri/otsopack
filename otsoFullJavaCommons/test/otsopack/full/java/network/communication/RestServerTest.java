package otsopack.full.java.network.communication;

import static org.junit.Assert.assertEquals;

import java.net.URLEncoder;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restlet.data.Status;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class RestServerTest {
	RestServer rs;
	
	@Before
	public void setUp() throws Exception {
		this.rs = new RestServer();
		this.rs.startup();
	}

	@After
	public void tearDown() throws Exception {
		this.rs.shutdown();
	}

	@Test
	public void testCreatePrefix() throws Exception {
		final ClientResource cr = new ClientResource("http://localhost:8182/prefixes");
		final IPrefixesResource prefrsc = cr.wrap(IPrefixesResource.class);
		
		HashMap<String, String> prefixes = prefrsc.retrieve();
		assertEquals(prefixes.size(), 0);
		
		PrefixesResource.create( "rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		PrefixesResource.create( "rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		PrefixesResource.create( "xsd", "http://www.w3.org/2001/XMLSchema#");
		PrefixesResource.create( "owl", "http://www.w3.org/2002/07/owl#");
		
		prefixes = prefrsc.retrieve();
		assertEquals(prefixes.size(), 4);
	}
	
	@Test
	public void testGetPrefixes() throws Exception {
		final ClientResource cr = new ClientResource("http://localhost:8182/prefixes");
		final IPrefixesResource prefrsc = cr.wrap(IPrefixesResource.class);
		PrefixesResource.create( "rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		PrefixesResource.create( "rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		PrefixesResource.create( "xsd", "http://www.w3.org/2001/XMLSchema#");
		PrefixesResource.create( "owl", "http://www.w3.org/2002/07/owl#");
		
		// Test java object serialization retrieval
		final HashMap<String, String> prefixes = prefrsc.retrieve();
		assertEquals(prefixes.size(), 4);
		checkIfItContainsPrefix(prefixes,"rdf","http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		checkIfItContainsPrefix(prefixes,"rdfs","http://www.w3.org/2000/01/rdf-schema#");
		checkIfItContainsPrefix(prefixes,"xsd","http://www.w3.org/2001/XMLSchema#");
		checkIfItContainsPrefix(prefixes,"owl","http://www.w3.org/2002/07/owl#");
		
		// Test json retrieval
		final String expectedJson = "{" +
				"\"rdfs\":\"http://www.w3.org/2000/01/rdf-schema#\"," +
				"\"owl\":\"http://www.w3.org/2002/07/owl#\"," +
				"\"xsd\":\"http://www.w3.org/2001/XMLSchema#\"," +
				"\"rdf\":\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"" +
			"}"; 
		assertEquals(expectedJson, prefrsc.retrieveJson());
	}
	
	private void checkIfItContainsPrefix(HashMap<String, String> prefixes, String name, String uri) {
		assertEquals(uri, prefixes.get(name));
	}

	@Test
	public void testGetPrefix() throws Exception {
		final String RDF_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		PrefixesResource.create("rdf", RDF_URI);
		
		ClientResource cr = new ClientResource("http://localhost:8182/prefixes/" + URLEncoder.encode(RDF_URI, "utf-8"));
		IPrefixResource prefixrsc = cr.wrap(IPrefixResource.class);
		
		// Test json retrieval
		assertEquals(prefixrsc.retrieveJson(),"\"rdf\"");
		
		
		// Test non existing prefix
		cr = new ClientResource("http://localhost:8182/prefixes/doesnotexist");
		try {
			prefixrsc.retrieveJson();
		} catch(ResourceException re) {
			assertEquals(re.getStatus(),Status.CLIENT_ERROR_NOT_FOUND);
		}
	}
}
