package otsopack.droid.dataaccess.simplestore;

import java.util.HashSet;
import java.util.Set;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.exceptions.PersistenceException;
import otsopack.full.java.dataaccess.simplestore.ISimpleStore;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class SQLiteAndroidStore implements ISimpleStore {
	
	private Context dbContext;
	private SQLiteDatabase db;
	private SQLiteStatement getSpecificGraph;
	private SQLiteStatement getGraphsURIs; 
	private SQLiteStatement insertGraph; 
	private SQLiteStatement deleteGraph; 
	
	public SQLiteAndroidStore(Context dbContext) {
		this.dbContext = dbContext;
	}
	
	@Override
	public void startup() throws PersistenceException {
		OtsobaseOpenHelper helper = new OtsobaseOpenHelper(dbContext);
		this.db = helper.getWritableDatabase();
	}

	@Override
	public void shutdown() throws PersistenceException {
		try {
			this.getSpecificGraph.close();
			this.getGraphsURIs.close();
			this.insertGraph.close();
			this.deleteGraph.close();

			this.db.close();
		} catch (SQLException e) {
			throw new PersistenceException("Connection with sqlite database could not be closed.");
		}
	}

	@Override
	public void clear() throws PersistenceException {
		try {
			this.db.execSQL("DELETE FROM " + OtsobaseOpenHelper.TABLE_NAME);
		} catch (SQLException e) {
			throw new PersistenceException("Database could not be cleared.");
		}
	}

	@Override
	public Set<String> getGraphsURIs(String spaceuri) throws PersistenceException {
		try {
			final Cursor c = this.db.query(OtsobaseOpenHelper.TABLE_NAME, new String[] {"graphuri"},"spaceuri=?s AND graphuri=?s", new String[] {spaceuri}, null, null, null);
			final Set<String> ret = new HashSet<String>();
			while (!c.isAfterLast()) {
				ret.add(c.getString(1));
				c.moveToNext();
			}
			return ret;
		} catch (SQLException e) {
			throw new PersistenceException("Graphs selection statement could not be executed.");
		}
	}

	@Override
	public void insertGraph(String spaceuri, String graphuri, Graph graph) throws PersistenceException {
		try {
			final ContentValues cv = new ContentValues();
			cv.put("graphuri", graphuri);
			cv.put("spaceuri", graphuri);
			cv.put("format", graph.getFormat().getName());
			cv.put("data", graph.getData().getBytes());
			long updated = this.db.insert(OtsobaseOpenHelper.TABLE_NAME,null,cv);
			if (updated==-1) throw new PersistenceException("Graphs could not be stored.");
		} catch (SQLException e) {
			throw new PersistenceException("Graphs selection statement could not be executed.");
		}
	}

	@Override
	public void deleteGraph(String spaceuri, String graphuri) throws PersistenceException {
		try {
			this.db.delete(OtsobaseOpenHelper.TABLE_NAME,"spaceuri=?s AND graphuri=?s", new String[] {spaceuri,graphuri});
		} catch (SQLException e) {
			throw new PersistenceException("Graph removal statement could not be executed.");
		}
	}

	@Override
	public Graph getGraph(String spaceuri, String graphuri) throws PersistenceException {
		try {
			final Cursor c = this.db.query(OtsobaseOpenHelper.TABLE_NAME, new String[] {"format","data"},"spaceuri=?s AND graphuri=?s", new String[] {spaceuri, graphuri}, null, null, null);
			if (!c.isAfterLast()) {
				return new Graph(new String(c.getBlob(2)), SemanticFormat.getSemanticFormat(c.getString(1)));
			}
		} catch (SQLException e) {
			throw new PersistenceException("Graphs selection statement could not be executed.");
		}
		throw new PersistenceException("Graphs not found in the database.");
	}
}