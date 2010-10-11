grammar ModelFile;

options {
	output = template; // For list labels (like (ids+=compartmentID)+)
}

@parser::header {
package edu.iu.epic.modeling.compartment.converters.text.generated;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.iu.epic.modeling.compartment.model.Compartment;
import edu.iu.epic.modeling.compartment.model.Model;
import edu.iu.epic.modeling.compartment.model.exception.ModelModificationException;
}

@lexer::header {
package edu.iu.epic.modeling.compartment.converters.text.generated;
}

@parser::members {
// The model that this parser will produce.
private Model model = new Model();
public Model getModel() {
	return model;
}

public static class UncheckedParsingException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public UncheckedParsingException(Throwable cause) {
		super(cause);
	}
	
	public UncheckedParsingException(String message) {
		super(message);
	}
}


public static ModelFileParser createParserOn(ANTLRStringStream stream) {
	ModelFileLexer lex = new ModelFileLexer(stream);		
	CommonTokenStream tokens = new CommonTokenStream(lex);
   	return new ModelFileParser(tokens);
}

public static ModelFileParser createParserOn(String input) {
   	return createParserOn(new ANTLRStringStream(input));
}

public static ModelFileParser createParserOn(File file) throws IOException {
	return createParserOn(new ANTLRFileStream(file.getCanonicalPath()));
}
}


// Glob multiple spaces or tabs into one ignored token.
WHITESPACE
	: ('\t' | ' ')+ {$channel = HIDDEN;}
	;


// Global
modelFile
	: line EOF
	| (NEWLINE | (line NEWLINE))+ EOF
	;
line
	: COMMENT
	| (ID '=' .*)=> parameterAssignment
	| (ID ('--' | '->') .*)=> transition
	;
NEWLINE
	: ('\r'? '\n')=> '\r'? '\n'
	| '\r'
	;
COMMENT
	: '#' (~('\n'|'\r'))* {$channel = HIDDEN;}
	;


// Parameters
parameterAssignment
	: parameterID '=' parameterValue[new HashSet()]	{ model.setParameterDefinition($parameterID.text, $parameterValue.text); }
	;
	catch[ModelModificationException e] { throw new UncheckedParsingException(e); }
parameterID
	: ID
	;
parameterIDValidator
	: ID EOF
	;
parameterValueValidator[Set referencedParameters]
	: parameterValue[referencedParameters] EOF
	;
parameterValue[Set referencedParameters]
	: arithmeticParameterExpression[referencedParameters]
	;	
arithmeticParameterExpression[Set referencedParameters]
	: arithmeticParameterTerm[referencedParameters] (('+' | '-') arithmeticParameterTerm[referencedParameters])*
	;
arithmeticParameterTerm[Set referencedParameters]
	: arithmeticParameterFactor[referencedParameters] (('*' | '/') arithmeticParameterFactor[referencedParameters])*
	;
arithmeticParameterFactor[Set referencedParameters]
	: parameterID { referencedParameters.add($parameterID.text); }
	| number
	;


// Transitions
transitionRatio
	: parameterValue[new HashSet()]
	;
transition
	@init { boolean isSecondary = false; }
	: ratioTransition transitionRatio ('secondary' { isSecondary = true; })?
		{
		Compartment target = model.getOrAddCompartment($ratioTransition.target);
		target.setSecondary(isSecondary);
		
		model.addRatioTransition(
			model.getOrAddCompartment($ratioTransition.source),
			target,
			$transitionRatio.text);
		}
	| infectionTransition transitionRatio ('secondary' { isSecondary = true; })?
		{
		Compartment target = model.getOrAddCompartment($infectionTransition.target);
		target.setSecondary(isSecondary);
		
		model.addInfectionTransition(
			model.getOrAddCompartment($infectionTransition.source),
			model.getOrAddCompartment($infectionTransition.infector),
			target,
			$transitionRatio.text);
		}
	;
	catch[ModelModificationException e] { throw new UncheckedParsingException(e); }
ratioTransition returns [String source, String target]
	: s=compartmentID '->' t=compartmentID
		{
		$source = $s.text;
		$target = $t.text;
		}
	;
infectionTransition returns [String source, String infector, String target]
	: s=compartmentID '--' i=compartmentID '=' t=compartmentID
		{
		$source = $s.text;
		$infector = $i.text;
		$target = $t.text;
		}
	;
compartmentID
	: ID
	;
compartmentIDValidator
	: compartmentID EOF
	;


// Useful constants
fragment NUMERIC
	: '0'..'9'
	;
/*
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
*/
ID 
	: ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
   	;


number
	: ('-')? UNSIGNED_NUMBER
	;
UNSIGNED_NUMBER
	: (NUMERIC)+
	| (NUMERIC)* '.' (NUMERIC)+
	;
