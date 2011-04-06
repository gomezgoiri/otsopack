package otsopack.idp;

import java.util.HashMap;
import java.util.Map;

import otsopack.idp.authn.ICredentialsChecker;
import otsopack.idp.authn.memory.MemoryCredentialsChecker;

public class RestServerMain {
	public static void main(String [] args) throws Exception {
		final Map<String, String> credentials = new HashMap<String, String>();
		credentials.put("porduna",  "pablo");
		credentials.put("aigomez",  "aitor");
		credentials.put("ecastill", "eduardo");
		credentials.put("xlaiseca", "xabier");
		
		final ICredentialsChecker credentialsChecker = new MemoryCredentialsChecker(credentials);
		final IController controller = new Controller(credentialsChecker);
		final RestServer server = new RestServer(controller);
		server.startup();
	}
}
