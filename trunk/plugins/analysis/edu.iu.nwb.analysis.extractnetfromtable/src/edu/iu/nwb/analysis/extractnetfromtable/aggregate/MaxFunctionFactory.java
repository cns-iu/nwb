package edu.iu.nwb.analysis.extractnetfromtable.aggregate;

public class MaxFunctionFactory implements AggregateFunctionFactory {
	private static final AggregateFunctionName TYPE = AggregateFunctionName.MAX;

	@Override
	public AbstractAggregateFunction getFunction(Class c) {

		if (c.equals(int.class) || c.equals(Integer.class)
				|| c.equals(int[].class) || c.equals(Integer[].class)) {
			return new IntegerMax();
		}
		if (c.equals(double.class) || c.equals(Double.class)
				|| c.equals(double[].class) || c.equals(Double[].class)) {
			return new DoubleMax();
		}
		if (c.equals(float.class) || c.equals(Float.class)
				|| c.equals(float[].class) || c.equals(Float[].class)) {
			return new FloatMax();
		}

		return null; // can't handle that class;
	}

	@Override
	public AggregateFunctionName getType() {
		return MaxFunctionFactory.TYPE;
	}

	class IntegerMax extends AbstractAggregateFunction {
		private int value;

		public IntegerMax() {
			this.value = Integer.MIN_VALUE;
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
				if (((Number) object).intValue() > this.value) {
					this.value = ((Number) object).intValue();
				}
			} else {
				throw new IllegalArgumentException(
						"IntegerMax can only operate on Numbers.");
			}
		}

		@Override
		protected Integer cleanPrefuseIssue(Object object)
				throws ObjectCouldNotBeCleanedException {
			return cleanIntegerPrefuseBug(object);
		}
	}

	class FloatMax extends AbstractAggregateFunction {
		private float value;

		public FloatMax() {
			this.value = Float.MIN_VALUE;
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
				if (((Number) object).floatValue() > this.value) {
					this.value = ((Number) object).floatValue();
				}
			} else {
				throw new IllegalArgumentException(
						"FloatMax can only operate on Numbers.");
			}
		}

		@Override
		protected Float cleanPrefuseIssue(Object object)
				throws ObjectCouldNotBeCleanedException {
			return cleanFloatPrefuseBug(object);
		}
	}

	class DoubleMax extends AbstractAggregateFunction {
		private double value;

		public DoubleMax() {
			this.value = Double.MIN_VALUE;
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
				if (((Number) object).doubleValue() > this.value) {
					this.value = ((Number) object).doubleValue();
				}
			} else {
				throw new IllegalArgumentException(
						"DoubleMax can only operate on Numbers.");
			}
		}

		@Override
		protected Double cleanPrefuseIssue(Object object)
				throws ObjectCouldNotBeCleanedException {
			return cleanDoublePrefuseBug(object);
		}
	}
}