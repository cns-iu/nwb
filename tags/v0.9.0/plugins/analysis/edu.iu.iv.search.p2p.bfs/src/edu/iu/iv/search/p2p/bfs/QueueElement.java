package edu.iu.iv.search.p2p.bfs;

/**
 * This class provides an element in a search queue 
 * @author Hardik Sheth (hsheth@indiana.edu)
*/
public class QueueElement {
	
	private int value;
	private QueueElement prev;
	private QueueElement next;

	/**
	 * Constructor for QueueElement.
	 * Sets the value of the QueueElement
	 */
	public QueueElement(int value) {
		this.value = value;
	}
	
	public QueueElement() {		
		prev = null;
		next = null;
	}
	
	/**
	 * This method returns the value of the element
	 * @return value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * This method sets the value of the element
	 * @param value
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * This method returns the next element 
	 * @return next element
	 */
	public QueueElement getNext() {
		return next;
	}

	/**
	 * This method returns the previous element
	 * @return previous element
	 */
	public QueueElement getPrev() {
		return prev;
	}

	/**
	 * This method sets the next element
	 * @param element
	 */
	public void setNext(QueueElement element) {
		next = element;
	}

	/**
	 * This method sets the previous element
	 * @param element
	 */
	public void setPrev(QueueElement element) {
		prev = element;
	}

}