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

public class SQLiteAndroidStore implements ISimpleStore {
	
	private Context dbContext;
	private SQLiteDatabase db = null;
	
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
			this.db.close();
			this.db = null;
		} catch (SQLException e) {
			throw new PersistenceException("Connection with sqlite database could not be closed.");
		}
	}

	@Override
	public void clear() throws PersistenceException {
		if (this.db==null) throw new PersistenceException("The database should be opened before any other operation.");
		try {
			this.db.execSQL("DELETE FROM " + OtsobaseOpenHelper.TABLE_NAME);
		} catch (SQLException e) {
			throw new PersistenceException("Database could not be cleared.");
		}
	}

	@Override
	public Set<String> getGraphsURIs(String spaceuri) throws PersistenceException {
		if (this.db==null) throw new PersistenceException("The database should be opened before any other operation.");
		try {
			final Cursor c = this.db.query(OtsobaseOpenHelper.TABLE_NAME, new String[] {"graphuri"},"spaceuri=?", new String[] {spaceuri}, null, null, null);
			final Set<String> ret = new HashSet<String>();
			while (c.moveToNext()) {
				ret.add(c.getString(0));
			}
			return ret;
		} catch (SQLException e) {
			throw new PersistenceException("Graphs selection statement could not be executed.");
		}
	}

	@Override
	public void insertGraph(String spaceuri, String graphuri, Graph graph) throws PersistenceException {
		if (this.db==null) throw new PersistenceException("The database should be opened before any other operation.");
		try {
			final ContentValues cv = new ContentValues();
			cv.put("graphuri", graphuri);
			cv.put("spaceuri", spaceuri);
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
		if (this.db==null) throw new PersistenceException("The database should be opened before any other operation.");
		try {
			this.db.delete(OtsobaseOpenHelper.TABLE_NAME,"spaceuri=? AND graphuri=?", new String[] {spaceuri,graphuri});
		} catch (SQLException e) {
			throw new PersistenceException("Graph removal statement could not be executed.");
		}
	}

	@Override
	public Graph getGraph(String spaceuri, String graphuri) throws PersistenceException {
		if (this.db==null) throw new PersistenceException("The database should be opened before any other operation.");
		try {
			final Cursor c = this.db.query(OtsobaseOpenHelper.TABLE_NAME, new String[] {"format","data"},"spaceuri=?s AND graphuri=?s", new String[] {spaceuri, graphuri}, null, null, null);
			if (c.getCount()>0) {
				return new Graph(new String(c.getBlob(1)), SemanticFormat.getSemanticFormat(c.getString(0)));
			}
		} catch (SQLException e) {
			throw new PersistenceException("Graphs selection statement could not be executed.");
		}
		throw new PersistenceException("Graphs not found in the database.");
	}
}