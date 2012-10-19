/*
 * RDFAxiomWriter.java
 *
 * Created on 30 gennaio 2008, 18.44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.polimi.elet.contextaddict.microjena.rdf.model.impl;

import it.polimi.elet.contextaddict.microjena.graph.Axiom;
import it.polimi.elet.contextaddict.microjena.graph.Graph;
import it.polimi.elet.contextaddict.microjena.graph.Node;
import it.polimi.elet.contextaddict.microjena.vocabulary.RDF;
import it.polimi.elet.contextaddict.microjena.vocabulary.RDFS;

/**
 *
 * @author ilBuccia
 */
public class RDFAxiomWriter {

    public static void writeAxioms(Graph graph) {
	Node rdfType = RDF.type.asNode();
	Node rdfsResource = RDFS.Resource.asNode();
	Node rdfsClass = RDFS.Class.asNode();
	graph.add(new Axiom(rdfsClass, rdfType, rdfsClass ));
	graph.add(new Axiom(rdfsClass, RDFS.subClassOf.asNode(), rdfsResource ));
	graph.add(new Axiom(RDFS.label.asNode(), RDFS.range.asNode(), RDFS.Literal.asNode() ));
	graph.add(new Axiom(rdfsResource, rdfType, rdfsClass ));
	graph.add(new Axiom(RDF.subject.asNode(), rdfType, RDF.Property.asNode() ));
	graph.add(new Axiom(RDF.subject.asNode(), RDFS.domain.asNode(), RDF.Statement.asNode() ));
	graph.add(new Axiom(RDF.subject.asNode(), RDFS.subPropertyOf.asNode(), RDF.subject.asNode() ));
	graph.add(new Axiom(RDFS.subClassOf.asNode(), RDFS.domain.asNode(), rdfsClass ));
	graph.add(new Axiom(RDFS.subClassOf.asNode(), RDFS.range.asNode(), rdfsClass ));
	graph.add(new Axiom(RDF.Statement.asNode(), rdfType, rdfsClass ));
	graph.add(new Axiom(RDF.Statement.asNode(), RDFS.subClassOf.asNode(), rdfsResource ));
	graph.add(new Axiom(RDF.predicate.asNode(), rdfType, RDF.Property.asNode() ));
	graph.add(new Axiom(RDF.predicate.asNode(), RDFS.domain.asNode(), RDF.Statement.asNode() ));
	graph.add(new Axiom(RDF.predicate.asNode(), RDFS.subPropertyOf.asNode(), RDF.predicate.asNode() ));
	graph.add(new Axiom(RDFS.subPropertyOf.asNode(), RDFS.domain.asNode(), RDF.Property.asNode() ));
	graph.add(new Axiom(RDFS.subPropertyOf.asNode(), RDFS.range.asNode(), RDF.Property.asNode() ));
	graph.add(new Axiom(RDF.object.asNode(), rdfType, RDF.Property.asNode() ));
	graph.add(new Axiom(RDF.object.asNode(), RDFS.domain.asNode(), RDF.Statement.asNode() ));
	graph.add(new Axiom(RDF.object.asNode(), RDFS.subPropertyOf.asNode(), RDF.object.asNode() ));
	graph.add(new Axiom(Node.createURI(RDF.getURI() + "XMLLiteral"), rdfType, RDFS.Datatype.asNode() ));
	graph.add(new Axiom(rdfType, RDFS.range.asNode(), rdfsClass ));
	graph.add(new Axiom(RDF.rest.asNode(), rdfType, RDF.Property.asNode() ));
	graph.add(new Axiom(RDF.rest.asNode(), RDFS.domain.asNode(), RDF.List.asNode() ));
	graph.add(new Axiom(RDF.rest.asNode(), RDFS.range.asNode(), RDF.List.asNode() ));
	graph.add(new Axiom(RDF.rest.asNode(), RDFS.subPropertyOf.asNode(), RDF.rest.asNode() ));
	graph.add(new Axiom(RDFS.Literal.asNode(), rdfType, rdfsClass ));
	graph.add(new Axiom(RDFS.Literal.asNode(), RDFS.subClassOf.asNode(), rdfsResource ));
	graph.add(new Axiom(RDF.Property.asNode(), rdfType, rdfsClass ));
	graph.add(new Axiom(RDF.Property.asNode(), RDFS.subClassOf.asNode(), rdfsResource ));
	graph.add(new Axiom(RDF.List.asNode(), rdfType, rdfsClass ));
	graph.add(new Axiom(RDF.List.asNode(), RDFS.subClassOf.asNode(), rdfsResource ));
	graph.add(new Axiom(RDFS.domain.asNode(), RDFS.domain.asNode(), RDF.Property.asNode() ));
	graph.add(new Axiom(RDFS.domain.asNode(), RDFS.range.asNode(), rdfsClass ));
	graph.add(new Axiom(RDF.first.asNode(), rdfType, RDF.Property.asNode() ));
	graph.add(new Axiom(RDF.first.asNode(), RDFS.domain.asNode(), RDF.List.asNode() ));
	graph.add(new Axiom(RDF.first.asNode(), RDFS.subPropertyOf.asNode(), RDF.first.asNode() ));
	graph.add(new Axiom(RDF.nil.asNode(), rdfType, RDF.List.asNode() ));
	graph.add(new Axiom(RDFS.range.asNode(), RDFS.domain.asNode(), RDF.Property.asNode() ));
	graph.add(new Axiom(RDFS.range.asNode(), RDFS.range.asNode(), rdfsClass ));
	graph.add(new Axiom(RDFS.comment.asNode(), RDFS.range.asNode(), RDFS.Literal.asNode() ));
	graph.add(new Axiom(RDFS.Literal.asNode(), RDFS.subClassOf.asNode(), RDFS.Literal.asNode() ));
	graph.add(new Axiom(RDF.List.asNode(), RDFS.subClassOf.asNode(), RDF.List.asNode() ));
	graph.add(new Axiom(RDF.Bag.asNode(), RDFS.subClassOf.asNode(), RDF.Bag.asNode() ));
	graph.add(new Axiom(RDF.Bag.asNode(), RDFS.subClassOf.asNode(), RDFS.Container.asNode() ));
	graph.add(new Axiom(rdfsResource, RDFS.subClassOf.asNode(), rdfsResource ));
	graph.add(new Axiom(RDF.Seq.asNode(), RDFS.subClassOf.asNode(), RDF.Seq.asNode() ));
	graph.add(new Axiom(RDF.Seq.asNode(), RDFS.subClassOf.asNode(), RDFS.Container.asNode() ));
	graph.add(new Axiom(RDFS.ContainerMembershipProperty.asNode(), RDFS.subClassOf.asNode(), RDFS.ContainerMembershipProperty.asNode() ));
	graph.add(new Axiom(RDFS.ContainerMembershipProperty.asNode(), RDFS.subClassOf.asNode(), RDF.Property.asNode() ));
	graph.add(new Axiom(RDFS.ContainerMembershipProperty.asNode(), RDFS.subClassOf.asNode(), rdfsResource ));
	graph.add(new Axiom(RDF.Statement.asNode(), RDFS.subClassOf.asNode(), RDF.Statement.asNode() ));
	graph.add(new Axiom(rdfsClass, RDFS.subClassOf.asNode(), rdfsClass ));
	graph.add(new Axiom(RDF.Alt.asNode(), RDFS.subClassOf.asNode(), RDF.Alt.asNode() ));
	graph.add(new Axiom(RDF.Alt.asNode(), RDFS.subClassOf.asNode(), RDFS.Container.asNode() ));
	graph.add(new Axiom(RDFS.Container.asNode(), RDFS.subClassOf.asNode(), RDFS.Container.asNode() ));
	graph.add(new Axiom(RDF.Property.asNode(), RDFS.subClassOf.asNode(), RDF.Property.asNode() ));
	graph.add(new Axiom(RDFS.Datatype.asNode(), RDFS.subClassOf.asNode(), RDFS.Datatype.asNode() ));
	graph.add(new Axiom(RDFS.Datatype.asNode(), RDFS.subClassOf.asNode(), rdfsClass ));
	graph.add(new Axiom(RDFS.Datatype.asNode(), RDFS.subClassOf.asNode(), rdfsResource ));
	graph.add(new Axiom(RDFS.seeAlso.asNode(), RDFS.subPropertyOf.asNode(), RDFS.seeAlso.asNode() ));
	graph.add(new Axiom(RDFS.isDefinedBy.asNode(), RDFS.subPropertyOf.asNode(), RDFS.isDefinedBy.asNode() ));
	graph.add(new Axiom(RDFS.isDefinedBy.asNode(), RDFS.subPropertyOf.asNode(), RDFS.seeAlso.asNode() ));
	graph.add(new Axiom(RDF.Bag.asNode(), rdfType, rdfsClass ));
	graph.add(new Axiom(RDFS.Container.asNode(), rdfType, rdfsClass ));
	graph.add(new Axiom(RDF.Seq.asNode(), rdfType, rdfsClass ));
	graph.add(new Axiom(RDFS.ContainerMembershipProperty.asNode(), rdfType, rdfsClass ));
	graph.add(new Axiom(RDF.Alt.asNode(), rdfType, rdfsClass ));
	graph.add(new Axiom(RDFS.Datatype.asNode(), rdfType, rdfsClass ));
	graph.add(new Axiom(RDFS.seeAlso.asNode(), rdfType, RDF.Property.asNode() ));
	graph.add(new Axiom(RDFS.isDefinedBy.asNode(), rdfType, RDF.Property.asNode() ));
	graph.add(new Axiom(RDFS.comment.asNode(), rdfType, RDF.Property.asNode() ));
	graph.add(new Axiom(RDFS.range.asNode(), rdfType, RDF.Property.asNode() ));
	graph.add(new Axiom(RDFS.domain.asNode(), rdfType, RDF.Property.asNode() ));
	graph.add(new Axiom(rdfType, rdfType, RDF.Property.asNode() ));
	graph.add(new Axiom(RDFS.subPropertyOf.asNode(), rdfType, RDF.Property.asNode() ));
	graph.add(new Axiom(RDFS.subClassOf.asNode(), rdfType, RDF.Property.asNode() ));
	graph.add(new Axiom(RDFS.label.asNode(), rdfType, RDF.Property.asNode() ));
	graph.add(new Axiom(Node.createURI(RDF.getURI() + "XMLLiteral"), rdfType, rdfsResource ));
	graph.add(new Axiom(Node.createURI(RDF.getURI() + "XMLLiteral"), rdfType, rdfsClass ));
	graph.add(new Axiom(rdfsResource, rdfType, rdfsResource ));
	graph.add(new Axiom(RDF.Statement.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(RDF.Property.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(RDFS.Literal.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(rdfsClass, rdfType, rdfsResource ));
	graph.add(new Axiom(RDF.List.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(RDF.Bag.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(RDFS.Container.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(RDF.Seq.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(RDFS.ContainerMembershipProperty.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(RDF.Alt.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(RDFS.Datatype.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(RDF.first.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(RDF.rest.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(RDF.object.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(RDF.predicate.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(RDF.subject.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(RDFS.seeAlso.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(RDFS.isDefinedBy.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(RDFS.comment.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(RDFS.range.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(RDFS.domain.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(rdfType, rdfType, rdfsResource ));
	graph.add(new Axiom(RDFS.subPropertyOf.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(RDFS.subClassOf.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(RDFS.label.asNode(), rdfType, rdfsResource ));
	graph.add(new Axiom(RDF.nil.asNode(), rdfType, rdfsResource ));		
    }
}
