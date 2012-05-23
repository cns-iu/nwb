package edu.iu.nwb.analysis.pagerank.weighted;

import java.util.Map;

public interface WeightAccessor {
	/**
	 * Get a weight from the given <code>attributes</code> mapping.
	 * 
	 * <p>
	 * The weight must be a {@link Float} because NWB does not support
	 * {@link Double}.
	 * </p>
	 * 
	 * @param attributes
	 *            A mapping of NWB attribute names to their values
	 * @return The weight for the attribute as a {@link Double}.
	 * @throws InvalidWeightException
	 *             If the weight found in the attributes is invalid.
	 * 
	 */
	public float getWeight(Map<String, Object> attributes)
			throws InvalidWeightException;

	/**
	 * This exception represents an invalid weight was found in the attributes.
	 */
	public static class InvalidWeightException extends Exception {
		private static final long serialVersionUID = -6947539735113224760L;

		/**
		 * @see Exception#Exception()
		 */
		public InvalidWeightException() {
			super();
		}

		/**
		 * @see Exception#Exception(String)
		 */
		public InvalidWeightException(String message) {
			super(message);
		}

		/**
		 * @see Exception#Exception(Throwable)
		 */
		public InvalidWeightException(Throwable cause) {
			super(cause);
		}

		/**
		 * @see Exception#Exception(String, Throwable)
		 */
		public InvalidWeightException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
