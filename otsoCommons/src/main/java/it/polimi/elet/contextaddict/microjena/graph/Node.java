/*
  (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
  [See end of file]
  $Id: Node.java,v 1.56 2007/01/02 11:49:18 andy_seaborne Exp $
 */

package it.polimi.elet.contextaddict.microjena.graph;

import it.polimi.elet.contextaddict.microjena.datatypes.DatatypeFormatException;
import it.polimi.elet.contextaddict.microjena.datatypes.RDFDatatype;
import it.polimi.elet.contextaddict.microjena.datatypes.TypeMapper;
import it.polimi.elet.contextaddict.microjena.datatypes.xsd.XSDDatatype;
import it.polimi.elet.contextaddict.microjena.graph.impl.LiteralLabel;
import it.polimi.elet.contextaddict.microjena.rdf.model.AnonId;
import it.polimi.elet.contextaddict.microjena.shared.JenaException;
import it.polimi.elet.contextaddict.microjena.shared.PrefixMapping;
import it.polimi.elet.contextaddict.microjena.util.Map;

/**
 * A Node has five subtypes: Node_Blank, Node_Anon, Node_URI,
 * Node_Variable, and Node_ANY.
 * Nodes are only constructed by the node factory methods, and they will
 * attempt to re-use existing nodes with the same label if they are recent
 * enough.
 * @author Jeremy Carroll and Chris Dollin
 */

public abstract class Node {
    
    /** contains URI */
    final protected Object label;
    
    /** map containing pointers (String)URI -> (Node)node */
    static final Map present = new Map();
    
    /**
     * The canonical instance of Node_ANY. No other instances are required.
     */
    public static final Node ANY = new Node_ANY(); //Utilizzato per le query
    
    static final String RDFprefix = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    
    /**
     * Returns a Node described by the string, primarily for testing purposes.
     * The string represents a URI, a numeric literal, a string literal, a bnode label,
     * or a variable.
     * <ul>
     * <li> 'some text' :: a string literal with that text
     * <li> 'some text'someLanguage:: a string literal with that text and language
     * <li> 'some text'someURI:: a typed literal with that text and datatype
     * <li> digits :: a literal [OF WHAT TYPE] with that [numeric] value
     * <li> _XXX :: a bnode with an AnonId built from _XXX
     * <li> name:stuff :: the URI; name may be expanded using the Extended map
     * </ul>
     * @param x the string describing the node
     * @return a node of the appropriate type with the appropriate label
     */
    public static Node create( String x ) {
	return create( PrefixMapping.Standard, x);
    }
    
    /**
     * As for create(String), but the PrefixMapping used to translate URI strings
     * is an additional argument.
     * @param pm the PrefixMapping for translating pre:X strings
     * @param x the string encoding the node to create
     * @return a node with the appropriate type and label
     */
    public static Node create( PrefixMapping pm, String x ) {
	if (x.equals( "" ))
	    throw new JenaException( "Node.create does not accept an empty string as argument" );
	char first = x.charAt( 0 );
	if (first == '\'' || first == '\"')
	    return Node.createLiteral( newString( pm, first, x ) );
	if (Character.isDigit( first ))
	    return Node.createLiteral( x, "", XSDDatatype.XSDinteger );
	if (first == '_')
	    return Node.createAnon( new AnonId( x ) );
	if (x.equals( "??" ))
	    return Node.ANY;
	if (first == '&')
	    return Node.createURI( "q:" + x.substring( 1 ) );
	int colon = x.indexOf( ':' );
	String d = pm.getNsPrefixURI( "" );
	return colon < 0
		? Node.createURI( (d == null ? "eh:/" : d) + x )
		: Node.createURI( pm.expandPrefix( x ) )
		;
    }
    
    private static String unEscape( String spelling ) {
	if (spelling.indexOf( '\\' ) < 0) return spelling;
	StringBuffer result = new StringBuffer( spelling.length() );
	int start = 0;
	while (true) {
	    int b = spelling.indexOf( '\\', start );
	    if (b < 0) break;
	    result.append( spelling.substring( start, b ) );
	    result.append( unEscape( spelling.charAt( b + 1 ) ) );
	    start = b + 2;
	}
	result.append( spelling.substring( start ) );
	return result.toString();
    }
    
    private static char unEscape( char ch ) {
	switch (ch) {
	    case '\\':
	    case '\"':
	    case '\'': return ch;
	    case 'n': return '\n';
	    case 's': return ' ';
	    case 't': return '\t';
	    default: return 'Z';
	}
    }
    
    private static LiteralLabel newString( PrefixMapping pm, char quote, String nodeString ) {
	int close = nodeString.lastIndexOf( quote );
	return literal( pm, nodeString.substring( 1, close ), nodeString.substring( close + 1 ) );
    }
    
