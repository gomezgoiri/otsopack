/*
    (c) Copyright 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
    [See end of file]
    $Id: ModelCom.java,v 1.117 2007/01/02 11:48:30 andy_seaborne Exp $
 */

package it.polimi.elet.contextaddict.microjena.rdf.model.impl;

import it.polimi.elet.contextaddict.microjena.datatypes.DatatypeFormatException;
import it.polimi.elet.contextaddict.microjena.datatypes.RDFDatatype;
import it.polimi.elet.contextaddict.microjena.datatypes.TypeMapper;
import it.polimi.elet.contextaddict.microjena.enhanced.EnhGraph;
import it.polimi.elet.contextaddict.microjena.enhanced.EnhNode;
import it.polimi.elet.contextaddict.microjena.graph.Graph;
import it.polimi.elet.contextaddict.microjena.graph.GraphUtil;
import it.polimi.elet.contextaddict.microjena.graph.Node;
import it.polimi.elet.contextaddict.microjena.graph.Triple;
import it.polimi.elet.contextaddict.microjena.graph.impl.LiteralLabel;
import it.polimi.elet.contextaddict.microjena.ontology.ConversionException;
import it.polimi.elet.contextaddict.microjena.rdf.model.Alt;
import it.polimi.elet.contextaddict.microjena.rdf.model.AnonId;
import it.polimi.elet.contextaddict.microjena.rdf.model.Bag;
import it.polimi.elet.contextaddict.microjena.rdf.model.Container;
import it.polimi.elet.contextaddict.microjena.rdf.model.GraphWriter;
import it.polimi.elet.contextaddict.microjena.rdf.model.Literal;
import it.polimi.elet.contextaddict.microjena.rdf.model.Model;
import it.polimi.elet.contextaddict.microjena.rdf.model.ModelFactory;
import it.polimi.elet.contextaddict.microjena.rdf.model.NodeIterator;
import it.polimi.elet.contextaddict.microjena.rdf.model.NsIterator;
import it.polimi.elet.contextaddict.microjena.rdf.model.Property;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFList;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFNode;
import it.polimi.elet.contextaddict.microjena.rdf.model.RSIterator;
import it.polimi.elet.contextaddict.microjena.rdf.model.ReifiedStatement;
import it.polimi.elet.contextaddict.microjena.rdf.model.ResIterator;
import it.polimi.elet.contextaddict.microjena.rdf.model.Resource;
import it.polimi.elet.contextaddict.microjena.rdf.model.ResourceF;
import it.polimi.elet.contextaddict.microjena.rdf.model.Selector;
import it.polimi.elet.contextaddict.microjena.rdf.model.Seq;
import it.polimi.elet.contextaddict.microjena.rdf.model.Statement;
import it.polimi.elet.contextaddict.microjena.rdf.model.StmtIterator;
import it.polimi.elet.contextaddict.microjena.shared.InvalidPropertyURIException;
import it.polimi.elet.contextaddict.microjena.shared.JenaException;
import it.polimi.elet.contextaddict.microjena.shared.PrefixMapping;
import it.polimi.elet.contextaddict.microjena.shared.PropertyNotFoundException;
import it.polimi.elet.contextaddict.microjena.shared.ReificationStyle;
import it.polimi.elet.contextaddict.microjena.shared.impl.PrefixMappingImpl;
import it.polimi.elet.contextaddict.microjena.util.Iterator;
import it.polimi.elet.contextaddict.microjena.util.List;
import it.polimi.elet.contextaddict.microjena.util.Map;
import it.polimi.elet.contextaddict.microjena.util.Set;
import it.polimi.elet.contextaddict.microjena.util.Util;
import it.polimi.elet.contextaddict.microjena.util.iterator.ClosableIterator;
import it.polimi.elet.contextaddict.microjena.util.iterator.ExtendedIterator;
import it.polimi.elet.contextaddict.microjena.util.iterator.IteratorImpl;
import it.polimi.elet.contextaddict.microjena.util.iterator.WrappedIterator;
import it.polimi.elet.contextaddict.microjena.vocabulary.RDF;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Vector;

/** Common methods for model implementations.
 *
 * <P>This class implements common methods, mainly convenience methods, for
 *    model implementations.  It is intended use is as a base class from which
 *    model implemenations can be derived.</P>
 *
 * @author bwm
 * hacked by Jeremy, tweaked by Chris (May 2002 - October 2002)
 */

public class ModelCom extends EnhGraph implements Model, PrefixMapping {
    
    //class that implements input and output of the Model
    GraphWriter graphWriter;
    
    public ModelCom( Graph base) {
	this(base, false);
    }
    
    public ModelCom( Graph base, boolean excludeAxioms ) {
	super( base );
	withDefaultMappings( defaultPrefixMapping );
	graphWriter = new GraphWriter(this);
	if(!excludeAxioms)
	    RDFAxiomWriter.writeAxioms(getGraph());
    }
    
    private static PrefixMapping defaultPrefixMapping = PrefixMapping.Factory.create();
    
    public static PrefixMapping getDefaultModelPrefixes() {
	return defaultPrefixMapping;
    }
    
    public static PrefixMapping setDefaultModelPrefixes( PrefixMapping pm ) {
	PrefixMapping result = defaultPrefixMapping;
	defaultPrefixMapping = pm;
	return result;
    }
    
