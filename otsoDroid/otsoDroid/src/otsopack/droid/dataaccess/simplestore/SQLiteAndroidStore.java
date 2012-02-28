/*
 * Copyright (C) 2008 onwards University of Deusto
 * 
 * All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */
package otsopack.droid.dataaccess.simplestore;

import java.util.HashSet;
import java.util.Set;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.exceptions.PersistenceException;
import otsopack.commons.dataaccess.simplestore.DatabaseTuple;
import otsopack.commons.dataaccess.simplestore.ISimpleStore;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteAndroidStore implements ISimpleStore {
	
	private Context dbContext;
	private SQLiteDatabase db;
	
	public SQLiteAndroidStore(Context dbContext) {
		this.dbContext = dbContext;
		this.db = null;
	}
	
	public SQLiteAndroidStore(SQLiteDatabase db) {
		this.db = db;
		this.dbContext = null;
	}
	
	@Override
	public void startup() throws PersistenceException {
		if (this.db==null) {
			final OtsobaseOpenHelper helper = new OtsobaseOpenHelper(dbContext);
			this.db = helper.getWritableDatabase();
		}
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
		Cursor c = null;
		try {
			c = this.db.query(OtsobaseOpenHelper.TABLE_NAME, new String[] {"graphuri"},"spaceuri=?", new String[] {spaceuri}, null, null, null);
			final Set<String> ret = new HashSet<String>();
			while (c.moveToNext()) {
				ret.add(c.getString(0));
			}
			return ret;
		} catch (SQLException e) {
			throw new PersistenceException("Graphs selection statement could not be executed.");
		}finally{
			if (c != null){
				c.close();
			}
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
	


	/* (non-Javadoc)
	 * @see otsopack.full.java.dataaccess.simplestore.ISimpleStore#getGraphs()
	 */
	@Override
	public Set<DatabaseTuple> getGraphs() throws PersistenceException {
		final Set<DatabaseTuple> tuples = new HashSet<DatabaseTuple>();
		Cursor c = null;
		try {
			c = this.db.query(OtsobaseOpenHelper.TABLE_NAME, new String[] {"spaceuri","graphuri","data","format"}, null, null, null, null, null);
			while (c.moveToNext()) {
				tuples.add(new DatabaseTuple(
						c.getString(0), c.getString(1),
						new Graph(new String(c.getBlob(2)), SemanticFormat.getSemanticFormat(c.getString(3)))
				));
			}
			return tuples;
		} catch (SQLException e) {
			throw new PersistenceException("Graphs selection statement could not be executed.");
		} finally {
			if (c != null){
				c.close();
			}
		}
	}

	/* (non-Javadoc)
	 * @see otsopack.full.java.dataaccess.simplestore.ISimpleStore#getGraphsFromSpace(java.lang.String)
	 */
	@Override
	public Set<DatabaseTuple> getGraphsFromSpace(String spaceuri)
			throws PersistenceException {
		final Set<DatabaseTuple> tuples = new HashSet<DatabaseTuple>();
		Cursor c = null;
		try {
			c = this.db.query(OtsobaseOpenHelper.TABLE_NAME, new String[] {"graphuri","data","format"},"spaceuri=?", new String[] {spaceuri}, null, null, null);
			while (c.moveToNext()) {
				tuples.add(new DatabaseTuple(
						spaceuri, c.getString(0),
						new Graph(new String(c.getBlob(1)), SemanticFormat.getSemanticFormat(c.getString(2)))
				));
			}
			return tuples;
		} catch (SQLException e) {
			throw new PersistenceException("Graphs selection statement could not be executed.");
		} finally {
			if (c != null){
				c.close();
			}
		}
	}
}