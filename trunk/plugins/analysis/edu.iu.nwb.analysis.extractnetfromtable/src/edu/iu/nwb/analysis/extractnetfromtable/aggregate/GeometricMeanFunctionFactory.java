package edu.iu.nwb.analysis.extractnetfromtable.aggregate;

public class GeometricMeanFunctionFactory implements AggregateFunctionFactory {
	private static final AggregateFunctionName TYPE = AggregateFunctionName.GEOMETRICMEAN;

	@Override
	public AbstractAggregateFunction getFunction(Class c) {
		if (c.equals(int.class) || c.equals(Integer.class)
				|| c.equals(int[].class) || c.equals(Integer[].class)) {
			return new DoubleGeometricMean();
		}
		if (c.equals(double.class) || c.equals(Double.class)
				|| c.equals(double[].class) || c.equals(Double[].class)) {
			return new DoubleGeometricMean();
		}
		if (c.equals(float.class) || c.equals(Float.class)
				|| c.equals(float[].class) || c.equals(Float[].class)) {
			return new FloatGeometricMean();
		}
		return null; // throw some sort of error to let them know that the class
						// is not handled.

	}

	@Override
	public AggregateFunctionName getType() {
		return GeometricMeanFunctionFactory.TYPE;
	}

	class DoubleGeometricMean extends AbstractAggregateFunction {
		private double value;
		private long items;

		public DoubleGeometricMean() {
			this.items = 0;
			this.value = 1.0;
		}

		@Override
		public Object getResult() {
			final double result = Math.pow(this.value, (1.0 / this.items));
			return result;
		}

		@Override
		public Class getType() {
			return Double.class;
		}

		@Override
		protected void innerOperate(Object object) {
			if (object instanceof Number) {
				this.items += 1;
				this.value *= ((Number) object).doubleValue();
			} else {
				throw new IllegalArgumentException(
						"DoubleGeometricMean can only operate on Numbers.");
			}
		}

		@Override
		protected Double cleanPrefuseIssue(Object object)
				throws ObjectCouldNotBeCleanedException {
			return cleanDoublePrefuseBug(object);
		}
	}

	class FloatGeometricMean extends AbstractAggregateFunction {
		private float value;
		private long items;

		public FloatGeometricMean() {
			this.value = 1;
			this.items = 0;
		}

		@Override
		public Object getResult() {
			final float result = (float) Math.pow(this.value,
					(1 / (float) this.items));
			return result;
		}

		@Override
		public Class getType() {
			return Float.class;
		}

		@Override
		protected void innerOperate(Object object) {
			if (object instanceof Number) {
				this.items += 1;
				this.value *= ((Number) object).floatValue();
			} else {
				throw new IllegalArgumentException(
						"FloatArithmeticMean can only operate on Numbers.");
			}
		}

		@Override
		protected Float cleanPrefuseIssue(Object object)
				throws ObjectCouldNotBeCleanedException {
			return cleanFloatPrefuseBug(object);
		}
	}

}