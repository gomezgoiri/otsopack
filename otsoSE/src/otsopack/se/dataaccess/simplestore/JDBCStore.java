/*
 * Copyright (C) 2008-2011 University of Deusto
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
package otsopack.se.dataaccess.simplestore;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.exceptions.PersistenceException;
import otsopack.full.java.dataaccess.simplestore.DatabaseTuple;
import otsopack.full.java.dataaccess.simplestore.ISimpleStore;

public class JDBCStore implements ISimpleStore {
	// everything in the same table (we just use sqlite to persist info...)
	private final String TABLE_NAME = "Graphs";
	
	private Connection conn;
	private PreparedStatement getGraphsFromSpace;
	private PreparedStatement getGraphsURIs;
	private PreparedStatement insertGraph;
	private PreparedStatement deleteGraph;
	private PreparedStatement getAllGraphs;

	public JDBCStore() throws PersistenceException {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			throw new PersistenceException("sqlite driver could not be found.");
		}
	}
	
	/* (non-Javadoc)
	 * @see otsopack.full.java.dataaccess.sqlite.ISimplePersistentStrategy#startup()
	 */
	@Override
	public void startup() throws PersistenceException {
		try {
			// TODO become this database location configurable?
			this.conn = DriverManager.getConnection("jdbc:sqlite:dbOtsoPack");
			if( !doesTableExists() ) {
				final Statement stmt = this.conn.createStatement();
				createTable(stmt);
				stmt.close();
			}
			createPreparedStatements();
		} catch (SQLException e) {
			throw new PersistenceException("Connection with sqlite database could not be stablished.");
		}
	}
	
	/*protected boolean doesDatabaseExists() throws PersistenceException {
		try {
			final DatabaseMetaData meta = this.conn.getMetaData();
	        final ResultSet rs = meta.getTables(null, null, this.DATABASE_NAME, null);
	        return rs.next();
		} catch(SQLException e) {
			throw new PersistenceException("The existence of the local database could not be checked.");
		}
	}
	
	protected void createDatabase(Statement stmt) throws PersistenceException {
		try {
			stmt.executeUpdate("CREATE DATABASE '" + this.DATABASE_NAME + "'");
		} catch (SQLException e) {
			throw new PersistenceException("Database could not be created.");
		}
	}*/
	
	protected boolean doesTableExists() throws PersistenceException {
		try {
			final DatabaseMetaData meta = this.conn.getMetaData();
	        final ResultSet rs = meta.getTables(null, null, this.TABLE_NAME, null);
	        return rs.next();
		} catch(SQLException e) {
			throw new PersistenceException("The existence of the table could not be checked.");
		}
	}
	
	protected void createTable(Statement stmt) throws PersistenceException {
	    final String tableCreationSQL = "CREATE TABLE " + this.TABLE_NAME + " (" +
	      					"	graphuri VARCHAR(1000)," +
	      					"	spaceuri VARCHAR(1000)," +
	      					"	format VARCHAR(100)," +
	      					"	data BLOB" +
	      					");";
	    try {
			stmt.executeUpdate(tableCreationSQL);
		} catch (SQLException e) {
			throw new PersistenceException("Main table could not be created.");
		}
	}
	
	protected void createPreparedStatements() throws PersistenceException {
		try {
			this.getAllGraphs = this.conn.prepareStatement(
					"SELECT spaceuri, graphuri, data, format FROM " + this.TABLE_NAME );
			this.getGraphsFromSpace = this.conn.prepareStatement(
					"SELECT spaceuri, graphuri, data, format FROM " +
					this.TABLE_NAME + " WHERE spaceuri=?" );
			this.getGraphsURIs = this.conn.prepareStatement(
					"SELECT graphuri FROM " + this.TABLE_NAME + " WHERE " +
					"spaceuri=?" );
			this.insertGraph = this.conn.prepareStatement(
					"INSERT INTO " + this.TABLE_NAME + " VALUES(?,?,?,?)" );
			this.deleteGraph = this.conn.prepareStatement(
					"DELETE FROM " + this.TABLE_NAME + " WHERE " +
					"spaceuri=? AND graphuri=?" );
		} catch (SQLException e) {
			throw new PersistenceException("Prepared statement could not be created.");
		}
	}
	
	/* (non-Javadoc)
	 * @see otsopack.full.java.dataaccess.sqlite.ISimplePersistentStrategy#getGraphsURIs(java.lang.String)
	 */
	@Override
	public Set<String> getGraphsURIs(String spaceuri) throws PersistenceException {
		try {
			final Set<String> ret = new HashSet<String>();
			this.getGraphsURIs.setString(1,spaceuri);
			// automatically closed in the next creation
			final ResultSet rs = this.getGraphsURIs.executeQuery();
			while (rs.next()) {
				ret.add( rs.getString(1) );
			}
			return ret;
		} catch (SQLException e) {
			throw new PersistenceException("Graphs selection statement could not be executed.");
		}
	}
	
	/* (non-Javadoc)
	 * @see otsopack.full.java.dataaccess.sqlite.ISimplePersistentStrategy#insertGraph(java.lang.String, java.lang.String, otsopack.commons.data.Graph)
	 */
	@Override
	public void insertGraph(String spaceuri, String graphuri, Graph graph) throws PersistenceException {
		try {
			this.insertGraph.setString(1,graphuri);
			this.insertGraph.setString(2,spaceuri);
			this.insertGraph.setString(3,graph.getFormat().getName());
			this.insertGraph.setBytes(4,graph.getData().getBytes());
			
			// automatically closed in the next creation
			final int updated = this.insertGraph.executeUpdate();
			//TODO if it takes too long to do a simple write, we can persist it later using...
			//this.insertGraph.addBatch();
			if (updated==0) throw new PersistenceException("Graphs could not be stored.");
		} catch (SQLException e) {
			throw new PersistenceException("Graphs selection statement could not be executed.");
		}
	}
	
	/* (non-Javadoc)
	 * @see otsopack.full.java.dataaccess.sqlite.ISimplePersistentStrategy#deleteGraph(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteGraph(String spaceuri, String graphuri) throws PersistenceException {
		try {
			this.deleteGraph.setString(1,spaceuri);
			this.deleteGraph.setString(2,graphuri);
			this.deleteGraph.executeUpdate();
			//TODO if it takes too long to do a simple take, we can persist it later using...
			//this.deleteGraph.addBatch();
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
		try {
			final ResultSet rs = this.getAllGraphs.executeQuery();
			while (rs.next()) {
				tuples.add(new DatabaseTuple(
						rs.getString(1), rs.getString(2),
						new Graph(new String(rs.getBytes(3)), SemanticFormat.getSemanticFormat(rs.getString(4)))
				));
			}
			return tuples;
		} catch (SQLException e) {
			throw new PersistenceException("Graphs selection statement could not be executed.");
		}
	}

	/* (non-Javadoc)
	 * @see otsopack.full.java.dataaccess.simplestore.ISimpleStore#getGraphsFromSpace(java.lang.String)
	 */
	@Override
	public Set<DatabaseTuple> getGraphsFromSpace(String spaceuri)
			throws PersistenceException {
		final Set<DatabaseTuple> tuples = new HashSet<DatabaseTuple>();
		try {
			this.getGraphsFromSpace.setString(1,spaceuri);
			final ResultSet rs = this.getGraphsFromSpace.executeQuery();
			while (rs.next()) {
				tuples.add(new DatabaseTuple(
						rs.getString(1), rs.getString(2),
						new Graph(new String(rs.getBytes(3)), SemanticFormat.getSemanticFormat(rs.getString(4)))
				));
			}
			return tuples;
		} catch (SQLException e) {
			throw new PersistenceException("Graphs selection statement could not be executed.");
		}
	}
	
	/* (non-Javadoc)
	 * @see otsopack.full.java.dataaccess.sqlite.ISimplePersistentStrategy#clear()
	 */
	@Override
	public void clear() throws PersistenceException {
		try {
			final Statement stmt = this.conn.createStatement();
			// not prepared because it won't be usual
			stmt.executeUpdate("DELETE FROM " + this.TABLE_NAME);
			stmt.close();
		} catch (SQLException e) {
			throw new PersistenceException("Database could not be cleared.");
		}
	}
	
	/* (non-Javadoc)
	 * @see otsopack.full.java.dataaccess.sqlite.ISimplePersistentStrategy#shutdown()
	 */
	@Override
	public void shutdown() throws PersistenceException {
		try {
			this.getGraphsFromSpace.close();
			this.getGraphsURIs.close();
			this.insertGraph.close();
			this.deleteGraph.close();

			this.conn.close();
		} catch (SQLException e) {
			throw new PersistenceException("Connection with sqlite database could not be closed.");
		}
	}
}
