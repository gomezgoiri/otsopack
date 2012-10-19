package es.deustotech.microjena.rdf.model.impl;

import it.polimi.elet.contextaddict.microjena.datatypes.RDFDatatype;
import it.polimi.elet.contextaddict.microjena.datatypes.TypeMapper;
import it.polimi.elet.contextaddict.microjena.datatypes.xsd.XSDDatatype;
import it.polimi.elet.contextaddict.microjena.rdf.model.Property;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFNode;
import it.polimi.elet.contextaddict.microjena.rdf.model.Resource;
import it.polimi.elet.contextaddict.microjena.rdf.model.ResourceFactory;
import it.polimi.elet.contextaddict.microjena.rdf.model.Selector;
import it.polimi.elet.contextaddict.microjena.rdf.model.Statement;
import es.deustotech.microjena.rdf.model.vocabulary.OP;
import es.deustotech.microjena.rdf.model.vocabulary.XMLSchema;

public class SelectorImpl implements Selector {
	RDFNode object;
	Property predicate;
	Resource subject;
	
	protected SelectorImpl() {
		this(null, null, null);
	}
	
	protected SelectorImpl(String template) throws InvalidTemplateException {
		parseTemplate(template);
	}
	
		protected void parseTemplate(String template) throws InvalidTemplateException {
			String comparisonOperator = null;
			
			template = template.trim();
			if( template.startsWith("?") ) {
				subject = null;
			} else if( template.startsWith("<") ) {
				String subj = template.substring(template.indexOf("<")+1, template.indexOf("> "));
				subject = ResourceFactory.createResource(subj);
			} else throw new InvalidTemplateException();
			template = template.substring(template.indexOf(" "));
			
			template = template.trim();
			if( template.startsWith("?") ) {
				predicate = null;
			} else if( template.startsWith("= ") ||  template.startsWith("!= ") ||
					 template.startsWith("< ") ||  template.startsWith("> ") ||
					 template.startsWith("<= ") ||  template.startsWith(">= ") ) {
				comparisonOperator = template.substring(0,template.indexOf(" "));
			} else if( template.startsWith("<")	) {
				String pred = template.substring(template.indexOf("<")+1, template.indexOf("> "));
				predicate = ResourceFactory.createProperty(pred);
			} else throw new InvalidTemplateException();
			template = template.substring(template.indexOf(" "));
			
			template = template.trim();
			if( template.startsWith("?") ) {
				object = null;
				if( comparisonOperator!=null ) {
					if( comparisonOperator.equals("=") ) {
						predicate = ResourceFactory.createProperty(OP.UNSPECIFIED_EQUAL);
					} else if( comparisonOperator.equals("!=") ) {
						predicate = ResourceFactory.createProperty(OP.UNSPECIFIED_NOT_EQUAL);
					} else if( comparisonOperator.equals("<") ) {
						predicate = ResourceFactory.createProperty(OP.UNSPECIFIED_LESS_THAN);
					} else if( comparisonOperator.equals(">") ) {
						predicate = ResourceFactory.createProperty(OP.UNSPECIFIED_GREATER_THAN);
					} else if( comparisonOperator.equals("<=") ) {
						predicate = ResourceFactory.createProperty(OP.UNSPECIFIED_LESS_EQUALS);
					} else if( comparisonOperator.equals(">=") ) {
						predicate = ResourceFactory.createProperty(OP.UNSPECIFIED_GREATER_EQUALS);
					}
				}
			} else if( template.startsWith("<") && template.indexOf("> ")!=-1 ) {
				if( comparisonOperator!=null )
					throw new InvalidTemplateException("A comparison operator should have a literal object.");
				String obj = template.substring(template.indexOf("<")+1, template.indexOf("> "));
				obj = obj.trim();
				object = ResourceFactory.createResource(obj);
			} else if( template.indexOf("^^<"+XMLSchema.NAMESPACE) != -1 ) {
				template = template.substring(template.indexOf("\"")+1);
				String lex = template.substring(0,template.indexOf("\""));
				template = template.substring(template.indexOf("\"")+1);
				
				String dType = template.substring(template.indexOf("^^<")+3, template.indexOf(">"));
				
				RDFDatatype datatype = TypeMapper.getInstance().getTypeByName(dType);
				object = ResourceFactory.createTypedLiteral(lex, datatype);
				
				if( comparisonOperator!=null ) {
					if (datatype.equals(XSDDatatype.XSDboolean) ) {
						if( comparisonOperator.equals("=") ) {
							predicate = ResourceFactory.createProperty(OP.BOOLEAN_EQUAL);
						} else if( comparisonOperator.equals("!=") ) {
							predicate = ResourceFactory.createProperty(OP.BOOLEAN_NOT_EQUAL);
						} else if( comparisonOperator.equals("<") ) {
							predicate = ResourceFactory.createProperty(OP.BOOLEAN_LESS_THAN);
						} else if( comparisonOperator.equals(">") ) {
							predicate = ResourceFactory.createProperty(OP.BOOLEAN_GREATER_THAN);
						} else if( comparisonOperator.equals("<=") ) {
							predicate = ResourceFactory.createProperty(OP.BOOLEAN_LESS_EQUALS);
						} else if( comparisonOperator.equals(">=") ) {
							predicate = ResourceFactory.createProperty(OP.BOOLEAN_GREATER_EQUALS);
						}
					} else /*if( datatype.equals(XSDDatatype.XSDfloat) || datatype.equals(XSDDatatype.XSDdouble) ||
							datatype.equals(XSDDatatype.XSDint) || datatype.equals(XSDDatatype.XSDlong) ||
							datatype.equals(XSDDatatype.XSDinteger) || datatype.equals(XSDDatatype.XSDstring) ) */ {
						// string using fn:compare
						if( comparisonOperator.equals("=") ) {
							predicate = ResourceFactory.createProperty(OP.NUMERIC_EQUAL);
						} else if( comparisonOperator.equals("!=") ) {
							predicate = ResourceFactory.createProperty(OP.NUMERIC_NOT_EQUAL);
						} else if( comparisonOperator.equals("<") ) {
							predicate = ResourceFactory.createProperty(OP.NUMERIC_LESS_THAN);
						} else if( comparisonOperator.equals(">") ) {
							predicate = ResourceFactory.createProperty(OP.NUMERIC_GREATER_THAN);
						} else if( comparisonOperator.equals("<=") ) {
							predicate = ResourceFactory.createProperty(OP.NUMERIC_LESS_EQUALS);
						} else if( comparisonOperator.equals(">=") ) {
							predicate = ResourceFactory.createProperty(OP.NUMERIC_GREATER_EQUALS);
						}
					} // and what about xs:dateTime?
				}
			} else throw new InvalidTemplateException();
			
			if( template.indexOf(" ")==-1)
				throw new InvalidTemplateException();
			template = template.substring(template.indexOf(" "));
			template = template.trim();
			
			//Check whether it ends with a .
			if( !template.equals(".") )
				throw new InvalidTemplateException();
		}
	