    private static LiteralLabel literal( PrefixMapping pm, String spelling, String langOrType ) {
	String content = unEscape( spelling );
	int colon = langOrType.indexOf( ':' );
	return colon < 0
		? new LiteralLabel( content, langOrType, false )
		: LiteralLabel.createLiteralLabel( content, "", getType( pm.expandPrefix( langOrType ) ) )
		;
    }
    
    private static RDFDatatype getType( String s ) {
	return TypeMapper.getInstance().getSafeTypeByName( s );
    }
    
    /** make a blank node with a fresh anon id */
    public static Node createAnon() {
	return createAnon( AnonId.create() ); }
    
    /** make a blank node with the specified label */
    public static Node createAnon( AnonId id ) {
	return create( makeAnon, id ); }
    
    /** make a literal node with the specified literal value */
    public static Node createLiteral( LiteralLabel lit ) {
	return create( makeLiteral, lit ); }
    
    /** make a URI node with the specified URIref string */
    public static Node createURI( String uri ) {
	return create( makeURI, uri ); }
    
    public static Node createLiteral( String value ) {
	return createLiteral( value, "", false ); }
    
    /** make a literal with specified language and XMLishness.
     * _lit_ must *not* be null.
     * @param isXml If true then lit is exclusive canonical XML of type
     * rdf:XMLLiteral, and no checking will be invoked.
     */
    public static Node createLiteral( String lit, String lang, boolean isXml ) {
	if (lit == null)
	    throw new NullPointerException( "null for literals has been illegal since Jena 2.0" );
	return createLiteral( new LiteralLabel( lit, lang, isXml ) );
    }
    
    /**
     * Build a typed literal node from its lexical form. The
     * lexical form will be parsed now and the value stored. If
     * the form is not legal this will throw an exception.
     *
     * @param lex the lexical form of the literal
     * @param lang the optional language tag
     * @param dtype the type of the literal, null for old style "plain" literals
     * @throws DatatypeFormatException if lex is not a legal form of dtype
     */
    public static Node createLiteral( String lex, String lang, RDFDatatype dtype )
    throws DatatypeFormatException {
	return createLiteral( LiteralLabel.createLiteralLabel( lex, lang, dtype ) ); }
    
    public static Node createUncachedLiteral( Object value, String lang, RDFDatatype dtype )
    throws DatatypeFormatException {
	return new Node_Literal( new LiteralLabel( value, lang, dtype ) ); }
    
    /**
     * Answer true iff this node is concrete, ie not variable, ie URI, blank, or literal.
     */
    public abstract boolean isConcrete();
    
    /**
     * Answer true iff this node is a literal node [subclasses override]
     */
    public boolean isLiteral() {
	return false;
    }
    
    /**
     * Answer true iff this node is a blank node [subclasses override]
     */
    public boolean isBlank() {
	return false;
    }
    
    /**
     * Answer true iff this node is a URI node [subclasses override]
     */
    public boolean isURI() {
	return false;
    }
    
    /**
     * Answer true iff this node is a variable node - subclasses override
     */
    public boolean isVariable() {
	return false;
    }
    
    /** get the blank node id if the node is blank, otherwise die horribly */
    public AnonId getBlankNodeId() {
	throw new JenaException( this + " is not a blank node" );
    }
    
    /**
     * Answer the label of this blank node or throw an UnsupportedOperationException
     * if it's not blank.
     */
    public String getBlankNodeLabel() {
	return getBlankNodeId().getLabelString();
    }
    
    /**
     * Answer the literal value of a literal node, or throw an UnsupportedOperationException
     * if it's not a literal node
     */
    public LiteralLabel getLiteral() {
	throw new JenaException( this + " is not a literal node" );
    }
    
    /**
     * Answer the value of this node's literal value, if it is a literal;
     * otherwise die horribly.
     */
    public Object getLiteralValue() {
	throw new NotLiteral( this );
    }
    
    /**
     * Answer the lexical form of this node's literal value, if it is a literal;
     * otherwise die horribly.
     */
    public String getLiteralLexicalForm() {
	throw new NotLiteral( this );
    }
    
    /**
     * Answer the language of this node's literal value, if it is a literal;
     * otherwise die horribly.
     */
    public String getLiteralLanguage() {
	throw new NotLiteral( this );
    }
    
    /**
     * Answer the data-type URI of this node's literal value, if it is a
     * literal; otherwise die horribly.
     */
    public String getLiteralDatatypeURI() {
	throw new NotLiteral( this );
    }
    
    /**
     * Answer the RDF datatype object of this node's literal value, if it is
     * a literal; otherwise die horribly.
     */
    public RDFDatatype getLiteralDatatype() {
	throw new NotLiteral( this );
    }
    
    public boolean getLiteralIsXML() {
	throw new NotLiteral( this );
    }
    
    /**
     * Exception thrown if a literal-access operation is attemted on a
     * non-literal node.
     */
    public static class NotLiteral extends JenaException {
	public NotLiteral( Node it ) {
	    super( it + " is not a literal node" );
	}
    }
    