    public Graph getGraph() {
	//graph punta al grafo (ed � dichiarato in enhGraph)
	return graph;
    }
    
    protected static Model createWorkModel() {
	return ModelFactory.createDefaultModel();
    }
    
    public RDFNode asRDFNode( Node n ) {
	return new ResourceImpl(n,this);
    }
    
    /**
     * the ModelReifier does everything to do with reification.
     */
    protected ModelReifier modelReifier = new ModelReifier( this );
    
    public Resource getResource(String uri, ResourceF f)  {
	try {
	    return f.createResource(getResource(uri));
	} catch (Exception e) {
	    throw new JenaException(e);
	}
    }
    
    public Model add(Resource s, Property p, boolean o)  {
	return add(s, p, String.valueOf( o ) );
    }
    
    public Model add(Resource s, Property p, long o)  {
	return add(s, p, String.valueOf( o ) );
    }
    
    public Model add(Resource s, Property p, char o)  {
	return add(s, p, String.valueOf( o ) );
    }
    
    public Model add(Resource s, Property p, float o)  {
	return add(s, p, String.valueOf( o ) );
    }
    
    public Model add(Resource s, Property p, double o)  {
	return add(s, p, String.valueOf( o ) );
    }
    
    public Model add(Resource s, Property p, String o)  {
	return add( s, p, o, "", false );
    }
    
    public Model add(Resource s, Property p, String o, boolean wellFormed) {
	add( s, p, literal( o, "", wellFormed ) );
	return this;
    }
    
    public Model add( Resource s, Property p, String o, String lang, boolean wellFormed)  {
	add( s, p, literal( o, lang, wellFormed ) );
	return this;
    }
    
    public Model add(Resource s, Property p, String lex, RDFDatatype datatype) {
	add( s, p, literal( lex, datatype)) ;
	return this;
    }
    
    private Literal literal( String s, String lang, boolean wellFormed ) {
	return new LiteralImpl( Node.createLiteral( s, lang, wellFormed), this ); }
    
    private Literal literal( String lex, RDFDatatype datatype) {
	return new LiteralImpl( Node.createLiteral( lex, "", datatype), this ); }
    
    public Model add( Resource s, Property p, String o, String l ) {
	return add( s, p, o, l, false ); }
    
    /**
     * ensure that an object is an RDFNode. If it isn't, fabricate a literal
     * from its string representation. NOTE: probably proper data-typing
     * makes this suspect - Chris introduced it to abstract from some existing code.
     */
    private RDFNode ensureRDFNode( Object o ) {
	return o instanceof RDFNode
		? (RDFNode) o
		: literal( o.toString(), null, false )
		;
    }
    
    public Model add(Resource s, Property p, Object o)  {
	return add( s, p, ensureRDFNode( o ) );
    }
    
    public Model add( StmtIterator iter )  {
	try {
	    while(iter.hasNext())
		this.add(iter.nextStatement());
	} finally {
	    iter.close();
	}
	return this;
    }
    
    public Model add( Model m ) {
	return add(m.listStatements());
    }
 
    public Model read(InputStream reader, String lang) {
	return read(reader, null, lang);
    }
    
    public Model read(InputStream reader, String base, String lang) {
	//base is ignored until an implementation of an RDF reader comes
	if(lang.equals("N-TRIPLE")) {
	    try {
		graphWriter.readNTriple(reader);
	    } catch (IOException ex) {
		throw new JenaException("An error occoured while reading the model");
	    }
	    return this;
	} else {
	    throw new JenaException("Language " + lang + "is not implemented for input");
	}
    }

    public Model write(OutputStream writer, String lang) {
	if(lang.equals("N-TRIPLE")) {
	    try {
		graphWriter.writeNTriple(writer);
	    } catch (IOException ex) {
		throw new JenaException("An error occoured while writing the model");
	    }
	    return this;
	} else {
	    throw new JenaException("Language " + lang + "is not implemented for output");
	}
    }

    public Model remove(Statement s)  {
	return remove(s.getSubject(), s.getPredicate(), s.getObject());
    }
    
    public Model remove( Resource s, Property p, RDFNode o ) {
	return remove(s, p, o, false);
    }
    
    public Model remove( Resource s, Property p, RDFNode o, boolean removeAxiom ) {
	graph.delete( Triple.create( s.asNode(), p.asNode(), o.asNode() ), removeAxiom );
	return this;
    }
    
    public Model remove( StmtIterator iter ) {
	while(iter.hasNext())
	    remove(iter.nextStatement());
	return this;
    }
    
    public Model remove( Model m ) {
	return remove(m.listStatements());
    }
    
    public Model removeAll() {
	removeAll(Node.ANY, Node.ANY, Node.ANY);
	return this;
    }
    
    public Model removeAll( Resource s, Property p, RDFNode o ) {
	removeAll(asNode(s), asNode(p), asNode(o));
	return this;
    }
    
    private Model removeAll( Node s, Node p, Node o ) {
	ExtendedIterator it = getGraph().find(s, p, o);
	while(it.hasNext())
	    getGraph().delete((Triple)it.next());
	return this;
    }
    
    public boolean contains( Resource s, Property p, boolean o ) {
	return contains(s, p, String.valueOf( o ) );
    }
    
    public boolean contains( Resource s, Property p, long o ) {
	return contains(s, p, String.valueOf( o ) ); 
    }
    
