/*****************************************************************************
 * Source code information
 * -----------------------
 * Original author    Ian Dickinson, HP Labs Bristol
 * Author email       Ian.Dickinson@hp.com
 * Package            Jena 2
 * Web                http://sourceforge.net/projects/jena/
 * Created            27-Mar-2003
 * Filename           $RCSfile: OntClassImpl.java,v $
 * Revision           $Revision: 1.53 $
 * Release status     $State: Exp $
 *
 * Last modified on   $Date: 2007/01/02 11:49:47 $
 *               by   $Author: andy_seaborne $
 *
 * (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 * (see footer for full conditions)
 *****************************************************************************/

package it.polimi.elet.contextaddict.microjena.ontology.impl;

import it.polimi.elet.contextaddict.microjena.enhanced.EnhGraph;
import it.polimi.elet.contextaddict.microjena.enhanced.EnhNode;
import it.polimi.elet.contextaddict.microjena.enhanced.Implementation;
import it.polimi.elet.contextaddict.microjena.graph.Node;
import it.polimi.elet.contextaddict.microjena.ontology.ConversionException;
import it.polimi.elet.contextaddict.microjena.ontology.Individual;
import it.polimi.elet.contextaddict.microjena.ontology.OntClass;
import it.polimi.elet.contextaddict.microjena.ontology.OntModel;
import it.polimi.elet.contextaddict.microjena.ontology.Profile;
import it.polimi.elet.contextaddict.microjena.rdf.model.Property;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFNode;
import it.polimi.elet.contextaddict.microjena.rdf.model.Resource;
import it.polimi.elet.contextaddict.microjena.rdf.model.Statement;
import it.polimi.elet.contextaddict.microjena.rdf.model.StmtIterator;
import it.polimi.elet.contextaddict.microjena.rdf.model.impl.ResourceImpl;
import it.polimi.elet.contextaddict.microjena.util.Iterator;
import it.polimi.elet.contextaddict.microjena.util.List;
import it.polimi.elet.contextaddict.microjena.util.Set;
import it.polimi.elet.contextaddict.microjena.util.iterator.ExtendedIterator;
import it.polimi.elet.contextaddict.microjena.util.iterator.WrappedIterator;
import it.polimi.elet.contextaddict.microjena.vocabulary.OWL;
import it.polimi.elet.contextaddict.microjena.vocabulary.RDF;
import it.polimi.elet.contextaddict.microjena.vocabulary.RDFS;

/**
 * <p>
 * Implementation of the ontology abstraction representing ontology classes.
 * </p>
 *
 * @author Ian Dickinson, HP Labs
 *         (<a  href="mailto:Ian.Dickinson@hp.com" >email</a>)
 * @version CVS $Id: OntClassImpl.java,v 1.53 2007/01/02 11:49:47 andy_seaborne Exp $
 */
public class OntClassImpl extends OntResourceImpl implements OntClass {

    /* LDP never returns properties in these namespaces */
    private static final String[] IGNORE_NAMESPACES = new String[] {
	OWL.NS,
	RDF.getURI(),
	RDFS.getURI(),
    };
    
    /**
     * A factory for generating OntClass facets from nodes in enhanced graphs.
     * Note: should not be invoked directly by user code: use
     * {@link tesi.rdf.model.RDFNode#as as()} instead.
     */
    public static Implementation factory = new Implementation() {
	public EnhNode wrap( Node n, EnhGraph eg ) {
	    if (canWrap( n, eg )) {
		return new OntClassImpl( n, eg );
	    } else {
		throw new ConversionException( "Cannot convert node " + n.toString() + " to OntClass: it does not have rdf:type owl:Class or equivalent");
	    }
	}
	
	public boolean canWrap( Node node, EnhGraph eg ) {
	    // node will support being an OntClass facet if it has rdf:type owl:Class or equivalent
	    Profile profile = (eg instanceof OntModel) ? ((OntModel) eg).getProfile() : null;
	    return (profile != null)  &&  profile.isSupported( node, eg, OntClass.class );
	}
    };
    
    /**
     * <p>
     * Construct an ontology class node represented by the given node in the given graph.
     * </p>
     *
     * @param n The node that represents the resource
     * @param g The enh graph that contains n
     */
    public OntClassImpl( Node n, EnhGraph g ) {
	super( n, g );
    }
    
