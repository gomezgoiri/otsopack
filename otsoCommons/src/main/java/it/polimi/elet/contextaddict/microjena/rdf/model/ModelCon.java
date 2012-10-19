/*
 *  (c) Copyright 2000, 2001, 2002, 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
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
 * ModelCon.java
 *
 * Created on 7 December 2001, 11:00
 */

package it.polimi.elet.contextaddict.microjena.rdf.model;

import it.polimi.elet.contextaddict.microjena.datatypes.DatatypeFormatException;
import it.polimi.elet.contextaddict.microjena.datatypes.RDFDatatype;
import it.polimi.elet.contextaddict.microjena.graph.Node;


/** Convenience methods which extend the {@link Model} interface.
 * <P>The {@link Model} interface provides a set of primitive operations on
 *    an RDF model.  This interface extends those methods with a
 *    set of convenience methods.</P>
 * <p>This interface provides methods supporting typed literals.  This means
 *    that methods are provided which will translate a built in type, or an
 *    object to an RDF Literal.  This translation is done by invoking the
 *    <CODE>toString()</CODE> method of the object, or its built in equivalent.
 *    The reverse translation is also supported.  This is built in for built
 *    in types.  Factory objects, provided by the application, are used
 *    for application objects.</p>
 * <p>This interface provides methods for supporting enhanced resources.  An
 *    enhanced resource is a resource to which the application has added
 *    behaviour.  RDF containers are examples of enhanced resources built in
 *    to this package.  Enhanced resources are supported by encapsulating a
 *    resource created by an implementation in another class which adds
 *    the extra behaviour.  Factory objects are used to construct such
 *    enhanced resources.</p>
 * @author bwm
 * @version Release='$Name:  $'
 * Revision='$Revision: 1.20 $'
 * Date='$Date: 2007/01/02 11:48:34 $'
 */
public interface ModelCon {
    
    /** Return a Resource instance in this model.
     *
     * <p>Subsequent operations on the returned object may modify this model.</p>
     * <p>The resource is assumed to already exist in the model.  If it does not,
     * <CODE>createResource</CODE> should be used instead.</p>
     * @return a resource instance created by the factory provided
     * @param uri the URI of the resource
     * @param f the factory object
 
     */
    Resource getResource(String uri, ResourceF f) ;
    
    /** Return a Property instance in this model.
     *
     * <p>Subsequent operations on the returned property may modify this model.</p>
     * <p>The property is assumed to already exist in the model.  If it does not,
     * <CODE>createProperty</CODE> should be used instead.</p>
     * @return a property object
     * @param uri the URI of the property
 
     */
    Property getProperty(String uri) ;
    
    /** Return a Bag instance in this model.
     *
     * <p>Subsequent operations on the returned bag may modify this model.</p>
     * <p>The bag is assumed to already exist in the model.  If it does not,
     * <CODE>createBag</CODE> should be used instead.</p>
     * @return a bag instance
     * @param uri the URI of the bag.
 
     */
    Bag getBag(String uri) ;
    
    /** Return a bag instance based on a given resource.
     *
     * <p> This method enables an application to treat any resource as a bag.
     *     It is in effect an unsafe downcast.</p>
     *
     * <p>Subsequent operations on the returned bag may modify this model.</p>
     * <p>The bag is assumed to already exist in the model.  If it does not,
     * <CODE>createBag</CODE> should be used instead.</p>
     * @return a bag instance
     * @param r an untyped Resource instance
 
     */
    Bag getBag(Resource r) ;
    
    /** Return an Alt instance in this model.
     *
     * <p>Subsequent operations on the returned object may modify this model.</p>
     * <p>The alt is assumed to already exist in the model.  If it does not,
     * <CODE>createAlt</CODE> should be used instead.</p>
     * @return an alt instance
     * @param uri the URI of the alt
 
     */
    Alt getAlt(String uri) ;
    
