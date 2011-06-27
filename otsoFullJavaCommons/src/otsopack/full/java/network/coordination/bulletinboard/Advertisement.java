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
package otsopack.full.java.network.coordination.bulletinboard;

import otsopack.commons.data.NotificableTemplate;

public class Advertisement extends AbstractNotificableElement {
	public Advertisement(String id, long expiration, NotificableTemplate tpl) {
		super(id, expiration, tpl);
	}
}