    // subClassOf
    
    /**
     * <p>Assert that this class is sub-class of the given class. Any existing
     * statements for <code>subClassOf</code> will be removed.</p>
     * @param cls The class that this class is a sub-class of
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public void setSuperClass( Resource cls ) {
	setPropertyValue( getProfile().SUB_CLASS_OF(), "SUB_CLASS_OF", cls );
    }
    
    /**
     * <p>Add a super-class of this class.</p>
     * @param cls A class that is a super-class of this class.
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public void addSuperClass( Resource cls ) {
	addPropertyValue( getProfile().SUB_CLASS_OF(), "SUB_CLASS_OF", cls );
    }
    
    /**
     * <p>Answer a class that is the super-class of this class. If there is
     * more than one such class, an arbitrary selection is made.</p>
     * @return A super-class of this class
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public OntClass getSuperClass() {
	RDFNode rdfNode = getModel().getProperty(this, getProfile().SUB_CLASS_OF()).getObject();
	if(rdfNode != null) {
	    OntClass result = new OntClassImpl(rdfNode.asNode(), getModelCom());
	    return result;
	} else {
	    return null;
	}
    }
    
    /**
     * <p>Answer an iterator over all of the classes that are declared to be super-classes of
     * this class. Each element of the iterator will be an {@link OntClass}.</p>
     * @return An iterator over the super-classes of this class.
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public ExtendedIterator listSuperClasses() {
	return listSuperClasses( false );
    }
    
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
    public ExtendedIterator listSuperClasses( boolean direct ) {
	if(direct) {
	    StmtIterator stmts = getModel().listStatements(this, getProfile().SUB_CLASS_OF(), (RDFNode)null);
	    Set result = new Set();
	    while(stmts.hasNext())
		result.add(stmts.nextStatement().getObject());
	    return WrappedIterator.create(result.iterator());
	} else {
	    Set lower = new Set();
	    Set computed = new Set();
	    catchSuperClasses(this, lower, computed, getProfile().SUB_CLASS_OF());
	    return WrappedIterator.create(computed.iterator());
	}
    }
    
    /**
     * <p>Answer true if the given class is a super-class of this class.</p>
     * @param cls A class to test.
     * @return True if the given class is a super-class of this class.
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public boolean hasSuperClass( Resource cls ) {
	return hasSuperClass( cls, false );
    }
    
    /**
     * <p>Answer true if this class has any super-class in the model. Note that
     * when using a reasoner, all OWL classes have owl:Thing as a super-class.</p>
     * @return True if this class has any known super-class.
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public boolean hasSuperClass() {
	return getSuperClass() != null;
    }
    
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
    public boolean hasSuperClass( Resource cls, boolean direct ) {
	if (!direct) {
	    // don't need any special case, we just get the property
	    return hasPropertyValue( getProfile().SUB_CLASS_OF(), "SUB_CLASS_OF", cls );
	} else {
	    ExtendedIterator subClasses = listSuperClasses(false);
	    if(subClasses != null) {
		boolean result = false;
		while(!result && subClasses.hasNext())
		    result = subClasses.next().equals(cls);
		return result;
	    } else
		return false;
	}
    }
    
    /**
     * <p>Remove the given class from the super-classes of this class.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param cls A class to be removed from the super-classes of this class
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} class is not supported in the current language profile.
     */
    public void removeSuperClass( Resource cls ) {
	removePropertyValue( getProfile().SUB_CLASS_OF(), "SUB_CLASS_OF", cls );
    }
    
