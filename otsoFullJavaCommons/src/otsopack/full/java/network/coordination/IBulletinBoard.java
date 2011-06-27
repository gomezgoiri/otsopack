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
 * Author: FILLME
 *
 */
package otsopack.full.java.network.coordination;

import java.util.Collection;

import otsopack.commons.data.NotificableTemplate;
import otsopack.full.java.network.coordination.bulletinboard.Advertisement;

public interface IBulletinBoard {
	String suscribe(Node node, NotificableTemplate tpl);
	void updateSubscription(String subscriptionId, long extratime);
	void unsuscribe(String subscriptionId);
	
	String advertise(NotificableTemplate tpl);
	void updateAdvertisement(String advId, long extratime);
	void unadvertise(String advId);
	
	Collection<Advertisement> getAdvertises();
}