    /** get the URI of this node if it has one, else die horribly */
    public String getURI() {
	throw new JenaException( this + " is not a URI node" );
    }
    
    /** get the namespace part of this node if it's a URI node, else die horribly */
    public String getNameSpace() {
	throw new JenaException( this + " is not a URI node" );
    }
    
    /** get the localname part of this node if it's a URI node, else die horribly */
    public String getLocalName() {
	throw new JenaException( this + " is not a URI node" );
    }
    
    /** get a variable nodes name, otherwise die horribly */
    public String getName() {
	throw new JenaException( "this (" + this.getClass() + ") is not a variable node" );
    }
    
    /** answer true iff this node is a URI node with the given URI */
    public boolean hasURI( String uri ) {
	return false;
    }
    
    /** an abstraction to allow code sharing */
    static abstract class NodeMaker {
	abstract Node construct( Object x );
    }
    
    static final NodeMaker makeAnon = new NodeMaker() {
	Node construct( Object x ) {
	    return new Node_Blank( x );
	}
    };
    
    static final NodeMaker makeLiteral = new NodeMaker() {
	Node construct( Object x ) {
	    return new Node_Literal( x );
	}
    };
    
    static final NodeMaker makeURI = new NodeMaker() {
	Node construct( Object x ) {
	    return new Node_URI( x );
	}
    };
    
    /**
     * The canonical NULL. It appears here so that revised definitions [eg as a bnode]
     * that require the cache-and-maker system will work; the NodeMaker constants
     * should be non-null at this point.
     */
    public static final Node NULL = new Node_NULL();
    
    /**
     * keep the distinguishing label value.
     */
    
    /* package visibility only */
    Node( Object label ) {
	this.label = label;
    }
    
    /**
     * We object strongly to null labels: for example, they make .equals flaky. We reuse nodes
     * from the recent cache if we can. Otherwise, the maker knows how to construct a new
     * node of the correct class, and we add that node to the cache. create is
     * synchronised to avoid threading problems - a separate thread might zap the
     * cache entry that get is currently looking at.
     */
    public static synchronized Node create( NodeMaker maker, Object label ) {
	if (label == null) throw new JenaException( "Node.make: null label" );
	Node node = (Node) present.get( label );
	return node == null ? cacheNewNode( label, maker.construct( label ) ) : node;
    }
    
    /**
     * cache the node <code>n</code> under the key <code>label</code>,
     * and return that node.
     */
    private static Node cacheNewNode( Object label, Node n ) {
	present.put( label, n );
	return n;
    }
    
    /**
     * Nodes only equal other Nodes that have equal labels.
     */
    public abstract boolean equals(Object o);
    
    /**
     * Test that two nodes are semantically equivalent.
     * In some cases this may be the sames as equals, in others
     * equals is stricter. For example, two xsd:int literals with
     * the same value but different language tag are semantically
     * equivalent but distinguished by the java equality function
     * in order to support round tripping.
     * <p>Default implementation is to use equals, subclasses should
     * override this.</p>
     */
    public boolean sameValueAs(Object o) {
	return equals( o ); }
    
    public int hashCode() {
	return label.hashCode() * 31; }
    
    /**
     * Answer true iff this node accepts the other one as a match.
     * The default is an equality test; it is over-ridden in subclasses to
     * provide the appropriate semantics for literals, ANY, and variables.
     *
     * @param other a node to test for matching
     * @return true iff this node accepts the other as a match
     */
    public boolean matches( Node other ) {
	return equals( other ); }
    
    /**
     * Answer a human-readable representation of this Node. It will not compress URIs,
     * nor quote literals (because at the moment too many places use toString() for
     * something machine-oriented).
     */
    public String toString() {
	return toString( null ); }
    
    /**
     * Answer a human-readable representation of this Node where literals are
     * quoted according to <code>quoting</code> but URIs are not compressed.
     */
    public String toString( boolean quoting ) {
	return toString( null, quoting ); }
    
    /**
     * Answer a human-readable representation of the Node, quoting literals and
     * compressing URIs.
     */
    public String toString( PrefixMapping pm ) {
	return toString( pm, true ); }
    
    /**
     * Answer a human readable representation of this Node, quoting literals if specified,
     * and compressing URIs using the prefix mapping supplied.
     */
    public String toString( PrefixMapping pm, boolean quoting ) {
	return label.toString(); }
}

/*
    (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
    All rights reserved.
 
    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions
    are met:
 
    1. Redistributions of source code must retain the above copyright
       notice, this list of conditions and the following disclaimer.
 
    2. Redistributions in binary form must reproduce the above copyright
       notice, this list of conditions and the following disclaimer in the
       documentation and/or other materials provided with the distribution.
 
    3. The name of the author may not be used to endorse or promote products
       derived from this software without specific prior written permission.
 
    THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
    IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
    OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
    IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
    INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
    NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
    DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
    THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
    (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
    THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
