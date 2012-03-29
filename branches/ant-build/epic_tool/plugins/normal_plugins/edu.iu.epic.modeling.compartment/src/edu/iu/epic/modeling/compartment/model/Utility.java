package edu.iu.epic.modeling.compartment.model;

public final class Utility {
	private Utility() {
		// Static methods only.
	}

	public static void checkForNullArgument(String name, Object toCheck) {
		if (name == null) {
			throw new IllegalArgumentException("Please don't call me with a null name.");
		}
		if (toCheck == null) {
			throw new IllegalArgumentException(name + " cannot be null");
		}
	}
}
