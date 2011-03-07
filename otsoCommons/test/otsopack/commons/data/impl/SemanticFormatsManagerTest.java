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

package otsopack.commons.data.impl;

import otsopack.commons.data.ISemanticFormatConversor;
import junit.framework.TestCase;

public class SemanticFormatsManagerTest extends TestCase {
	
	private SemanticFormatsManager formats;
	private final String ENGLISH = "english";
	private final String SPANISH = "spanish";
	private final String FRENCH  = "french";
	
	private ISemanticFormatConversor english2spanish = new ISemanticFormatConversor() {
		
		public String convert(String originalFormat, String originalText, String targetFormat) {
			if(originalText.equals("hello"))
				return "hola";
			if(originalText.equals("bye"))
				return "adios";
			throw new IllegalArgumentException("Couldn't translate: " + originalText);
		}
		
		public boolean canConvert(String inputFormat, String outputFormat) {
			return inputFormat.equals(ENGLISH) && outputFormat.equals(SPANISH);
		}
	};
	
	private ISemanticFormatConversor spanish2english = new ISemanticFormatConversor() {
		
		public String convert(String originalFormat, String originalText, String targetFormat) {
			if(originalText.equals("hola"))
				return "hello";
			if(originalText.equals("adios"))
				return "bye";
			throw new IllegalArgumentException("Couldn't translate: " + originalText);
		}
		
		public boolean canConvert(String inputFormat, String outputFormat) {
			return inputFormat.equals(SPANISH) && outputFormat.equals(ENGLISH);
		}
	};
	
	public void setUp(){
		this.formats = new SemanticFormatsManager();
	}
	
	public void testCanConvert(){
		SemanticFormatsManager.initialize(new ISemanticFormatConversor[]{spanish2english, english2spanish});
		assertTrue(this.formats.canConvert(ENGLISH, SPANISH));
		assertTrue(this.formats.canConvert(SPANISH, ENGLISH));
		assertFalse(this.formats.canConvert(SPANISH, FRENCH));
		assertFalse(this.formats.canConvert(FRENCH,  SPANISH));
	}
	
	public void testConvert(){
		SemanticFormatsManager.initialize(new ISemanticFormatConversor[]{spanish2english, english2spanish});
		assertEquals("hola",  this.formats.convert(ENGLISH, "hello", SPANISH));
		assertEquals("hello", this.formats.convert(SPANISH, "hola",  ENGLISH));
	}
}