    /** Return an Alt instance based on a given resource.
     *
     * <p> This method enables an application to treat any resource as an Alt.
     *     It is in effect an unsafe downcast.</p>
     *
     * <p>Subsequent operations on the returned Alt may modify this model.</p>
     * <p>The bag is assumed to already exist in the model.  If it does not,
     * <CODE>createAlt</CODE> should be used instead.</p>
     * @return an Alt instance
     * @param r an untyped Resource instance
 
     */
    Alt getAlt(Resource r) ;
    /** Return a Seq instance in this model.
     *
     * <p>Subsequent operations on the returned bag may modify this model.</p>
     * <p>The seq is assumed to already exist in the model.  If it does not,
     * <CODE>createSeq</CODE> should be used instead.</p>
     * @return a seq instance
     * @param uri the URI of the seq
 
     */
    Seq getSeq(String uri) ;
    
    /** Return a Seq instance based on a given resource.
     *
     * <p> This method enables an application to treat any resource as a Seq.
     *     It is in effect an unsafe downcast.</p>
     *
     * <p>Subsequent operations on the returned Seq may modify this model.</p>
     * <p>The Seq is assumed to already exist in the model.  If it does not,
     * <CODE>createAlt</CODE> should be used instead.</p>
     * @return an Alt instance
     * @param r an untyped Resource instance
 
     */
    Seq getSeq(Resource r) ;
    
    /** Create a new anonymous resource with a given type.
     *
     * <p> Subsequent operations on the returned resource may modify this model.
     * </p>
     * <p> The resource is created and an rdf:type property added to the model
     * to specify its type. </p>
     * @param type the type of the resource to be created.
     * @return a new anonymous resource linked to this model.
     */
    public Resource createResource(Resource type) ;
    
    /**
    Create or find an RDFNode
    (a {@link Resource} or a {@link Literal})
    from a graph Node. This is provided for users and
    developers operating at the API/SPI interface, where Resources are
    constructed from Nodes. Providing this method allows each Model
    the opportunity to cache node-to-resource maps if it requires.
 
    @param n the graph.Node on which to base the Model.RDFNode
    @return a suitable RDFNode
     */
    public RDFNode getRDFNode( Node n );
    
    /** Create a new resource with a given type.
     *
     * <p> Subsequent operations on the returned resource may modify this model.
     * </p>
     * <p> The resource is created and an rdf:type property added to the model
     * to specify its type. </p>
     * @param type the type of the resource to be created.
     * @return a new resource linked to this model.
     * @param uri The URI of the new resource.
     */
    public Resource createResource(String uri, Resource type) ;
    
    /** Create a new anonymous resource using the supplied factory.
     *
     * <p> Subsequent operations on the returned resource may modify this model.
     * </p>
     * @return a new anonymous resource linked to this model.
     * @param f A factory object to create the returned object.
 .
     */
    public Resource createResource(ResourceF f) ;
    
    /** Create a new resource using the supplied factory.
     *
     * <p> Subsequent operations on the returned resource may modify this model.
     * </p>
     * @return a new resource linked to this model.
     * @param uri the URI of the resource
     * @param f A factory to create the returned object.
 .
     */
    public Resource createResource(String uri, ResourceF f) ;
    
    /** Create a property.
     *
     * <p> Subsequent operations on the returned property may modify this model.
     * </p>
     * @param uri the URI of the property
 
     * @return a property instance
     */
    public Property createProperty(String uri) ;
    
    /** create a literal from a boolean value.
     *
     * <p> The value is converted to a string using its <CODE>toString</CODE>
     * method. </p>
     * @param v the value of the literal
     * @return a new literal representing the value v
     * @deprecated Applications should use typed literals
     */
    public Literal createLiteral(boolean v) ;
    /** create a literal from an integer value.
     *
     * @param v the value of the literal
     * @return a new literal representing the value v
     * @deprecated Applications should use typed literals
     */
    public Literal createLiteral(long v) ;
    /** create a literal from a char value.
     *
     * @param v the value of the literal
     * @return a new literal representing the value v
     * @deprecated Applications should use typed literals
     */
    public Literal createLiteral(char v) ;
    /** create a literal from a float value.
     *
     * @param v the value of the literal
     * @return a new literal representing the value v
     * @deprecated Applications should use typed literals
     */
    public Literal createLiteral(float v) ;
    /** create a literal from a double value.
     *
     * @param v the value of the literal
     * @return a new literal representing the value v
     * @deprecated Applications should use typed literals
     */
    public Literal createLiteral(double v) ;
    
