package edu.iu.nwb.analysis.extractnetfromtable.aggregate;

public class CountFunctionFactory implements AggregateFunctionFactory {
	private static final AggregateFunctionName TYPE = AggregateFunctionName.COUNT;

	@Override
	public AbstractAggregateFunction getFunction(Class c) {
		return new Count();
	}

	@Override
	public AggregateFunctionName getType() {
		return CountFunctionFactory.TYPE;
	}

	class Count extends AbstractAggregateFunction {
		private int total;

		public Count() {
			this.total = 0;
		}

		@Override
		public Object getResult() {
			return this.total;
		}

		@Override
		public Class getType() {

			/*
			 * the return data type has to be *int* & not *Integer*. It changes
			 * the behavior of nwb system drastically.
			 */
			return int.class;
		}

		/**
		 * The actual object is ignored.
		 */
		@Override
		protected void innerOperate(Object object) {
			this.total += 1;
		}

		/**
		 * Unimplemented. The actual object is not important for this aggregate
		 * function.
		 */
		@Override
		protected Object cleanPrefuseIssue(Object object)
				throws ObjectCouldNotBeCleanedException {
			return object;
		}
	}
}