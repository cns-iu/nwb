package edu.iu.epic.modeling.compartment.grammar.parsing;

import org.antlr.runtime.ANTLRStringStream;

public class StrictModelFileLexer extends ModelFileLexer {
//	private RecognitionException re;
	
	public StrictModelFileLexer(ANTLRStringStream stream) {
		super(stream);
//		re = null;
	}

//	public RecognitionException getRecognitionException() {
//		return re;
//	}
//	
//	public boolean encounteredRecognitionException() {
//		return (re != null);
//	}
	
//	@Override
//    public void displayRecognitionError(String[] array, RecognitionException re) {
////    	System.out.println("Recording this RecognitionException: ");
////    	re.printStackTrace(System.err);
//		super.displayRecognitionError(array, re);
//		throw new UncheckedParsingException(re);
////    	this.re = re;
//    }
//	
//	@Override
//	public void reportError(RecognitionException e) {
//		super.reportError(e);
//		throw new UncheckedParsingException(e);
//	}
//	
//	@Override
//	public void recover(RecognitionException re) {
//		
////		this.re = re;
//		super.recover(re);
//		throw new UncheckedParsingException(re);
//	}
//	
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
//		super.recoverFromMismatchedToken(input, ttype, follow);
//		throw new UncheckedParsingException(input.toString());
////		throw new MismatchedTokenException(ttype, input);
//	}
//	
//	@Override
//	public Object recoverFromMismatchedSet(
//			IntStream input, RecognitionException re, BitSet follow) throws RecognitionException {
//		super.recoverFromMismatchedSet(input, re, follow);
//		throw new UncheckedParsingException(re);
////		this.re = re;
////		throw re;
////		return super.recoverFromMismatchedSet(input, re, follow);
//	}
}