    /** create a literal from a String value.
     *
     * @param v the value of the literal
     * @return a new literal representing the value v
     */
    public Literal createLiteral(String v) ;
    
    /** create a literal from an Object.
     *
     * @return a new literal representing the value v
     * @param v the value of the literal.
     * @deprecated Applications should use typed literals
     */
    public Literal createLiteral(Object v) ;
    
    /** create a type literal from a boolean value.
     *
     * <p> The value is converted to a string using its <CODE>toString</CODE>
     * method. </p>
     * @param v the value of the literal
     * @return a new literal representing the value v
     */
    public Literal createTypedLiteral(boolean v) ;
    
    /** create a typed literal from an integer value.
     *
     * @param v the value of the literal
     * @return a new literal representing the value v
     */
    public Literal createTypedLiteral(int v) ;
    
    /** create a typed literal from an integer value.
     *
     * @param v the value of the literal
     * @return a new literal representing the value v
     */
    public Literal createTypedLiteral(long v) ;
    
    /**
     * Create a typed literal of type xsd:dateTime from a Calendar object.
     */
    //public Literal createTypedLiteral(Calendar d);
    
    /** create a typed literal from a char value.
     *
     * @param v the value of the literal
     * @return a new literal representing the value v
     */
    public Literal createTypedLiteral(char v) ;
    
    /** create a typed literal from a float value.
     *
     * @param v the value of the literal
     * @return a new literal representing the value v
     */
    public Literal createTypedLiteral(float v) ;
    
    /** create a typed literal from a double value.
     *
     * @param v the value of the literal
     * @return a new literal representing the value v
     */
    public Literal createTypedLiteral(double v) ;
    
    /** create a typed literal from a String value.
     *
     * @param v the value of the literal
     * @return a new literal representing the value v
     */
    public Literal createTypedLiteral(String v) ;
    
    /** create a literal from an Object.
     *
     * @return a new literal representing the value v
     * @param v the value of the literal.
     */
    public Literal createTypedLiteral(Object v) ;
    
    /**
     * Build a typed literal from its lexical form. The
     * lexical form will be parsed now and the value stored. If
     * the form is not legal this will throw an exception.
     * <p>
     * Note that in preview releases of Jena2 it was also possible to specify
     *   a language type. Changes to the RDF specification mean that this is no longer
     *   legal except for plain literals. To create a plain literal with a language tag
     *   use {@link Model#createLiteral(String, String) createLiteral}.
     * </p>
     *
     * @param lex the lexical form of the literal
     * @param typeURI the uri of the type of the literal, null for old style "plain" literals
     * @throws DatatypeFormatException if lex is not a legal form of dtype
     */
    public Literal createTypedLiteral(String lex, String typeURI)  ;
    
    /**
     * Build a typed literal from its value form.
     * <p>
     * Note that in preview releases of Jena2 it was also possible to specify
     *   a language type. Changes to the RDF specification mean that this is no longer
     *   legal except for plain literals. To create a plain literal with a language tag
     *   use {@link Model#createLiteral(String, String) createLiteral}.
     * </p>
     *
     * @param value the value of the literal
     * @param typeURI the URI of the type of the literal, null for old style "plain" literals
     */
    public Literal createTypedLiteral(Object value, String typeURI);
    
    /** Create a Statement instance.
     *
     * <p>Subsequent operations on the statement or any of its parts may
     * modify this model.</p>
     * <p>Creating a statement does not add it to the set of statements in the
     * model. </p>
     * <p>The value o will be converted to a Literal.</P>
     * @deprecated Applications should use typed literals
     * @param s the subject of the statement
     * @param p the predicate of the statement
     * @param o is the value to be the object of the statement
     *
     * @return the new statement
     */
    public Statement createStatement(Resource s, Property p, boolean o)  ;
    