    /**
     * <p>Assert that this class is super-class of the given class. Any existing
     * statements for <code>subClassOf</code> on <code>prop</code> will be removed.</p>
     * @param cls The class that is a sub-class of this class
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public void setSubClass( Resource cls ) {
	// first we have to remove all of the inverse sub-class links
	checkProfile( getProfile().SUB_CLASS_OF(), "SUB_CLASS_OF" );
	getModel().remove(getModel().listStatements( null, getProfile().SUB_CLASS_OF(), this ));
	OntClass aus = new OntClassImpl(cls.asNode(), getModelCom());
	aus.addSuperClass( this );
    }
    
    /**
     * <p>Add a sub-class of this class.</p>
     * @param cls A class that is a sub-class of this class.
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public void addSubClass( Resource cls ) {
	OntClass aus = (OntClass)OntClassImpl.factory.wrap(cls.asNode(), getModelCom());
	aus.addSuperClass( this );
    }
    
    /**
     * <p>Answer a class that is the sub-class of this class. If there is
     * more than one such class, an arbitrary selection is made. If
     * there is no such class, return null.</p>
     * @return A sub-class of this class or null
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()}
     * property is not supported in the current language profile.
     */
    public OntClass getSubClass() {
	StmtIterator i = getModel().listStatements( null, getProfile().SUB_CLASS_OF(), this );
	try {
	    if (i.hasNext()) {
		return new OntClassImpl( i.nextStatement().getSubject().asNode(), getModelCom());
	    }
	    else {
		return null;
	    }
	} finally {
	    i.close();
	}
    }
    
    /**
     * <p>Answer an iterator over all of the classes that are declared to be sub-classes of
     * this class. Each element of the iterator will be an {@link OntClass}.</p>
     * @return An iterator over the sub-classes of this class.
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public ExtendedIterator listSubClasses() {
	return listSubClasses( false );
    }
    
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
     * root.
     * </p>
     *
     * @param direct If true, only answer the direcly adjacent classes in the
     * sub-class relation: i&#046;e&#046; eliminate any class for which there is a longer route
     * to reach that child under the sub-class relation.
     * @return an iterator over the resources representing this class's sub-classes
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public ExtendedIterator listSubClasses( boolean direct ) {
	if(direct) {
	    StmtIterator stmts = getModel().listStatements(null, getProfile().SUB_CLASS_OF(), this);
	    Set result = new Set();
	    while(stmts.hasNext())
		result.add(stmts.nextStatement().getSubject());
	    return WrappedIterator.create(result.iterator());
	}
	else {
	    Set lower = new Set();
	    Set computed = new Set();
	    catchSubClasses(this, lower, computed, getProfile().SUB_CLASS_OF());
	    ExtendedIterator it = WrappedIterator.create(computed.iterator());
	    Set result = new Set();
	    while(it.hasNext())
		result.add(new OntClassImpl(((RDFNode)it.next()).asNode(), getModelCom()));
	    return WrappedIterator.create(result.iterator());
	}
    }
    
    /**
     * <p>This method searches throw a tree of transitive properties 'relation'
     * It has to be called with 'lower' and 'computed' empty Sets
     * It returns the collection of resources in 'computed' and the only resource
     * which take place on a leave of the tree in the 'lower' set</p>
     */
    protected void catchSubClasses(Resource actualClass, Set lower, Set computed, Property relation) {
	//iterator on actualClass' subclasses
	StmtIterator subClasses = getModel().listStatements(null, relation, actualClass);
	Resource aus;
	while(subClasses.hasNext()) {
	    aus = new ResourceImpl(subClasses.nextStatement().getSubject().asNode(), getModelCom());
	    if(!computed.contains(aus)) {
		computed.add(aus);
		lower.remove(aus); //forse inutile
		catchSubClasses(aus, lower, computed, relation);
	    }
	    else {
		lower.remove(aus);
	    }
	}
    }
    
    /**
     * <p>Answer true if the given class is a sub-class of this class.</p>
     * @param cls A class to test.
     * @return True if the given class is a sub-class of this class.
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public boolean hasSubClass( Resource cls ) {
	return hasSubClass( cls, false );
    }
    
    /**
     * <p>Answer true if this class has any sub-class in the model. Note that
     * when using a reasoner, all OWL classes have owl:Nothing as a sub-class.</p>
     * @return True if this class has any known sub-class.
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} property is not supported in the current language profile.
     */
    public boolean hasSubClass() {
	return getSubClass() != null;
    }
    
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
    public boolean hasSubClass( Resource cls, boolean direct ) {
	if (getModel() instanceof OntModel &&
		(cls.getModel() == null || !(cls.getModel() instanceof OntModel))) {
	    // could be outside an ontmodel if a constant
	    cls = (Resource) cls.inModel( getModel() );
	}
	return (new OntClassImpl(cls.asNode(), getModelCom())).hasSuperClass(this,direct);
    }
    