    public boolean contains( Resource s, Property p, char o ) {
	return contains(s, p, String.valueOf( o ) );
    }
    
    public boolean contains( Resource s, Property p, float o ) {
	return contains(s, p, String.valueOf( o ) );
    }
    
    public boolean contains( Resource s, Property p, double o ) {
	return contains(s, p, String.valueOf( o ) );
    }
    
    public boolean contains( Resource s, Property p, String o ) {
	return contains( s, p, o, "" );
    }
    
    public boolean contains( Resource s, Property p, String o, String l ) {
	return contains( s, p, literal( o, l, false ) );
    }
    
    public boolean contains(Resource s, Property p, Object o) {
	return contains( s, p, ensureRDFNode( o ) );
    }
    
    public boolean containsAny( Model model ) {
	return containsAnyThenClose( model.listStatements() );
    }
    
    public boolean containsAll( Model model ) {
	return containsAllThenClose( model.listStatements() );
    }
    
    protected boolean containsAnyThenClose( StmtIterator iter ) {
	try {
	    return containsAny( iter );
	} finally {
	    iter.close();
	}
    }
    
    protected boolean containsAllThenClose( StmtIterator iter ) {
	try {
	    return containsAll( iter );
	} finally {
	    iter.close();
	}
    }
    
    public boolean containsAny( StmtIterator iter ) {
	while (iter.hasNext()) if (contains(iter.nextStatement())) return true;
	return false;
    }
    
    public boolean containsAll( StmtIterator iter ) {
	while (iter.hasNext()) if (!contains(iter.nextStatement())) return false;
	return true;
    }
    
    protected StmtIterator listStatements( Resource S, Property P, Node O ) {
	return IteratorCast.asStmtIterator(graph.find( asNode( S ), asNode( P ), O ), this);
    }
    
    public StmtIterator listStatements( Resource S, Property P, RDFNode O ) {
	return listStatements( S, P, asNode( O ) );
    }
    
    public StmtIterator listStatements( Resource S, Property P, String O ) {
	return O == null
		? listStatements(S, P, Node.ANY)
		: listStatements( S, P, Node.createLiteral( O ) );
    }
    
    public StmtIterator listStatements( Resource S, Property P, String O, String L ) {
	return O == null
		? listStatements(S, P, Node.ANY)
		: listStatements( S, P, Node.createLiteral( O, L, false ) );
    }
    
    public StmtIterator listStatements( Resource S, Property P, boolean O ) {
	return listStatements( S, P, String.valueOf( O ) );
    }
    
    public StmtIterator listStatements( Resource S, Property P, long O ) {
	return listStatements( S, P, String.valueOf( O ) );
    }
    
    public StmtIterator listStatements( Resource S, Property P, char  O ) {
	return listStatements( S, P, String.valueOf( O ) ); 
    }
    
    public StmtIterator listStatements( Resource S, Property P, float O ) {
	return listStatements( S, P, String.valueOf( O ) ); 
    }
    
    public StmtIterator listStatements( Resource S, Property P, double  O ) {
	return listStatements( S, P, String.valueOf( O ) ); 
    }
    
    public ResIterator listSubjectsWithProperty( Property p, boolean o ) {
	return listSubjectsWithProperty(p, String.valueOf( o ) ); 
    }
    
    public ResIterator listSubjectsWithProperty( Property p, long o ) {
	return listSubjectsWithProperty(p, String.valueOf( o ) ); 
    }
    
    public ResIterator listSubjectsWithProperty( Property p, char o ) {
	return listSubjectsWithProperty(p, String.valueOf( o ) ); 
    }
    
    public ResIterator listSubjectsWithProperty( Property p, float o ) {
	return listSubjectsWithProperty(p, String.valueOf( o ) ); 
    }
    
    public ResIterator listSubjectsWithProperty( Property p, double o ) {
	return listSubjectsWithProperty(p, String.valueOf( o ) ); 
    }
    
    public ResIterator listSubjectsWithProperty( Property p, String o ) {
	return listSubjectsWithProperty( p, o, "" ); 
    }
    
    public ResIterator listSubjectsWithProperty( Property p, String o, String l ) {
	return listSubjectsWithProperty(p, literal( o, l, false ) ); 
    }
    
    public ResIterator listSubjectsWithProperty( Property p, Object o ) {
	return listSubjectsWithProperty( p, ensureRDFNode( o ) ); 
    }
    
    public Resource createResource( Resource type ) {
	return createResource().addProperty( RDF.type, type ); 
    }
    
    public Resource createResource( String uri, Resource type ) {
	return getResource( uri ).addProperty( RDF.type, type ); 
    }
    
    public Resource createResource( ResourceF f ) {
	return createResource( null, f ); 
    }
    
    public Resource createResource( AnonId id ) {
	return new ResourceImpl( id, this ); 
    }
    
    public Resource createResource( String uri, ResourceF f ) {
	return f.createResource( createResource( uri ) ); 
    }
    
    
    /** create a type literal from a boolean value.
     *
     * <p> The value is converted to a string using its <CODE>toString</CODE>
     * method. </p>
     * @param v the value of the literal
     *
     * @return a new literal representing the value v
     */
    public Literal createTypedLiteral( boolean v )  {
	return createTypedLiteral( new Boolean( v ) );
    }
    
