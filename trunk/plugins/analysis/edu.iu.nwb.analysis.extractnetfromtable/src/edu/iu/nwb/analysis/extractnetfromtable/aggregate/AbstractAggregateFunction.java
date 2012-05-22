package edu.iu.nwb.analysis.extractnetfromtable.aggregate;

import org.cishell.utilities.PrefuseUtilities;
import org.cishell.utilities.PrefuseUtilities.EmptyInterpretedObjectException;
import org.cishell.utilities.PrefuseUtilities.UninterpretableObjectException;

public abstract class AbstractAggregateFunction {

	public abstract Object getResult();

	public abstract Class getType();

	/**
	 * Return the number of records that were skipped when processing due to
	 * prefuse interpretation exceptions.
	 * 
	 * @return The number of skipped columns.
	 */
	public int skippedColumns() {
		return this.skippedCount;
	}

	/**
	 * Operate the function using the provided object. Inheriting classes should
	 * override {@link #innerOperate(Object)} instead.
	 * 
	 * @param object
	 *            The object that should contribute to the function.
	 */
	public final void operate(Object object) {
		try {
			Object cleanObject = cleanPrefuseIssue(object);
			innerOperate(cleanObject);
		} catch (ObjectCouldNotBeCleanedException e) {
			incrementSkippedColumns();
		}
	}

	/**
	 * Perform the function operation on the object. The object should already
	 * be cleaned by {@link #cleanPrefuseIssue(Object)}.
	 * 
	 * @param object
	 *            The object to be operated.
	 */
	protected abstract void innerOperate(Object object);

	/**
	 * Any {@link AbstractAggregateFunction} must clean objects for a Prefuse
	 * issue where columns that have an empty value for a record are wrapped in an
	 * array. The functions should not throw an error for these empty values,
	 * but instead record the number of records that were skipped and provide
	 * them if the function {@link #skippedColumns()} is called.
	 * 
	 * @param object
	 *            The object to be cleaned.
	 * @return The cleaned object.
	 * @throws ObjectCouldNotBeCleanedException
	 *             If the object could not be cleaned.
	 */
	protected abstract Object cleanPrefuseIssue(Object object)
			throws ObjectCouldNotBeCleanedException;

	/**
	 * An implementation of {@link #cleanPrefuseIssue(Object)} for
	 * {@link Number} objects.
	 * 
	 * @param object
	 *            The object to be cleaned.
	 * @return The object represented as a {@link Number}.
	 * @throws ObjectCouldNotBeCleanedException
	 *             If the object cannot be interpreted as a {@link Number} or
	 *             the object represented the empty object in Prefuse.
	 */
	protected static Number cleanNumberPrefuseBug(Object object)
			throws ObjectCouldNotBeCleanedException {
		try {
			return PrefuseUtilities.interpretObjectAsNumber(object);
		} catch (EmptyInterpretedObjectException e) {
			throw new ObjectCouldNotBeCleanedException(e);
		} catch (UninterpretableObjectException e) {
			throw new ObjectCouldNotBeCleanedException(e);
		}
	}

	/**
	 * An implementation of {@link #cleanPrefuseIssue(Object)} for
	 * {@link Integer} objects.
	 * 
	 * @param object
	 *            The object to be cleaned.
	 * @return The object represented as a {@link Integer}.
	 * @throws ObjectCouldNotBeCleanedException
	 *             If the object cannot be interpreted as a {@link Integer} or
	 *             the object represented the empty object in Prefuse.
	 */
	protected static Integer cleanIntegerPrefuseBug(Object object)
			throws ObjectCouldNotBeCleanedException {
		try {
			return PrefuseUtilities.interpretObjectAsInteger(object);
		} catch (EmptyInterpretedObjectException e) {
			throw new ObjectCouldNotBeCleanedException(e);
		} catch (UninterpretableObjectException e) {
			throw new ObjectCouldNotBeCleanedException(e);
		}
	}

