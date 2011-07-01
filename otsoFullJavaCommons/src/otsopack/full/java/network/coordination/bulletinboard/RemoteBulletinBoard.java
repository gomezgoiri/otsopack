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
package otsopack.full.java.network.coordination.bulletinboard;

import otsopack.full.java.network.coordination.IBulletinBoard;
import otsopack.full.java.network.coordination.bulletinboard.data.Advertisement;
import otsopack.full.java.network.coordination.bulletinboard.data.Subscription;
import otsopack.full.java.network.coordination.bulletinboard.http.HttpBulletinBoardClient;

public class RemoteBulletinBoard implements IBulletinBoard {
	// client with the bulletin board server
	HttpBulletinBoardClient client;
	
	// server to receive notifications
	// bulletin board for local subscriptions
	
	private String uri;
	
	public RemoteBulletinBoard(String uri) {
		this.uri = uri;
		// inconsistent during a lapse of time?
		this.client = new HttpBulletinBoardClient(this);
	}

	/* (non-Javadoc)
	 * @see otsopack.full.java.network.coordination.IBulletinBoard#subscribe(otsopack.full.java.network.coordination.bulletinboard.data.Subscription)
	 */
	@Override
	public String subscribe(Subscription subscription) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see otsopack.full.java.network.coordination.IBulletinBoard#updateSubscription(java.lang.String, long)
	 */
	@Override
	public void updateSubscription(String subscriptionId, long extratime) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see otsopack.full.java.network.coordination.IBulletinBoard#unsuscribe(java.lang.String)
	 */
	@Override
	public void unsuscribe(String subscriptionId) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see otsopack.full.java.network.coordination.IBulletinBoard#advertise(otsopack.commons.data.NotificableTemplate)
	 */
	@Override
	public String advertise(Advertisement adv) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see otsopack.full.java.network.coordination.IBulletinBoard#updateAdvertisement(java.lang.String, long)
	 */
	@Override
	public void updateAdvertisement(String advId, long extratime) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see otsopack.full.java.network.coordination.IBulletinBoard#unadvertise(java.lang.String)
	 */
	@Override
	public void unadvertise(String advId) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see otsopack.full.java.network.coordination.IBulletinBoard#getAdvertises()
	 */
	@Override
	public Advertisement[] getAdvertises() {
		return this.client.getAdvertises();
	}
	
	public String getURI() {
		return this.uri;
	}
}