    /** create a typed literal from an integer value.
     *
     * @param v the value of the literal
     *
     * @return a new literal representing the value v
     */
    public Literal createTypedLiteral(int v)   {
	return createTypedLiteral(new Integer(v));
    }
    
    /** create a typed literal from a long integer value.
     *
     * @param v the value of the literal
     *
     * @return a new literal representing the value v
     */
    public Literal createTypedLiteral(long v)   {
	return createTypedLiteral(new Long(v));
    }
    
    /** create a typed literal from a char value.
     *
     * @param v the value of the literal
     *
     * @return a new literal representing the value v
     */
    public Literal createTypedLiteral(char v)  {
	return createTypedLiteral(new Character(v));
    }
    
    /** create a typed literal from a float value.
     *
     * @param v the value of the literal
     *
     * @return a new literal representing the value v
     */
    public Literal createTypedLiteral(float v)  {
	return createTypedLiteral(new Float(v));
    }
    
    /** create a typed literal from a double value.
     *
     * @param v the value of the literal
     *
     * @return a new literal representing the value v
     */
    public Literal createTypedLiteral(double v)  {
	return createTypedLiteral(new Double(v));
    }
    
    /** create a typed literal from a String value.
     *
     * @param v the value of the literal
     *
     * @return a new literal representing the value v
     */
    public Literal createTypedLiteral(String v)  {
	LiteralLabel ll = new LiteralLabel(v);
	return new LiteralImpl(Node.createLiteral(ll), this);
    }
    
    /**
     * Build a typed literal from its lexical form. The
     * lexical form will be parsed now and the value stored. If
     * the form is not legal this will throw an exception.
     *
     * @param lex the lexical form of the literal
     * @param dtype the type of the literal, null for old style "plain" literals
     * @throws DatatypeFormatException if lex is not a legal form of dtype
     */
    public Literal createTypedLiteral(String lex, RDFDatatype dtype) throws DatatypeFormatException {
	return new LiteralImpl( Node.createLiteral( lex, "", dtype ), this);
    }
    
    /**
     * Build a typed literal from its value form.
     *
     * @param value the value of the literal
     * @param dtype the type of the literal, null for old style "plain" literals
     */
    public Literal createTypedLiteral(Object value, RDFDatatype dtype) {
	LiteralLabel ll = new LiteralLabel(value, "", dtype);
	return new LiteralImpl( Node.createLiteral(ll), this );
    }
    
    /**
     * Build a typed literal from its lexical form. The
     * lexical form will be parsed now and the value stored. If
     * the form is not legal this will throw an exception.
     *
     * @param lex the lexical form of the literal
     * @param typeURI the uri of the type of the literal, null for old style "plain" literals
     * @throws DatatypeFormatException if lex is not a legal form of dtype
     */
    public Literal createTypedLiteral(String lex, String typeURI)  {
	RDFDatatype dt = TypeMapper.getInstance().getSafeTypeByName(typeURI);
	LiteralLabel ll = LiteralLabel.createLiteralLabel( lex, "", dt );
	return new LiteralImpl( Node.createLiteral(ll), this );
    }
    
    /**
     * Build a typed literal from its value form.
     *
     * @param value the value of the literal
     * @param typeURI the URI of the type of the literal, null for old style "plain" literals
     */
    public Literal createTypedLiteral(Object value, String typeURI) {
	RDFDatatype dt = TypeMapper.getInstance().getSafeTypeByName(typeURI);
	LiteralLabel ll = new LiteralLabel(value, "", dt);
	return new LiteralImpl(Node.createLiteral(ll), this);
    }
    
    /**
     * Build a typed literal label from its value form using
     * whatever datatype is currently registered as the the default
     * representation for this java class. No language tag is supplied.
     * @param value the literal value to encapsulate
     */
    public Literal createTypedLiteral( Object value ) {
	// Catch special case of a Calendar which we want to act as if it were an XSDDateTime
	if (value instanceof Calendar)
	    return createTypedLiteral( (Calendar)value );
	LiteralLabel ll = new LiteralLabel( value );
	return new LiteralImpl( Node.createLiteral( ll ), this);
    }
    
    public Literal createLiteral( boolean v ) {
	return createLiteral( String.valueOf( v ), "" ); 
    }
    
    public Literal createLiteral( int v ) {
	return createLiteral( String.valueOf( v ), "" ); 
    }
    
    public Literal createLiteral( long v ) {
	return createLiteral( String.valueOf( v ), "" ); 
    }
    
    public Literal createLiteral( char v ) {
	return createLiteral( String.valueOf( v ), "" ); 
    }
    
    public Literal createLiteral( float v ) {
	return createLiteral( String.valueOf( v ), "" ); 
    }
    
    public Literal createLiteral( double v ) {
	return createLiteral( String.valueOf( v ), "" );
    }
    
    public Literal createLiteral( String v ) {
	return createLiteral( v, "" ); 
    }
    
    public Literal createLiteral( String v, String l ) {
	return literal( v, l, false ); 
    }
    
    public Literal createLiteral( String v, boolean wellFormed ) {
	return literal( v, "", wellFormed ); 
    } 
    
    public Literal createLiteral(String v, String l, boolean wellFormed) {
	return literal( v, l, wellFormed ); 
    }
    
    public Literal createLiteral( Object v ) {
	return createLiteral( v.toString(), "" ); 
    }
    
    public Statement createStatement( Resource r, Property p, boolean o ) {
	return createStatement( r, p, createLiteral( o ) ); 
    }
    
