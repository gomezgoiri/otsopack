package es.deustotech.microjena.rdf.model.impl;

import it.polimi.elet.contextaddict.microjena.datatypes.DatatypeFormatException;
import it.polimi.elet.contextaddict.microjena.rdf.model.Literal;
import it.polimi.elet.contextaddict.microjena.rdf.model.Property;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFNode;
import it.polimi.elet.contextaddict.microjena.rdf.model.Resource;
import it.polimi.elet.contextaddict.microjena.rdf.model.Selector;
import it.polimi.elet.contextaddict.microjena.rdf.model.Statement;
import it.polimi.elet.contextaddict.microjena.shared.BadBooleanException;
import es.deustotech.microjena.rdf.model.vocabulary.OP;

public class ComparisonSelectorImpl implements Selector {
	Resource subject;
	Property predicate;
	Property binaryOperator;
	Literal object;
	
	protected ComparisonSelectorImpl() {
		this(null, null, null, null);
	}
	
	/**
	 * A ComparisonSelector should be written like this:
	 * "
	 * 		[ &lt;http://aitor> &lt;http://age> ?o . ,
	 * 		?o > "13"^^&lt;http://www.w3.org/2001/XMLSchema#int> . ]
	 * "
	 * or
	 * "
	 * 		[ ?s &lt;http://age> ?o . ,
	 * 		?o > "13"^^&lt;http://www.w3.org/2001/XMLSchema#int> . ]
	 * "
	 * or (this is equal to ?s ?p ?o .)
	 * "
	 * 		[ ?s ?p ?o . ,
	 * 		?o ?bo ?lit . ]
	 * "
	 * @param template
	 * @throws InvalidTemplateException
	 */
	protected ComparisonSelectorImpl(String template) throws InvalidTemplateException {
		parseTemplate(template);
	}
	
		protected void parseTemplate(String template) throws InvalidTemplateException {
			//instead of template.replaceAll("[ \t\n]","");
			template = template.replace('\n',' ');
			template = template.replace('\t',' ');
			
			template = template.trim();
			if( !template.startsWith("[") || !template.endsWith("]"))
				throw new InvalidTemplateException("The template should start with a \"[\" and end with \"]\"");
			template = template.substring(1); // get rid of "["
			template = template.substring(0,template.length()-1); // get rid of "]"
			
			int breakLine = template.indexOf(" , ");
			
			if(breakLine==-1)
				throw new InvalidTemplateException("A \\n must divide the two templates");
			
			String firstTemplate = template.substring(0,breakLine);
			String secondTemplate = template.substring(breakLine+3); //" , " == 3 characters
			
			if( !bothTemplatesHaveSameVariables(firstTemplate, secondTemplate) )
				new InvalidTemplateException("The first template's object variable must be same as second's subject");
			
			SelectorImpl selectorImpl = new SelectorImpl(firstTemplate);
			SelectorImpl selectorImpl2 = new SelectorImpl(secondTemplate);
			
			if( selectorImpl.getObject()!=null || selectorImpl2.getSubject()!=null  )
				new InvalidTemplateException("The first template's subject and second one's subject must be both variables"); // after bothTemplatesHaveSameVariables this won't be ever true
			
			if( !selectorImpl2.getPredicate().toString().startsWith(OP.NAMESPACE) )
				new InvalidTemplateException("You must provide a binary comparison operator in the second predicate");
			
			if( !(selectorImpl2.getObject() instanceof Literal) )
				new InvalidTemplateException("You must provide a literal object in the second template");
			
			subject = selectorImpl.getSubject();
			predicate = selectorImpl.getPredicate();
			binaryOperator = selectorImpl2.getPredicate();
			object = (Literal) selectorImpl2.getObject();
		}
		
			/* <http://aitor> <http://age> ?o .
			 * ?o > "13"^^<http://www.w3.org/2001/XMLSchema#int>.
			 */
			protected static boolean bothTemplatesHaveSameVariables(String firstTpl, String secondTpl) {
				firstTpl = firstTpl.trim();
				secondTpl = secondTpl.trim();
				
				firstTpl = firstTpl.substring( 0, firstTpl.indexOf(" .") ); // get rid of the last part
				if( firstTpl.lastIndexOf('?')==-1 ) return false;
				String firstVariableName = firstTpl.substring( firstTpl.lastIndexOf('?') );
				
				secondTpl = secondTpl.substring( 0, secondTpl.indexOf(' ') ); // get rid of the last part
				if( secondTpl.lastIndexOf('?')==-1 ) return false;
				String secondVariableName = secondTpl.substring( secondTpl.indexOf('?') );
				
				return firstVariableName.equals(secondVariableName);
			}
	
	public ComparisonSelectorImpl(Resource subj, Property pred, Property binaryOp, Literal obj) {
		subject = subj;
		predicate = pred;
		binaryOperator = binaryOp;
		object = obj;
	}

	public RDFNode getObject() {
		return object;
	}

	public Property getPredicate() {
		return predicate;
	}
	
	public Property getBinaryOperator() {
		return binaryOperator;
	}
	
	public Resource getSubject() {
		return subject;
	}
	
	public boolean isSimple() {
		return false;
	}