	/**
	 * An implementation of {@link #cleanPrefuseIssue(Object)} for {@link Float}
	 * objects.
	 * 
	 * @param object
	 *            The object to be cleaned.
	 * @return The object represented as a {@link Float}.
	 * @throws ObjectCouldNotBeCleanedException
	 *             If the object cannot be interpreted as a {@link Float} or the
	 *             object represented the empty object in Prefuse.
	 */
	protected static Float cleanFloatPrefuseBug(Object object)
			throws ObjectCouldNotBeCleanedException {
		try {
			return PrefuseUtilities.interpretObjectAsFloat(object);
		} catch (EmptyInterpretedObjectException e) {
			throw new ObjectCouldNotBeCleanedException(e);
		} catch (UninterpretableObjectException e) {
			throw new ObjectCouldNotBeCleanedException(e);
		}
	}

	/**
	 * An implementation of {@link #cleanPrefuseIssue(Object)} for
	 * {@link Double} objects.
	 * 
	 * @param object
	 *            The object to be cleaned.
	 * @return The object represented as a {@link Double}.
	 * @throws ObjectCouldNotBeCleanedException
	 *             If the object cannot be interpreted as a {@link Double} or
	 *             the object represented the empty object in Prefuse.
	 */
	protected static Double cleanDoublePrefuseBug(Object object)
			throws ObjectCouldNotBeCleanedException {
		try {
			return PrefuseUtilities.interpretObjectAsDouble(object);
		} catch (EmptyInterpretedObjectException e) {
			throw new ObjectCouldNotBeCleanedException(e);
		} catch (UninterpretableObjectException e) {
			throw new ObjectCouldNotBeCleanedException(e);
		}
	}

	/**
	 * An implementation of {@link #cleanPrefuseIssue(Object)} for
	 * {@link Boolean} objects.
	 * 
	 * @param object
	 *            The object to be cleaned.
	 * @return The object represented as a {@link Boolean}.
	 * @throws ObjectCouldNotBeCleanedException
	 *             If the object represents the empty object in Prefuse.
	 */
	protected static Boolean cleanBooleanPrefuseBug(Object object)
			throws ObjectCouldNotBeCleanedException {
		try {
			return PrefuseUtilities.interpretObjectAsBoolean(object);
		} catch (EmptyInterpretedObjectException e) {
			throw new ObjectCouldNotBeCleanedException(e);
		}
	}

	/**
	 * An implementation of {@link #cleanPrefuseIssue(Object)} for
	 * {@link String} objects.
	 * 
	 * @param object
	 *            The object to be cleaned.
	 * @return The object represented as a {@link String}.
	 * @throws ObjectCouldNotBeCleanedException
	 *             If the object represents the empty object in Prefuse.
	 */
	protected static String cleanStringPrefuseBug(Object object)
			throws ObjectCouldNotBeCleanedException {
		try {
			return PrefuseUtilities.interpretObjectAsString(object);
		} catch (EmptyInterpretedObjectException e) {
			throw new ObjectCouldNotBeCleanedException(e);
		}
	}

	private int skippedCount;

	private void incrementSkippedColumns() {
		this.skippedCount++;
	}

	/**
	 * The exception to be thrown in the object could not be cleaned by
	 * {@link AbstractAggregateFunction#cleanPrefuseBug}.
	 */
	protected static class ObjectCouldNotBeCleanedException extends Exception {
		private static final long serialVersionUID = -776940374030425321L;

		/**
		 * @see Exception#Exception()
		 */
		public ObjectCouldNotBeCleanedException() {
			super();
		}

		/**
		 * @see Exception#Exception(String)
		 */
		public ObjectCouldNotBeCleanedException(String message) {
			super(message);
		}

		/**
		 * @see Exception#Exception(Throwable)
		 */
		public ObjectCouldNotBeCleanedException(Throwable cause) {
			super(cause);
		}

		/**
		 * @see Exception#Exception(String, Throwable)
		 */
		public ObjectCouldNotBeCleanedException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
