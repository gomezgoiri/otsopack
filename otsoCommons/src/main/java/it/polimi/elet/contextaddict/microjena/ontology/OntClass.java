/*****************************************************************************
 * Source code information
 * -----------------------
 * Original author    Ian Dickinson, HP Labs Bristol
 * Author email       Ian.Dickinson@hp.com
 * Package            Jena 2
 * Web                http://sourceforge.net/projects/jena/
 * Created            08-May-2003
 * Filename           $RCSfile: OntClass.java,v $
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
 * OntClass.java
 *
 * Created on 4 agosto 2007, 18.34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.polimi.elet.contextaddict.microjena.ontology;

import it.polimi.elet.contextaddict.microjena.rdf.model.Property;
import it.polimi.elet.contextaddict.microjena.rdf.model.Resource;
import it.polimi.elet.contextaddict.microjena.util.iterator.ExtendedIterator;

/**
 * <p>
 * Interface that represents an ontology node characterising a class description.
 * </p>
 *
 * @author Ian Dickinson, HP Labs
 *         (<a  href="mailto:Ian.Dickinson@hp.com" >email</a>)
 * @version CVS $Id: OntClass.java,v 1.28 2007/01/02 11:48:48 andy_seaborne Exp $
 */
public interface OntClass extends OntResource {

    // subClassOf
    
    /**
     * <p>Assert that this class is sub-class of the given class. Any existing
     * statements for <code>subClassOf</code> will be removed.</p>
     * @param cls The class that this class is a sub-class of
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public void setSuperClass( Resource cls );

    /**
     * <p>Add a super-class of this class.</p>
     * @param cls A class that is a super-class of this class.
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public void addSuperClass( Resource cls );

    /**
     * <p>Answer a class that is the super-class of this class. If there is
     * more than one such class, an arbitrary selection is made. If there
     * is no such super-class, return null.</p>
     * @return A super-class of this class or null
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()}
     * property is not supported in the current language profile.
     */
    public OntClass getSuperClass();

    /**
     * <p>Answer an iterator over all of the classes that are declared to be super-classes of
     * this class. Each element of the iterator will be an {@link OntClass}.</p>
     * @return An iterator over the super-classes of this class.
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public ExtendedIterator listSuperClasses();

    /**
     * <p>Answer an iterator over all of the classes that are declared to be super-classes of
     * this class. Each element of the iterator will be an {@link OntClass}.
     * See {@link #listSubClasses( boolean )} for a full explanation of the <em>direct</em>
     * parameter.
     * </p>
     *
     * @param direct If true, only answer the direcly adjacent classes in the
     * super-class relation: i&#046;e&#046; eliminate any class for which there is a longer route
     * to reach that child under the super-class relation.
     * @return an iterator over the resources representing this class's sub-classes.
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public ExtendedIterator listSuperClasses( boolean direct );

    /**
     * <p>Answer true if the given class is a super-class of this class.</p>
     * @param cls A class to test.
     * @return True if the given class is a super-class of this class.
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public boolean hasSuperClass( Resource cls );

    /**
     * <p>Answer true if this class has any super-class in the model. Note that
     * when using a reasoner, all OWL classes have owl:Thing as a super-class.</p>
     * @return True if this class has any known super-class.
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public boolean hasSuperClass();

    /**
     * <p>Answer true if the given class is a super-class of this class.
     * See {@link #listSubClasses( boolean )} for a full explanation of the <em>direct</em>
     * parameter.
     * </p>
     * @param cls A class to test.
     * @param direct If true, only search the classes that are directly adjacent to this
     * class in the class hierarchy.
     * @return True if the given class is a super-class of this class.
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public boolean hasSuperClass( Resource cls, boolean direct );

    /**
     * <p>Remove the given class from the super-classes of this class.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param cls A class to be removed from the super-classes of this class
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} class is not supported in the current language profile.
     */
    public void removeSuperClass( Resource cls);