    /** Create a Statement instance.
     *
     * <p>Subsequent operations on the statement or any of its parts may
     * modify this model.</p>
     * <p>Creating a statement does not add it to the set of statements in the
     * model. </p>
     * <p>The value o will be converted to a Literal.</P>
     * @deprecated Applications should use typed literals
     * @param s the subject of the statement
     * @param p the predicate of the statement
     * @param o is the value to be the object of the statement
     *
     * @return the new statement
     */
    public Statement createStatement(Resource s, Property p, long o) ;
    
    /** Create a Statement instance.
     *
     * <p>Subsequent operations on the statement or any of its parts may
     * modify this model.</p>
     * <p>Creating a statement does not add it to the set of statements in the
     * model. </p>
     * <p>The value o will be converted to a Literal.</P>
     * @deprecated Applications should use typed literals
     * @param s the subject of the statement
     * @param p the predicate of the statement
     * @param o is the value to be the object of the statement
     *
     * @return the new statement
     */
    public Statement createStatement(Resource s, Property p, char o) ;
    
    /** Create a Statement instance.
     *
     * <p>Subsequent operations on the statement or any of its parts may
     * modify this model.</p>
     * <p>Creating a statement does not add it to the set of statements in the
     * model. </p>
     * <p>The value o will be converted to a Literal.</P>
     * @deprecated Applications should use typed literals
     * @param s the subject of the statement
     * @param p the predicate of the statement
     * @param o is the value to be the object of the statement
     *
     * @return the new statement
     */
    public Statement createStatement(Resource s, Property p, float o) ;
    
    /** Create a Statement instance.
     *
     * <p>Subsequent operations on the statement or any of its parts may
     * modify this model.</p>
     * <p>Creating a statement does not add it to the set of statements in the
     * model. </p>
     * <p>The value o will be converted to a Literal.</P>
     * @deprecated Applications should use typed literals
     * @param s the subject of the statement
     * @param p the predicate of the statement
     * @param o is the value to be the object of the statement
     *
     * @return the new statement
     */
    public Statement createStatement(Resource s, Property p, double o) ;
    
    /** Create a Statement instance.
     *
     * <p>Subsequent operations on the statement or any of its parts may
     * modify this model.</p>
     * <p>Creating a statement does not add it to the set of statements in the
     * model. </p>
     * <p>The Object o will be converted to a Literal.</P>
     * @param s the subject of the statement
     * @param p the predicate of the statement
     * @param o is the value to be the object of the statement
     *
     * @return the new statement
     */
    public Statement createStatement(Resource s, Property p, String o)  ;
    
    /** Create a Statement instance.
     *
     * <p>Subsequent operations on the statement or any of its parts may
     * modify this model.</p>
     * <p>Creating a statement does not add it to the set of statements in the
     * model. </p>
     * <p>The Object o will be converted to a Literal.</P>
     * @param s the subject of the statement
     * @param p the predicate of the statement
     * @param o is the value to be the object of the statement
     * @param l the language associated with the object
     *
     * @return the new statement
     */
    public Statement createStatement(Resource s, Property p, String o, String l) ;
    
    /** Create a Statement instance.
     *
     * <p>Subsequent operations on the statement or any of its parts may
     * modify this model.</p>
     * <p>Creating a statement does not add it to the set of statements in the
     * model. </p>
     * <p>The Object o will be converted to a Literal.</P>
     * @param s the subject of the statement
     * @param p the predicate of the statement
     * @param o is the value to be the object of the statement
     * @param wellFormed true if the string is well formed XML
     *
     * @return the new statement
     */
    public Statement createStatement(Resource s, Property p, String o,
	    boolean wellFormed) ;
    