    /**
     * <p>Remove the given class from the sub-classes of this class.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param cls A class to be removed from the sub-classes of this class
     * @exception OntProfileException If the {@link Profile#SUB_CLASS_OF()} class is not supported in the current language profile.
     */
    public void removeSubClass( Resource cls ) {
	(new OntClassImpl(cls.asNode(), getModelCom())).removeSuperClass( this );
    }
    
    
    // equivalentClass
    
    /**
     * <p>Assert that the given class is equivalent to this class. Any existing
     * statements for <code>equivalentClass</code> will be removed.</p>
     * @param cls The class that this class is a equivalent to.
     * @exception OntProfileException If the {@link Profile#EQUIVALENT_CLASS()} property is not supported in the current language profile.
     */
    public void setEquivalentClass( Resource cls ) {
	setPropertyValue( getProfile().EQUIVALENT_CLASS(), "EQUIVALENT_CLASS", cls );
    }
    
    /**
     * <p>Add a class that is equivalent to this class.</p>
     * @param cls A class that is equivalent to this class.
     * @exception OntProfileException If the {@link Profile#EQUIVALENT_CLASS()} property is not supported in the current language profile.
     */
    public void addEquivalentClass( Resource cls ) {
	addPropertyValue( getProfile().EQUIVALENT_CLASS(), "EQUIVALENT_CLASS", cls );
    }
    
    /**
     * <p>Answer a class that is equivalent to this class. If there is
     * more than one such class, an arbitrary selection is made.</p>
     * @return A class equivalent to this class
     * @exception OntProfileException If the {@link Profile#EQUIVALENT_CLASS()} property is not supported in the current language profile.
     */
    public OntClass getEquivalentClass() {
	ExtendedIterator aus = listEquivalentClasses();
	if(!aus.hasNext())
	    return null;
	else
	    return (OntClass)aus.next();
    }
    
    /**
     * <p>Answer an iterator over all of the classes that are declared to be equivalent classes to
     * this class. Each element of the iterator will be an {@link OntClass}.</p>
     * @return An iterator over the classes equivalent to this class.
     * @exception OntProfileException If the {@link Profile#EQUIVALENT_CLASS()} property is not supported in the current language profile.
     */
    public ExtendedIterator listEquivalentClasses() {
	StmtIterator stmt = listProperties(getProfile().EQUIVALENT_CLASS());
	Set result = new Set();
	while(stmt.hasNext())
	    result.add(new OntClassImpl(stmt.nextStatement().getObject().asNode(), getModelCom()));
	return WrappedIterator.create(result.iterator());
    }
    
    /**
     * <p>Answer true if the given class is equivalent to this class.</p>
     * @param cls A class to test for
     * @return True if the given property is equivalent to this class.
     * @exception OntProfileException If the {@link Profile#EQUIVALENT_CLASS()} property is not supported in the current language profile.
     */
    public boolean hasEquivalentClass( Resource cls ) {
	return hasPropertyValue( getProfile().EQUIVALENT_CLASS(), "EQUIVALENT_CLASS", cls );
    }
    
    /**
     * <p>Remove the statement that this class and the given class are
     * equivalent.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param cls A class that may be declared to be equivalent to this class, and which is no longer equivalent
     * @exception OntProfileException If the {@link Profile#EQUIVALENT_CLASS()()} property is not supported in the current language profile.
     */
    public void removeEquivalentClass( Resource cls ) {
	removePropertyValue( getProfile().EQUIVALENT_CLASS(), "EQUIVALENT_CLASS", cls );
    }
    
    // disjointWith
    
    /**
     * <p>Assert that this class is disjoint with the given class. Any existing
     * statements for <code>disjointWith</code> will be removed.</p>
     * @param cls The property that this class is disjoint with.
     * @exception OntProfileException If the {@link Profile#DISJOINT_WITH()} property is not supported in the current language profile.
     */
    public void setDisjointWith( Resource cls ) {
	setPropertyValue( getProfile().DISJOINT_WITH(), "DISJOINT_WITH", cls );
    }
    
