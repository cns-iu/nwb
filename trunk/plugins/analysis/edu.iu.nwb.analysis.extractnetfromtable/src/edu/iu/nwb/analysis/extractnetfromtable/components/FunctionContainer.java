package edu.iu.nwb.analysis.extractnetfromtable.components;

import prefuse.data.Tuple;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AggregateFunction;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AssembleAggregateFunctions;

public class FunctionContainer {
	
	protected static ValueAttributes mutateFunctions(Tuple tup, prefuse.data.Table t, int rowNumber, ValueAttributes va, 
			AggregateFunctionMappings afm, int nodeType){
		
		AssembleAggregateFunctions aaf = AssembleAggregateFunctions.defaultAssembly();
		
		AggregateFunction af;
		String operateColumn = null;
		int appliedNodeType;
		
		for(int k = 0; k < tup.getColumnCount(); k++){
			final String colName = tup.getColumnName(k);
			af = va.getFunction(k);  //see if the function already exists.
			if(af == null) //if not, try to create it.
				af = aaf.getAggregateFunction(afm.getFunctionFromColumnName(colName), tup.getColumnType(k));
				
			if(af != null){
				
				operateColumn = afm.getOriginalColumnFromFunctionColumn(colName);
				appliedNodeType = afm.getAppliedNodeType(colName);
				if(appliedNodeType == nodeType || appliedNodeType == AggregateFunctionMappings.SOURCEANDTARGET){
					af.operate(t.get(rowNumber, operateColumn));
					tup.set(k, af.getResult());
				}
				if(va.getFunction(k)== null)
					va.addFunction(k, af);
			}
		}
		return va;
	}

}
