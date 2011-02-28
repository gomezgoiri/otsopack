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

package otsopack.commons.stats;

public class Measure {
	String primitive;
	long time;
	long triples;
	
	public String getPrimitive() {
		return primitive;
	}
	public void setPrimitive(String primitive) {
		this.primitive = primitive;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public long getTriples() {
		return triples;
	}
	public void setTriples(long triples) {
		this.triples = triples;
	}
}
