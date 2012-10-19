package es.deustotech.microjena.rdf.model.impl;

import it.polimi.elet.contextaddict.microjena.rdf.model.Literal;
import it.polimi.elet.contextaddict.microjena.rdf.model.Property;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFNode;
import it.polimi.elet.contextaddict.microjena.rdf.model.Resource;
import it.polimi.elet.contextaddict.microjena.rdf.model.Selector;

public class SelectorFactory {
	private SelectorFactory() {
	}
	
	public static Selector createSelector(String selector) throws InvalidTemplateException {
		selector = selector.trim();
		if( selector.startsWith("[") && selector.endsWith("]") ) {
			return new ComparisonSelectorImpl(selector);
		} else {
			return new SelectorImpl(selector);
		}
	}
	
	public static Selector createSelector(Resource subject, Property predicate, RDFNode object) {
		SelectorImpl sel = new SelectorImpl();
		sel.setSubject(subject);
		sel.setPredicate(predicate);
		sel.setObject(object);
		return sel;
	}
	
	public static Selector createSelector(Resource subject, Property predicate, Property binaryOperator, Literal object) throws InvalidTemplateException {
		if( binaryOperator==null )
			return createSelector(subject,predicate,object);
		
		ComparisonSelectorImpl sel = new ComparisonSelectorImpl();
		sel.setSubject(subject);
		sel.setPredicate(predicate);
		sel.setBinaryOperator(binaryOperator);
		sel.setObject(object);
		return sel;
	}
	
	public static Selector createSelectorFromSubject(Resource subject) throws InvalidTemplateException {
		SelectorImpl sel = new SelectorImpl();
		sel.setSubject(subject);
		return sel;
	}
	
	public static Selector createSelectorFromPredicate(Property predicate) throws InvalidTemplateException {
		SelectorImpl sel = new SelectorImpl();
		sel.setPredicate(predicate);
		return sel;
	}
	
	public static Selector createSelectorFromObject(RDFNode object) throws InvalidTemplateException {
		SelectorImpl sel = new SelectorImpl();
		sel.setObject(object);
		return sel;
	}
}