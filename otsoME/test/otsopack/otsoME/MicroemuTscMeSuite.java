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
package otsopack.otsoME;

import jmunit.framework.cldc11.TestSuite;
import otsopack.otsoME.network.communication.demand.DemandRecordTest;
import otsopack.otsoME.network.communication.demand.local.LocalDemandManagerTest;
import otsopack.otsoME.network.communication.demand.remote.GarbageCollectorTest;
import otsopack.otsoME.network.communication.demand.remote.RemoteDemandManagerTest;
import otsopack.otsoME.network.communication.incoming.IncomingListTest;
import otsopack.otsoME.network.communication.incoming.response.LockModelResponseTest;
import otsopack.otsoME.network.communication.incoming.response.ModelResponseTest;
import otsopack.otsoME.network.communication.incoming.response.URIResponseTest;
import otsopack.otsoME.network.communication.notifications.AdvertisementTest;
import otsopack.otsoME.network.communication.notifications.NotificationContainerTest;
import otsopack.otsoME.network.communication.notifications.SubscriptionTest;

public class MicroemuTscMeSuite extends TestSuite {
	
	public MicroemuTscMeSuite(){
		super("All-Tests-Without-recordstore");
	}
	
	public MicroemuTscMeSuite(String title){
		super(title);
	}
	
	{
		// NETWORK //
		 /* communication */
		  /*/ demand /*/
		add(new LocalDemandManagerTest());
		add(new GarbageCollectorTest());
		add(new RemoteDemandManagerTest());
		add(new DemandRecordTest());
		
		  /*/ incoming /*/
		add(new ModelResponseTest());
		add(new LockModelResponseTest());
		add(new URIResponseTest());
		add(new IncomingListTest());
		
		  /*/ notifications /*/
		add(new AdvertisementTest());
		add(new NotificationContainerTest());
		add(new SubscriptionTest());
		
		  /*/ util /*/
		add(new NotificationContainerTest());
		add(new SubscriptionTest());
		
		
		 /* coordination */
		//add(new JxmeCoordinationTest());
		
		
		//add(new JxmeNetworkTest());
	}
}
