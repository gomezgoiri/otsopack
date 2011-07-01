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
package otsopack.full.java.network.coordination.bulletinboard.http;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import otsopack.full.java.network.coordination.bulletinboard.RemoteBulletinBoard;
import otsopack.full.java.network.coordination.bulletinboard.data.Advertisement;

public class HttpBulletinBoardClientTest {
	private int PORT = 18086;
	private RemoteBulletinBoard client;
	private BulletinBoardManager manager;

	@Before
	public void setUp() throws Exception {
		this.manager = new BulletinBoardManager(this.PORT);
		this.manager.start();
		
		this.client = this.manager.createClient();
	}

	@After
	public void tearDown() throws Exception {
		this.manager.stop();
	}

	/**
	 * Test method for {@link otsopack.full.java.network.coordination.bulletinboard.http.HttpBulletinBoardClient#getAdvertises()}.
	 */
	@Test
	public void testGetAdvertises() {
		Advertisement[] advertises = this.client.getAdvertises();
		for(Advertisement adv: advertises) {
			System.out.println(adv.getID());
		}
		//assertThat(advertises, hasItem(SpaceManagerManager.NODE1));
	}

	/**
	 * Test method for {@link otsopack.full.java.network.coordination.bulletinboard.http.HttpBulletinBoardClient#advertise(otsopack.full.java.network.coordination.bulletinboard.data.Advertisement)}.
	 */
	@Test
	public void testAdvertise() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link otsopack.full.java.network.coordination.bulletinboard.http.HttpBulletinBoardClient#unadvertise(otsopack.full.java.network.coordination.bulletinboard.data.Advertisement)}.
	 */
	@Test
	public void testUnadvertise() {
		fail("Not yet implemented");
	}
}