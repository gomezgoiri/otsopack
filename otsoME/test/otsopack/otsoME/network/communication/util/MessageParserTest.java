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
package otsopack.otsoME.network.communication.util;

import java.io.ByteArrayOutputStream;
import java.util.Vector;

import otsopack.otsoMobile.data.IGraph;
import otsopack.otsoMobile.data.IModel;
import otsopack.otsoMobile.data.ISemanticFactory;
import otsopack.otsoMobile.data.ITemplate;
import otsopack.otsoMobile.data.impl.SemanticFactory;
import otsopack.otsoMobile.data.impl.microjena.MicrojenaFactory;
import otsopack.otsoMobile.exceptions.MalformedMessageException;
import otsopack.otsoMobile.exceptions.MalformedTemplateException;
import otsopack.otsoMobile.exceptions.TripleParseException;
import otsopack.otsoME.network.communication.util.MessageParser;
import otsopack.otsoME.sampledata.ExampleME;
import jmunit.framework.cldc11.TestCase;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;

public class MessageParserTest extends TestCase {	
	public MessageParserTest() {
		super(30, "MessageParserTest");
	}
	
	public void setUp()	throws Throwable {
		super.setUp();
		SemanticFactory.initialize(new MicrojenaFactory());
	}
	
	public void tearDown() {
	}
	
	public void test(int testNumber) throws Throwable {
		switch ( testNumber ) {
			case 28: testParseResponseMessage1();
				break;
			case 29: testParseResponseMessage2();
				break;
			case 0: testQueryCreator();
				break;
			case 1: testQueryParser();
				break;
			case 2: testReadTemplateCreator();
				break;
			case 3: testReadTemplateParser();
				break;
			case 4: testReadURICreator();
				break;
			case 5: testReadURIParser();
				break;
			case 6: testTakeTemplateCreator();
				break;
			case 7: testTakeTemplateParser();
				break;
			case 8: testTakeURICreator();
				break;
			case 9: testTakeURIParser();
				break;
			case 10: testAdvertiseCreator();
				break;
			case 11: testAdvertiseParser();
				break;
			case 12: testUnadvertiseCreator();
				break;
			case 13: testUnadvertiseParser();
				break;
			case 14: testSubscribeCreator();
				break;
			case 15: testSubscribeParser();
				break;
			case 16: testUnsubscribeCreator();
				break;
			case 17: testUnsubscribeParser();
				break;
			case 18: testQueryMultipleCreator();
				break;
			case 19: testQueryMultipleParser();
				break;
			case 20: testDemandCreator();
				break;
			case 21: testDemandParser();
				break;
			case 22: testSuggestCreator();
				break;
			case 23: testSuggestParser();
				break;
			case 24: testObtainDemandsCreator();
				break;
			case 25: testObtainDemandsParser();
				break;
			case 26: testResponseDemandsCreator();
				break;
			case 27: testResponseDemandsParser();
				break;
			default:
				break;
		}
	}

		private void checkSender(final Message msg) {
			checkSender( msg, "default" );
		}
		
