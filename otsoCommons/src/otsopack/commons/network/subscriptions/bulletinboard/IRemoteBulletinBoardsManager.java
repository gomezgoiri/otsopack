/*
 * Copyright (C) 2012 onwards University of Deusto
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
package otsopack.commons.network.subscriptions.bulletinboard;

import otsopack.commons.ILayer;
import otsopack.commons.exceptions.SubscriptionException;

public interface IRemoteBulletinBoardsManager extends ILayer {
	public void createRemoteBulletinBoard(String spaceURI, int port) throws SubscriptionException;
}
