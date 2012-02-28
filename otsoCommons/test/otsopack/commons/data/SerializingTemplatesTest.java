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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.commons.data;

import junit.framework.TestCase;

public class SerializingTemplatesTest extends TestCase {
	
	public void testWrongJSON(){
		try{
			SerializableTemplateFactory.create("foo");
			fail(TemplateDeserializingException.class.getName() + " expected");
		}catch(TemplateDeserializingException tde){
			// ok
		}
	}
	
	public void testWrongCode(){
		try{
			SerializableTemplateFactory.create("{ \"type\" : \"foo\" }");
			fail(TemplateDeserializingException.class.getName() + " expected");
		}catch(TemplateDeserializingException tde){
			// ok
		}
	}
	
	public void testWildcardUriTemplate() throws Exception{
		WildcardTemplate tpl = WildcardTemplate.createWithURI("foo", "bar", "http://foo/bar");
		SerializableTemplate tpl2 = SerializableTemplateFactory.create(tpl.serialize());
		assertEquals(tpl, tpl2);
	}
	
	public void testWildcardNullTemplate() throws Exception{
		WildcardTemplate tpl = WildcardTemplate.createWithNull("foo", "bar");
		SerializableTemplate tpl2 = SerializableTemplateFactory.create(tpl.serialize());
		assertEquals(tpl, tpl2);
	}
	
	public void testWildcardStringLiteralTemplate() throws Exception{
		WildcardTemplate tpl = WildcardTemplate.createWithLiteral("foo", "bar", "foobar");
		SerializableTemplate tpl2 = SerializableTemplateFactory.create(tpl.serialize());
		assertEquals(tpl, tpl2);
	}
	
	public void testWildcardNumberLiteralTemplate() throws Exception{
		WildcardTemplate tpl = WildcardTemplate.createWithLiteral("foo", "bar", new Integer(5));
		SerializableTemplate tpl2 = SerializableTemplateFactory.create(tpl.serialize());
		assertEquals(tpl, tpl2);
	}
}