	public SelectorImpl(Resource subj, Property pred, RDFNode obj) {
		object = obj;
		predicate = pred;
		subject = subj;
	}

	public RDFNode getObject() {
		return object;
	}

	public Property getPredicate() {
		return predicate;
	}

	public Resource getSubject() {
		return subject;
	}

	public boolean isSimple() {
		return false;
	}

	public boolean test(Statement arg0) {
		boolean esSubj = (subject==null) || (subject!=null && arg0.getSubject().equals(subject));
		boolean esPred = (predicate==null) || (predicate!=null && arg0.getPredicate().equals(predicate));
		boolean esObj = (object==null) || (object!=null && arg0.getObject().equals(object));
		return esObj && esPred && esSubj;
	}

	public void setObject(RDFNode object) {
		this.object = object;
	}

	public void setPredicate(Property predicate) {
		this.predicate = predicate;
	}

	public void setSubject(Resource subject) {
		this.subject = subject;
	}
	
	public String toString() {
		String ret = (subject==null)?"?s":("<"+subject.toString()+">");
		ret += " "+ ((predicate==null)?"?p":("<"+predicate.toString()+">"));
		ret += " "+ ((object==null)?"?o":("<"+object.toString()+">")) + " .";
		return ret;
	}
	
	public boolean equals(Object o) {
		boolean ret = false;
		if(o instanceof Selector) {
			if(object==null) ret = ((Selector)o).getObject()==null;
			else ret = object.equals( ((Selector)o).getObject() );
			
			if(predicate==null) ret &= ((Selector)o).getPredicate()==null;
			else ret &= predicate.equals( ((Selector)o).getPredicate() );
			
			if(subject==null) ret &= ((Selector)o).getSubject()==null;
			else ret &= subject.equals( ((Selector)o).getSubject() );
		}
		return ret;
	}
	
	public int hashCode() {
		int hash = 0;
		hash += (subject==null)?0:subject.hashCode();
		hash += (predicate==null)?0:predicate.hashCode();
		hash += (object==null)?0:object.hashCode();
		return hash; // another less complicated alternative toString().hashCode();
	}
}