	public boolean test(Statement stmt) {
		boolean esSubj = (subject==null) || (subject!=null && stmt.getSubject().equals(subject));
		boolean esPred = (predicate==null) || (predicate!=null && stmt.getPredicate().equals(predicate));		
		if( !esSubj || !esPred ) return false;
		
		// if the literal to compare with is not specified -> every comparison will be true
		if( object==null ) return true;
		// if it isn't a literal -> false
		if( !stmt.getObject().isLiteral() ) return false;
		
		String bo = binaryOperator.getURI();
		if( bo.equals(OP.BOOLEAN_EQUAL) || bo.equals(OP.BOOLEAN_NOT_EQUAL) ||
			bo.equals(OP.BOOLEAN_GREATER_EQUALS) || bo.equals(OP.BOOLEAN_GREATER_THAN) ||
			bo.equals(OP.BOOLEAN_LESS_EQUALS) || bo.equals(OP.BOOLEAN_LESS_THAN) ) {
			boolean stmtBool = false;
			try {
				stmtBool = stmt.getLiteral().getBoolean();
			} catch(DatatypeFormatException dfe) {
				return false;
			} catch(BadBooleanException bbe) {
				return false;
			}
			if( bo.equals(OP.BOOLEAN_EQUAL) ) {
				return stmtBool == object.getBoolean();
			} else if( bo.equals(OP.BOOLEAN_NOT_EQUAL) ) {
				return stmtBool != object.getBoolean();
			} else if( bo.equals(OP.BOOLEAN_GREATER_THAN) ) {
				return stmtBool && !object.getBoolean();
			} else if( bo.equals(OP.BOOLEAN_GREATER_EQUALS) ) {
				return !(!stmtBool && object.getBoolean());
			} else if( bo.equals(OP.BOOLEAN_LESS_THAN) ) {
				return !stmtBool && object.getBoolean(); // stmtBool<true
			} else if( bo.equals(OP.BOOLEAN_LESS_EQUALS) ) {
				return !(!object.getBoolean() && stmtBool);
			}
		} else
		if ( bo.equals(OP.NUMERIC_EQUAL) || bo.equals(OP.NUMERIC_NOT_EQUAL) ||
			 bo.equals(OP.NUMERIC_GREATER_EQUALS) || bo.equals(OP.NUMERIC_GREATER_THAN) ||
			 bo.equals(OP.NUMERIC_LESS_EQUALS) || bo.equals(OP.NUMERIC_LESS_THAN) ) {
			double stmtNum = -1;
			try {
				stmtNum = stmt.getLiteral().getDouble();
			} catch(NumberFormatException nfe) {
				return false;
			} catch(DatatypeFormatException dfe) {
				return false;
			}
			if( bo.equals(OP.NUMERIC_EQUAL) ) {
				return stmtNum == object.getDouble() ;
			} else if( bo.equals(OP.NUMERIC_NOT_EQUAL) ) {
				return stmtNum != object.getDouble();
			} else if( bo.equals(OP.NUMERIC_GREATER_THAN) ) {
				return stmtNum > object.getDouble();
			} else if( bo.equals(OP.NUMERIC_GREATER_EQUALS) ) {
				return stmtNum >= object.getDouble();
			} else if( bo.equals(OP.NUMERIC_LESS_THAN) ) {
				return stmtNum < object.getDouble(); // stmtBool<true
			} else if( bo.equals(OP.NUMERIC_LESS_EQUALS) ) {
				return stmtNum <= object.getDouble();
			}
		}
		return false;
	}

	public void setObject(Literal object) {
		this.object = object;
	}

	public void setPredicate(Property predicate) {
		this.predicate = predicate;
	}
	
	public void setBinaryOperator(Property binaryOperator) throws InvalidTemplateException {
		if( !binaryOperator.getURI().startsWith(OP.NAMESPACE) )
			new InvalidTemplateException("You must provide a binary comparison operator in the second predicate"); 
		this.binaryOperator = binaryOperator;
	}

	public void setSubject(Resource subject) {
		this.subject = subject;
	}
	
	public String toString() {
		String subj = (subject==null)?"?s":("<"+subject.toString()+">");
		String pred = ((predicate==null)?"?p":("<"+predicate.toString()+">"));
		String bo = ((binaryOperator==null)?"?bo":("<"+binaryOperator.toString()+">"));
		String obj = ((object==null)?"?lit":getLiteralNTriple());
		
		String ret = "[ " + subj + " " + pred + " ?o . , ";
		ret += "?o " + bo + " " + obj + " . ]";
		
		return ret;
	}
	
	private String getLiteralNTriple() {
		return "\""+object.getLexicalForm()+"\"^^<"+object.getDatatypeURI()+">";
	}
	
	public boolean equals(Object o) {
		boolean ret = false;
		if(o instanceof ComparisonSelectorImpl) {
			if(subject==null) ret = ((Selector)o).getSubject()==null;
			else ret = subject.equals( ((Selector)o).getSubject() );
			
			if(predicate==null) ret &= ((Selector)o).getPredicate()==null;
			else ret &= predicate.equals( ((Selector)o).getPredicate() );
			
			if(binaryOperator==null) ret &= ((ComparisonSelectorImpl)o).getBinaryOperator()==null;
			else ret &= binaryOperator.equals( ((ComparisonSelectorImpl)o).getBinaryOperator() );
			
			if(object==null) ret &= ((Selector)o).getObject()==null;
			else ret &= object.equals( ((Selector)o).getObject() );
		}
		return ret;
	}
	
	public int hashCode() {
		int hash = 0;
		hash += (subject==null)?0:subject.hashCode();
		hash += (predicate==null)?0:predicate.hashCode();
		hash += (binaryOperator==null)?0:binaryOperator.hashCode();
		hash += (object==null)?0:object.hashCode();
		return hash; // another less complicated alternative toString().hashCode();
	}
}
