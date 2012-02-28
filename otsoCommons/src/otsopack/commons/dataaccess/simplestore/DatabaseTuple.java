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
package otsopack.commons.dataaccess.simplestore;

import otsopack.commons.data.Graph;

public class DatabaseTuple {
	final private String spaceuri;
	final private String graphuri;
	final private Graph graph;
	final private int hash;
	
	public DatabaseTuple(String space, String graphu, Graph graph) {
		this.spaceuri = space;
		this.graphuri = graphu;
		this.graph = graph;
		this.hash = (this.spaceuri+this.graphuri).hashCode();
	}

	public String getSpaceuri() {
		return this.spaceuri;
	}

	public String getGraphuri() {
		return this.graphuri;
	}

	public Graph getGraph() {
		return this.graph;
	}

	@Override
	public int hashCode() {
		return this.hash;
	}
}