    /** Create a Statement instance.
     *
     * <p>Subsequent operations on the statement or any of its parts may
     * modify this model.</p>
     * <p>Creating a statement does not add it to the set of statements in the
     * model. </p>
     * <p>The Object o will be converted to a Literal.</P>
     * @param s the subject of the statement
     * @param p the predicate of the statement
     * @param o is the value to be the object of the statement
     * @param l the language associated with the object
     * @param wellFormed true of the string is well formed XML
     *
     * @return the new statement
     */
    public Statement createStatement(Resource s, Property p, String o, String l,
	    boolean wellFormed) ;
    
    /** Create a Statement instance.
     *
     * <p>Subsequent operations on the statement or any of its parts may
     * modify this model.</p>
     * <p>Creating a statement does not add it to the set of statements in the
     * model. </p>
     * <p>The value o will be converted to a Literal.</P>
     * @deprecated Applications should use typed literals
     * @param s the subject of the statement
     * @param p the predicate of the statement
     * @param o is the value to be the object of the statement
     *
     * @return the new statement
     */
    public Statement createStatement(Resource s, Property p, Object o)   ;
    
    /** Create a new anonymous bag.
     *
     * <p>Subsequent operations on the bag or any of its parts may
     * modify this model.</p>
     * <p>A statement defining the type of the new bag is added to this model.
     * </p>
     * @return a new anonymous bag.
     */
    public Bag createBag() ;
    
    /** Create a new bag.
     *
     * <p>Subsequent operations on the bag or any of its parts may
     * modify this model.</p>
     * <p>A statement defining the type of the new bag is added to this model.
     * </p>
     * @param uri The URI of the new Bag.
     * @return a new bag.
     */
    public Bag createBag(String uri) ;
    
    /** Create a new anonymous alt.
     *
     * <p>Subsequent operations on the alt or any of its parts may
     * modify this model.</p>
     * <p>A statement defining the type of the new alt is added to this model.
     * </p>
     * @return a new anonymous alt.
     */
    public Alt createAlt() ;
    
    /** Create a new alt.
     *
     * <p>Subsequent operations on the alt or any of its parts may
     * modify this model.</p>
     * <p>A statement defining the type of the new alt is added to this model.
     * </p>
     * @param uri The URI of the new alt.
     * @return a new alt.
     */
    public Alt createAlt(String uri) ;
    
    /** Create a new anonymous seq.
     *
     * <p>Subsequent operations on the seq or any of its parts may
     * modify this model.</p>
     * <p>A statement defining the type of the new seq is added to this model.
     * </p>
     * @return a new anonymous seq.
     */
    public Seq createSeq() ;
    
    /** Create a new seq.
     *
     * <p>Subsequent operations on the seq or any of its parts may
     * modify this model.</p>
     * <p>A statement defining the type of the new seq is added to this model.
     * </p>
     * @param uri The URI of the new seq.
     * @return a new seq.
     */
    public Seq createSeq(String uri) ;
    
    /** add a statement to this model.
     * @return this model
     * @param s the subject of the statement to add
     * @param p the predicate of the statement to add
     * @param o the object of the statement to add
     */
    Model add(Resource s, Property p, RDFNode o)     ;
    
    /** add a statement to this model.
     *
     * @return this model
     * @param s the subject of the statement to add
     * @param p the predicate of the statement to add
     * @param o the object of the statement to add
     * @deprecated Applications should use typed literals
     */
    Model add(Resource s, Property p, boolean o) ;
    
    /** add a statement to this model.
     *
     * @return this model
     * @param s the subject of the statement to add
     * @param p the predicate of the statement to add
     * @param o the object of the statement to add
     * @deprecated Applications should use typed literals
     */
    Model add(Resource s, Property p, long o) ;
    
    /** add a statement to this model.
     *
     * @return this model
     * @param s the subject of the statement to add
     * @param p the predicate of the statement to add
     * @param o the object of the statement to add
     * @deprecated Applications should use typed literals
     */
    Model add(Resource s, Property p, char o) ;
    