    public Statement createStatement( Resource r, Property p, long o ) {
	return createStatement( r, p, createLiteral( o ) ); 
    }
    
    public Statement createStatement( Resource r, Property p, char o ) {
	return createStatement( r, p, createLiteral( o ) ); 
    }
    
    public Statement createStatement( Resource r, Property p, float o ) {
	return createStatement( r, p, createLiteral( o ) ); 
    }
    
    public Statement createStatement( Resource r, Property p, double o ) {
	return createStatement( r, p, createLiteral( o ) ); 
    }
    
    public Statement createStatement( Resource r, Property p, String o ) {
	return createStatement( r, p, createLiteral( o ) ); 
    }
    
    public Statement createStatement(Resource r, Property p, Object o) {
	return createStatement( r, p, ensureRDFNode( o ) ); 
    }
    
    public Statement createStatement( Resource r, Property p, String o, boolean wellFormed ) {
	return createStatement( r, p, o, "", wellFormed ); 
    }
    
    public Statement createStatement(Resource r, Property p, String o, String l) {
	return createStatement( r, p, o, l, false ); 
    }
    
    public Statement createStatement( Resource r, Property p, String o, String l, boolean wellFormed ) {
	return createStatement( r, p, literal( o, l, wellFormed ) ); 
    }
    
    public Bag createBag() {
	return createBag( null ); 
    }
    
    public Alt createAlt() {
	return createAlt( null ); 
    }
    
    public Seq createSeq() {
	return createSeq( null ); 
    }
    
    //static method to create brand new list
    private RDFList createNewList() {
	return new RDFListImpl(Node.createURI(RDF.nil.getURI()), this);
    }
    
    //static method to create brand new list
    private RDFList createNewList(Resource res) {
	return new RDFListImpl(res.asNode(), this);
    }
    
    /**
     * Answer a (the) new empty list
     * @return An RDF-encoded list of no elements (ie nil)
     */
    public RDFList createList() {
	return createNewList();
    }
    
    /**
     * <p>Answer a new list containing the resources from the given iterator, in order.</p>
     * @param members An iterator, each value of which is expected to be an RDFNode.
     * @return An RDF-encoded list of the elements of the iterator
     */
    public RDFList createList( Iterator members ) {
	RDFList list = createList();
	
	while (members != null && members.hasNext()) {
	    list = list.with( (RDFNode) members.next() );
	}
	
	return list;
    }
    
    
    /**
     * <p>Answer a new list containing the RDF nodes from the given array, in order</p>
     * @param members An array of RDFNodes that will be the members of the list
     * @return An RDF-encoded list
     */
    public RDFList createList( RDFNode[] members ) {
	Vector result = new Vector(10,10);
	for(int i=0; i<members.length; i++)
	    result.addElement(members[i]);
	return createList(new IteratorImpl(result));
    }
    
    private Resource getResourceByUri(String uri) {
	Node result = Node.createURI(uri);
	return new ResourceImpl(result, this);
    }
    
    public RDFNode getRDFNode( Node n ) {
	return asRDFNode( n );
    }
    
    public Resource getResource( String uri ) {
	Resource catched = getResourceByUri(uri);
	return catched;
    }
    
    public Property getProperty( String uri ) {
	if (uri == null)
	    throw new InvalidPropertyURIException( null );
	Resource aus = getResourceByUri(uri);
	if(PropertyImpl.factory.canWrap(aus.asNode(),this))
	    return new PropertyImpl(aus.asNode(), this);
	else
	    throw new ConversionException("Cannot convert node " + aus + " to OntProperty");
    }
    
    public Alt getAlt( String uri ) {
	return getAlt(getResourceByUri(uri));
    }
    
    public Bag getBag( String uri ) {
	return getBag(getResourceByUri(uri));
    }
    
    public Seq getSeq( String uri ) {
	return getSeq(getResourceByUri(uri));
    }
    
    public Property getProperty( String nameSpace,String localName ) {
	return getProperty( nameSpace + localName );
    }
    
    public Seq getSeq( Resource r ) {
	if(SeqImpl.factory.canWrap(r.asNode(),this)) {
	    return (Seq)SeqImpl.factory.wrap(r.asNode(),this);
	}
	else {
	    throw new InvalidCastException("Seq", "Resource");
	}
    }

    public Bag getBag( Resource r ) {
	if(BagImpl.factory.canWrap(r.asNode(),this)) {
	    return (Bag)BagImpl.factory.wrap(r.asNode(),this);
	}
	else {
	    throw new InvalidCastException("Bag", "Resource");
	}
    }
    
    static private Node makeURI(String uri) {
	return uri == null
		? Node.createAnon()
		: Node.createURI( uri );
    }
    
    public Alt getAlt( Resource r ) {
	if(AltImpl.factory.canWrap(r.asNode(),this)) {
	    return (Alt)AltImpl.factory.wrap(r.asNode(),this);
	}
	else {
	    throw new InvalidCastException("Alt", "Resource");
	}
    }
    
    public long size() {
	return graph.size();
    }
    
    public boolean isEmpty() {
	return graph.isEmpty();
    }
    
    private void updateNamespace( Set set, Iterator it ) {
	while (it.hasNext()) {
	    Node node = (Node) it.next();
	    if (node.isURI()) {
		String uri = node.getURI();
		String ns = uri.substring( 0, Util.splitNamespace( uri ) );
		// String ns = IteratorFactory.asResource( node, this ).getNameSpace();
		set.add( ns );
	    }
	}
    }
    
