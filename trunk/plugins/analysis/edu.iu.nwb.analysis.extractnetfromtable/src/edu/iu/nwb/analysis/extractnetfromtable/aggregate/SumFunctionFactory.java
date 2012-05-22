package edu.iu.nwb.analysis.extractnetfromtable.aggregate;

public class SumFunctionFactory implements AggregateFunctionFactory {
	private static final AggregateFunctionName TYPE = AggregateFunctionName.SUM;

	@Override
	public AbstractAggregateFunction getFunction(Class c) {
		if (c.equals(int.class) || c.equals(Integer.class)
				|| c.equals(int[].class) || c.equals(Integer[].class)) {
			return new IntegerSum();
		}
		if (c.equals(double.class) || c.equals(Double.class)
				|| c.equals(double[].class) || c.equals(Double[].class)) {
			return new DoubleSum();
		}
		if (c.equals(float.class) || c.equals(Float.class)
				|| c.equals(float[].class) || c.equals(Float[].class)) {
			return new FloatSum();
		}
		return null; // can't handle class error.
	}

	@Override
	public AggregateFunctionName getType() {
		return SumFunctionFactory.TYPE;
	}

	class DoubleSum extends AbstractAggregateFunction {
		private double total;

		public DoubleSum() {
			this.total = 0;
		}

		@Override
		public Double getResult() {
			return this.total;
		}

		@Override
		public Class getType() {
			return Double.class;
		}

		@Override
		protected void innerOperate(Object object) {
			if (object instanceof Number) {
				this.total += ((Number) object).doubleValue();
			} else {
				throw new IllegalArgumentException(
						"DoubleSum can only operate on Numbers.");
			}
		}

		@Override
		protected Double cleanPrefuseIssue(Object object)
				throws ObjectCouldNotBeCleanedException {
			return cleanDoublePrefuseBug(object);
		}
	}

	class FloatSum extends AbstractAggregateFunction {
		private float total;

		public FloatSum() {
			this.total = 0;
		}

		@Override
		public Float getResult() {
			return this.total;
		}

		@Override
		public Class getType() {
			return Float.class;
		}

		@Override
		protected void innerOperate(Object object) {
			if (object instanceof Number) {
				this.total += ((Number) object).floatValue();
			} else {
				throw new IllegalArgumentException(
						"FloatSum can only operate on Numbers.");
			}
		}

		@Override
		protected Float cleanPrefuseIssue(Object object)
				throws ObjectCouldNotBeCleanedException {
			return cleanFloatPrefuseBug(object);
		}
	}

	class IntegerSum extends AbstractAggregateFunction {
		private int total;

		public IntegerSum() {
			this.total = 0;
		}

		@Override
		public Integer getResult() {
			return this.total;
		}

		@Override
		public Class getType() {
			return Integer.class;
		}

		@Override
		protected void innerOperate(Object object) {
			if (object instanceof Number) {
				this.total += ((Number) object).intValue();
			} else {
				throw new IllegalArgumentException(
						"IntegerSum can only operate on Numbers.");
			}
		}

		@Override
		protected Integer cleanPrefuseIssue(Object object)
				throws ObjectCouldNotBeCleanedException {
			return cleanIntegerPrefuseBug(object);
		}
	}
}