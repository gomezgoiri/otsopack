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

	@Override
	public String subscribe(Subscription subscription) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateSubscription(String subscriptionId, long extratime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unsuscribe(String subscriptionId) {
		// TODO Auto-generated method stub

	}

	@Override
	public String advertise(Advertisement adv) {
		return this.client.advertise(adv);
	}

	@Override
	public void updateAdvertisement(String advId, long extratime) {
		this.client.updateAdvertise(new Advertisement(advId, extratime, null));
	}

	@Override
	public void unadvertise(String advId) {
		this.client.unadvertise(advId);
	}

	@Override
	public Advertisement[] getAdvertises() {
		return this.client.getAdvertises();
	}

	public String getURI() {
		return this.uri;
	}
}
