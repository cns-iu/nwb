grammar ModelFile;

options {
	output = template; // For list labels (like (ids+=compartmentID)+)
}

tokens {
	ADD  = '+';
	SUB  = '-';
	MULT = '*';
	DIV  = '/';
	
	DECIMAL_POINT = '.';
	
	COMMENT_MARKER = '#';
	
	SPONTANEOUS_INTO = '->';
	INTERACTED_ON_BY = '--';
	INTERACTS_INTO = '=';
}

@header {
package edu.iu.epic.spemshell.runner.parsing.generated;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
}

@lexer::header {package edu.iu.epic.spemshell.runner.parsing.generated;}

@members {
private Map<String, String> parameterBindings = new LinkedHashMap<String, String>();
public Map<String, String> getParameterBindings() {
	return parameterBindings;
}

private Set<String> referencedParameters = new HashSet<String>();
public Set<String> getReferencedParameters() {
	return referencedParameters;
}

private String susceptibleCompartmentID;
public String getSusceptibleCompartmentID() {
	return susceptibleCompartmentID;
}

private Set<String> infectionCompartments = new HashSet<String>();
public Set<String> getInfectionCompartments() {
	return infectionCompartments;
}

/*
private Set<String> latentCompartments = new HashSet<String>();
public Set<String> getLatentCompartments() {
	return latentCompartments;
}

private Set<String> recoveredCompartments = new HashSet<String>();
public Set<String> getRecoveredCompartments() {
	return recoveredCompartments;
}
*/
}


// Ignore whitespace
WHITESPACE
	: ('\t' | ' ')+ {$channel = HIDDEN;}
	;


// Global
modelFile
	: (line)+
	;
line
	: (COMMENT | parameterAssignment | compartmentDeclaration | transitionRule)? NEWLINE
	;
NEWLINE
	: '\r'
	| '\n'
	| '\r\n'	
	;
// ( options {greedy=false;} : . )*
COMMENT
	: COMMENT_MARKER .* NEWLINE {$channel = HIDDEN;}
	;


// Parameters
parameterAssignment
	: parameterID '=' parameterValue {parameterBindings.put($parameterID.text, $parameterValue.text);}
	;
parameterID
	: ID
	;

// We'll have to see how Bruno feels about certain things.. parentheses, exponentiation?  Is there an upper limit?
parameterValue
	: arithmeticParameterExpression
	;	
arithmeticParameterExpression
	: arithmeticParameterTerm ((ADD | SUB) arithmeticParameterTerm)*
	;
arithmeticParameterTerm
	: arithmeticParameterFactor ((MULT | DIV) arithmeticParameterFactor)*
	;
arithmeticParameterFactor
	: parameterID {referencedParameters.add($parameterID.text);}
	| number	
	;


// Compartment declarations
compartmentDeclaration
	: susceptibleCompartmentDeclaration
	| infectedCompartmentDeclaration
	| latentCompartmentDeclaration
	| recoveredCompartmentDeclaration
	;
susceptibleCompartmentDeclaration
	: 'susceptible' compartmentID {susceptibleCompartmentID = $compartmentID.text;}
	;
infectedCompartmentDeclaration
	: 'infection' compartmentID {infectionCompartments.add($compartmentID.text);}
	;
latentCompartmentDeclaration
	: 'latent' compartmentID+ // TODO
	;
recoveredCompartmentDeclaration
	: 'recovered' compartmentID+ // TODO
	;


// Transitions
// Note we are currently assuming no "secondary" stuff
transitionRule
	: transitionRelation transitionRate
	;
// Ask whether numeric constants are permissible here, or are we strictly limited to expressions on PARAMETER_KEYs?
transitionRate
	: parameterValue
	;
transitionRelation
	: spontaneousTransitionRelation
	| interactionTransitionRelation
	;
spontaneousTransitionRelation
	: compartmentID SPONTANEOUS_INTO compartmentID
	;
compartmentID
	: ID
	;
interactionTransitionRelation
	: compartmentID INTERACTED_ON_BY compartmentID INTERACTS_INTO compartmentID
	;


// Useful constants
fragment NUMERIC
	: '0'..'9'
	;	
fragment ALPHABETIC
	: 'a'..'z'
	| 'A'..'Z'
	;
fragment ALPHABETIC_
	: ALPHABETIC
	| '_'
	;
fragment ALPHANUMERIC
	: ALPHABETIC
	| NUMERIC
	;
fragment ALPHANUMERIC_
	: ALPHANUMERIC
	| '_'
	;
ID
	: ALPHABETIC_ (ALPHANUMERIC_)*
	;


number
	: ('-')? UNSIGNED_NUMBER
	;
// TODO Testing!
UNSIGNED_INTEGER
	: (NUMERIC)+ (DECIMAL_POINT)?
	;
fragment UNSIGNED_REAL
	: ((NUMERIC)+)? DECIMAL_POINT (NUMERIC)+
	;
UNSIGNED_NUMBER
	: UNSIGNED_INTEGER
	| UNSIGNED_REAL
	;

