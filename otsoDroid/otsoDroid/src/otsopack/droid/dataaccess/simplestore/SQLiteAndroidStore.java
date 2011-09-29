package otsopack.droid.dataaccess.simplestore;

import java.util.Set;

import otsopack.commons.data.Graph;
import otsopack.commons.exceptions.PersistenceException;
import otsopack.full.java.dataaccess.simplestore.ISimpleStore;

public class SQLiteAndroidStore implements ISimpleStore {

	@Override
	public void startup() throws PersistenceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() throws PersistenceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() throws PersistenceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String> getGraphsURIs(String spaceuri)
			throws PersistenceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertGraph(String spaceuri, String graphuri, Graph graph)
			throws PersistenceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteGraph(String spaceuri, String graphuri)
			throws PersistenceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Graph getGraph(String spaceuri, String graphuri)
			throws PersistenceException {
		// TODO Auto-generated method stub
		return null;
	}

}
