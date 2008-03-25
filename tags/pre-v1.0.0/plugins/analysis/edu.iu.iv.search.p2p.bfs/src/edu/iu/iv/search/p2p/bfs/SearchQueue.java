package edu.iu.iv.search.p2p.bfs;

/**
 * This method provides the queue functionality for BreadthFirstSearch.
 * @author Hardik Sheth (hsheth@indiana.edu)
*/
public class SearchQueue {

	private QueueElement head; 	// head element in the queue
	private QueueElement tail; 		// tail element in the queue
	
	public SearchQueue() {
		head = null;
		tail = null;
	}
	
	/**
	 * Insert an element in the queue
	 * @param value
	 */
	public void Enqueue(int value)
	{
		QueueElement newElement = new QueueElement(value);
	   	newElement.setNext(tail);
	   	newElement.setPrev(null);
	   	if (tail != null)
			tail.setPrev(newElement);
	   	tail = newElement;
	   	if (head == null)
			head = tail;
	}
 
    /**
     * @return Returns true if the queue is empty and false otherwise.
     */
	public boolean isEmpty()
	{
		if (head == null)
			return true;
		else
			return false;
	}
	
	/**
	 * Return the value of first element in the queue
	 * @return value of first element
	 */
	public int Dequeue()
	{
		if (head == null)
	  	{
			System.err.println("Attempting to dequeue the front of an empty queue");
		 	return -1;
	  	}
	  	int temp = head.getValue();
	  	head = head.getPrev();
	  	if (head == null)
			tail = null;
	  	else
			head.setNext(null);
	  	return temp;
   	}

} // end of class SearchQueue