    /** add a statement to this model.
     *
     * @return this model
     * @param s the subject of the statement to add
     * @param p the predicate of the statement to add
     * @param o the object of the statement to add
     * @deprecated Applications should use typed literals
     */
    Model add(Resource s, Property p, float o) ;
    /** add a statement to this model.
     *
     * @return this model
     * @param s the subject of the statement to add
     * @param p the predicate of the statement to add
     * @param o the object of the statement to add
     * @deprecated Applications should use typed literals
     */
    Model add(Resource s, Property p, double o) ;
    
    /** add a statement to this model.
     *
     * @return this model
     * @param s the subject of the statement to add
     * @param p the predicate of the statement to add
     * @param o the object of the statement to add
     */
    Model add(Resource s, Property p, String o) ;
    
    /** add a statement to this model.
     *
     * @return this model
     * @param s the subject of the statement to add
     * @param p the predicate of the statement to add
     * @param lex the lexcial form of the literal
     * @param datatype the datatype of the literal
     */
    Model add(Resource s, Property p, String lex, RDFDatatype datatype) ;
   
    /** add a statement to this model.
     *
     * @return this model
     * @param s the subject of the statement to add
     * @param p the predicate of the statement to add
     * @param o the object of the statement to add
     * @param wellFormed true if o is well formed XML
     */
    Model add(Resource s, Property p, String o, boolean wellFormed) ;
    
    /** add a statement to this model.
     *
     * @return this model
     * @param s the subject of the statement to add
     * @param p the predicate of the statement to add
     * @param o the object of the statement to add
     * @param l the language associated with the object
 
     */
    Model add(Resource s, Property p, String o, String l) ;
    
    /**
     * @deprecated since Jena2. It is no longer legal to have a language
     * tag on a well-formed XMLLiteral. Use the 4-argument form of
     * {@link #add(Resource, Property, String, boolean) add} instead.
     *
     * add a statement to this model.
     *
     * @return this model
     * @param s the subject of the statement to add
     * @param p the predicate of the statement to add
     * @param o the object of the statement to add
     * @param l the language associated with the object
     * @param wellFormed true if o is well formed XML
 
     */
    Model add(Resource s, Property p, String o, String l, boolean wellFormed) ;
    
    /** add a statement to this model.
     *
     * @return this model
     * @param s the subject of the statement to add
     * @param p the predicate of the statement to add
     * @param o the object of the statement to add
     * @deprecated Applications should use typed literals
     */
    Model add(Resource s, Property p, Object o) ;
    
    /**
     * remove the statement <code>(s, p, o)</code> from this model and
     * answer this model. None of <code>s, p, o</code> are permitted to
     * be <code>null</code>: for wildcard removal, see <code>removeAll</code>.
     */
    Model remove( Resource s, Property p, RDFNode o );
    
    Model remove( Resource s, Property p, RDFNode o, boolean removeAxiom );

    /** Remove all the Statements returned by an iterator.
     * @return this model
     * @param iter the iterator which returns the statements to be removed.
 
     */
    Model remove(StmtIterator iter) ;
    
    /** Remove all the Statements in a given model, including reified statements
     * @return this model
     * @param m the model containing the statements to be removed.
 
     */
    Model remove(Model m) ;
       
    /** Find all the statements matching a pattern.
     * <p>Return an iterator over all the statements in a model
     *  that match a pattern.  The statements selected are those
     *  whose subject matches the <code>subject</code> argument,
     *  whose predicate matches the <code>predicate</code> argument
     *  and whose object matchesthe <code>object</code> argument.</p>
     * @deprecated Applications should use typed literals
     * @return an iterator over the subjects
     * @param subject   The subject sought
     * @param predicate The predicate sought
     * @param object    The value sought
 
     */
    StmtIterator listStatements(Resource subject,
	    Property predicate,
	    boolean object)
	    ;
    
    /** Find all the statements matching a pattern.
     * <p>Return an iterator over all the statements in a model
     *  that match a pattern.  The statements selected are those
     *  whose subject matches the <code>subject</code> argument,
     *  whose predicate matches the <code>predicate</code> argument
     *  and whose object matchesthe <code>object</code> argument.</p>
     * @deprecated Applications should use typed literals
     * @return an iterator over the subjects
     * @param subject   The subject sought
     * @param predicate The predicate sought
     * @param object    The value sought
 
     */
    StmtIterator listStatements(Resource subject,
	    Property predicate,
	    long     object)
	    ;
    
