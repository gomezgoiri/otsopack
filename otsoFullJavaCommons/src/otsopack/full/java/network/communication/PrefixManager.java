package otsopack.full.java.network.communication;

import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

public class PrefixManager extends ServerResource implements PrefixResource {
    /* (non-Javadoc)
	 * @see otsopack.full.java.network.communication.PrefixResource#retrieve()
	 */
    @Override
	@Get
    public String retrieve() {
    	return "pr0n";
    }

    /* (non-Javadoc)
	 * @see otsopack.full.java.network.communication.PrefixResource#store(otsopack.full.java.network.communication.Prefix)
	 */
    @Override
	@Put
    public void store(Prefix prefix) {
    	
    }

    /*@Delete
    public void remove()*/
}
