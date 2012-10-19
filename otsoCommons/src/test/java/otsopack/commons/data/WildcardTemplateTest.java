/*
 * Copyright (C) 2012 onwards University of Deusto
 * 
 * All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author:	Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */
package otsopack.commons.data;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class WildcardTemplateTest {
	@Test
	public void testBothSidesMatch() {
		WildcardTemplate tpl1 = WildcardTemplate.createWithNull("http://s", "http://p");
		WildcardTemplate tpl2 = WildcardTemplate.createWithNull("http://s", "http://p");
		assertTrue(tpl1.match(tpl2));
		assertTrue(tpl2.match(tpl1));
		
		tpl1 = WildcardTemplate.createWithLiteral("http://s", "http://p", Integer.valueOf(1));
		tpl2 = WildcardTemplate.createWithLiteral("http://s", "http://p", Integer.valueOf(1));
		assertTrue(tpl1.match(tpl2));
		assertTrue(tpl2.match(tpl1));
		
		tpl1 = WildcardTemplate.createWithURI("http://s", "http://p", "http://o");
		tpl2 = WildcardTemplate.createWithURI("http://s", "http://p", "http://o");
		assertTrue(tpl1.match(tpl2));
		assertTrue(tpl2.match(tpl1));
	}
	
	@Test
	public void testBothSidesUnmatch() {
		WildcardTemplate tpl1 = WildcardTemplate.createWithNull("http://s", "http://p1");
		WildcardTemplate tpl2 = WildcardTemplate.createWithNull("http://s", "http://p2");
		assertFalse(tpl1.match(tpl2));
		assertFalse(tpl2.match(tpl1));
		
		tpl1 = WildcardTemplate.createWithLiteral("http://s", "http://p", Integer.valueOf(1));
		tpl2 = WildcardTemplate.createWithLiteral("http://s", "http://p", Integer.valueOf(2));
		assertFalse(tpl1.match(tpl2));
		assertFalse(tpl2.match(tpl1));
		
		tpl1 = WildcardTemplate.createWithURI("http://s", "http://p", "http://o1");
		tpl2 = WildcardTemplate.createWithLiteral("http://s", "http://p", "http://o2");
		assertFalse(tpl1.match(tpl2));
		assertFalse(tpl2.match(tpl1));
	}
	
	@Test
	public void testOneSideMatch() {
		WildcardTemplate tpl1 = WildcardTemplate.createWithNull("http://s", "http://p");
		WildcardTemplate tpl2 = WildcardTemplate.createWithLiteral("http://s", "http://p", Integer.valueOf(1));
		assertTrue(tpl1.match(tpl2));
		assertFalse(tpl2.match(tpl1));
		
		tpl1 = WildcardTemplate.createWithNull("http://s", null);
		tpl2 = WildcardTemplate.createWithLiteral("http://s", null, Integer.valueOf(1));
		assertTrue(tpl1.match(tpl2));
		assertFalse(tpl2.match(tpl1));
		
		tpl1 = WildcardTemplate.createWithNull(null, null);
		tpl2 = WildcardTemplate.createWithLiteral("http://s", "http://p", Integer.valueOf(1));
		assertTrue(tpl1.match(tpl2));
		assertFalse(tpl2.match(tpl1));
	}
}
