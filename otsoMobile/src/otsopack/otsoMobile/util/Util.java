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

package otsopack.otsoMobile.util;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
	public static String normalizeSpaceURI(String spaceURI, String postfix) {				
		if(spaceURI.toString().endsWith("/")) {
			return new String(spaceURI.toString() + postfix);
		}
		return new String(spaceURI.toString() + "/" + postfix);
	}
	
	/**
	 * J2ME/J9 compatibility instead of Long.toHexString
	 * 
	 * Copied from bluecove:
	 * 	http://www.bluecove.org/bluecove/apidocs/com/intel/bluetooth/Utils.html
	 */
	public static String toHexString(long l) {
		StringBuffer buf = new StringBuffer();
		String lo = Integer.toHexString((int) l);
		if (l > 0xffffffffl) {
			String hi = Integer.toHexString((int) (l >> 32));
			buf.append(hi);
			for (int i = lo.length(); i < 8; i++) {
				buf.append('0');
			}
		}
		buf.append(lo);
		return buf.toString();
	}
	
	/**
	 * Equivalent to (JavaSE):
	 * 		MessageDigest.getInstance("MD5").digest(sInput.getBytes());
	 * 
	 * Source: http://www.anavi.org/article/107/
	 */
	public static byte[] md5(String sInput) throws NoSuchAlgorithmException, DigestException {
		//MD5 digest length is 128b (32 characters)
		int nDigestLen = 16;
		byte[] PlainText = sInput.getBytes();
		//Allocate memory for the encrypted text
		byte[] encryptedText = new byte[nDigestLen];
		MessageDigest Cipher;
		Cipher = MessageDigest.getInstance("MD5");
		Cipher.update(PlainText, 0, PlainText.length);
		Cipher.digest(encryptedText, 0, nDigestLen);
		//Convert result to hexidecimal
		return encryptedText;
	}
}