    private Iterator listPredicates() {
	Set predicates = new Set();
	ClosableIterator it = graph.find( Triple.ANY );
	while (it.hasNext())
	    predicates.add( ((Triple) it.next()).getPredicate() );
	return predicates.iterator();
    }
    
    private Iterator listTypes() {
	Set types = new Set();
	ClosableIterator it = graph.find( null, RDF.type.asNode(), null );
	while (it.hasNext()) types.add( ((Triple) it.next()).getObject() );
	return types.iterator();
    }
    
    public NsIterator listNameSpaces()  {
	Set nameSpaces = new Set();
	updateNamespace( nameSpaces, listPredicates() );
	updateNamespace( nameSpaces, listTypes() );
	return new NsIteratorImpl(nameSpaces.iterator(), nameSpaces);
    }
    
    private PrefixMapping getPrefixMapping() {
	return getGraph().getPrefixMapping(); }
    
    public boolean samePrefixMappingAs( PrefixMapping other ) {
	return getPrefixMapping().samePrefixMappingAs( other ); }
    
    public PrefixMapping lock() {
	getPrefixMapping().lock();
	return this;
    }
    
    public PrefixMapping setNsPrefix( String prefix, String uri ) {
	getPrefixMapping().setNsPrefix( prefix, uri );
	return this;
    }
    
    public PrefixMapping removeNsPrefix( String prefix ) {
	getPrefixMapping().removeNsPrefix( prefix );
	return this;
    }
    
    public PrefixMapping setNsPrefixes( PrefixMapping pm ) {
	getPrefixMapping().setNsPrefixes( pm );
	return this;
    }
    
    public PrefixMapping setNsPrefixes( Map map ) {
	getPrefixMapping().setNsPrefixes( map );
	return this;
    }
    
    public PrefixMapping withDefaultMappings( PrefixMapping other ) {
	getPrefixMapping().withDefaultMappings( other );
	return this;
    }
    
    public String getNsPrefixURI( String prefix ) {
	return getPrefixMapping().getNsPrefixURI( prefix );
    }
    
    public String getNsURIPrefix( String uri ) {
	return getPrefixMapping().getNsURIPrefix( uri ); 
    }
    
    public Map getNsPrefixMap() {
	return getPrefixMapping().getNsPrefixMap();
    }
    
    public String expandPrefix( String prefixed ) {
	return getPrefixMapping().expandPrefix( prefixed ); 
    }
    
    public String usePrefix( String uri ) {
	return getPrefixMapping().shortForm( uri ); 
    }
    
    public String qnameFor( String uri ) {
	return getPrefixMapping().qnameFor( uri ); 
    }
    
    public String shortForm( String uri ) {
	return getPrefixMapping().shortForm( uri ); 
    }
    
    /**
     * Service method to update the namespaces of  a Model given the
     * mappings from prefix names to sets of URIs.
     *
     * If the prefix maps to multiple URIs, then we discard it completely.
     *
     * @param the Model who's namespace is to be updated
     * @param ns the namespace map to add to the Model
     */
    public static void addNamespaces( Model m, Map ns ) {
	PrefixMapping pm = m;
	Iterator it  = ns.entrySet().iterator();
	while (it.hasNext()) {
	    Map.Entry e = (Map.Entry) it.next();
	    String key = (String) e.getKey();
	    Set  values = (Set) e.getValue();
	    Set niceValues = new Set();
	    Iterator them = values.iterator();
	    while (them.hasNext()) {
		String uri = (String) them.next();
		if (PrefixMappingImpl.isNiceURI( uri ))
		    niceValues.add( uri );
	    }
	    if (niceValues.size() == 1)
		pm.setNsPrefix( key, (String) niceValues.iterator().next() );
	}
    }
    
    public StmtIterator listStatements() {
	return IteratorCast.asStmtIterator(GraphUtil.findAll(graph), this);
    }
    
    /**
     * add a Statement to this Model by adding its SPO components.
     */
    public Model add( Statement s ) {
	add( s.getSubject(), s.getPredicate(), s.getObject() );
	return this;
    }
    
    /**
     * Add all the statements to the model by converting them to an array of corresponding
     * triples and removing those from the underlying graph.
     */
    public Model add( Statement [] statements ) {
	int i = 0;
	for(i=0;i<statements.length;i++) {
	    add(statements[i]);
	}
	return this;
    }
    
    /**
     * Add all the statements to the model by converting the list to an array of
     * Statement and removing that.
     */
    public Model add( List statements ) {
	ExtendedIterator it = WrappedIterator.create(statements.iterator());
	while(it.hasNext()) {
	    add((Statement)it.next());
	}
	return this;
    }
    
    /**
     * remove all the Statements from the model by converting them to triples and
     * removing those triples from the underlying graph.
     */
    public Model remove( Statement [] statements ) {
	for(int i=0; i<statements.length;i++)
	    remove(statements[i]);
	return this;
    }
    
    /**
     * Remove all the Statements from the model by converting the List to a
     * List(Statement) and removing that.
     */
    public Model remove( List statements ) {
	ExtendedIterator it = WrappedIterator.create(statements.iterator());
	while(it.hasNext()) {
	    remove((Statement)it.next());
	}
	return this;
    }
    
