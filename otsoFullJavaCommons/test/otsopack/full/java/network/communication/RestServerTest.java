package otsopack.full.java.network.communication;

import static org.junit.Assert.assertEquals;

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
		ClientResource cr = new ClientResource("http://localhost:8182/prefixes");
		IPrefixesResource prefrsc = cr.wrap(IPrefixesResource.class);
		prefrsc.create( new Prefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#") );
		prefrsc.create( new Prefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#") );
		prefrsc.create( new Prefix("xsd", "http://www.w3.org/2001/XMLSchema#") );
		prefrsc.create( new Prefix("owl", "http://www.w3.org/2002/07/owl#") );
		
		Prefix[] prefixes = prefrsc.retrieve();
		assertEquals(prefixes.length, 4);
	}
	
	@Test
	public void testGetPrefix() throws Exception {
		ClientResource cr = new ClientResource("http://localhost:8182/prefixes");
		IPrefixesResource prefixesrsc = cr.wrap(IPrefixesResource.class);
		prefixesrsc.create( new Prefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#") );
		
		cr = new ClientResource("http://localhost:8182/prefixes/rdf");
		IPrefixResource prefixrsc = cr.wrap(IPrefixResource.class);
		
		// Test json retrieval
		Prefix pref = prefixrsc.retrieve();
		assertEquals(pref.getName(),"rdf");
		assertEquals(pref.getUri(),"http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		
		// Test java object serialization retrieval
		assertEquals(prefixrsc.retrieveJson(),"{\"name\":\"rdf\",\"uri\":\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"}");
		
		
		// Test non existing prefix
		cr = new ClientResource("http://localhost:8182/prefixes/doesnotexist");
		try {
			prefixrsc.retrieve();
		} catch(ResourceException re) {
			assertEquals(re.getStatus(),Status.CLIENT_ERROR_NOT_FOUND);
		}
	}
}
