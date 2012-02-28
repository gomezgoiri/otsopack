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

package otsopack.commons.data.impl;

import java.util.Vector;

import junit.framework.TestCase;

public class AbstractTripleTest extends TestCase {
	
	public void testSplitSpace_empty(){
		final Vector splitted = AbstractTriple.splitSpace("");
		assertEquals(1, splitted.size());
		assertEquals("", splitted.get(0));
	}
	
	public void testSplitSpace_1word(){
		final Vector splitted = AbstractTriple.splitSpace("asdf");
		assertEquals(1, splitted.size());
		assertEquals("asdf", splitted.get(0));
	}
	
	public void testSplitSpace_2word(){
		final Vector splitted = AbstractTriple.splitSpace("asdf fdsa");
		assertEquals(2, splitted.size());
		assertEquals("asdf", splitted.get(0));
		assertEquals("fdsa", splitted.get(1));
	}
	
	public void testSplitSpace_2wordStartingBySpace(){
		final Vector splitted = AbstractTriple.splitSpace(" asdf fdsa");
		assertEquals(3,      splitted.size());
		assertEquals("",     splitted.get(0));
		assertEquals("asdf", splitted.get(1));
		assertEquals("fdsa", splitted.get(2));
	}
	
	public void testSplitSpace_2wordWithTwoSpaces(){
		final Vector splitted = AbstractTriple.splitSpace("asdf  fdsa");
		assertEquals(3,      splitted.size());
		assertEquals("asdf", splitted.get(0));
		assertEquals("",     splitted.get(1));
		assertEquals("fdsa", splitted.get(2));
	}
	
	public void testSplitSpace_4words(){
		final Vector splitted = AbstractTriple.splitSpace("asdf fdsa foo bar");
		assertEquals(4, splitted.size());
		assertEquals("asdf", splitted.get(0));
		assertEquals("fdsa", splitted.get(1));
		assertEquals("foo", splitted.get(2));
		assertEquals("bar", splitted.get(3));
	}
	
	public void testSplitSpace_1space(){
		final Vector splitted = AbstractTriple.splitSpace(" ");
		assertEquals(2, splitted.size());
		assertEquals("", splitted.get(0));
		assertEquals("", splitted.get(1));
	}
	
	public void testSplitSpace_2spaces(){
		final Vector splitted = AbstractTriple.splitSpace("  ");
		assertEquals(3, splitted.size());
		assertEquals("", splitted.get(0));
		assertEquals("", splitted.get(1));
		assertEquals("", splitted.get(2));
	}
}