    /**
     * <p>Assert that this class is super-class of the given class. Any existing
     * statements for <code>subClassOf</code> on <code>prop</code> will be removed.</p>
     * @param cls The class that is a sub-class of this class
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public void setSubClass( Resource cls );

    /**
     * <p>Add a sub-class of this class.</p>
     * @param cls A class that is a sub-class of this class.
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public void addSubClass( Resource cls );

    /**
     * <p>Answer a class that is the sub-class of this class. If there is
     * more than one such class, an arbitrary selection is made. If
     * there is no such class, return null.</p>
     * @return A sub-class of this class or null
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()}
     * property is not supported in the current language profile.
     */
    public OntClass getSubClass();

    /**
     * <p>Answer an iterator over all of the classes that are declared to be sub-classes of
     * this class. Each element of the iterator will be an {@link OntClass}.</p>
     * @return An iterator over the sub-classes of this class.
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public ExtendedIterator listSubClasses();

    /**
     * <p>
     * Answer an iterator over the classes that are declared to be sub-classes of
     * this class. Each element of the iterator will be an {@link OntClass}. The
     * distinguishing extra parameter for this method is the flag <code>direct</code>
     * that allows some selectivity over the classes that appear in the iterator.
     * Consider the following scenario:
     * <code><pre>
     *   :B rdfs:subClassOf :A.
     *   :C rdfs:subClassOf :A.
     *   :D rdfs:subClassof :C.
     * </pre></code>
     * (so A has two sub-classes, B and C, and C has sub-class D).  In a raw model, with
     * no inference support, listing the sub-classes of A will answer B and C.  In an
     * inferencing model, <code>rdfs:subClassOf</code> is known to be transitive, so
     * the sub-classes iterator will include D.  The <code>direct</code> sub-classes
     * are those members of the closure of the subClassOf relation, restricted to classes that
     * cannot be reached by a longer route, i.e. the ones that are <em>directly</em> adjacent
     * to the given root.  Thus, the direct sub-classes of A are B and C only, and not D -
     * even in an inferencing graph.  Note that this is not the same as the entailments
     * from the raw graph. Suppose we add to this example:
     * <code><pre>
     *   :D rdfs:subClassof :A.
     * </pre></code>
     * Now, in the raw graph, A has sub-class C.  But the direct sub-classes of A remain
     * B and C, since there is a longer path A-C-D that means that D is not a direct sub-class
     * of A.  The assertion in the raw graph that A has sub-class D is essentially redundant,
     * since this can be inferred from the closure of the graph.
     * </p>
     * <p>
     * <strong>Note:</strong> This is is a change from the behaviour of Jena 1, which took a
     * parameter <code>closed</code> to compute the closure over transitivity and equivalence
     * of sub-classes.  The closure capability in Jena2 is determined by the inference engine
     * that is wrapped with the ontology model.  The direct parameter is provided to allow,
     * for exmaple, a level-by-level traversal of the class hierarchy, starting at some given
     * root. Observe that in Jena 1, passing <code>true</code> will tend to increase the number of
     * results returned; in Jena 2 passing <code>true</code> will tend to reduce the number
     * of results.
     * </p>
     *
     * @param direct If true, only answer the direcly adjacent classes in the
     * sub-class relation: i&#046;e&#046; eliminate any class for which there is a longer route
     * to reach that child under the sub-class relation.
     * @return an iterator over the resources representing this class's sub-classes
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public ExtendedIterator listSubClasses( boolean direct );

    /**
     * <p>Answer true if the given class is a sub-class of this class.</p>
     * @param cls A class to test.
     * @return True if the given class is a sub-class of this class.
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public boolean hasSubClass( Resource cls );

    /**
     * <p>Answer true if this class has any sub-class in the model. Note that
     * when using a reasoner, all OWL classes have owl:Nothing as a sub-class.</p>
     * @return True if this class has any known sub-class.
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public boolean hasSubClass();

    /**
     * <p>Answer true if the given class is a sub-class of this class.
     * See {@link #listSubClasses( boolean )} for a full explanation of the <em>direct</em>
     * parameter.
     * </p>
     * @param cls A class to test.
     * @param direct If true, only search the classes that are directly adjacent to this
     * class in the class hierarchy.
     * @return True if the given class is a sub-class of this class.
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public boolean hasSubClass( Resource cls, boolean direct );

    /**
     * <p>Remove the given class from the sub-classes of this class.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param cls A class to be removed from the sub-classes of this class
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} class is not supported in the current language profile.
     */
    public void removeSubClass( Resource cls );


