package edu.iu.nwb.analysis.extractnetfromtable.aggregate;

/**
 * Aggregate Function Names
 * 
 */
public enum AggregateFunctionName {
	ARITHMETICMEAN("arithmeticmean"),
	SUM("sum"),
	COUNT("count"),
	GEOMETRICMEAN("geometricmean"),
	MAX("max"),
	MIN("min"),
	MODE("mode");

	private final String name;

	AggregateFunctionName(final String name) {
		this.name = name;
	}

	/**
	 * Find a {@link AggregateFunctionName} by its name.
	 * 
	 * @param name
	 *            The name of the {@link AggregateFunctionName}.
	 * @return The {@link AggregateFunctionName} specified by the given name.
	 * @throws NullPointerException
	 *             if {@code name} is null.
	 * @throws IllegalArgumentException
	 *             if there is no {@link AggregateFunctionName} with the
	 *             specified name.
	 */
	public static AggregateFunctionName fromString(String name) {
		if (null == name) {
			throw new NullPointerException();
		}

		for (AggregateFunctionName aggregateFunctionName : AggregateFunctionName
				.values()) {
			if (aggregateFunctionName.name.equalsIgnoreCase(name)) {
				return aggregateFunctionName;
			}
		}

		throw new IllegalArgumentException(
				"No AggregateFunctionNames could be found for '" + name + "'.");
	}

	@Override
	public final String toString() {
		return this.name;
	}
}