    /** Find all the statements matching a pattern.
     * <p>Return an iterator over all the statements in a model
     *  that match a pattern.  The statements selected are those
     *  whose subject matches the <code>subject</code> argument,
     *  whose predicate matches the <code>predicate</code> argument
     *  and whose object matchesthe <code>object</code> argument.</p>
     * @deprecated Applications should use typed literals
     * @return an iterator over the subjects
     * @param subject   The subject sought
     * @param predicate The predicate sought
     * @param object    The value sought
     */
    StmtIterator listStatements(Resource subject, Property predicate, char object) ;
    
    /** Find all the statements matching a pattern.
     * <p>Return an iterator over all the statements in a model
     *  that match a pattern.  The statements selected are those
     *  whose subject matches the <code>subject</code> argument,
     *  whose predicate matches the <code>predicate</code> argument
     *  and whose object matchesthe <code>object</code> argument.</p>
     * @deprecated Applications should use typed literals
     * @return an iterator over the subjects
     * @param subject   The subject sought
     * @param predicate The predicate sought
     * @param object    The value sought
 
     */
    StmtIterator listStatements(Resource subject, Property predicate, float object) ;
    
    /** Find all the statements matching a pattern.
     * <p>Return an iterator over all the statements in a model
     *  that match a pattern.  The statements selected are those
     *  whose subject matches the <code>subject</code> argument,
     *  whose predicate matches the <code>predicate</code> argument
     *  and whose object matchesthe <code>object</code> argument.</p>
     * @deprecated Applications should use typed literals
     * @return an iterator over the subjects
     * @param subject   The subject sought
     * @param predicate The predicate sought
     * @param object    The value sought
 
     */
    StmtIterator listStatements(Resource subject, Property predicate, double object) ;
    
    /** Find all the statements matching a pattern.
     * <p>Return an iterator over all the statements in a model
     *  that match a pattern.  The statements selected are those
     *  whose subject matches the <code>subject</code> argument,
     *  whose predicate matches the <code>predicate</code> argument
     *  and whose object matchesthe <code>object</code> argument.</p>
     * @return an iterator over the subjects
     * @param subject   The subject sought
     * @param predicate The predicate sought
     * @param object    The value sought
 
     */
    StmtIterator listStatements(Resource subject, Property predicate, String object) ;
    
    /** Find all the statements matching a pattern.
     * <p>Return an iterator over all the statements in a model
     *  that match a pattern.  The statements selected are those
     *  whose subject matches the <code>subject</code> argument,
     *  whose predicate matches the <code>predicate</code> argument
     *  and whose object matchesthe <code>object</code> argument.
     *  If an argument is <code>null</code> it matches anything.</p>
     * @return an iterator over the subjects
     * @param subject   The subject sought
     * @param predicate The predicate sought
     * @param object    The value sought
     * @param lang      The lang code ofthe string.
 
     */
    StmtIterator listStatements(Resource subject, Property predicate, String object, String   lang) ;
    
    /** List all subjects with a given property and property value.
     * @deprecated Applications should use typed literals
     * @return an iterator over the subjects
     * @param p The predicate sought
     * @param o The value sought
 
     */
    ResIterator listSubjectsWithProperty(Property p, boolean o) ;
    
    /** List all the subjects with a given property and property value.
     * @deprecated Applications should use typed literals
     * @return an iterator over the subjects
     * @param p The predicate sought
     * @param o The value sought
 
     */
    ResIterator listSubjectsWithProperty(Property p, long o) ;
    
    /** List all subjects with a given property and property value.
     * @deprecated Applications should use typed literals
     * @return an iterator over the subjects
     * @param p The predicate sought
     * @param o The value sought
 
     */
    ResIterator listSubjectsWithProperty(Property p, char o) ;
    