    // equivalentClass

    /**
     * <p>Assert that the given class is equivalent to this class. Any existing
     * statements for <code>equivalentClass</code> will be removed.</p>
     * @param cls The class that this class is a equivalent to.
     * @exception OntProfileException If the {@link Profile#EQUIVALENT_CLASS()} property is not supported in the current language profile.
     */
    public void setEquivalentClass( Resource cls );

    /**
     * <p>Add a class that is equivalent to this class.</p>
     * @param cls A class that is equivalent to this class.
     * @exception OntProfileException If the {@link Profile#EQUIVALENT_CLASS()} property is not supported in the current language profile.
     */
    public void addEquivalentClass( Resource cls );

    /**
     * <p>Answer a class that is equivalent to this class. If there is
     * more than one such class, an arbitrary selection is made.</p>
     * @return A class equivalent to this class
     * @exception OntProfileException If the {@link Profile#EQUIVALENT_CLASS()} property is not supported in the current language profile.
     */
    public OntClass getEquivalentClass();

    /**
     * <p>Answer an iterator over all of the classes that are declared to be equivalent classes to
     * this class. Each element of the iterator will be an {@link OntClass}.</p>
     * @return An iterator over the classes equivalent to this class.
     * @exception OntProfileException If the {@link Profile#EQUIVALENT_CLASS()} property is not supported in the current language profile.
     */
    public ExtendedIterator listEquivalentClasses();

    /**
     * <p>Answer true if the given class is equivalent to this class.</p>
     * @param cls A class to test for
     * @return True if the given property is equivalent to this class.
     * @exception OntProfileException If the {@link Profile#EQUIVALENT_CLASS()} property is not supported in the current language profile.
     */
    public boolean hasEquivalentClass( Resource cls );

    /**
     * <p>Remove the statement that this class and the given class are
     * equivalent.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param cls A class that may be declared to be equivalent to this class, and which is no longer equivalent
     * @exception OntProfileException If the {@link Profile#EQUIVALENT_CLASS()()} property is not supported in the current language profile.
     */
    public void removeEquivalentClass( Resource cls );


    // disjointWith

    /**
     * <p>Assert that this class is disjoint with the given class. Any existing
     * statements for <code>disjointWith</code> will be removed.</p>
     * @param cls The property that this class is disjoint with.
     * @exception OntProfileException If the {@link Profile#DISJOINT_WITH()} property is not supported in the current language profile.
     */
    public void setDisjointWith( Resource cls );

    /**
     * <p>Add a class that this class is disjoint with.</p>
     * @param cls A class that has no instances in common with this class.
     * @exception OntProfileException If the {@link Profile#DISJOINT_WITH()} property is not supported in the current language profile.
     */
    public void addDisjointWith( Resource cls );

    /**
     * <p>Answer a class with which this class is disjoint. If there is
     * more than one such class, an arbitrary selection is made.</p>
     * @return A class disjoint with this class
     * @exception OntProfileException If the {@link Profile#DISJOINT_WITH()} property is not supported in the current language profile.
     */
    public OntClass getDisjointWith();

    /**
     * <p>Answer an iterator over all of the classes that this class is declared to be disjoint with.
     * Each element of the iterator will be an {@link OntClass}.</p>
     * @return An iterator over the classes disjoint with this class.
     * @exception OntProfileException If the {@link Profile#DISJOINT_WITH()} property is not supported in the current language profile.
     */
    public ExtendedIterator listDisjointWith();

    /**
     * <p>Answer true if this class is disjoint with the given class.</p>
     * @param cls A class to test
     * @return True if the this class is disjoint with the the given class.
     * @exception OntProfileException If the {@link Profile#DISJOINT_WITH()} property is not supported in the current language profile.
     */
    public boolean isDisjointWith( Resource cls );

