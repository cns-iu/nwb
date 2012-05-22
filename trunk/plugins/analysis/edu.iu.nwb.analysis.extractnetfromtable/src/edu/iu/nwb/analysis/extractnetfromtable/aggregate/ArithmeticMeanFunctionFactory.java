package edu.iu.nwb.analysis.extractnetfromtable.aggregate;

public class ArithmeticMeanFunctionFactory implements AggregateFunctionFactory {
	private final static AggregateFunctionName TYPE = AggregateFunctionName.ARITHMETICMEAN;

	@Override
	public AbstractAggregateFunction getFunction(Class c) {
		if (c.equals(int.class) || c.equals(Integer.class)
				|| c.equals(Integer[].class) || c.equals(int[].class)) {
			return new DoubleArithmeticMean();
		}
		if (c.equals(double.class) || c.equals(Double.class)
				|| c.equals(Double[].class) || c.equals(double[].class)) {
			return new DoubleArithmeticMean();
		}
		if (c.equals(float.class) || c.equals(Float.class)
				|| c.equals(Float[].class) || c.equals(float[].class)) {
			return new FloatArithmeticMean();
		}
		return null;
	}

	@Override
	public AggregateFunctionName getType() {
		return ArithmeticMeanFunctionFactory.TYPE;
	}

	class DoubleArithmeticMean extends AbstractAggregateFunction {
		private double total;
		private long items;

		public DoubleArithmeticMean() {
			this.total = 0;
			this.items = 0;
		}

		@Override
		public Double getResult() {
			return new Double(this.total / (this.items));
		}

		@Override
		public Class<Double> getType() {
			return Double.class;
		}

		@Override
		protected void innerOperate(Object object) {
			if (object instanceof Number) {
				this.items += 1;
				this.total += ((Number) object).doubleValue();
			} else {
				throw new IllegalArgumentException(
						"DoubleArithmeticMean can only operate on Numbers.");
			}
		}

		@Override
		protected Double cleanPrefuseIssue(Object object)
				throws ObjectCouldNotBeCleanedException {
			return cleanDoublePrefuseBug(object);
		}
	}

	class FloatArithmeticMean extends AbstractAggregateFunction {
		private float total;
		private long items;

		public FloatArithmeticMean() {
			this.total = 0;
			this.items = 0;
		}

		@Override
		public Float getResult() {
			return new Float(this.total / (this.items));
		}

		@Override
		public Class<Float> getType() {
			return Float.class;
		}

		@Override
		protected void innerOperate(Object object) {
			if (object instanceof Number) {
				this.items += 1;
				this.total += ((Number) object).floatValue();
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