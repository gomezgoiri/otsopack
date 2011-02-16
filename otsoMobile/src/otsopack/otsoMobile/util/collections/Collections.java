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

package otsopack.otsoMobile.util.collections;

public class Collections {
	public static java.util.Vector subList(java.util.Vector v, int first, int last){
		final java.util.Vector newVector = new java.util.Vector(last - first);
		for(int i = first; i < last; ++i){
			final Object element = v.elementAt(i);
			newVector.addElement(element);
		}
		return newVector;
	}

	public static void sort(java.util.Vector records) {
		int max = records.size();
	    for (int i = 1; i < max; i++) {
	      Object value = records.elementAt(i);
	      int j = i - 1;
	      Object valueB;
	      while (j >= 0 && ((Comparable)(valueB = records.elementAt(j))).compareTo(value) > 0) {
	    	  records.setElementAt(valueB, j + 1);
	        j--;
	      }
	      records.setElementAt(value, j + 1);
	    }
	}

	public static Collection vector2collection(java.util.Vector vector){
		return new Vector(vector);
	}
}
