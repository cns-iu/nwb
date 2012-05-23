package edu.iu.nwb.analysis.extractnetfromtable.components;

import prefuse.data.Table;
import prefuse.data.Tuple;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AbstractAggregateFunction;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AssembleAggregateFunctions;

public class FunctionContainer {
	boolean hasSkippedColumns;
	
	public FunctionContainer() {
		this.hasSkippedColumns = false;
	}
	
	protected ValueAttributes mutateFunctions(Tuple tuple, Table t,
			int rowNumber, ValueAttributes va, AggregateFunctionMappings aggregateFunctionMappings,
			int nodeType) {

		AssembleAggregateFunctions assembleAggregateFunction =
			AssembleAggregateFunctions.defaultAssembly();

		AbstractAggregateFunction aggregateFunction;
		String operateColumn = null;
		int appliedNodeType;
		for (int cc = 0; cc < tuple.getColumnCount(); cc++) {
			final String columnName = tuple.getColumnName(cc);
			// see if the function already exists.
			aggregateFunction = va.getFunction(cc); 
			
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

				if (aggregateFunction.skippedColumns() > 0) {
					this.hasSkippedColumns = true;
				}
				
				appliedNodeType = aggregateFunctionMappings.getAppliedNodeType(columnName);

				if (aggregateFunction.skippedColumns() > 0) {
					this.hasSkippedColumns = true;
				}
				
				if (appliedNodeType == nodeType
						|| appliedNodeType == AggregateFunctionMappings.SOURCE_AND_TARGET) {
					aggregateFunction.operate(t.get(rowNumber, operateColumn));
					tuple.set(cc, aggregateFunction.getResult());
				}

				if (va.getFunction(cc) == null) {
					va.putFunction(cc, aggregateFunction);
				}
				if (aggregateFunction.skippedColumns() > 0) {
					this.hasSkippedColumns = true;
				}
			}
		}
		
		return va;
	}
}
