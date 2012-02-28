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
package otsopack.droid.network.communication.demand.remote;

import otsopack.commons.data.Graph;
import otsopack.commons.data.Template;
import otsopack.commons.exceptions.UnsupportedTemplateException;

public interface IRemoteDemandManager extends IRemoteDemandIOManager {

	public abstract void startup();
	public abstract void shutdown();
	
	public abstract boolean hasAnyPeerResponsabilityOverThisKnowledge(
			Graph triples) throws UnsupportedTemplateException;

	public abstract byte[] exportRecords();
	
	/*
	 * (non-Javadoc)
	 * @see es.deusto.tecnologico.tscME.network.communication.demand.remote.IRemoteDemandIOManager#demandReceived(es.deusto.tecnologico.tscME.data.Template, long)
	 */
	public abstract void demandReceived(Template template, long leaseTime);
	
	/* (non-Javadoc)
	 * @see es.deusto.tecnologico.tscME.network.communication.demand.remote.IRemoteDemandInputManager#importRecords(byte[])
	 */
	public abstract void importRecords(byte[] bytes);


}