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

import junit.framework.TestCase;
import otsopack.commons.data.ISemanticFormatConversor;
import otsopack.commons.data.SemanticFormat;

public class SemanticFormatsManagerTest extends TestCase {
	
	private SemanticFormatsManager formats;
	private final SemanticFormat ENGLISH = new SemanticFormat("english");
	private final SemanticFormat SPANISH = new SemanticFormat("spanish");
	private final SemanticFormat FRENCH  = new SemanticFormat("french");
	
	private ISemanticFormatConversor english2spanish = new ISemanticFormatConversor() {
		
		public String convert(SemanticFormat originalFormat, String originalText, SemanticFormat targetFormat) {
			if(originalText.equals("hello"))
				return "hola";
			if(originalText.equals("bye"))
				return "adios";
			throw new IllegalArgumentException("Couldn't translate: " + originalText);
		}
		
		public boolean canConvert(SemanticFormat inputFormat, SemanticFormat outputFormat) {
			return inputFormat.equals(ENGLISH) && outputFormat.equals(SPANISH);
		}

		public boolean isOutputSupported(SemanticFormat outputFormat) {
			return outputFormat.equals(SPANISH);
		}

		public boolean isInputSupported(SemanticFormat inputFormat) {
			return inputFormat.equals(ENGLISH);
		}
	};
	
	private ISemanticFormatConversor spanish2english = new ISemanticFormatConversor() {
		
		public String convert(SemanticFormat originalFormat, String originalText, SemanticFormat targetFormat) {
			if(originalText.equals("hola"))
				return "hello";
			if(originalText.equals("adios"))
				return "bye";
			throw new IllegalArgumentException("Couldn't translate: " + originalText);
		}
		
		public boolean canConvert(SemanticFormat inputFormat, SemanticFormat outputFormat) {
			return inputFormat.equals(SPANISH) && outputFormat.equals(ENGLISH);
		}

		public boolean isOutputSupported(SemanticFormat outputFormat) {
			return outputFormat.equals(ENGLISH);
		}

		public boolean isInputSupported(SemanticFormat inputFormat) {
			return inputFormat.equals(SPANISH);
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
