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

import java.util.Vector;

public class Statistics {
	static Statistics singleton;
	Vector/*<Measure>*/ measures;
	int numberOfResponses;
	
	private Statistics() {
		numberOfResponses = 1;
		measures = new Vector();
	}
	
	public static Statistics getSingleton() {
		if(singleton==null) singleton=new Statistics();
		return singleton;
	}

	public static void addMeasure(String primitive, long time, long triples) {
		Measure measure = new Measure();
		measure.setPrimitive(primitive);
		measure.setTime(time);
		measure.setTriples(triples);
		getSingleton().measures.addElement(measure);
	}
	
	public static Vector getMeasure() {
		return getSingleton().measures;
	}

	public static void setNumberOfResponses(int numberOfResponses) {
		getSingleton().numberOfResponses = numberOfResponses;
	}
	
	public static int getNumberOfResponses() {
		return getSingleton().numberOfResponses;
	}
}