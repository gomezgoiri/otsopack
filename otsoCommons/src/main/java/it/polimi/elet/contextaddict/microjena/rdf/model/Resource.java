/*****************************************************************************
 * Source code information
 * -----------------------
 * Original author    Ian Dickinson, HP Labs Bristol
 * Author email       Ian.Dickinson@hp.com
 * Package            Jena 2
 * Web                http://sourceforge.net/projects/jena/
 * Created            08-May-2003
 * Filename           $RCSfile: Resource.java,v $
 * Revision           $Revision: 1.8 $
 * Release status     $State: Exp $
 *
 * Last modified on   $Date: 2007/01/02 11:48:48 $
 *               by   $Author: andy_seaborne $
 *
 * (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 * (see footer for full conditions)
 *****************************************************************************/

/*
 * Resource.java
 *
 * Created on 4 agosto 2007, 18.18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.polimi.elet.contextaddict.microjena.rdf.model;

import it.polimi.elet.contextaddict.microjena.datatypes.RDFDatatype;
import it.polimi.elet.contextaddict.microjena.graph.Node;
import it.polimi.elet.contextaddict.microjena.shared.PropertyNotFoundException;


/** An RDF Resource.

    Resource instances when created may be associated with a specific model.
    Resources created <i>by</i> a model will refer to that model, and support
    a range of methods, such as <code>getProperty()</code> and
    <code>addProperty()</code> which will access or modify that model.  This
    enables the programmer to write code in a compact and easy style.
    
<p>
    Resources created by ResourceFactory will not refer to any model, and will 
    not permit operations which require a model. Such resources are useful
    as general constants.
 
  <p>This interface provides methods supporting typed literals.  This means
     that methods are provided which will translate a built in type, or an
     object to an RDF Literal.  This translation is done by invoking the
     <CODE>toString()</CODE> method of the object, or its built in equivalent.
     The reverse translation is also supported.  This is built in for built
     in types.  Factory objects, provided by the application, are used
     for application objects.</p>
  <p>This interface provides methods for supporting enhanced resources.  An
     enhanced resource is a resource to which the application has added
     behaviour.  RDF containers are examples of enhanced resources built in
     to this package.  Enhanced resources are supported by encapsulating a
     resource created by an implementation in another class which adds
     the extra behaviour.  Factory objects are used to construct such
     enhanced resources.</p>
  @author bwm
  @version Release='$Name:  $' Revision='$Revision: 1.22 $' Date='$Date: 2007/01/02 11:48:35 $'
*/
public interface Resource extends RDFNode {

    /** Returns an a unique identifier for anonymous resources.
     *
     * <p>The id is unique within the scope of a particular implementation.  All
     * models within an implementation will use the same id for the same anonymous
     * resource.</p>
     *
     * <p>This method is undefined if called on resources which are not anonymous
     * and may raise an exception.</p>
     * @return A unique id for an anonymous resource.
     */
    public AnonId getId();

    /**
        Answer the underlying [SPI] Node of this Resource.
        @deprecated use asNode().
    */
    public Node getNode();

    /**
        Answer true iff this Resource is a URI resource with the given URI.
        Using this is preferred to using getURI() and .equals().
    */
    public boolean hasURI( String uri );

    /** Return the URI of the resource, or null if it's a bnode.
     * @return The URI of the resource, or null if it's a bnode.
     */
    public String getURI();

    /** Returns the namespace associated with this resource.
     * @return The namespace for this property.
     */
    public String getNameSpace();

    /** Returns the name of this resource within its namespace.
     * @return The name of this property within its namespace.
     */
    public String getLocalName();

    /** Return a string representation of the resource.
     *
     * Returns the URI of the resource unless the resource is anonymous
     * in which case it returns the id of the resource enclosed in square
     * brackets.
     * @return Return a string representation of the resource.
     * if it is anonymous.
     */
    public String toString();

    /** Determine whether two objects represent the same resource.
     *
     * <p>A resource can only be equal to another resource.
     * If both resources are not anonymous, then they are equal if the URI's are
     * equal.  If both resources are anonymous, they are equal only if there Id's
     * are the same.  If one resource is anonymous and the other is not, then they
     * are not equal.</p>
     * @param o The object to be compared.
     * @return true if and only if both objects are equal
     */
    public boolean equals( Object o );

