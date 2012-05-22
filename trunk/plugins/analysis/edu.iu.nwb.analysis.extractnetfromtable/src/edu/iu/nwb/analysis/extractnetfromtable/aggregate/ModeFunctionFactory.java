package edu.iu.nwb.analysis.extractnetfromtable.aggregate;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ModeFunctionFactory implements AggregateFunctionFactory {
	private static final AggregateFunctionName TYPE = AggregateFunctionName.MODE;

	@Override
	public AbstractAggregateFunction getFunction(Class c) {
		if (int.class.equals(c) || Integer.class.equals(c)
				|| int[].class.equals(c) || Integer[].class.equals(c)) {
			return new IntegerMode();
		} else if (boolean.class.equals(c) || Boolean.class.equals(c)
				|| boolean[].class.equals(c) || Boolean[].class.equals(c)) {
			return new BooleanMode();
		} else {
			return new StringMode();
		}
	}

	@Override
	public AggregateFunctionName getType() {
		return ModeFunctionFactory.TYPE;
	}

	private static abstract class AbstractModeFunction extends AbstractAggregateFunction {
		private Map<Object, Integer> objectToOccurrences;

		public AbstractModeFunction() {
			this.objectToOccurrences = new HashMap<Object, Integer>();
		}

		@Override
		public Object getResult() {
			return findKeyWithMaxValue(this.objectToOccurrences);
		}

		@Override
		public void innerOperate(Object object) {
			if (!this.objectToOccurrences.containsKey(object)) {
				this.objectToOccurrences.put(object, 0);
			}

			Integer oldNumberOfOccurrences = this.objectToOccurrences
					.get(object);

			this.objectToOccurrences.put(object, (oldNumberOfOccurrences + 1));
		}

		/**
		 * Get the key with the greatest mapped {@link Integer}.
		 * 
		 * @param map
		 *            A map from {@link Object} to {@link Integer}.
		 * @return The object which was mapped to the highest value.
		 *         {@code null } will be returned if the map was empty.
		 */
		private static Object findKeyWithMaxValue(Map<Object, Integer> map) {
			Object maxKey = null;
			Integer maxValue = Integer.MIN_VALUE;

			for (Entry<Object, Integer> numericEntry : map.entrySet()) {
				Integer value = numericEntry.getValue();

				if (value >= maxValue) {
					maxKey = numericEntry.getKey();
					maxValue = value;
				}
			}

			return maxKey;
		}
	}

	class IntegerMode extends AbstractModeFunction {
		public Class getType() {
			return Integer.class;
		}

		@Override
		protected Integer cleanPrefuseIssue(Object object)
				throws ObjectCouldNotBeCleanedException {
			return cleanIntegerPrefuseBug(object);
		}
	}

	class StringMode extends AbstractModeFunction {
		public Class getType() {
			return String.class;
		}

		@Override
		protected String cleanPrefuseIssue(Object object)
				throws ObjectCouldNotBeCleanedException {
			return cleanStringPrefuseBug(object);
		}
	}

	class BooleanMode extends AbstractModeFunction {
		public Class getType() {
			return Boolean.class;
		}

		@Override
		protected Boolean cleanPrefuseIssue(Object object)
				throws ObjectCouldNotBeCleanedException {
			return cleanBooleanPrefuseBug(object);
		}
	}
}
