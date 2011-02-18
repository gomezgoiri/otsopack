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

import otsopack.otsoME.dataaccess.recordstore.RecordStoreDataAccessTest;
import otsopack.otsoME.dataaccess.recordstore.space.GraphRecordTest;
import otsopack.otsoME.dataaccess.recordstore.space.SpaceRecordTest;
import otsopack.otsoME.log.FileWriterTest;
import otsopack.otsoME.network.communication.JxmeSpaceTest;
import otsopack.otsoME.network.communication.util.MessageParserTest;
import otsopack.otsoME.util.UtilTest;

public class TscMESuite extends MicroemuTscMeSuite {
	public TscMESuite() {
		super("All-Tests");
				
		// DATA ACCESS //
		add(new GraphRecordTest());
		add(new SpaceRecordTest());
		add(new RecordStoreDataAccessTest());
		
		// LOG //
		add(new FileWriterTest());

		/*
		 * It doesn't make sense, but the ones below also fail :-(
		 */
		
		/*/ util /*/
		add(new MessageParserTest());
		add(new JxmeSpaceTest());

		// UTIL //
		add(new UtilTest());
	}
}