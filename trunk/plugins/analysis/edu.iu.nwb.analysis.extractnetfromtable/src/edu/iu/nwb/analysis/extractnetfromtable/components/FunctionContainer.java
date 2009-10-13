package edu.iu.nwb.analysis.extractnetfromtable.components;

import prefuse.data.Tuple;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AggregateFunction;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AssembleAggregateFunctions;

public class FunctionContainer {
	
	protected static ValueAttributes mutateFunctions(Tuple tuple, prefuse.data.Table t, int rowNumber, ValueAttributes va, 
			AggregateFunctionMappings aggregateFunctionMappings, int nodeType){
		
		AssembleAggregateFunctions assembleAggregateFunction = AssembleAggregateFunctions.defaultAssembly();
		
		AggregateFunction aggregateFunction;
		String operateColumn = null;
		int appliedNodeType;
		
		for(int k = 0; k < tuple.getColumnCount(); k++){
			final String columnName = tuple.getColumnName(k);
			aggregateFunction = va.getFunction(k);  //see if the function already exists.
			/*
			 * If not, try to create it.
			 * */
			if(aggregateFunction == null) { 
				aggregateFunction = assembleAggregateFunction.getAggregateFunction(aggregateFunctionMappings.getFunctionFromColumnName(columnName), tuple.getColumnType(k));
				
			}
				
			if(aggregateFunction != null){
				
				operateColumn = aggregateFunctionMappings.getOriginalColumnFromFunctionColumn(columnName);
				
				appliedNodeType = aggregateFunctionMappings.getAppliedNodeType(columnName);

				
				
				if(appliedNodeType == nodeType || appliedNodeType == AggregateFunctionMappings.SOURCEANDTARGET){
					aggregateFunction.operate(t.get(rowNumber, operateColumn));
					tuple.set(k, aggregateFunction.getResult());
				
				}
				
				if(va.getFunction(k)== null)
					va.addFunction(k, aggregateFunction);
			}
		}
		return va;
	}

}
