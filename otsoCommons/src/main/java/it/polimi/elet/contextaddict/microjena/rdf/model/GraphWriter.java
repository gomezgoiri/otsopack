/*
 * GraphWriter.java
 *
 * Created on 21 gennaio 2008, 11.41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.polimi.elet.contextaddict.microjena.rdf.model;

import it.polimi.elet.contextaddict.microjena.datatypes.RDFDatatype;
import it.polimi.elet.contextaddict.microjena.datatypes.TypeMapper;
import it.polimi.elet.contextaddict.microjena.graph.Axiom;
import it.polimi.elet.contextaddict.microjena.graph.GraphUtil;
import it.polimi.elet.contextaddict.microjena.graph.Node;
import it.polimi.elet.contextaddict.microjena.graph.Triple;
import it.polimi.elet.contextaddict.microjena.graph.impl.LiteralLabel;
import it.polimi.elet.contextaddict.microjena.ontology.OntModel;
import it.polimi.elet.contextaddict.microjena.shared.InvalidPropertyURIException;
import it.polimi.elet.contextaddict.microjena.shared.SyntaxError;
import it.polimi.elet.contextaddict.microjena.util.iterator.ExtendedIterator;
import it.polimi.elet.contextaddict.microjena.vocabulary.OWL;
import it.polimi.elet.contextaddict.microjena.vocabulary.RDF;
import it.polimi.elet.contextaddict.microjena.vocabulary.RDFS;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author ilBuccia
 */
public class GraphWriter {
    
    private Model model;
    private int line, column;
    private boolean catchOwlAxioms;
    
    /** Creates a new instance of GraphWriter */
    public GraphWriter(Model m) {
	model=m;
	catchOwlAxioms = m instanceof OntModel;
    }
    
    public void readNTriple(InputStream is) throws IOException {
	line=1;
	column=1;
	Node subject, predicate, object;
	while(true) {
	    subject = readNode(is, false, false, true);
	    if(subject != null) {
		predicate = readNode(is, true, false, false);
		object = readNode(is, false, true, false);
		Triple addTriple = new Triple(subject, predicate, object);
		model.getGraph().add(addTriple);
		if(catchOwlAxioms)
		    catchAxioms(addTriple);
		closeLine(is);
	    }
	    else
		//end of file
		break;
	}
    }
    
    private Node readNode(InputStream is, boolean requestURI, boolean allowLiteral, boolean allowEndOfFile) throws IOException {
	int aus = nextValidChar(is, !allowEndOfFile);
	if(requestURI && aus != '<')
	    throw new InvalidPropertyURIException("");
	if(allowEndOfFile && aus == -1)
	    //no more triples
	    return null;
	else {
	    if(aus == '<') {
		//URI
		return Node.createURI(readURI(is));
	    }
	    else {
		if(aus == '_') {
		    if((aus = readChar(is, true)) != ':')
			throw new SyntaxError("Syntax error at line "+line+" position "+column+": expected \":\"");
		    if((aus = readChar(is, true)) != 'A') {
			return Node.createAnon(new AnonId(new String(new byte[] {Byte.parseByte(String.valueOf(aus))}).concat(parseString(is, ' '))));
		    }
		    else
			//Add an 'A' before the AnonId
			return Node.createAnon(new AnonId(parseString(is, ' ')));
		}
		else {
		    if( allowLiteral && aus == '"') {
			return Node.createLiteral(readLiteral(is));
		    }
		    else {
			throw new SyntaxError("Syntax error at line "+line+" position "+column+": unexpected input");
		    }
		}
	    }
	}
    }
    
    public void writeNTriple(OutputStream os) throws IOException {
	ExtendedIterator it = GraphUtil.findAll(model.getGraph());
	Triple t;
	Node subj, pred, obj;
	while(it.hasNext()) {
	    t = (Triple)it.next();
	    if(!(t instanceof Axiom)) {
		writeNode(os, t.getSubject());
		os.write((int)' ');
		writeNode(os, t.getPredicate());
		os.write((int)' ');
		writeNode(os, t.getObject());
		os.write((int)' ');
		os.write((int)'.');
		os.write((int)'\n');
	    }
	}
    }
    
    
    
    //PRIVATE IMPLEMENTATIONS
    
	//WRITER IMPLEMENTATION
    
    private void writeNode(OutputStream os, Node n) throws IOException {
	if(n.isURI()) {
	    writeURI(os, n.getURI());
	}
	else {
	    if(n.isBlank())
		os.write("_:A".concat(n.toString()).getBytes());
	    else {
		if(n.isLiteral()) {
		    writeLiteral(os, n);
		}
	    }
	}
    }
    
    private void writeURI(OutputStream os, String uri) throws IOException {
	os.write((int)'<');
	os.write(uri.getBytes());
	os.write((int)'>');
    }
    
