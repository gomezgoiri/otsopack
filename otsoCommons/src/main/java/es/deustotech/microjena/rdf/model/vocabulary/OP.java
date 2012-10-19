package es.deustotech.microjena.rdf.model.vocabulary;

/**
 * http://www.w3.org/TR/rdf-sparql-query/#OperatorMapping
 * @author tulvur
 */
public class OP {
	/** op -> http://www.foo.com/op# */
	public static final String NAMESPACE = "http://www.foo.com/op#";
	
	/** op:numeric-equal */
	public final static String NUMERIC_EQUAL;
	
	/** op:boolean-equal */
	public final static String BOOLEAN_EQUAL;
	
	/** op:dateTime-equal */
	public final static String DATETIME_EQUAL;
	
	/** op:dateTime-equal */
	public final static String UNSPECIFIED_EQUAL;
	
	/** op:numeric-not-equal */
	public final static String NUMERIC_NOT_EQUAL;
	
	/** op:boolean-not-equal */
	public final static String BOOLEAN_NOT_EQUAL;
	
	/** op:dateTime-not-equal */
	public final static String DATETIME_NOT_EQUAL;
	
	/** op:unspecified-not-equal */
	public final static String UNSPECIFIED_NOT_EQUAL;
	
	/** op:numeric-less-than */
	public final static String NUMERIC_LESS_THAN;
	
	/** op:boolean-less-than */
	public final static String BOOLEAN_LESS_THAN;
	
	/** op:dateTime-less-than */
	public final static String DATETIME_LESS_THAN;
	
	/** op:unspecified-less-than */
	public final static String UNSPECIFIED_LESS_THAN;
	
	/** op:numeric-greater-than */
	public final static String NUMERIC_GREATER_THAN;
	
	/** op:boolean-greater-than */
	public final static String BOOLEAN_GREATER_THAN;
	
	/** op:dateTime-greater-than */
	public final static String DATETIME_GREATER_THAN;
	
	/** op:unspecified-greater-than */
	public final static String UNSPECIFIED_GREATER_THAN;
	
	/** op:numeric-greater-equals */
	public final static String NUMERIC_GREATER_EQUALS;
	
	/** op:boolean-greater-equals */
	public final static String BOOLEAN_GREATER_EQUALS;
	
	/** op:dateTime-greater-equals */
	public final static String DATETIME_GREATER_EQUALS;
	
	/** op:unspecified-greater-equals */
	public final static String UNSPECIFIED_GREATER_EQUALS;
	
	/** op:numeric-less-equals */
	public final static String NUMERIC_LESS_EQUALS;
	
	/** op:boolean-less-equals */
	public final static String BOOLEAN_LESS_EQUALS;
	
	/** op:dateTime-less-equals */
	public final static String DATETIME_LESS_EQUALS;
	
	/** op:unspecified-less-equals */
	public final static String UNSPECIFIED_LESS_EQUALS;

	static {
		NUMERIC_EQUAL = OP.NAMESPACE + "numeric-equal";
		BOOLEAN_EQUAL = OP.NAMESPACE + "boolean-equal";
		DATETIME_EQUAL = OP.NAMESPACE + "dateTime-equal";
		UNSPECIFIED_EQUAL = OP.NAMESPACE + "unspecified-equal";
		NUMERIC_NOT_EQUAL = OP.NAMESPACE + "numeric-not-equal";
		BOOLEAN_NOT_EQUAL = OP.NAMESPACE + "boolean-not-equal";
		DATETIME_NOT_EQUAL = OP.NAMESPACE + "dateTime-not-equal";
		UNSPECIFIED_NOT_EQUAL = OP.NAMESPACE + "unspecified-not-equal";
		NUMERIC_LESS_THAN = OP.NAMESPACE + "numeric-less-than";
		BOOLEAN_LESS_THAN = OP.NAMESPACE + "boolean-less-than";
		DATETIME_LESS_THAN = OP.NAMESPACE + "dateTime-less-than";
		UNSPECIFIED_LESS_THAN = OP.NAMESPACE + "unspecified-less-than";
		NUMERIC_LESS_EQUALS = OP.NAMESPACE + "numeric-less-equals";
		BOOLEAN_LESS_EQUALS = OP.NAMESPACE + "boolean-less-equals";
		DATETIME_LESS_EQUALS = OP.NAMESPACE + "dateTime-less-equals";
		UNSPECIFIED_LESS_EQUALS = OP.NAMESPACE + "unspecified-less-equals";
		NUMERIC_GREATER_THAN = OP.NAMESPACE + "numeric-greater-than";
		BOOLEAN_GREATER_THAN = OP.NAMESPACE + "boolean-greater-than";
		DATETIME_GREATER_THAN = OP.NAMESPACE + "dateTime-greater-than";
		UNSPECIFIED_GREATER_THAN = OP.NAMESPACE + "unspecified-greater-than";
		NUMERIC_GREATER_EQUALS = OP.NAMESPACE + "numeric-greater-equals";
		BOOLEAN_GREATER_EQUALS = OP.NAMESPACE + "boolean-greater-equals";
		DATETIME_GREATER_EQUALS = OP.NAMESPACE + "dateTime-greater-equals";
		UNSPECIFIED_GREATER_EQUALS = OP.NAMESPACE + "unspecified-greater-equals";
	}
}