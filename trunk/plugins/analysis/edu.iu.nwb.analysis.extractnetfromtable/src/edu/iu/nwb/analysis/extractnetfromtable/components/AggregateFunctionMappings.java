package edu.iu.nwb.analysis.extractnetfromtable.components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

import org.osgi.service.log.LogService;

import prefuse.data.Schema;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AssembleAggregateFunctions;

public class AggregateFunctionMappings {
	private final HashMap metaColumnNameToFunctionMap = new HashMap();
	private final HashMap functionColumnToOriginalColumnMap = new HashMap();
	private final HashMap functionColumnToAppliedNodeTypeMap = new HashMap();
	private final HashMap labelToFunctionMap = new HashMap();
	public static final int SOURCEANDTARGET = 0;
	public static final int SOURCE = 1;
	public static final int TARGET = 2;
	
	
	public void addFunctionMapping(String functionValueCol, String originalCol, String functionType){
		this.metaColumnNameToFunctionMap.put(functionValueCol, functionType);
		this.functionColumnToOriginalColumnMap.put(functionValueCol, originalCol);
		this.functionColumnToAppliedNodeTypeMap.put(functionValueCol, new Integer(SOURCEANDTARGET));
	}
	
	public void addFunctionMapping(String functionValueCol, String originalCol, String functionType, int nodeType){
		this.metaColumnNameToFunctionMap.put(functionValueCol, functionType);
		this.functionColumnToOriginalColumnMap.put(functionValueCol, originalCol);
		this.functionColumnToAppliedNodeTypeMap.put(functionValueCol, new Integer(nodeType));
	}
	
	
	
	public ValueAttributes addFunctionRow(Object label, ValueAttributes va){
		
			this.labelToFunctionMap.put(label, va);
			return va;
		
	}
	
	public String getFunctionFromColumnName(String columnName){
		return (String) this.metaColumnNameToFunctionMap.get(columnName);
	}
	
	public String getOriginalColumnFromFunctionColumn(String columnName){
		return (String) this.functionColumnToOriginalColumnMap.get(columnName);
	}
	
	public ValueAttributes getFunctionRow(Object label){
		return (ValueAttributes)this.labelToFunctionMap.get(label);
	}
	
	public int getAppliedNodeType(String columnName){
		return ((Integer)this.functionColumnToAppliedNodeTypeMap.get(columnName)).intValue();
	}
	
	public static void parseProperties(Schema input, Schema nodes, Schema edges, Properties p, 
			AggregateFunctionMappings nodeFunctionMappings, 
			AggregateFunctionMappings edgeFunctionMappings, LogService log){
		
		if(p != null){
			HashSet functionNames = new HashSet(AssembleAggregateFunctions.defaultAssembly().getFunctionNames());
			HashSet columnNames = new HashSet();

			for(int i = 0; i < input.getColumnCount(); i++){
				columnNames.add(input.getColumnName(i));
			}

			for (final Iterator it = p.keySet().iterator(); it.hasNext();) {
				final String key = (String) it.next();
				String[] functionDefinitionLHS = key.split("\\.");
				String[] functionDefinitionRHS = p.getProperty(key).split("\\.");
				String applyToNodeType = null;
				int nodeType = -1;
				
				
				final String sourceColumnName = functionDefinitionRHS[0];
				final Class columnType = input.getColumnType(sourceColumnName);
				final String function = functionDefinitionRHS[functionDefinitionRHS.length-1];
				if(functionDefinitionRHS.length == 3){
					applyToNodeType = functionDefinitionRHS[1];	
				}
				
				try{
				if(applyToNodeType.equalsIgnoreCase("source")){
					nodeType = AggregateFunctionMappings.SOURCE;
				}
				else if(applyToNodeType.equalsIgnoreCase("target")){
					nodeType = AggregateFunctionMappings.TARGET;
				}
				else{
					nodeType = AggregateFunctionMappings.SOURCEANDTARGET;
				}
				}catch(NullPointerException npe){
					nodeType = AggregateFunctionMappings.SOURCEANDTARGET;
				}
				
				
				String newColumnName = functionDefinitionLHS[functionDefinitionLHS.length-1];
				
				if(functionNames.contains(function) && columnNames.contains(sourceColumnName) && !columnNames.contains(newColumnName)){
					if (key.startsWith("edge.")) {
						createColumn(newColumnName,sourceColumnName, function, columnType, edges);
						edgeFunctionMappings.addFunctionMapping(newColumnName, sourceColumnName, function);
					}
					if (key.startsWith("node.")) {
						createColumn(newColumnName, sourceColumnName, function, columnType,
								nodes);
						nodeFunctionMappings.addFunctionMapping(newColumnName, sourceColumnName, function,nodeType);
					}
				}

				if(!functionNames.contains(function)){
					log.log(LogService.LOG_WARNING, "Unrecognized function: "+ function + ".\nContinuing with " +
					"extraction, but ignoring this specific analysis.");
				}
				if(!columnNames.contains(sourceColumnName)){
					log.log(LogService.LOG_WARNING, "Unrecognized column: "+ sourceColumnName + ".\nContinuing with " +
					"extraction, but ignoring this specific analysis.");
				}
				if(columnNames.contains(newColumnName)){
					log.log(LogService.LOG_WARNING, "The column: "+ newColumnName + " already exists." +
							"\nContinuing with " +
					"extraction, but ignoring this specific analysis.");
				}
			}

		}
	}
	
	private static void createColumn(String newColumnName, String calculateColumnName, String function, Class columnType, Schema newSchema) {	
		Class finalType = null;
		finalType = AssembleAggregateFunctions.defaultAssembly().getAggregateFunction(function, columnType).getType();
		newSchema.addColumn(newColumnName, finalType);
	}
	
	
}
