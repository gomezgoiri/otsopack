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

package otsopack.commons.data;

public interface ITemplate {
	String toString();
	boolean equals(Object object);
	/**
	 * Do the template tpl match this template?
	 * 
	 * Example:
	 * <ul>
	 *		<li>"<http://s1> ?p ?o ." matches "?s ?p ?o ."</li>
	 *		<li>"?s ?p ?o ." doesn't match "<http://s1> ?p ?o ."</li>
	 *		<li>"?s <http://p2> ?o ." doesn't match "<http://s2> <http://p2> ?o ."</li>
	 *		<li>"?s ?p <http://o3> ." doesn't match "?s <http://p3> ?o ."</li>
	 * </ul>
	 * 
	 * Needed for notifications. If you are subscribed to template t1
	 * and notify tpl2, will you receive the notification?<br>
	 * 		tpl.match(tpl2);
	 * 
	 * @param tpl
	 * @return
	 */
	boolean match(ITemplate tpl);
}
