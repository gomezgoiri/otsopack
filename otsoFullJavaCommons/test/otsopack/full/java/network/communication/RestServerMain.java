package otsopack.full.java.network.communication;

/**
 * 
 * This class is not a JUnit test, but only to manually test the server.
 * 
 */
public class RestServerMain {
	public static void main(String [] args) throws Exception {
		new RestServer().startup();
	}
}
