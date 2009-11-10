package edu.iu.epic.modeling.compartment.grammar.parsing;

import java.io.File;
import java.io.IOException;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.TokenStream;

public class StrictModelFileParser extends ModelFileParser {
//	private RecognitionException re;

	
	public StrictModelFileParser(TokenStream input) {
		super(input);
	}
	
	
	public static StrictModelFileParser createParserOn(ANTLRStringStream stream) {
		StrictModelFileLexer lex = new StrictModelFileLexer(stream);		
		CommonTokenStream tokens = new CommonTokenStream(lex);
	   	return new StrictModelFileParser(tokens);
	}
	
	public static StrictModelFileParser createParserOn(String input) {
	   	return createParserOn(new ANTLRStringStream(input));
	}
	
	public static StrictModelFileParser createParserOn(File file) throws IOException {
		return createParserOn(new ANTLRFileStream(file.getCanonicalPath()));
	}
	
	
//	public boolean encounteredRecognitionException() {
//		return (encounteredRecognitionExceptionWhileParsing()
//				|| encounteredRecognitionExceptionWhileLexing());
//	}
//	
//	public boolean encounteredRecognitionExceptionWhileParsing() {
//		return (re != null);
//	}

//	// TODO This is a rather intense cast; how might we avoid it?
//	public boolean encounteredRecognitionExceptionWhileLexing() {
//		TokenStream inputTokenStream = getTokenStream();
//		TokenSource inputTokenSource = inputTokenStream.getTokenSource();
//		StrictModelFileLexer lexer = (StrictModelFileLexer) inputTokenSource;
//		
//		return lexer.encounteredRecognitionException();
//	}
	
//	@Override
//	public void recover(IntStream stream, RecognitionException re) {
//		
////		this.re = re;
//		super.recover(stream, re);
//		throw new UncheckedParsingException(re);
//	}
//	
//	@Override
//	protected Object recoverFromMismatchedToken(
//			IntStream input, int ttype, BitSet follow) throws RecognitionException {
////		throw new UncheckedParsingException(input.toString());
//		super.recoverFromMismatchedToken(input, ttype, follow);
//		throw new MismatchedTokenException(ttype, input);
//	}
//	
//	@Override
//	public Object recoverFromMismatchedSet(
//			IntStream input, RecognitionException re, BitSet follow) throws RecognitionException {
//		super.recoverFromMismatchedSet(input, re, follow);
//		throw new UncheckedParsingException(re);
////		this.re = re;
////		return super.recoverFromMismatchedSet(input, re, follow);
//	}
}