    /**
     * <p>Remove the statement that this class and the given class are
     * disjoint.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param cls A class that may be declared to be disjoint with this class, and which is no longer disjoint
     * @exception OntProfileException If the {@link Profile#DISJOINT_WITH()()()} property is not supported in the current language profile.
     */
    public void removeDisjointWith( Resource cls );


    // other utility methods

    /**
     * <p>Answer an iteration of the properties associated with a frame-like
     * view of this class. Note that many cases of determining whether a
     * property is associated with a class depends on RDFS or OWL reasoning.
     * This method may therefore return complete results only in models that
     * have an attached reasoner.
     * See the
     * <a href="../../../../../../how-to/rdf-frames.html">RDF frames how-to</a>
     * for full details.<p>
     * @return An iteration of the properties that are associated with this class
     * by their domain.
     */
    public ExtendedIterator listDeclaredProperties();


    /**
     * <p>Answer an iteration of the properties associated with a frame-like
     * view of this class. Note that many cases of determining whether a
     * property is associated with a class depends on RDFS or OWL reasoning.
     * This method may therefore return complete results only in models that
     * have an attached reasoner. See the
     * <a href="../../../../../../how-to/rdf-frames.html">RDF frames how-to</a>
     * for full details.<p>
     * @param direct If true, restrict the properties returned to those directly
     * associated with this class.
     * @return An iteration of the properties that are associated with this class
     * by their domain.
     */
    public ExtendedIterator listDeclaredProperties( boolean direct );


    /**
     * <p>Answer true if the given property is one of the declared properties
     * of this class. For details, see {@link #listDeclaredProperties(boolean)}.</p>
     * @param p A property to test
     * @param direct If true, only direct associations between classes and properties
     * are considered
     * @return True if <code>p</code> is one of the declared properties of
     * this class
     */
    public boolean hasDeclaredProperty( Property p, boolean direct );


    /**
     * <p>Answer an iterator over the individuals in the model that have this
     * class among their types. Equivalent to {@link #listInstances(boolean)}
     * with an argument <code>false</code>.<p>
     *
     * @return An iterator over those instances that have this class as one of
     *         the classes to which they belong
     */
    public ExtendedIterator listInstances();


    /**
     * <p>Answer an iterator over the individuals in the model that have this
     * class among their types, optionally excluding sub-classes of this class.<p>
     *
     * @param  direct If true, only direct instances are counted (i.e. not instances
     * of sub-classes of this class)
     * @return An iterator over those instances that have this class as one of
     *         the classes to which they belong
     */
    public ExtendedIterator listInstances( boolean direct );


    /**
     * <p>Answer a new individual that has this class as its <code>rdf:type</code></p>
     * @return A new anonymous individual that is an instance of this class
     */
    public Individual createIndividual();


    /**
     * <p>Answer a new individual that has this class as its <code>rdf:type</code></p>
     * @param uri The URI of the new individual
     * @return A new named individual that is an instance of this class
     */
    public Individual createIndividual( String uri );

    /**
     * <p>Answer true if this class is one of the roots of the local class hierarchy.
     * This will be true if either (i) this class has <code>owl:Thing</code>
     * (or <code>daml:Thing</code>) as a direct super-class, or (ii) it has
     * no declared super-classes (including anonymous class expressions).</p>
     * @return True if this class is the root of the class hierarchy in the
     * model it is attached to
     */
    public boolean isHierarchyRoot();

    /**
     * <p>Answer true if this class is an enumerated class expression</p>
     * @return True if this is an enumerated class expression
     */
    public boolean isEnumeratedClass();

    /**
     * <p>Answer true if this class is a union class expression</p>
     * @return True if this is a union class expression
     */
    public boolean isUnionClass();

    /**
     * <p>Answer true if this class is an intersection class expression</p>
     * @return True if this is an intersection class expression
     */
    public boolean isIntersectionClass();

    /**
     * <p>Answer true if this class is a complement class expression</p>
     * @return True if this is a complement class expression
     */
    public boolean isComplementClass();

    /**
     * <p>Answer true if this class is a property restriction</p>
     * @return True if this is a restriction
     */
    public boolean isRestriction();

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


