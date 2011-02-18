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
package otsopack.otsoME.util;

import java.security.DigestException;
import java.security.NoSuchAlgorithmException;

import jmunit.framework.cldc11.TestCase;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.pipe.PipeID;
import otsopack.otsoMobile.util.Util;

//TODO a lot of untested utility methods in Util class
public class UtilTest extends TestCase {

	public UtilTest() {
		super(2, "UtilTest");
	}
	
	public void test(int testNumber) throws Throwable {
		switch ( testNumber ) {
			case 0: testMd5();
				break;
			case 1: testGeneratePipeIDs();
				break;
			default:
				break;
		}
	}
	
	static private String byteArrayToString(byte[] b) {
			String ret = "";
			for(int i=0; i<b.length; i++) {
				String s = Integer.toHexString(b[i]);
				if( s.length()==1 ) // a number lower than 10
					ret += "0" + s + " ";
				else ret += s.substring(s.length()-2,s.length())+ " ";
			}
			return ret.trim();
		}
	
	public void testMd5() throws NoSuchAlgorithmException, DigestException {
		// In JavaSE
		//byte[] seed1  = MessageDigest.getInstance("MD5").digest(space1.toString().getBytes());
		
		// In JavaME
		byte[] b1 = Util.md5("tsc://www.morelab.deusto.es/smartlab/test");
		byte[] b2 = Util.md5("tsc://www.morelab.deusto.es/garilab/test");
		
		// We test them with those bytes obtained using JavaSE
		assertEquals( byteArrayToString(b1), "64 4d 49 d3 56 2c eb a8 84 3c 10 af 68 ca d7 e8");
		assertEquals( byteArrayToString(b2), "28 fb f9 98 db a5 fb e0 f4 34 77 9b fb 04 7c 47");
	}
	
	// Are generated pipe IDs equals to those generated in Jxse?
	public void testGeneratePipeIDs() throws NoSuchAlgorithmException, DigestException {
		String[] spaceURIs = { "tsc://www.morelab.deusto.es/lab1/test",
								"tsc://www.morelab.deusto.es/lab2/test", 
							};
		
		String[] expectedURIs = {
				"urn:jxta:uuid-59616261646162614E50472050325033C64E2285C12E4AC5ABE4A0394FE5519C04",
				"urn:jxta:uuid-59616261646162614E50472050325033B2FB582554854B388E14A88E3320F62404" 
				};
		
		String[] generated = new String[spaceURIs.length];

		//All the ids are as expected
		for (int i=0; i<spaceURIs.length; i++) {
			byte[] preCookedID = Util.md5(spaceURIs[i]);
			
			//PipeIDs generated using defaultNetPeerGroupID
			PipeID pipeID = IDFactory.newPipeID(PeerGroupID.defaultNetPeerGroupID, preCookedID);
			generated[i] = pipeID.toString();

			assertEquals(expectedURIs[i], generated[i]);
			
			//All the ids are different
			for (int j=0; j<i; j++) {
				assertFalse("Duplicated PipeIDs", generated[j].equals(generated[i]));
			}
		}		
	}
}