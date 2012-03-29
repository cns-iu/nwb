package edu.iu.nwb.analysis.extractnetfromtable.components;

import prefuse.data.Table;
import prefuse.data.Tuple;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AggregateFunction;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AssembleAggregateFunctions;

public class FunctionContainer {
	protected static ValueAttributes mutateFunctions(Tuple tuple, Table t,
			int rowNumber, ValueAttributes va, AggregateFunctionMappings aggregateFunctionMappings,
			int nodeType) {

		AssembleAggregateFunctions assembleAggregateFunction =
			AssembleAggregateFunctions.defaultAssembly();

		AggregateFunction aggregateFunction;
		String operateColumn = null;
		int appliedNodeType;

		for (int cc = 0; cc < tuple.getColumnCount(); cc++) {
			final String columnName = tuple.getColumnName(cc);
			aggregateFunction = va.getFunction(cc); // see if the function
													// already exists.

			// If not, try to create it.
			if (aggregateFunction == null) {
				aggregateFunction =
					assembleAggregateFunction.getAggregateFunction(
							aggregateFunctionMappings.getFunctionFromColumnName(columnName),
							tuple.getColumnType(cc));
			}

			if (aggregateFunction != null) {
				operateColumn = aggregateFunctionMappings
						.getOriginalColumnFromFunctionColumn(columnName);

				appliedNodeType = aggregateFunctionMappings.getAppliedNodeType(columnName);

				if (appliedNodeType == nodeType
						|| appliedNodeType == AggregateFunctionMappings.SOURCE_AND_TARGET) {
					aggregateFunction.operate(t.get(rowNumber, operateColumn));
					tuple.set(cc, aggregateFunction.getResult());
				}

				if (va.getFunction(cc) == null) {
					va.addFunction(cc, aggregateFunction);
				}
			}
		}
		
		return va;
	}
}