    /** Get a property value of this resource.
     *
     * <p>The model associated with the resource instance is searched for statements
     * whose subject is this resource and whose predicate is p.  If such a statement
     * is found, it is returned.  If several such statements are found, any one may
     * be returned.  If no such statements are found, an exception is thrown.</p>
     * @param p The property sought.
     * @return some (this, p, ?O) statement if one exists
     * @throws PropertyNotFoundException if no such statement found
     */
    public Statement getRequiredProperty( Property p );

    /**
     Answer some statement (this, p, O) in the associated model. If there are several
     such statements, any one of them may be returned. If no such statements exist,
     null is returned - in this is differs from getRequiredProperty.
     @param p the property sought
     @return a statement (this, p, O), or null if no such statements exist here
     */
    public Statement getProperty( Property p );

    /** List all the values of the property p.
     *
     * <p>Returns an iterator over all the statements in the associated model whose
     * subject is this resource and whose predicate is p.</p>
     * @param p The predicate sought.
     * @return An iterator over the statements.
     */
    public StmtIterator listProperties( Property p );

    /** Return an iterator over all the properties of this resource.
     *
     * <p>The model associated with this resource is search and an iterator is
     * returned which iterates over all the statements which have this resource
     * as a subject.</p>
     * @return An iterator over all the statements about this object.
     */
    public StmtIterator listProperties();

    /** Add a property to this resource.
     *
     * <p>A statement with this resource as the subject, p as the predicate and o
     * as the object is added to the model associated with this resource.</p>
     * <p> o is converted to a string by calling its <CODE>toString()</CODE>
     * method.</p>
     * @deprecated Applications should use typed literals 
     * @param p The property to be added.
     * @param o The value of the property to be added.
     * @return This resource to allow cascading calls.
     */
    public Resource addProperty( Property p, boolean o );

    /** Add a property to this resource.
     *
     * <p>A statement with this resource as the subject, p as the predicate and o
     * as the object is added to the model associated with this resource.</p>
     * <p> o is converted to a string by calling its <CODE>toString()</CODE>
     * method.</p>
     * @deprecated Applications should use typed literals 
     * @param p The property to be added.
     * @param o The value of the property to be added.
     * @return This resource to allow cascading calls.
     */
    public Resource addProperty( Property p, long o );

    /** Add a property to this resource.
     *
     * <p>A statement with this resource as the subject, p as the predicate and o
     * as the object is added to the model associated with this resource.</p>
     * <p> o is converted to a string by calling its <CODE>toString()</CODE>
     * method.</p>
     * @deprecated Applications should use typed literals 
     * @param p The property to be added.
     * @param o The value of the property to be added.
     * @return This resource to allow cascading calls.
     */
    public Resource addProperty( Property p, char o );

    /** Add a property to this resource.
     *
     * <p>A statement with this resource as the subject, p as the predicate and o
     * as the object is added to the model associated with this resource.</p>
     * <p> o is converted to a string by calling its <CODE>toString()</CODE>
     * method.</p>
     * @deprecated Applications should use typed literals 
     * @param p The property to be added.
     * @param o The value of the property to be added.
     * @return This resource to allow cascading calls.
     */
    public Resource addProperty( Property p, float o );

    /** Add a property to this resource.
     *
     * <p>A statement with this resource as the subject, p as the predicate and o
     * as the object is added to the model associated with this resource.</p>
     * <p> o is converted to a string by calling its <CODE>toString()</CODE>
     * method.</p>
     * @deprecated Applications should use typed literals 
     * @param p The property to be added.
     * @param o The value of the property to be added.
     * @return This resource to allow cascading calls.
     */
    public Resource addProperty( Property p, double o );

    /** Add a property to this resource.
     *
     * <p>A statement with this resource as the subject, p as the predicate and o
     * as the object is added to the model associated with this resource.</p>
     * @param p The property to be added.
     * @param o The value of the property to be added.
     * @return This resource to allow cascading calls.
     */
    public Resource addProperty( Property p, String o );

    /** Add a property to this resource.
     *
     * <p>A statement with this resource as the subject, p as the predicate and o
     * as the object is added to the model associated with this resource.</p>
     * @param p The property to be added.
     * @param o The value of the property to be added.
     * @param l the language of the property
     * @return This resource to allow cascading calls.
     */
    public Resource addProperty( Property p, String o, String l );