    /**
     * <p>Add a class that this class is disjoint with.</p>
     * @param cls A class that has no instances in common with this class.
     * @exception OntProfileException If the {@link Profile#DISJOINT_WITH()} property is not supported in the current language profile.
     */
    public void addDisjointWith( Resource cls ) {
	addPropertyValue( getProfile().DISJOINT_WITH(), "DISJOINT_WITH", cls );
    }
    
    /**
     * <p>Answer a class with which this class is disjoint. If there is
     * more than one such class, an arbitrary selection is made.</p>
     * @return A class disjoint with this class
     * @exception OntProfileException If the {@link Profile#DISJOINT_WITH()} property is not supported in the current language profile.
     */
    public OntClass getDisjointWith() {
	ExtendedIterator aus = listDisjointWith();
	if(!aus.hasNext())
	    return null;
	else
	    return (OntClass)aus.next();
    }
    
    /**
     * <p>Answer an iterator over all of the classes that this class is declared to be disjoint with.
     * Each element of the iterator will be an {@link OntClass}.</p>
     * @return An iterator over the classes disjoint with this class.
     * @exception OntProfileException If the {@link Profile#DISJOINT_WITH()} property is not supported in the current language profile.
     */
    public ExtendedIterator listDisjointWith() {
	StmtIterator stmt = listProperties(getProfile().DISJOINT_WITH());
	Set result = new Set();
	while(stmt.hasNext())
	    result.add(new OntClassImpl(stmt.nextStatement().getObject().asNode(), getModelCom()));
	return WrappedIterator.create(result.iterator());
    }
    
    /**
     * <p>Answer true if this class is disjoint with the given class.</p>
     * @param cls A class to test
     * @return True if the this class is disjoint with the the given class.
     * @exception OntProfileException If the {@link Profile#DISJOINT_WITH()} property is not supported in the current language profile.
     */
    public boolean isDisjointWith( Resource cls ) {
	return hasPropertyValue( getProfile().DISJOINT_WITH(), "DISJOINT_WITH", cls );
    }
    
    /**
     * <p>Remove the statement that this class and the given class are
     * disjoint.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param cls A class that may be declared to be disjoint with this class, and which is no longer disjoint
     * @exception OntProfileException If the {@link Profile#DISJOINT_WITH()()()} property is not supported in the current language profile.
     */
    public void removeDisjointWith( Resource cls ) {
	removePropertyValue( getProfile().DISJOINT_WITH(), "DISJOINT_WITH", cls );
    }
    
    
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
    public ExtendedIterator listDeclaredProperties() {
	return listDeclaredProperties( false );
    }
    
    
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
    public ExtendedIterator listDeclaredProperties( boolean direct ) {
	// first collect the candidate properties
	Set candSet = new Set();
	// if the attached model does inference, it will potentially find more of these
	// than a non-inference model
	for (Iterator i = listAllProperties(); i.hasNext(); ) {
	    candSet.add(new OntPropertyImpl( ((Statement) i.next()).getSubject().asNode(), getModelCom() ));
	}
	// now we iterate over the candidates and check that they match all domain constraints
	List cands = new List();
	Iterator it = candSet.iterator();
	while(it.hasNext())
	    cands.add(it.next());
	for (int j = cands.size() -1; j >= 0; j--) {
	    Property cand = (Property) cands.get( j );
	    if (!hasDeclaredProperty( cand, direct )) {
		cands.remove( j );
	    }
	}
	return WrappedIterator.create( cands.iterator() );
    }
    
    /**
     * <p>Answer true if the given property is one of the declared properties
     * of this class. For details, see {@link #listDeclaredProperties(boolean)}.</p>
     * @param p A property to test
     * @param direct If true, only direct associations between classes and properties
     * are considered
     * @return True if <code>p</code> is one of the declared properties of
     * this class
     */
    public boolean hasDeclaredProperty( Property p, boolean direct ) {
	return testDomain( p, direct );
    }
    
