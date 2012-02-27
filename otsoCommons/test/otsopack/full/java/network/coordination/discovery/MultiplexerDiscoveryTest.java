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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.full.java.network.coordination.discovery;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import otsopack.full.java.network.coordination.ISpaceManager;
import otsopack.full.java.network.coordination.spacemanager.HttpSpaceManager;


public class MultiplexerDiscoveryTest {
	private final SimpleDiscovery discovery1 = new SimpleDiscovery(new HttpSpaceManager("http://ts.alimerka.es/discovery/sample01/"), new HttpSpaceManager("http://ts.alimerka.es/discovery/sample02/"));
	private final SimpleDiscovery discovery2 = new SimpleDiscovery(new HttpSpaceManager("http://sample01.morelab.deusto.es"), new HttpSpaceManager("http://sample02.morelab.deusto.es"));
	
	@Test
	public void testSingle() throws DiscoveryException{
		final MultiplexerDiscovery md = new MultiplexerDiscovery(this.discovery1);
		final ISpaceManager [] managers = md.getSpaceManagers("");
		assertArrayEquals(new ISpaceManager[]{ 
				new HttpSpaceManager("http://ts.alimerka.es/discovery/sample01/"),
				new HttpSpaceManager("http://ts.alimerka.es/discovery/sample02/"),
		}, managers);
	}
	
	@Test
	public void testMultiple() throws DiscoveryException{
		final MultiplexerDiscovery md = new MultiplexerDiscovery(this.discovery1, this.discovery2);
		final ISpaceManager [] managers = md.getSpaceManagers("");
		assertArrayEquals(new ISpaceManager[]{ 
				new HttpSpaceManager("http://ts.alimerka.es/discovery/sample01/"),
				new HttpSpaceManager("http://ts.alimerka.es/discovery/sample02/"),
				new HttpSpaceManager("http://sample01.morelab.deusto.es"),
				new HttpSpaceManager("http://sample02.morelab.deusto.es"),
		}, managers);
	}
}
