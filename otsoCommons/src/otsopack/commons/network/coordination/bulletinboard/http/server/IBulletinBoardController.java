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
package otsopack.commons.network.coordination.bulletinboard.http.server;

import otsopack.commons.network.coordination.IBulletinBoard;
import otsopack.restlet.commons.ICommonsController;

public interface IBulletinBoardController extends ICommonsController {
	public IBulletinBoard getBulletinBoard();
}
