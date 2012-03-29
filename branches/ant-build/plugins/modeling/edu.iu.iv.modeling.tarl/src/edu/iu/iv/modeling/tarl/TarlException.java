package edu.iu.iv.modeling.tarl;


/**
 * Defines the exception used by the Tarl model.  This exception is used to enforce any constraint imposed by the model.  If the constraint is not met, the exception is thrown.  For example, a constraint of the model is : An author needs to have exactly one topic.  Thus, if an attempt is made to initialize an author object with no topics, the TarlException with the appropriate description is thrown. <br>
 * It should be noted that this exception is a checked exception and hence needs to be handled explicitly by all the methods.
 *
 * @author Jeegar T Maru
 * @see Exception
 */
public class TarlException extends Exception {
    private static final long serialVersionUID = -5680382250359678116L;

    /**
	 * Creates a new instance of <code>TarlException</code> with the detail message as null
	 */
	public TarlException() {
		super();
	}

	/**
	 * Creates a new instance of <code>TarlException</code> with the specified detail message
	 *
	 * @param message Specifies the detail message for the exception
	 */
	public TarlException(String message) {
		super(message);
	}
}