		private void checkSender(final Message msg, final String peername) {
			final MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.SENDER);
			assertEquals( msgElement.toString(), peername );
		}
	
		private void checkSelector(final Message msg, final String template) {
			MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.TEMPLATE);
			assertEquals( msgElement.toString(), template );
		}
		
		
		private void checkModel(Message msg, IModel model) {
			MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.MODEL);
			ByteArrayOutputStream bin = new ByteArrayOutputStream();
	        model.write(bin,IModel.ntriple);
	        
	        byte[] expected = bin.toByteArray();
	        byte[] obtained = msgElement.getBytes(true);
	        assertEquals(obtained.length, expected.length);
	        for(int i=0; i<expected.length; i++) {
	        	assertEquals(obtained[i],expected[i]);
	        }
		}
	
	void testParseResponseMessage1() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final FakeCallback fl = new FakeCallback();
		final Vector listeners = new Vector();
		listeners.addElement(fl);
		final ITemplate template = sf.createTemplate("?s <http://predicado> ?o .");
		final IGraph triples = sf.createEmptyGraph();
			triples.add(sf.createTriple("http://subject1","http://predicate1","http://object1"));
			triples.add(sf.createTriple("http://subject2","http://predicate1","http://object1"));
			triples.add(sf.createTriple("http://subject2","http://predicate2","http://object2"));
		final Message msg = MessageParser.createResponseMessage(null, template, sf.createModelForGraph(triples)); 
		MessageParser.parseMessage(msg, listeners);
		assertTrue( fl.isResponseReceived() );
		assertEquals( fl.getSelector(), template );
		assertTrue( fl.getModel().getGraph().containsAll(triples) );
	}
	
	void testParseResponseMessage2() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final FakeCallback fl = new FakeCallback();
		final Vector listeners = new Vector();
		listeners.addElement(fl);
		final String responseURI = "http://espaciointerestelar/grafo466";
		final IGraph triples = sf.createEmptyGraph();
		triples.add(sf.createTriple("http://subject1","http://predicate1","http://object1"));
		triples.add(sf.createTriple("http://subject2","http://predicate1","http://object1"));
		triples.add(sf.createTriple("http://subject2","http://predicate2","http://object2"));
		final Message msg = MessageParser.createResponseMessage(null, responseURI, sf.createModelForGraph(triples)); 
		MessageParser.parseMessage(msg, listeners);
		assertTrue( fl.isResponseReceived() );
		assertEquals( fl.getGraphURI(), responseURI );
		assertTrue( fl.getModel().getGraph().containsAll(triples) );
	}

	void testQueryCreator() throws MalformedTemplateException {
		final ITemplate template = new SemanticFactory().createTemplate("<"+ExampleME.subj1+"> ?p ?o .");
		final Message msg = MessageParser.createQueryMessage(null,template);
		
		checkSender(msg);
		
		final MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.QUERY );
		
		checkSelector(msg, "<"+ExampleME.subj1+"> ?p ?o .");
	}
	
	void testQueryParser() throws MalformedMessageException, MalformedTemplateException {
		final FakeCallback fl = new FakeCallback();
		final Vector listeners = new Vector();
		listeners.addElement(fl);
		final ITemplate template = new SemanticFactory().createTemplate("<"+ExampleME.subj1+"> ?p ?o .");
		final Message msg = MessageParser.createQueryMessage(null,template);
		MessageParser.parseMessage(msg, listeners);
		assertTrue( fl.isQueryReceived() );
		assertEquals( fl.getSelector(), template );
	}
	
	void testReadTemplateCreator() throws MalformedTemplateException {
		final ITemplate template = new SemanticFactory().createTemplate("?s ?p <"+ExampleME.subj1+"> .");
		final Message msg = MessageParser.createReadMessage(null,template);
		
		checkSender(msg);

		final MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.READ );
		
		checkSelector(msg, "?s ?p <"+ExampleME.subj1+"> .");
	}
	
	void testReadTemplateParser() throws MalformedMessageException, MalformedTemplateException {
		final FakeCallback fl = new FakeCallback();
		final Vector listeners = new Vector();
		listeners.addElement(fl);
		final ITemplate template = new SemanticFactory().createTemplate("?s ?p <"+ExampleME.subj1+"> .");
		final Message msg = MessageParser.createReadMessage(null,template);
		MessageParser.parseMessage(msg, listeners);
		assertTrue( fl.isReadReceived() );
		assertEquals( fl.getSelector(), template );
	}
	
	void testReadURICreator() {
		final String uri = "ts://testuricreator/graph21";
		final Message msg = MessageParser.createReadMessage(null,uri);
		
		checkSender(msg);
		
		MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.READ );
		
		msgElement = msg.getMessageElement(null,MessageParser.Properties.GRAPHURI);
		assertEquals( msgElement.toString(), uri.toString());
	}
	
	void testReadURIParser() throws MalformedMessageException {
		final FakeCallback fl = new FakeCallback();
		final Vector listeners = new Vector();
		listeners.addElement(fl);
		final String uri = "ts://testuricreator/graph21";
		final Message msg = MessageParser.createReadMessage(null,uri);
		MessageParser.parseMessage(msg, listeners);
		assertTrue( fl.isReadReceived() );
		assertEquals( fl.getGraphURI(), uri.toString() );
	}
	
	void testTakeTemplateCreator() throws MalformedTemplateException {
		final ITemplate template = new SemanticFactory().createTemplate("?s ?p <"+ExampleME.subj1+"> .");
		final Message msg = MessageParser.createTakeMessage(null,template);
		
		checkSender(msg);
		
		final MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.TAKE );
		
		checkSelector(msg, "?s ?p <"+ExampleME.subj1+"> .");
	}
	
	void testTakeTemplateParser() throws MalformedMessageException, MalformedTemplateException {
		final FakeCallback fl = new FakeCallback();
		final Vector listeners = new Vector();
		listeners.addElement(fl);
		final ITemplate template = new SemanticFactory().createTemplate("<"+ExampleME.subj1+"> ?p ?o .");
		final Message msg = MessageParser.createTakeMessage(null,template);
		MessageParser.parseMessage(msg, listeners);
		assertTrue( fl.isTakeReceived() );
		assertEquals( fl.getSelector(), template );
	}
	
	void testTakeURICreator() {
		final String uri = "ts://testuricreator/graph21";
		final Message msg = MessageParser.createTakeMessage(null,uri);
		
		checkSender(msg);
		
		MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.TAKE );
		
		msgElement = msg.getMessageElement(null,MessageParser.Properties.GRAPHURI);
		assertEquals( msgElement.toString(), uri.toString());
	}
	
	void testTakeURIParser() throws MalformedMessageException {
		final FakeCallback fl = new FakeCallback();
		final Vector listeners = new Vector();
		listeners.addElement(fl);
		final String uri = "ts://testuricreator/graph21";
		final Message msg = MessageParser.createTakeMessage(null,uri);
		MessageParser.parseMessage(msg, listeners);
		assertTrue( fl.isTakeReceived() );
		assertEquals( fl.getGraphURI(), uri.toString() );
	}
	
	
	void testAdvertiseCreator() throws MalformedTemplateException {
		final ITemplate template = new SemanticFactory().createTemplate("<"+ExampleME.subj1+"> ?p ?o .");
		final Message msg = MessageParser.createAdvertiseMessage(null,template);
		
		checkSender(msg);
		
		final MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.ADVERTISE );
		
		checkSelector(msg, "<"+ExampleME.subj1+"> ?p ?o .");
	}
	
	void testAdvertiseParser() throws MalformedMessageException, MalformedTemplateException {
		final FakeCallback fl = new FakeCallback();
		final Vector listeners = new Vector();
		listeners.addElement(fl);
		final ITemplate template = new SemanticFactory().createTemplate("?s ?p <"+ExampleME.subj2+"> .");
		final Message msg = MessageParser.createAdvertiseMessage(null,template);
		MessageParser.parseMessage(msg, listeners);
		assertTrue( fl.isAdvertiseReceived() );
		assertEquals( fl.getSelector(), template );
	}
	
	void testUnadvertiseCreator() {
		final String uri = "ts://testuricreator/adv68542";
		final Message msg = MessageParser.createUnadvertiseMessage(null,uri);
		
		checkSender(msg);
		
		MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.UNADVERTISE );
		
		msgElement = msg.getMessageElement(null,MessageParser.Properties.TEMPLATEURI);
		assertEquals( msgElement.toString(), uri.toString());
	}
	
	void testUnadvertiseParser() throws MalformedMessageException {
		final FakeCallback fl = new FakeCallback();
		final Vector listeners = new Vector();
		listeners.addElement(fl);
		final String uri = "ts://testuricreator/adv24594";
		final Message msg = MessageParser.createUnadvertiseMessage(null,uri);
		MessageParser.parseMessage(msg, listeners);
		assertTrue( fl.isUnadvertiseReceived() );
		assertEquals( fl.getAdvsubsURI(), uri.toString() );
	}
	
	void testSubscribeCreator() throws MalformedTemplateException {
		final ITemplate template = new SemanticFactory().createTemplate("<"+ExampleME.subj1+"> ?p <"+ExampleME.subj2+"> .");
		final Message msg = MessageParser.createSubscribeMessage(null,template);
		
		checkSender(msg);

		final MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.SUBSCRIBE );
		
		checkSelector(msg, "<"+ExampleME.subj1+"> ?p <"+ExampleME.subj2+"> .");
	}
	
	void testSubscribeParser() throws MalformedMessageException, MalformedTemplateException {
		final FakeCallback fl = new FakeCallback();
		final Vector listeners = new Vector();
		listeners.addElement(fl);
		final ITemplate template = new SemanticFactory().createTemplate("<"+ExampleME.subj3+"> <"+ExampleME.prop1+"> ?o .");
		final Message msg = MessageParser.createSubscribeMessage(null,template);
		MessageParser.parseMessage(msg, listeners);
		assertTrue( fl.isSubscribeReceived() );
		assertEquals( fl.getSelector(), template );
	}
	
	void testUnsubscribeCreator() {
		final String uri = "ts://testuricreator/subs24593";
		final Message msg = MessageParser.createUnsubscribeMessage(null,uri);
		
		checkSender(msg);
		
		MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.UNSUBSCRIBE );
		
		msgElement = msg.getMessageElement(null,MessageParser.Properties.TEMPLATEURI);
		assertEquals( msgElement.toString(), uri.toString());
	}
	
	void testUnsubscribeParser() throws MalformedMessageException {
		final FakeCallback fl = new FakeCallback();
		final Vector listeners = new Vector();
		listeners.addElement(fl);
		final String uri = "ts://testuricreator/subs24594";
		final Message msg = MessageParser.createUnsubscribeMessage(null,uri);
		MessageParser.parseMessage(msg, listeners);
		assertTrue( fl.isUnsubscribeReceived() );
		assertEquals( fl.getAdvsubsURI(), uri.toString() );
	}
	
	void testQueryMultipleCreator() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final ITemplate[] templates = new ITemplate[4];
			templates[0] = sf.createTemplate("<"+ExampleME.subj1+"> ?p ?o .");
			templates[1] = sf.createTemplate("?s ?p ?o .");
			templates[2] = sf.createTemplate("<"+ExampleME.subj1+"> ?p <"+ExampleME.subj4+"> .");
			templates[3] = sf.createTemplate("<"+ExampleME.subj1+"> <"+ExampleME.prop2+"> ?o .");
		final Message msg = MessageParser.createQueryMultipleMessage(null,templates);
		
		checkSender(msg);
		
		MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.QUERYMULTIPLE );
		
		msgElement = msg.getMessageElement(null,MessageParser.Properties.TEMPLATE_MULTIPLE);
			String templatesStr = "";
			for(int i=0; i<templates.length; i++)
				templatesStr += templates[i].toString()+"\n";
		assertEquals( msgElement.toString(), templatesStr);
	}
	
	void testQueryMultipleParser() throws MalformedMessageException, MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final FakeCallback fl = new FakeCallback();
		final Vector listeners = new Vector();
		listeners.addElement(fl);
		final ITemplate[] templates = new ITemplate[4];
			templates[0] = sf.createTemplate("<"+ExampleME.subj1+"> ?p ?o .");
			templates[1] = sf.createTemplate("?s ?p ?o .");
			templates[2] = sf.createTemplate("<"+ExampleME.subj1+"> ?p <"+ExampleME.subj4+"> .");
			templates[3] = sf.createTemplate("<"+ExampleME.subj1+"> <"+ExampleME.prop2+"> ?o .");		final Message msg = MessageParser.createQueryMultipleMessage(null,templates);
		MessageParser.parseMessage(msg,listeners);
		assertTrue( fl.isQueryMultipleReceived() );
		
		final ITemplate[] retSelectors = fl.getSelectors();
		for(int i=0; i<retSelectors.length; i++) {
			boolean found = false; // just in case I will use a set which "disorders" its elements
			for(int j=0; j<templates.length; j++) {
				if( retSelectors[i].equals(templates[j]) ) {
					found = true;
					break;
				}
			}
			assertTrue(found);
		}
	}

	void testDemandCreator() throws MalformedTemplateException {
		final ITemplate template = new SemanticFactory().createTemplate("?s ?p <"+ExampleME.subj1+"> .");
		final Message msg = MessageParser.createDemandMessage(null, template, 2000);
		
		checkSender(msg);
		checkSelector(msg, "?s ?p <"+ExampleME.subj1+"> .");
		
		MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.DEMAND );
		
		msgElement = msg.getMessageElement(null,MessageParser.Properties.LEASE_TIME);
		assertEquals( msgElement.toString(), "2000" );
	}
	
	void testDemandParser() throws MalformedMessageException, MalformedTemplateException {
		final FakeCallback fl = new FakeCallback();
		final Vector listeners = new Vector();
		listeners.addElement(fl);
		final ITemplate template = new SemanticFactory().createTemplate("<"+ExampleME.subj2+"> ?p <"+ExampleME.subj1+"> .");
		final Message msg = MessageParser.createDemandMessage(null,template, 2000);
		MessageParser.parseMessage(msg,listeners);
		assertTrue( fl.isDemandReceived() );
		assertEquals( fl.getSelector(), template );
		assertEquals( fl.getLeaseTime(), 2000 );
	}

	void testSuggestCreator() throws TripleParseException {
		final ISemanticFactory sf = new SemanticFactory();
		final IGraph graph = sf.createEmptyGraph();
		graph.add( sf.createTriple(ExampleME.subj1, ExampleME.prop1, ExampleME.obj3));
		graph.add(sf.createTriple(ExampleME.subj1, ExampleME.prop2, ExampleME.obj10));
		graph.add( sf.createTriple(ExampleME.subj1, ExampleME.prop3, ExampleME.obj7));
		final Message msg = MessageParser.createSuggestMessage(null, sf.createModelForGraph(graph));
		
		checkSender(msg);
		checkModel(msg, sf.createModelForGraph(graph));
		final MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.SUGGEST );

	}

	void testSuggestParser() throws MalformedMessageException, TripleParseException {
		final ISemanticFactory sf = new SemanticFactory();
		final FakeCallback fl = new FakeCallback();
		final Vector listeners = new Vector();
		listeners.addElement(fl);
		final IGraph graph = sf.createEmptyGraph();
		graph.add( sf.createTriple(ExampleME.subj1, ExampleME.prop1, ExampleME.obj3));
		graph.add(sf.createTriple(ExampleME.subj1, ExampleME.prop2, ExampleME.obj10));
		graph.add( sf.createTriple(ExampleME.subj1, ExampleME.prop3, ExampleME.obj7));
		final Message msg = MessageParser.createSuggestMessage(null, sf.createModelForGraph(graph));
		MessageParser.parseMessage(msg,listeners);
		assertTrue( fl.isSuggestReceived() );
		final IGraph obtainedGraph = fl.getModel().getGraph();
		assertTrue( graph.containsAll(obtainedGraph) );
		assertTrue( obtainedGraph.containsAll(graph) );
	}
	
	void testObtainDemandsCreator() {
		final Message msg = MessageParser.createObtainDemandsMessage(null);
		
		checkSender(msg);
		final MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.OBTAIN_DMNDS );
	}
	
	void testObtainDemandsParser() throws MalformedMessageException {
		final FakeCallback fl = new FakeCallback();
		final Vector listeners = new Vector();
		listeners.addElement(fl);
		final Message msg = MessageParser.createObtainDemandsMessage(null);
		MessageParser.parseMessage(msg,listeners);
		assertTrue( fl.isObtainDemands() );
	}
	
	void testResponseDemandsCreator() {
		byte[] bytes = new byte[5];
		bytes[0] = (byte)21;
		bytes[1] = (byte)22;
		bytes[3] = (byte)23;
		bytes[4] = (byte)24;
		final Message msg = MessageParser.createResponseDemandsMessage(null,bytes);
		
		checkSender(msg);
		MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.RESPONSE_DMNDS );
		
		byte[] obtained = msg.getMessageElement(null,MessageParser.Properties.BYTES).getBytes(false);
		for(int i=0; i<bytes.length; i++) {
			assertEquals(bytes[i],obtained[i]);
		}
	}
	
	void testResponseDemandsParser() throws MalformedMessageException {
		final FakeCallback fl = new FakeCallback();
		final Vector listeners = new Vector();
		listeners.addElement(fl);
		byte[] bytes = new byte[5];
		bytes[0] = (byte)21;
		bytes[1] = (byte)22;
		bytes[3] = (byte)23;
		bytes[4] = (byte)24;
		final Message msg = MessageParser.createResponseDemandsMessage(null,bytes);
		MessageParser.parseMessage(msg,listeners);
		
		assertTrue( fl.isResponseReceived() );
		for(int i=0; i<bytes.length; i++) {
			assertEquals(bytes[i],fl.getBytes()[i]);
		}
	}
}