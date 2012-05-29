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
package otsopack.commons.network.subscriptions.bulletinboard.http.server;

import otsopack.commons.network.subscriptions.bulletinboard.IBulletinBoardOuterFacade;

public class BulletinBoardController implements IBulletinBoardController {
	
	final IBulletinBoardOuterFacade local;
	
	public BulletinBoardController(IBulletinBoardOuterFacade localBulletinBoard) {
		this.local = localBulletinBoard;
	}
	
	@Override
	public IBulletinBoardOuterFacade getBulletinBoard() {
		return this.local;
	}
}