    private void writeLiteral(OutputStream os, Node n) throws IOException {
	os.write((int)'"');
	os.write(n.getLiteralLexicalForm().getBytes());
	os.write((int)'"');
	String aus = n.getLiteralLanguage();
	if(aus != null) {
	    if(! aus.equals("")) {
			os.write((int)'@');
			os.write(aus.getBytes());
	    }
	}
	aus = n.getLiteralDatatypeURI();
	if(aus != null) {
	    if(! aus.equals("")) {
		os.write((int)'^');
		os.write((int)'^');
		writeURI(os, aus);
	    }
	}
    }
    
	//READER IMPLEMENTATION
    
    private String parseString(InputStream is, char term) throws IOException {
	StringBuffer result = new StringBuffer();
	int aus = readChar(is, true);
	while( (aus != -1) && (aus != (int)term)) {
	    result.append((char)aus);
	    aus = readChar(is, true);
	}
	return result.toString();
    }
    
    private int nextValidChar(InputStream is, boolean catchEndOfFile) throws IOException {
	int result;
	result = readChar(is, catchEndOfFile);
	while( result == ' ' || result == '\n')
	    result = readChar(is, catchEndOfFile);
	return result;
    }
    
    private String readURI(InputStream is) throws IOException {
	return parseString(is, '>');
    }
    
    /** returns the Literal read, and closes the line in the file
     *  Literals are supposed to be placed only as the Object of
     *  a Statement.
     *  The pointer of the InputStream is left at the fist char
     *  position of the next line (if there is one).
     */
    private LiteralLabel readLiteral(InputStream is) throws IOException {
	int aus;
	String lexical=null, lang=null, datatypeURI=null;
	lexical = parseString(is, '"');
	aus = readChar(is, true);
	if(aus == '@') {
	    StringBuffer langBuffer = new StringBuffer();
	    aus = readChar(is, true);
	    while( (aus != '^') && (aus!=' ') && (aus != '.') ) {
		langBuffer.append((char)aus);
		aus = readChar(is, true);
	    }
	    lang = langBuffer.toString();
	}
	if(aus == '^') {
	    if((aus = readChar(is, true)) != '^')
		throw new SyntaxError("Syntax error at line "+line+" position "+column+": expected \"^^\"");
	    if((aus = readChar(is, true)) != '<')
		throw new SyntaxError("Syntax error at line "+line+" position "+column+": expected \"^^<\"");
	    datatypeURI = readURI(is);
	}

	//parameters are caught. 'lang' and 'datatypeURI' are allowed to be NULL
	//reader ignores LANG when Literal has a datatype
	if(datatypeURI == null && lang != null)
	    return new LiteralLabel(lexical, lang, false);
	else
	    if(datatypeURI != null) {
	    	RDFDatatype rdt = TypeMapper.getInstance().getSafeTypeByName(datatypeURI);
			//in this case lang is ignored
			return new LiteralLabel(lexical, null, rdt);
	    } else
		//both lang and datatype were NULL
		return new LiteralLabel(lexical);
    }
    
    private void closeLine(InputStream is) throws IOException {
	//catching new line or end of file
	if(!catchRequiredChar(is, '.'))
	    throw new SyntaxError("Syntax error at line "+line+" position "+column+": expected \".\"");
    }
    
    private boolean catchRequiredChar(InputStream is, char find) throws IOException {
	int aus = nextValidChar(is, false);
	return (char)aus == find;
    }
    
    private int readChar(InputStream is, boolean catchEndOfFile) throws IOException {
	int aus = is.read();
	if(aus != -1) {
	    if((char)aus == '\n') {
		line++;
		column = 1;
	    }
	    else {
		column++;
	    }
	}
	else {
	    if(catchEndOfFile)
		throw new SyntaxError("Syntax error at line "+line+" position "+column+": premature end of file");
	}
	return aus;
    }
    
    public void catchAxioms(Triple t) {
	Node subject = t.getSubject();
	if(t.matches(Node.ANY, RDF.first.asNode(), Node.ANY)) {
	    //resource is a list element
	    addResourceAxiom(subject);
	    addAxiom(subject, RDF.type.asNode(), RDF.List.asNode());
	}
	else {
	    if(t.matches(Node.ANY, RDF.type.asNode(), RDF.Property.asNode())) {
		//resource is an OntProperty
		addResourceAxiom(subject);
		addAxiom(subject, RDFS.subPropertyOf.asNode(), subject);
	    }
	    else {
		//resource is a class
		if(t.matches(Node.ANY, RDF.type.asNode(), OWL.Class.asNode())) {
		    addResourceAxiom(subject);
		    addClassAxiom(subject);
		    addAxiom(subject, RDFS.subClassOf.asNode(), subject);
		}
	    }
	}
    }
    
    private void addAxiom(Node subject, Node predicate, Node object) {
	model.getGraph().add(new Axiom(subject, predicate, object));	
    }
    
    private void addResourceAxiom(Node value) {
	model.getGraph().add(new Axiom(value, RDF.type.asNode(), RDFS.Resource.asNode()));
    }

    private void addClassAxiom(Node value) {
	model.getGraph().add(new Axiom(value, RDF.type.asNode(), RDFS.Class.asNode()));
    }        

}
