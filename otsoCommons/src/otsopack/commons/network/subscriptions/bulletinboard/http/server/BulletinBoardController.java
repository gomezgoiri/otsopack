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
import otsopack.commons.network.subscriptions.bulletinboard.IBulletinBoard;
import otsopack.commons.network.subscriptions.bulletinboard.LocalBulletinBoard;

public class BulletinBoardController implements IBulletinBoardController {
	
	final LocalBulletinBoard local;
	
	public BulletinBoardController(LocalBulletinBoard localBulletinBoard) {
		this.local = localBulletinBoard;
	}
	
	public BulletinBoardController(String spaceURI, IRegistry registry, IHTTPInformation infoHolder) {
		this(new LocalBulletinBoard(spaceURI, registry, infoHolder));
	}
	
	@Override
	public IBulletinBoard getBulletinBoard() {
		return this.local;
	}
	
	public void start() {
		this.local.start();
	}
	
	public void stop() {
		this.local.stop();
	}
}