    public Model add( Resource s, Property p, RDFNode o )  {
	modelReifier.noteIfReified( s, p, o );
	graph.add( Triple.create( s.asNode(), p.asNode(), o.asNode() ) );
	return this;
    }
    
    public ReificationStyle getReificationStyle() {
	return modelReifier.getReificationStyle(); }
    
    /**
     * @return an iterator which delivers all the ReifiedStatements in this model
     */
    public RSIterator listReifiedStatements() {
	return modelReifier.listReifiedStatements(); }
    
    /**
     * @return an iterator each of whose elements is a ReifiedStatement in this
     * model such that it's getStatement().equals( st )
     */
    public RSIterator listReifiedStatements( Statement st ) {
	return modelReifier.listReifiedStatements( st ); }
    
    /**
     * @return true iff this model has a reification of _s_ in some Statement
     */
    public boolean isReified( Statement s ) {
	return modelReifier.isReified( s ); }
    
    /**
     * get any reification of the given statement in this model; make
     * one if necessary.
     *
     * @param s for which a reification is sought
     * @return a ReifiedStatement that reifies _s_
     */
    public Resource getAnyReifiedStatement(Statement s) {
	return modelReifier.getAnyReifiedStatement( s ); }
    
    /**
     * remove any ReifiedStatements reifying the given statement
     * @param s the statement who's reifications are to be discarded
     */
    public void removeAllReifications( Statement s ) {
	modelReifier.removeAllReifications( s ); }
    
    public void removeReification( ReifiedStatement rs ) {
	modelReifier.removeReification( rs ); }
    
    /**
     * create a ReifiedStatement that encodes _s_ and belongs to this Model.
     */
    public ReifiedStatement createReifiedStatement( Statement s ) {
	return modelReifier.createReifiedStatement( s ); }
    
    public ReifiedStatement createReifiedStatement( String uri, Statement s ) {
	return modelReifier.createReifiedStatement( uri, s ); }
    
    public boolean contains( Statement s ) {
	return graph.contains( s.asTriple() ); }
    
    public boolean containsResource( RDFNode r ) {
	Node n = r.asNode();
	Iterator it = enhNodes.iterator();
	while(it.hasNext())
	    if(((EnhNode)it.next()).asNode().equals(n))
		return true;
	return false;
    }
    
    public boolean contains( Resource s, Property p ) {
	return contains( s, p, (RDFNode) null );  }
    
    public boolean contains( Resource s, Property p, RDFNode o ) {
	return graph.contains( asNode( s ), asNode( p ), asNode( o ) );
    }
    
    public Statement getRequiredProperty( Resource s, Property p ) {
	Statement st = getProperty( s, p );
	if (st == null)
	    throw new PropertyNotFoundException( p );
	return st;
    }
    
    public Statement getProperty( Resource s, Property p ) {
	StmtIterator iter = listStatements( s, p, (RDFNode) null );
	try {
	    return iter.hasNext() ? iter.nextStatement() : null;
	} finally {
	    iter.close();
	}
    }
    
    public static Node asNode( RDFNode x ) {
	return x == null ? Node.ANY : x.asNode();
    }
    
    private NodeIterator listObjectsFor( RDFNode s, RDFNode p ) {
	StmtIterator it = this.listStatements((Resource)s, (Property)p, (RDFNode)null);
	Set objects = new Set();
	while(it.hasNext())
	    objects.add((it.nextStatement()).getObject());
	return IteratorCast.asNodeIterator(objects.iterator());
    }
    
    private ResIterator listSubjectsFor( RDFNode p, RDFNode o ) {
	ExtendedIterator it = this.listStatements((Resource)null, (Property)p, o);
	Set subjects = new Set();
	while(it.hasNext())
	    subjects.add(((Statement)it.next()).getSubject());
	return IteratorCast.asResIterator(subjects.iterator());
    }
    
    public ResIterator listSubjects() {
	return listSubjectsFor( null, null ); }
    
    public ResIterator listSubjectsWithProperty(Property p) {
	return listSubjectsFor( p, null ); }
    
    public ResIterator listSubjectsWithProperty(Property p, RDFNode o) {
	return listSubjectsFor( p, o ); }
    
    public NodeIterator listObjects() {
	return listObjectsFor( null, null ); }
    
    public NodeIterator listObjectsOfProperty(Property p) {
	return listObjectsFor( null, p ); }
    
    public NodeIterator listObjectsOfProperty(Resource s, Property p) {
	return listObjectsFor( s, p ); }
    
    public StmtIterator listStatements( final Selector selector ) {
	return listStatements(selector.getSubject(), selector.getPredicate(), selector.getObject());
    }
    
    /**
     * Answer an [extended] iterator which returns the triples in this graph which
     * are selected by the (S, P, O) triple in the selector, ignoring any special
     * tests it may do.
     *
     * @param s a Selector used to supply subject, predicate, and object
     * @return an extended iterator over the matching (S, P, O) triples
     */
    public ExtendedIterator findTriplesFrom( Selector s ) {
	return graph.find( asNode( s.getSubject() ), asNode( s.getPredicate() ), asNode( s.getObject() ) );
    }
        
    public boolean independent() {
	return true; }
    
    public Resource createResource() {
	return new ResourceImpl(Node.createAnon(), this);
    }
    
    public Resource createResource( String uri ) {
	return getResource( uri );
    }
    
