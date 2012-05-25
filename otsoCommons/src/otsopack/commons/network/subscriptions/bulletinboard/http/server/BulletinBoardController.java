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

import otsopack.commons.network.IHTTPInformation;
import otsopack.commons.network.coordination.IRegistry;
import otsopack.commons.network.subscriptions.bulletinboard.IBulletinBoardRemoteFacade;
import otsopack.commons.network.subscriptions.bulletinboard.LocalBulletinBoard;

public class BulletinBoardController implements IBulletinBoardController {
	
	final IBulletinBoardRemoteFacade local;
	
	public BulletinBoardController(IBulletinBoardRemoteFacade localBulletinBoard) {
		this.local = localBulletinBoard;
	}
	
	public BulletinBoardController(String spaceURI, IRegistry registry, IHTTPInformation infoHolder) {
		this(new LocalBulletinBoard(spaceURI, registry, infoHolder));
	}
	
	@Override
	public IBulletinBoardRemoteFacade getBulletinBoard() {
		return this.local;
	}
	
	@Override
	public void start() {
		this.local.start();
	}
	
	@Override
	public void stop() {
		this.local.stop();
	}
}