    /**
     * <p>Answer an iterator over the individuals in the model that have this
     * class among their types.<p>
     *
     * @return An iterator over those instances that have this class as one of
     *         the classes to which they belong
     */
    public ExtendedIterator listInstances() {
	return listInstances( false );
    }
    
    
    /**
     * <p>Answer an iterator over the individuals in the model that have this
     * class among their types, optionally excluding sub-classes of this class.<p>
     *
     * @param  direct If true, only direct instances are counted (i.e. not instances
     * of sub-classes of this class)
     * @return An iterator over those instances that have this class as one of
     *         the classes to which they belong
     */
    public ExtendedIterator listInstances( final boolean direct ) {
	StmtIterator it = getModel().listStatements((Resource)null, RDF.type, this);
	Set result = new Set();
	while(it.hasNext())
	    result.add(it.nextStatement().getSubject());
	if(!direct) {
	    ExtendedIterator subClasses = this.listSubClasses();
	    while(subClasses.hasNext()) {
		StmtIterator it2 = getModel().listStatements((Resource)null, RDF.type, (RDFNode)subClasses.next());
		while(it2.hasNext())
		    result.add(it2.nextStatement().getSubject());
	    }
	}
	return WrappedIterator.create(result.iterator());
    }
    
    
    /**
     * <p>Answer a new individual that has this class as its <code>rdf:type</code></p>
     * @return A new anonymous individual that is an instance of this class
     */
    public Individual createIndividual() {
	return ((OntModel) getModel()).createIndividual( this );
    }
    
    
    /**
     * <p>Answer a new individual that has this class as its <code>rdf:type</code></p>
     * @param uri The URI of the new individual
     * @return A new named individual that is an instance of this class
     */
    public Individual createIndividual( String uri ) {
	return ((OntModel) getModel()).createIndividual( uri, this );
    }
    
    
    /**
     * <p>Answer true if this class is one of the roots of the class hierarchy.
     * This will be true if either (i) this class has <code>owl:Thing</code>
     * (or <code>daml:Thing</code>) as a direct super-class, or (ii) it has
     * no declared super-classes (including anonymous class expressions).</p>
     * @return True if this class is the root of the class hierarchy in the
     * model it is attached to
     */
    public boolean isHierarchyRoot() {
	// sanity check - :Nothing is never a root class
	if (equals( getProfile().NOTHING() )) {
	    return false;
	}
	
	/**
	 * Note: moved the initialisation of i outside the try-catch, otherwise an
	 * exception in listSuperClasses [eg a broken Graph implementation] will
	 * avoid i's initialisation but still run i.close, generating a mysterious
	 * NullPointerException. Signed, Mr Burnt Spines.
	 */
	ExtendedIterator i = listSuperClasses( true );
	try {
	    while (i.hasNext()) {
		Resource sup = (Resource) i.next();
		if (!(sup.equals( getProfile().THING() ) ||
			sup.equals( RDFS.Resource ) ||
			sup.equals( this ))) {
		    // a super that indicates this is not a root class
		    return false;
		}
	    }
	} finally {
	    i.close();
	}
	return true;
    }
        
    
    // sub-type testing
    
    /**
     * <p>Answer true if this class is an enumerated class expression</p>
     * @return True if this is an enumerated class expression
     */
    public boolean isEnumeratedClass() {
	return hasProperty( getProfile().ONE_OF() );
    }
    
    /**
     * <p>Answer true if this class is a union class expression</p>
     * @return True if this is a union class expression
     */
    public boolean isUnionClass() {
	return hasProperty( getProfile().UNION_OF() );
    }
    
    /**
     * <p>Answer true if this class is an intersection class expression</p>
     * @return True if this is an intersection class expression
     */
    public boolean isIntersectionClass() {
	return hasProperty( getProfile().INTERSECTION_OF() );
    }
    
    /**
     * <p>Answer true if this class is a complement class expression</p>
     * @return True if this is a complement class expression
     */
    public boolean isComplementClass() {
	return hasProperty( getProfile().COMPLEMENT_OF() );
    }
    
    /**
     * <p>Answer true if this class is a property restriction</p>
     * @return True if this is a restriction
     */
    public boolean isRestriction() {
	return hasProperty( getProfile().ON_PROPERTY() ) ||
		hasProperty( RDF.type, getProfile().RESTRICTION() );
    }
    
