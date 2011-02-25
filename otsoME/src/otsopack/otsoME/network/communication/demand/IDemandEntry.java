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
package otsopack.otsoME.network.communication.demand;

import otsopack.otsoCommons.data.ITemplate;
import otsopack.otsoCommons.util.collections.Comparable;

public interface IDemandEntry extends Comparable {
	/**
	 * @return
	 * 		Demanded template (which basically means somebody
	 * 	has responsibility over the graphs which match this template).
	 */
	public abstract ITemplate getTemplate();
	
	/**
	 * @return
	 * 		The time until this demand has validity.
	 */
	public abstract long getExpiryTime();
	
	/**
	 * The demands have a validity time.
	 * @return
	 * 		Has this demand already expired.
	 */
	public abstract boolean hasExpired();
}