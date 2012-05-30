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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.commons.network.communication.util;

import org.junit.Test;

import otsopack.commons.network.subscriptions.bulletinboard.http.serializables.SubscribeJSON;
import otsopack.commons.network.subscriptions.bulletinboard.http.serializables.TemplateJSON;

public class JSONDecoderTest {
	
	@Test
	public void testArray() throws Exception {
		final TemplateJSON tpl = new TemplateJSON("http://subject", "http://predicate", "http://object");
		final SubscribeJSON subs = SubscribeJSON.createSubscription("http://space/subscriptions/24534", tpl, "http://callbackuri", 1200L);
		
		final String resultTpl = JSONEncoder.encode(tpl);
		//assertEquals("{\"object\":\"http://object\",\"predicate\":\"http://predicate\",\"subject\":\"http://subject\"}", resultTpl);
		
		final String resultSubs = JSONEncoder.encode(subs);
		System.out.println(resultSubs);
	}
}