    /** Add a property to this resource.
    *
    * <p>A statement with this resource as the subject, p as the predicate and o
    * as the object is added to the model associated with this resource.</p>
    * @param p The property to be added.
    * @param lexicalForm  The lexical form of the literal
    * @param datatype     The datatype
    * @return This resource to allow cascading calls.
    */
   public Resource addProperty( Property p, String lexicalForm, RDFDatatype datatype );


   /** Add a property to this resource.
     *
     * <p>A statement with this resource as the subject, p as the predicate and o
     * as the object is added to the model associated with this resource.</p>
     * <p> o is converted to a string by calling its <CODE>toString()</CODE>
     * method.</p>
     * @deprecated Applications should use typed literals 
     * @param p The property to be added.
     * @param o The value of the property to be added.
     * @return This resource to allow cascading calls.
     */
    public Resource addProperty( Property p, Object o );

    /** Add a property to this resource.
     *
     * <p>A statement with this resource as the subject, p as the predicate and o
     * as the object is added to the model associated with this resource.</p>
     * @param p The property to be added.
     * @param o The value of the property to be added.
     * @return This resource to allow cascading calls.
     */
    public Resource addProperty( Property p, RDFNode o );

    /** Determine whether this resource has any values for a given property.
     * @param p The property sought.
     * @return true if and only if this resource has at least one
     * value for the property.
     */
    public boolean hasProperty( Property p );

    /** Test if this resource has a given property with a given value.
     * @deprecated Applications should use typed literals 
     * @param p The property sought.
     * @param o The value of the property sought.
     * @return true if and only if this resource has property p with
     * value o.
     */
    public boolean hasProperty( Property p, boolean o );

    /** Test if this resource has a given property with a given value.
     * @deprecated Applications should use typed literals 
     * @param p The property sought.
     * @param o The value of the property sought.
     * @return true if and only if this resource has property p with
     * value o.
     */
    public boolean hasProperty( Property p, long o );

    /** Test if this resource has a given property with a given value.
     * @deprecated Applications should use typed literals 
     * @param p The property sought.
     * @param o The value of the property sought.
     * @return true if and only if this resource has property p with
     * value o.
     */
    public boolean hasProperty( Property p, char o );

    /** Test if this resource has a given property with a given value.
     * @deprecated Applications should use typed literals 
     * @param p The property sought.
     * @param o The value of the property sought.
     * @return true if and only if this resource has property p with
     * value o.
     */
    public boolean hasProperty( Property p, float o );

    /** Test if this resource has a given property with a given value.
     * @deprecated Applications should use typed literals 
     * @param p The property sought.
     * @param o The value of the property sought.
     * @return true if and only if this resource has property p with
     * value o.
     */
    public boolean hasProperty( Property p, double o );

    /** Test if this resource has a given property with a given value.
     * @param p The property sought.
     * @param o The value of the property sought.
     * @return true if and only if this resource has property p with
     * value o.
     */
    public boolean hasProperty( Property p, String o );

    /** Test if this resource has a given property with a given value.
     * @param p The property sought.
     * @param o The value of the property sought.
     * @param l The language of the property sought.
     * @return true if and only if this resource has property p with
     * value o.
     */
    public boolean hasProperty( Property p, String o, String l );

    /** Test if this resource has a given property with a given value.
     * @deprecated Applications should use typed literals 
     * @param p The property sought.
     * @param o The value of the property sought.
     * @return true if and only if this resource has property p with
     * value o.
     */
    public boolean hasProperty( Property p, Object o );

    /** Test if this resource has a given property with a given value.
     * @param p The property sought.
     * @param o The value of the property sought.
     * @return true if and only if this resource has property p with
     * value o.
     */
    public boolean hasProperty( Property p, RDFNode o );

    /** Delete all the properties for this resource from the associated model.
     * @return This resource to permit cascading.
     */
    public Resource removeProperties();

    /**
     Delete all the statements with predicate <code>p</code> for this resource
     from its associated model.

     @param p the property to remove
     @return this resource, to permit cascading
     */
    public Resource removeAll( Property p );

    /** Return the model associated with this resource. If the Resource
     * was not created by a Model, the result may be null.
     * 
     * @return The model associated with this resource.
     */
    public Model getModel();
    
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