    public Property createProperty( String uri ) {
	return getProperty( uri );
    }
    
    public Property createProperty(String nameSpace, String localName) {
	return getProperty(nameSpace, localName);
    }
    
    /**
     * create a Statement from the given r, p, and o.
     */
    public Statement createStatement(Resource r, Property p, RDFNode o) {
	return new StatementImpl( r, p, o, this ); }
    
    public Bag createBag(String uri) {
	return (Bag) getBag(uri).addProperty( RDF.type, RDF.Bag ); }
    
    public Alt createAlt( String uri ) {
	return (Alt) getAlt(uri).addProperty( RDF.type, RDF.Alt ); }
    
    public Seq createSeq(String uri) {
	return (Seq) getSeq(uri).addProperty( RDF.type, RDF.Seq ); }
    
    /**
     * Answer a Statement in this Model whcih encodes the given Triple.
     * @param t a triple to wrap as a statement
     * @return a statement wrapping the triple and in this model
     */
    public Statement asStatement( Triple t ) {
	return StatementImpl.toStatement( t, this ); }
    
    public Statement [] asStatements( Triple [] triples ) {
	Statement [] result = new Statement [triples.length];
	for (int i = 0; i < triples.length; i += 1) result[i] = asStatement( triples[i] );
	return result;
    }
    
    public List asStatements( List triples ) {
	List L = new List( triples.size() );
	for (int i = 0; i < triples.size(); i += 1)
	    L.add( asStatement( (Triple) triples.get(i) ) );
	return L;
    }
    
    public Model asModel( Graph g ) {
	return new ModelCom( g );
    }
    
    public StmtIterator listBySubject( Container cont ) {
	return listStatements( cont, null, (RDFNode) null );
    }
    
    public void close() {
	graph.close();
    }
    
    public boolean isClosed() {
	return graph.isClosed();
    }
    
    public boolean supportsSetOperations() {
	return true;
    }

	public boolean isIsomorphicWith(Model m) {
        Graph L = /*ModelFactory.withHiddenStatements*/( this ).getGraph();            
        Graph R = /*ModelFactory.withHiddenStatements*/( m ).getGraph();
        return L.isIsomorphicWith( R );
	}
    
    public Model query( Selector selector ) {
	return createWorkModel().add( listStatements( selector ) );
    }
    
    public Model union( Model model ) {
	return createWorkModel() .add(this) .add( model );
    }
    
    /**
     * Intersect this with another model. As an attempt at optimisation, we try and ensure
     * we iterate over the smaller model first. Nowadays it's not clear that this is a good
     * idea, since <code>size()</code> can be expensive on database and inference
     * models.
     *
     * @see tesi.rdf.model.Model#intersection(tesi.rdf.model.Model)
     */
    public Model intersection( Model other ) {
	return this.size() < other.size()
	? intersect( this, other )
	: intersect( other, this );
    }
    
    /**
     * Answer a Model that is the intersection of the two argument models. The first
     * argument is the model iterated over, and the second argument is the one used
     * to check for membership. [So the first one should be "small" and the second one
     * "membership cheap".]
     */
    public static Model intersect( Model smaller, Model larger ) {
	Model result = createWorkModel();
	StmtIterator it = smaller.listStatements();
	try {
	    return addCommon( result, it, larger );
	} finally {
	    it.close();
	}
    }
    
    /**
     * Answer the argument result with all the statements from the statement iterator that
     * are in the other model added to it.
     *
     * @param result the Model to add statements to and return
     * @param it an iterator over the candidate statements
     * @param other the model that must contain the statements to be added
     * @return result, after the suitable statements have been added to it
     */
    protected static Model addCommon( Model result, StmtIterator it, Model other ) {
	while (it.hasNext()) {
	    Statement s = it.nextStatement();
	    if (other.getGraph().contains( s.asTriple() ))
		result.getGraph().add( s.asTriple() );
	}
	return result;
    }
    
    public Model difference(Model model)  {
//	Model resultModel = createWorkModel();
	Model resultModel = new ModelCom(it.polimi.elet.contextaddict.microjena.graph.Factory.createDefaultGraph(), true);
	StmtIterator iter = null;
	Statement stmt;
	try {
	    iter = listStatements();
	    while (iter.hasNext()) {
		stmt = iter.nextStatement();
		if (! model.getGraph().contains(stmt.asTriple())) {
		    resultModel.getGraph().add(stmt.asTriple());
		}
	    }
	    return resultModel;
	} finally {
	    iter.close();
	}
    }
    
    public String toString() {
	return "<ModelCom  " + getGraph() + " | " + reifiedToString() + ">";
    }
    
    public String reifiedToString() {
	return statementsToString( getHiddenStatements().listStatements() );
    }
    
    protected String statementsToString( StmtIterator it ) {
	StringBuffer b = new StringBuffer();
	while (it.hasNext()) b.append( " " ).append( it.nextStatement() );
	return b.toString();
    }
    
    /**
     * a read-only Model with all the statements of this Model and any
     * statements "hidden" by reification. That model is dynamic, ie
     * any changes this model will be reflected that one.
     */
    public Model getHiddenStatements() {
	return modelReifier.getHiddenStatements();
    }
           
}

/*
 *  (c) Copyright 2001, 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 *  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Model.java
 *
 * Created on 11 March 2001, 16:07
 */
