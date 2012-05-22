package edu.iu.nwb.analysis.extractnetfromtable.aggregate;

public class MinFunctionFactory implements AggregateFunctionFactory {
	private static final AggregateFunctionName TYPE = AggregateFunctionName.MIN;

	@Override
	public AggregateFunctionName getType() {
		return MinFunctionFactory.TYPE;
	}

	@Override
	public AbstractAggregateFunction getFunction(Class c) {
		if (c.equals(int.class) || c.equals(Integer.class)
				|| c.equals(int[].class) || c.equals(Integer[].class)) {
			return new IntegerMin();
		}
		if (c.equals(double.class) || c.equals(Double.class)
				|| c.equals(double[].class) || c.equals(Double[].class)) {
			return new DoubleMin();
		}
		if (c.equals(float.class) || c.equals(Float.class)
				|| c.equals(float[].class) || c.equals(Float[].class)) {
			return new FloatMin();
		}
		return null;
	}

	class DoubleMin extends AbstractAggregateFunction {
		private double value;

		public DoubleMin() {
			this.value = Double.MAX_VALUE;
		}

		@Override
		public Object getResult() {
			return new Double(this.value);
		}

		@Override
		public Class getType() {
			return Double.class;
		}

		@Override
		protected void innerOperate(Object object) {
			if (object instanceof Number) {
				if (((Number) object).doubleValue() < this.value) {
					this.value = ((Number) object).doubleValue();
				}
			} else {
				throw new IllegalArgumentException(
						"DoubleMin can only operate on Numbers.");
			}
		}

		@Override
		protected Double cleanPrefuseIssue(Object object)
				throws ObjectCouldNotBeCleanedException {
			return cleanDoublePrefuseBug(object);
		}
	}

	class FloatMin extends AbstractAggregateFunction {
		private float value;

		public FloatMin() {
			this.value = Float.MAX_VALUE;
		}

		@Override
		public Object getResult() {
			return new Float(this.value);
		}

		@Override
		public Class getType() {
			return Float.class;
		}

		@Override
		protected void innerOperate(Object object) {
			if (object instanceof Number) {
				if (((Number) object).floatValue() < this.value) {
					this.value = ((Number) object).floatValue();
				}
			} else {
				throw new IllegalArgumentException(
						"FloatMin can only operate on Numbers.");
			}
		}

		@Override
		protected Float cleanPrefuseIssue(Object object)
				throws ObjectCouldNotBeCleanedException {
			return cleanFloatPrefuseBug(object);
		}
	}

	class IntegerMin extends AbstractAggregateFunction {
		private int value;

		public IntegerMin() {
			this.value = Integer.MAX_VALUE;
		}

		@Override
		public Object getResult() {
			return this.value;
		}

		@Override
		public Class getType() {
			return Integer.class;
		}

		@Override
		protected void innerOperate(Object object) {
			if (object instanceof Number) {
				if (((Number) object).intValue() < this.value) {
					this.value = ((Number) object).intValue();
				}
			} else {
				throw new IllegalArgumentException(
						"IntegerMin can only operate on Numbers.");
			}

		}

		@Override
		protected Integer cleanPrefuseIssue(Object object)
				throws ObjectCouldNotBeCleanedException {
			return cleanIntegerPrefuseBug(object);
		}
	}
}