    /** List all subjects with a given property and property value.
     * @deprecated Applications should use typed literals
     * @return an iterator over the subjects
     * @param p The predicate sought
     * @param o The value sought
 
     */
    ResIterator listSubjectsWithProperty(Property p, float o) ;
    /** lists all subjects with a given property and property value.
     * @deprecated Applications should use typed literals
     * @return an iterator over the set of subjects
     * @param p The property sought.
     * @param o The property value sought.
 
     */
    ResIterator listSubjectsWithProperty(Property p, double o) ;
    /** lists all subjects with a given property and property value.
     * @return an iterator over the set of subjects
     * @param p The predicate sought.
     * @param o The property value sought.
 
     */
    ResIterator listSubjectsWithProperty(Property p, String o) ;
    /** lists all subjects with a given property and property value.
 
     * @return an iterator over the set of subjects
     * @param p The predicate sought.
     * @param o The property value sought.
     * @param l the language associated with the object
 
     */
    ResIterator listSubjectsWithProperty(Property p, String o, String l) ;
    
    /** List all subjects with a given property and property value.
     * @deprecated Applications should use typed literals
     * @return an iterator over the subjects
     * @param p The predicate sought
     * @param o The value sought
 
     */
    ResIterator listSubjectsWithProperty(Property p, Object o) ;
    
    /** Determine if a statement is present in this model.
     * @return true if the statement with subject s, property p and object o
     * is in the model, false otherwise
     * @param s The subject of the statment tested.
     * @param p The predicate of the statement tested.
     * @param o The object of the statement tested.
     * @deprecated Applications should use typed literals
     */
    boolean contains(Resource s, Property p, boolean o) ;
    
    /** Determine if a statement is present in this model.
     * @return true if the statement with subject s, property p and object o
     * is in the model, false otherwise
     * @param s The subject of the statment tested.
     * @param p The predicate of the statement tested.
     * @param o The object of the statement tested.
     * @deprecated Applications should use typed literals
     */
    boolean contains(Resource s, Property p, long o) ;
    
    /** Determine if a statement is present in this model.
     * @return true if the statement with subject s, property p and object o
     * is in the model, false otherwise
     * @param s The subject of the statment tested.
     * @param p The predicate of the statement tested.
     * @param o The object of the statement tested.
     * @deprecated Applications should use typed literals
     */
    boolean contains(Resource s, Property p, char o) ;
    
    /** Determine if a statement is present in this model.
     * @return true if the statement with subject s, property p and object o
     * is in the model, false otherwise
     * @param s The subject of the statment tested.
     * @param p The predicate of the statement tested.
     * @param o The object of the statement tested.
     * @deprecated Applications should use typed literals
     */
    boolean contains(Resource s, Property p, float o) ;
    
    /** Determine if a statement is present in this model.
     * @return true if the statement with subject s, property p and object o
     * is in the model, false otherwise
     * @param s The subject of the statment tested.
     * @param p The predicate of the statement tested.
     * @param o The object of the statement tested.
     * @deprecated Applications should use typed literals
     */
    boolean contains(Resource s, Property p, double o) ;
    
    /** Determine if a statement is present in this model.
     * @return true if the statement with subject s, property p and object o
     * is in the model, false otherwise
     * @param s The subject of the statment tested.
     * @param p The predicate of the statement tested.
     * @param o The object of the statement tested.
     */
    boolean contains(Resource s, Property p, String o) ;
    
    /** Determine if a statement is present in this model.
     * @return true if the statement with subject s, property p and object o
     * is in the model, false otherwise
     * @param s The subject of the statment tested.
     * @param p The predicate of the statement tested.
     * @param o The object of the statement tested.
     * @param l the language associated with the object
     */
    boolean contains(Resource s, Property p, String o, String l)
    ;
    
    /** Determine if a statement is present in this model.
     * @return true if the statement with subject s, property p and object o
     * is in the model, false otherwise
     * @param s The subject of the statment tested.
     * @param p The predicate of the statement tested.
     * @param o The object of the statement tested.
     * @deprecated Applications should use typed literals
     */
    boolean contains(Resource s, Property p, Object o) ;
}