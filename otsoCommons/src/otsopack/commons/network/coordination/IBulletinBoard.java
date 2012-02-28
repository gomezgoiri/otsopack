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
package otsopack.commons.network.coordination;

import otsopack.commons.network.coordination.bulletinboard.data.Advertisement;
import otsopack.commons.network.coordination.bulletinboard.data.Subscription;

public interface IBulletinBoard {
	String subscribe(Subscription subscription);
	void updateSubscription(String subscriptionId, long extratime);
	void unsubscribe(String subscriptionId);
	
	String advertise(Advertisement adv);
	void updateAdvertisement(String advId, long extratime);
	void unadvertise(String advId);
	
	Advertisement[] getAdvertisements();
}
