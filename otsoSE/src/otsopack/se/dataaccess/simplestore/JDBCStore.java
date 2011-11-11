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
	
	public JDBCStore() throws PersistenceException {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			throw new PersistenceException("sqlite driver could not be found: " + e.getMessage());
		}
	}
	
	// maybe in the future a connection pool could be used
	protected Connection openConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:sqlite:dbOtsoPack");
	}
	
	// maybe in the future a connection pool could be used
	protected void closeConnection(Connection conn) throws SQLException {
		conn.close();
	}
	
	/* (non-Javadoc)
	 * @see otsopack.full.java.dataaccess.sqlite.ISimplePersistentStrategy#startup()
	 */
	@Override
	public void startup() throws PersistenceException {
		try {
			// TODO become this database location configurable?
			final Connection conn = openConnection();
			if( !doesTableExists(conn) ) {
				final Statement stmt = conn.createStatement();
				createTable(stmt);
				stmt.close();
			}
			closeConnection(conn);
		} catch (SQLException e) {
			throw new PersistenceException("Connection with sqlite database could not be stablished: " + e.getMessage());
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
	
	protected boolean doesTableExists(Connection conn) throws PersistenceException {
		try {
			final DatabaseMetaData meta = conn.getMetaData();
	        final ResultSet rs = meta.getTables(null, null, this.TABLE_NAME, null);
	        return rs.next();
		} catch(SQLException e) {
			throw new PersistenceException("The existence of the table could not be checked: " + e.getMessage());
		}
	}
	
	protected void createTable(Statement stmt) throws PersistenceException {
	    final String tableCreationSQL = "CREATE TABLE " + this.TABLE_NAME + " (" +
	      					"	graphuri VARCHAR(1000) UNIQUE," +
	      					"	spaceuri VARCHAR(1000)," +
	      					"	format VARCHAR(100)," +
	      					"	data BLOB" +
	      					");";
	    try {
			stmt.executeUpdate(tableCreationSQL);
		} catch (SQLException e) {
			throw new PersistenceException("Main table could not be created: " + e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see otsopack.full.java.dataaccess.sqlite.ISimplePersistentStrategy#getGraphsURIs(java.lang.String)
	 */
	@Override
	public Set<String> getGraphsURIs(String spaceuri) throws PersistenceException {
		Connection conn = null;
		try {
			final Set<String> ret = new HashSet<String>();
			conn = openConnection();
			final PreparedStatement getGraphsURIs = conn.prepareStatement(
														"SELECT graphuri FROM " + this.TABLE_NAME + " WHERE " +
														"spaceuri=?" );
			getGraphsURIs.setString(1,spaceuri);
			// automatically closed in the next creation
			final ResultSet rs = getGraphsURIs.executeQuery();
			while (rs.next()) {
				ret.add( rs.getString(1) );
			}
			return ret;
		} catch (SQLException e) {
			throw new PersistenceException("Graphs selection statement could not be executed:" + e.getMessage());
		} finally {
			if (conn!=null) {
				try {
					closeConnection(conn);
				} catch (SQLException e) {
					throw new PersistenceException("Database connection could not be closed.");
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see otsopack.full.java.dataaccess.sqlite.ISimplePersistentStrategy#insertGraph(java.lang.String, java.lang.String, otsopack.commons.data.Graph)
	 */
	@Override
	public void insertGraph(String spaceuri, String graphuri, Graph graph) throws PersistenceException {
		Connection conn = null;
		try {
			conn = openConnection();
			
			final PreparedStatement insertGraph = conn.prepareStatement(
									"INSERT INTO " + this.TABLE_NAME + " VALUES(?,?,?,?)" );
			insertGraph.setString(1,graphuri);
			insertGraph.setString(2,spaceuri);
			insertGraph.setString(3,graph.getFormat().getName());
			insertGraph.setBytes(4,graph.getData().getBytes());
			
			// automatically closed in the next creation
			final int updated = insertGraph.executeUpdate();
			//TODO if it takes too long to do a simple write, we can persist it later using...
			//this.insertGraph.addBatch();
			if (updated==0) throw new PersistenceException("Graphs could not be stored: zero updates found");
		} catch (SQLException e) {
			throw new PersistenceException("Graphs selection statement could not be executed.\n" + e.getMessage());
		} finally {
			if (conn!=null) {
				try {
					closeConnection(conn);
				} catch (SQLException e) {
					throw new PersistenceException("Database connection could not be closed.");
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see otsopack.full.java.dataaccess.sqlite.ISimplePersistentStrategy#deleteGraph(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteGraph(String spaceuri, String graphuri) throws PersistenceException {
		Connection conn = null;
		try {
			conn = openConnection();
			
			final PreparedStatement deleteGraph = conn.prepareStatement(
										"DELETE FROM " + this.TABLE_NAME + " WHERE " +
										"spaceuri=? AND graphuri=?" );
			
			deleteGraph.setString(1,spaceuri);
			deleteGraph.setString(2,graphuri);
			deleteGraph.executeUpdate();
			//TODO if it takes too long to do a simple take, we can persist it later using...
			//this.deleteGraph.addBatch();
		} catch (SQLException e) {
			throw new PersistenceException("Graph removal statement could not be executed: " + e.getMessage());
		} finally {
			if (conn!=null) {
				try {
					closeConnection(conn);
				} catch (SQLException e) {
					throw new PersistenceException("Database connection could not be closed.");
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see otsopack.full.java.dataaccess.simplestore.ISimpleStore#getGraphs()
	 */
	@Override
	public Set<DatabaseTuple> getGraphs() throws PersistenceException {
		final Set<DatabaseTuple> tuples = new HashSet<DatabaseTuple>();
		
		Connection conn = null;
		try {
			conn = openConnection();
			
			final PreparedStatement getAllGraphs = conn.prepareStatement(
					"SELECT spaceuri, graphuri, data, format FROM " + this.TABLE_NAME );
			
			final ResultSet rs = getAllGraphs.executeQuery();
			while (rs.next()) {
				tuples.add(new DatabaseTuple(
						rs.getString(1), rs.getString(2),
						new Graph(new String(rs.getBytes(3)), SemanticFormat.getSemanticFormat(rs.getString(4)))
				));
			}
			return tuples;
		} catch (SQLException e) {
			throw new PersistenceException("Graphs selection statement could not be executed: " + e.getMessage());
		} finally {
			if (conn!=null) {
				try {
					closeConnection(conn);
				} catch (SQLException e) {
					throw new PersistenceException("Database connection could not be closed.");
				}
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
		
		Connection conn = null;
		try {
			conn = openConnection();
			
			final PreparedStatement getGraphsFromSpace = conn.prepareStatement(
					"SELECT spaceuri, graphuri, data, format FROM " +
					this.TABLE_NAME + " WHERE spaceuri=?" );
			
			getGraphsFromSpace.setString(1,spaceuri);
			final ResultSet rs = getGraphsFromSpace.executeQuery();
			while (rs.next()) {
				tuples.add(new DatabaseTuple(
						rs.getString(1), rs.getString(2),
						new Graph(new String(rs.getBytes(3)), SemanticFormat.getSemanticFormat(rs.getString(4)))
				));
			}
			return tuples;
		} catch (SQLException e) {
			throw new PersistenceException("Graphs selection statement could not be executed: " + e.getMessage());
		} finally {
			if (conn!=null) {
				try {
					closeConnection(conn);
				} catch (SQLException e) {
					throw new PersistenceException("Database connection could not be closed.");
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see otsopack.full.java.dataaccess.sqlite.ISimplePersistentStrategy#clear()
	 */
	@Override
	public void clear() throws PersistenceException {
		Connection conn = null;
		try {
			conn = openConnection();
			
			final Statement stmt = conn.createStatement();
			// not prepared because it won't be usual
			stmt.executeUpdate("DELETE FROM " + this.TABLE_NAME);
			stmt.close();
		} catch (SQLException e) {
			throw new PersistenceException("Database could not be cleared: " + e.getMessage());
		} finally {
			if (conn!=null) {
				try {
					closeConnection(conn);
				} catch (SQLException e) {
					throw new PersistenceException("Database connection could not be closed.");
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see otsopack.full.java.dataaccess.sqlite.ISimplePersistentStrategy#shutdown()
	 */
	@Override
	public void shutdown() throws PersistenceException {

	}
}
