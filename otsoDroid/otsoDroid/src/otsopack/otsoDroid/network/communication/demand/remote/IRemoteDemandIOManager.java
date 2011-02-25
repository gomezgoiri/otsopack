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
package otsopack.otsoDroid.network.communication.demand.remote;

import otsopack.otsoCommons.data.ITemplate;

public interface IRemoteDemandIOManager {
	public abstract void demandReceived(ITemplate template, long leaseTime);
	public abstract void importRecords(byte[] bytes);
	/**
	 * @return
	 * 		Byte value of the demand record or null if it is empty.
	 */
	public abstract byte[] exportRecords();
	public abstract boolean hasBeenInitialized();
}