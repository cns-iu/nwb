/*
 * E. g. "asdf " # 19
 * 
 * Created on Mar 17, 2003
 *
 * @author henkel@cs.colorado.edu
 * 
 */
package bibtex.dom;

import java.io.PrintWriter;

/**
 * 
 * Two abstract values concatenated by the hash-operator (#).
 * 
 * Examples:
 * <ul>
 * 	<li>acm # " SIGPLAN"</li>
 * 	<li>"10th " # pldi</li>
 * </ul>
 * 
 * 
 * @author henkel
 */
public final class BibtexConcatenatedValue extends BibtexAbstractValue {

	BibtexConcatenatedValue(BibtexFile file,BibtexAbstractValue left, BibtexAbstractValue right){
		super(file);
		this.left=left;
		this.right=right;
	}

	private BibtexAbstractValue left, right;

	/**
	 * @return BibtexValue
	 */
	public BibtexAbstractValue getLeft() {
		return left;
	}

	/**
	 * @return BibtexValue
	 */
	public BibtexAbstractValue getRight() {
		return right;
	}

	/**
	 * Sets the left.
	 * @param left The left to set
	 */
	public void setLeft(BibtexAbstractValue left) {
	     
	    
		this.left = left;
	}

	/**
	 * Sets the right.
	 * @param right The right to set
	 */
	public void setRight(BibtexAbstractValue right) {
	    
	    
		this.right = right;
	}

	/* (non-Javadoc)
	 * @see bibtex.dom.BibtexNode#printBibtex(java.io.PrintWriter)
	 */
	public void printBibtex(PrintWriter writer) {
	  
		this.left.printBibtex(writer);
		writer.print('#');
		this.right.printBibtex(writer);
	}

}