    /**
     * <p>Answer true if this class lies with the domain of p<p>
     * @param p
     * @param direct
     * @return
     */
    protected boolean testDomain( Property p, boolean direct ) {
	// we ignore any property in the DAML, OWL, etc namespace
	String namespace = p.getNameSpace();
	for (int i = 0; i < IGNORE_NAMESPACES.length; i++) {
	    if (namespace.equals( IGNORE_NAMESPACES[i] )) {
		return false;
	    }
	}
	
	// check for global props, that have no specific domain constraint
	boolean isGlobal = true;
	
	// flag for detecting the direct case
	boolean seenDirect = false;
	
	for (StmtIterator i = getModel().listStatements( p, getProfile().DOMAIN(), (RDFNode) null ); i.hasNext();  ) {
	    Resource domain = i.nextStatement().getResource();
	    
	    // there are some well-known values we ignore
	    if (!(domain.equals( getProfile().THING() ) || domain.equals( RDFS.Resource ))) {
		// not a generic domain
		isGlobal = false;
		
		if (domain.equals( this )) {
		    // if this class is actually in the domain (as opposed to one of this class's
		    // super-classes), then we've detected the direct property case
		    seenDirect = true;
		} else if (!canProveSuperClass( domain )) {
		    // there is a class in the domain of p that is not a super-class of this class
		    return false;
		}
	    }
	}
	
	if (direct) {
	    // if we're looking for direct props, we must either have seen the direct case
	    // or it's a global prop and this is a root class
	    return seenDirect || (isGlobal && isHierarchyRoot());
	} else {
	    // not direct, we must either found a global or a super-class prop
	    // otherwise the 'return false' above would have kicked in
	    return true;
	}
    }
    
    
    /**
     * <p>Answer an iterator over all of the properties in this model
     * @return
     */
    protected ExtendedIterator listAllProperties() {
	OntModel mOnt = (OntModel) getModel();
	ExtendedIterator pi = mOnt.listStatements( null, RDF.type, getProfile().PROPERTY() );
	// we manually check the other property types
	if (getProfile().OBJECT_PROPERTY() != null) {
	    pi = pi.andThen( mOnt.listStatements( null, RDF.type, getProfile().OBJECT_PROPERTY() ) );
	}
	if (getProfile().DATATYPE_PROPERTY() != null) {
	    pi = pi.andThen( mOnt.listStatements( null, RDF.type, getProfile().DATATYPE_PROPERTY() ) );
	}
	if (getProfile().FUNCTIONAL_PROPERTY() != null) {
	    pi = pi.andThen( mOnt.listStatements( null, RDF.type, getProfile().FUNCTIONAL_PROPERTY() ) );
	}
	if (getProfile().INVERSE_FUNCTIONAL_PROPERTY() != null) {
	    pi = pi.andThen( mOnt.listStatements( null, RDF.type, getProfile().INVERSE_FUNCTIONAL_PROPERTY() ) );
	}
	if (getProfile().SYMMETRIC_PROPERTY() != null) {
	    pi = pi.andThen( mOnt.listStatements( null, RDF.type, getProfile().SYMMETRIC_PROPERTY() ) );
	}
	if (getProfile().TRANSITIVE_PROPERTY() != null) {
	    pi = pi.andThen( mOnt.listStatements( null, RDF.type, getProfile().TRANSITIVE_PROPERTY() ) );
	}
	if (getProfile().ANNOTATION_PROPERTY() != null) {
	    pi = pi.andThen( mOnt.listStatements( null, RDF.type, getProfile().ANNOTATION_PROPERTY() ) );
	}
	return pi;
    }
    
    /**
     * <p>Answer true if we can demonstrate that this class has the given super-class.
     * If this model has a reasoner, this is equivalent to asking if the sub-class
     * relation holds. Otherwise, we simulate basic reasoning by searching upwards
     * through the class hierarchy.</p>
     * @param sup A super-class to test for
     * @return True if we can show that sup is a super-class of thsi class
     */
    protected boolean canProveSuperClass( Resource sup ) {
	Set seen = new Set();
	List queue = new List();
	queue.add( this );
	while (!queue.isEmpty()) {
	    OntClass c = (OntClass) queue.remove( 0 );
	    if (!seen.contains( c )) {
		seen.add( c );
		if (c.equals( sup )) {
		    // found the super class
		    return true;
		} else {
		    // queue the supers
		    for (Iterator i = c.listSuperClasses(); i.hasNext(); ) {
			queue.add( i.next() );
		    }
		}
	    }
	}
	// to get here, we didn't find the class we were looking for
	return false;
    }

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

