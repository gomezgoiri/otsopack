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
package otsopack.droid.network.communication.util;

import java.util.Vector;

import junit.framework.TestCase;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import otsopack.commons.data.Graph;
import otsopack.commons.data.IGraph;
import otsopack.commons.data.IModel;
import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.SemanticFormats;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.exceptions.MalformedMessageException;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.commons.exceptions.TripleParseException;
import otsopack.droid.network.communication.incoming.ITSCallback;
import otsopack.droid.sampledata.ExampleME;

public class MessageParserTest extends TestCase {	
	
	private MicrojenaFactory factory;
	
	public void setUp() throws Exception {
		super.setUp();
		factory = new MicrojenaFactory();
		SemanticFactory.initialize(factory);
	}
	
	public void tearDown() {
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
		
        final Graph graph = model.write(SemanticFormats.NTRIPLES);
        
        byte[] expected = graph.getData().getBytes();
        byte[] obtained = msgElement.getBytes(true);
        assertEquals(obtained.length, expected.length);
        for(int i=0; i<expected.length; i++) {
        	assertEquals(obtained[i],expected[i]);
        }
	}
	
	public void testParseResponseMessage1() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final FakeCallback fl = new FakeCallback();
		final Vector<ITSCallback> listeners = new Vector<ITSCallback>();
		listeners.addElement(fl);
		final ITemplate template = sf.createTemplate("?s <http://predicado> ?o .");
		final IGraph triples = sf.createEmptyGraph();
			triples.add(factory.createTriple("http://subject1","http://predicate1","http://object1"));
			triples.add(factory.createTriple("http://subject2","http://predicate1","http://object1"));
			triples.add(factory.createTriple("http://subject2","http://predicate2","http://object2"));
		final Message msg = MessageParser.createResponseMessage(null, template, sf.createModelForGraph(triples)); 
		MessageParser.parseMessage(msg, listeners);
		assertTrue( fl.isResponseReceived() );
		assertEquals( fl.getSelector(), template );
		assertTrue( fl.getModel().getGraph().getIGraph().containsAll(triples) );
	}
	
	public void testParseResponseMessage2() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final FakeCallback fl = new FakeCallback();
		final Vector<ITSCallback> listeners = new Vector<ITSCallback>();
		listeners.addElement(fl);
		final String responseURI = "http://espaciointerestelar/grafo466";
		final IGraph triples = sf.createEmptyGraph();
		triples.add(factory.createTriple("http://subject1","http://predicate1","http://object1"));
		triples.add(factory.createTriple("http://subject2","http://predicate1","http://object1"));
		triples.add(factory.createTriple("http://subject2","http://predicate2","http://object2"));
		final Message msg = MessageParser.createResponseMessage(null, responseURI, sf.createModelForGraph(triples)); 
		MessageParser.parseMessage(msg, listeners);
		assertTrue( fl.isResponseReceived() );
		assertEquals( fl.getGraphURI(), responseURI );
		assertTrue( fl.getModel().getGraph().getIGraph().containsAll(triples) );
	}

	public void testQueryCreator() throws MalformedTemplateException {
		final ITemplate template = new SemanticFactory().createTemplate("<"+ExampleME.subj1+"> ?p ?o .");
		final Message msg = MessageParser.createQueryMessage(null,template);
		
		checkSender(msg);
		
		final MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.QUERY );
		
		checkSelector(msg, "<"+ExampleME.subj1+"> ?p ?o .");
	}
	
	public void testQueryParser() throws MalformedMessageException, MalformedTemplateException {
		final FakeCallback fl = new FakeCallback();
		final Vector<ITSCallback> listeners = new Vector<ITSCallback>();
		listeners.addElement(fl);
		final ITemplate template = new SemanticFactory().createTemplate("<"+ExampleME.subj1+"> ?p ?o .");
		final Message msg = MessageParser.createQueryMessage(null,template);
		MessageParser.parseMessage(msg, listeners);
		assertTrue( fl.isQueryReceived() );
		assertEquals( fl.getSelector(), template );
	}
	
	public void testReadTemplateCreator() throws MalformedTemplateException {
		final ITemplate template = new SemanticFactory().createTemplate("?s ?p <"+ExampleME.subj1+"> .");
		final Message msg = MessageParser.createReadMessage(null,template);
		
		checkSender(msg);

		final MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.READ );
		
		checkSelector(msg, "?s ?p <"+ExampleME.subj1+"> .");
	}
	
	public void testReadTemplateParser() throws MalformedMessageException, MalformedTemplateException {
		final FakeCallback fl = new FakeCallback();
		final Vector<ITSCallback> listeners = new Vector<ITSCallback>();
		listeners.addElement(fl);
		final ITemplate template = new SemanticFactory().createTemplate("?s ?p <"+ExampleME.subj1+"> .");
		final Message msg = MessageParser.createReadMessage(null,template);
		MessageParser.parseMessage(msg, listeners);
		assertTrue( fl.isReadReceived() );
		assertEquals( fl.getSelector(), template );
	}
	
	public void testReadURICreator() {
		final String uri = "ts://testuricreator/graph21";
		final Message msg = MessageParser.createReadMessage(null,uri);
		
		checkSender(msg);
		
		MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.READ );
		
		msgElement = msg.getMessageElement(null,MessageParser.Properties.GRAPHURI);
		assertEquals( msgElement.toString(), uri.toString());
	}
	
	public void testReadURIParser() throws MalformedMessageException {
		final FakeCallback fl = new FakeCallback();
		final Vector<ITSCallback> listeners = new Vector<ITSCallback>();
		listeners.addElement(fl);
		final String uri = "ts://testuricreator/graph21";
		final Message msg = MessageParser.createReadMessage(null,uri);
		MessageParser.parseMessage(msg, listeners);
		assertTrue( fl.isReadReceived() );
		assertEquals( fl.getGraphURI(), uri.toString() );
	}
	
	public void testTakeTemplateCreator() throws MalformedTemplateException {
		final ITemplate template = new SemanticFactory().createTemplate("?s ?p <"+ExampleME.subj1+"> .");
		final Message msg = MessageParser.createTakeMessage(null,template);
		
		checkSender(msg);
		
		final MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.TAKE );
		
		checkSelector(msg, "?s ?p <"+ExampleME.subj1+"> .");
	}
	
	public void testTakeTemplateParser() throws MalformedMessageException, MalformedTemplateException {
		final FakeCallback fl = new FakeCallback();
		final Vector<ITSCallback> listeners = new Vector<ITSCallback>();
		listeners.addElement(fl);
		final ITemplate template = new SemanticFactory().createTemplate("<"+ExampleME.subj1+"> ?p ?o .");
		final Message msg = MessageParser.createTakeMessage(null,template);
		MessageParser.parseMessage(msg, listeners);
		assertTrue( fl.isTakeReceived() );
		assertEquals( fl.getSelector(), template );
	}
	
	public void testTakeURICreator() {
		final String uri = "ts://testuricreator/graph21";
		final Message msg = MessageParser.createTakeMessage(null,uri);
		
		checkSender(msg);
		
		MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.TAKE );
		
		msgElement = msg.getMessageElement(null,MessageParser.Properties.GRAPHURI);
		assertEquals( msgElement.toString(), uri.toString());
	}
	
	public void testTakeURIParser() throws MalformedMessageException {
		final FakeCallback fl = new FakeCallback();
		final Vector<ITSCallback> listeners = new Vector<ITSCallback>();
		listeners.addElement(fl);
		final String uri = "ts://testuricreator/graph21";
		final Message msg = MessageParser.createTakeMessage(null,uri);
		MessageParser.parseMessage(msg, listeners);
		assertTrue( fl.isTakeReceived() );
		assertEquals( fl.getGraphURI(), uri.toString() );
	}
	
	
	public void testAdvertiseCreator() throws MalformedTemplateException {
		final ITemplate template = new SemanticFactory().createTemplate("<"+ExampleME.subj1+"> ?p ?o .");
		final Message msg = MessageParser.createAdvertiseMessage(null,template);
		
		checkSender(msg);
		
		final MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.ADVERTISE );
		
		checkSelector(msg, "<"+ExampleME.subj1+"> ?p ?o .");
	}
	
	public void testAdvertiseParser() throws MalformedMessageException, MalformedTemplateException {
		final FakeCallback fl = new FakeCallback();
		final Vector<ITSCallback> listeners = new Vector<ITSCallback>();
		listeners.addElement(fl);
		final ITemplate template = new SemanticFactory().createTemplate("?s ?p <"+ExampleME.subj2+"> .");
		final Message msg = MessageParser.createAdvertiseMessage(null,template);
		MessageParser.parseMessage(msg, listeners);
		assertTrue( fl.isAdvertiseReceived() );
		assertEquals( fl.getSelector(), template );
	}
	
	public void testUnadvertiseCreator() {
		final String uri = "ts://testuricreator/adv68542";
		final Message msg = MessageParser.createUnadvertiseMessage(null,uri);
		
		checkSender(msg);
		
		MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.UNADVERTISE );
		
		msgElement = msg.getMessageElement(null,MessageParser.Properties.TEMPLATEURI);
		assertEquals( msgElement.toString(), uri.toString());
	}
	
	public void testUnadvertiseParser() throws MalformedMessageException {
		final FakeCallback fl = new FakeCallback();
		final Vector<ITSCallback> listeners = new Vector<ITSCallback>();
		listeners.addElement(fl);
		final String uri = "ts://testuricreator/adv24594";
		final Message msg = MessageParser.createUnadvertiseMessage(null,uri);
		MessageParser.parseMessage(msg, listeners);
		assertTrue( fl.isUnadvertiseReceived() );
		assertEquals( fl.getAdvsubsURI(), uri.toString() );
	}
	
	public void testSubscribeCreator() throws MalformedTemplateException {
		final ITemplate template = new SemanticFactory().createTemplate("<"+ExampleME.subj1+"> ?p <"+ExampleME.subj2+"> .");
		final Message msg = MessageParser.createSubscribeMessage(null,template);
		
		checkSender(msg);

		final MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.SUBSCRIBE );
		
		checkSelector(msg, "<"+ExampleME.subj1+"> ?p <"+ExampleME.subj2+"> .");
	}
	
	public void testSubscribeParser() throws MalformedMessageException, MalformedTemplateException {
		final FakeCallback fl = new FakeCallback();
		final Vector<ITSCallback> listeners = new Vector<ITSCallback>();
		listeners.addElement(fl);
		final ITemplate template = new SemanticFactory().createTemplate("<"+ExampleME.subj3+"> <"+ExampleME.prop1+"> ?o .");
		final Message msg = MessageParser.createSubscribeMessage(null,template);
		MessageParser.parseMessage(msg, listeners);
		assertTrue( fl.isSubscribeReceived() );
		assertEquals( fl.getSelector(), template );
	}
	
	public void testUnsubscribeCreator() {
		final String uri = "ts://testuricreator/subs24593";
		final Message msg = MessageParser.createUnsubscribeMessage(null,uri);
		
		checkSender(msg);
		
		MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.UNSUBSCRIBE );
		
		msgElement = msg.getMessageElement(null,MessageParser.Properties.TEMPLATEURI);
		assertEquals( msgElement.toString(), uri.toString());
	}
	
	public void testUnsubscribeParser() throws MalformedMessageException {
		final FakeCallback fl = new FakeCallback();
		final Vector<ITSCallback> listeners = new Vector<ITSCallback>();
		listeners.addElement(fl);
		final String uri = "ts://testuricreator/subs24594";
		final Message msg = MessageParser.createUnsubscribeMessage(null,uri);
		MessageParser.parseMessage(msg, listeners);
		assertTrue( fl.isUnsubscribeReceived() );
		assertEquals( fl.getAdvsubsURI(), uri.toString() );
	}
	
	public void testQueryMultipleCreator() throws MalformedTemplateException {
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
	
	public void testQueryMultipleParser() throws MalformedMessageException, MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final FakeCallback fl = new FakeCallback();
		final Vector<ITSCallback> listeners = new Vector<ITSCallback>();
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

	public void testDemandCreator() throws MalformedTemplateException {
		final ITemplate template = new SemanticFactory().createTemplate("?s ?p <"+ExampleME.subj1+"> .");
		final Message msg = MessageParser.createDemandMessage(null, template, 2000);
		
		checkSender(msg);
		checkSelector(msg, "?s ?p <"+ExampleME.subj1+"> .");
		
		MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.DEMAND );
		
		msgElement = msg.getMessageElement(null,MessageParser.Properties.LEASE_TIME);
		assertEquals( msgElement.toString(), "2000" );
	}
	
	public void testDemandParser() throws MalformedMessageException, MalformedTemplateException {
		final FakeCallback fl = new FakeCallback();
		final Vector<ITSCallback> listeners = new Vector<ITSCallback>();
		listeners.addElement(fl);
		final ITemplate template = new SemanticFactory().createTemplate("<"+ExampleME.subj2+"> ?p <"+ExampleME.subj1+"> .");
		final Message msg = MessageParser.createDemandMessage(null,template, 2000);
		MessageParser.parseMessage(msg,listeners);
		assertTrue( fl.isDemandReceived() );
		assertEquals( fl.getSelector(), template );
		assertEquals( fl.getLeaseTime(), 2000 );
	}

	public void testSuggestCreator() throws TripleParseException {
		final ISemanticFactory sf = new SemanticFactory();
		final IGraph graph = sf.createEmptyGraph();
		graph.add( factory.createTriple(ExampleME.subj1, ExampleME.prop1, ExampleME.obj3));
		graph.add(factory.createTriple(ExampleME.subj1, ExampleME.prop2, ExampleME.obj10));
		graph.add( factory.createTriple(ExampleME.subj1, ExampleME.prop3, ExampleME.obj7));
		final Message msg = MessageParser.createSuggestMessage(null, sf.createModelForGraph(graph));
		
		checkSender(msg);
		checkModel(msg, sf.createModelForGraph(graph));
		final MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.SUGGEST );

	}

	public void testSuggestParser() throws MalformedMessageException, TripleParseException {
		final ISemanticFactory sf = new SemanticFactory();
		final FakeCallback fl = new FakeCallback();
		final Vector<ITSCallback> listeners = new Vector<ITSCallback>();
		listeners.addElement(fl);
		final IGraph graph = sf.createEmptyGraph();
		graph.add( factory.createTriple(ExampleME.subj1, ExampleME.prop1, ExampleME.obj3));
		graph.add(factory.createTriple(ExampleME.subj1, ExampleME.prop2, ExampleME.obj10));
		graph.add( factory.createTriple(ExampleME.subj1, ExampleME.prop3, ExampleME.obj7));
		final Message msg = MessageParser.createSuggestMessage(null, sf.createModelForGraph(graph));
		MessageParser.parseMessage(msg,listeners);
		assertTrue( fl.isSuggestReceived() );
		final IGraph obtainedGraph = fl.getModel().getGraph().getIGraph();
		assertTrue( graph.containsAll(obtainedGraph) );
		assertTrue( obtainedGraph.containsAll(graph) );
	}
	
	public void testObtainDemandsCreator() {
		final Message msg = MessageParser.createObtainDemandsMessage(null);
		
		checkSender(msg);
		final MessageElement msgElement = msg.getMessageElement(null,MessageParser.Properties.REQUESTYPE);
		assertEquals( msgElement.toString(), MessageParser.TypeRequest.OBTAIN_DMNDS );
	}
	
	public void testObtainDemandsParser() throws MalformedMessageException {
		final FakeCallback fl = new FakeCallback();
		final Vector<ITSCallback> listeners = new Vector<ITSCallback>();
		listeners.addElement(fl);
		final Message msg = MessageParser.createObtainDemandsMessage(null);
		MessageParser.parseMessage(msg,listeners);
		assertTrue( fl.isObtainDemands() );
	}
	
	public void testResponseDemandsCreator() {
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
	
	public void testResponseDemandsParser() throws MalformedMessageException {
		final FakeCallback fl = new FakeCallback();
		final Vector<ITSCallback> listeners = new Vector<ITSCallback>();
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