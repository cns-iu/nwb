package edu.iu.nwb.analysis.extractnetfromtable.components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

import org.osgi.service.log.LogService;

import prefuse.data.Schema;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AggregateFunctionNames;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AssembleAggregateFunctions;

public class AggregateFunctionMappings {
	private final HashMap metaColumnNameToFunctionMap = new HashMap();
	private final HashMap functionColumnToOriginalColumnMap = new HashMap();
	private final HashMap functionColumnToAppliedNodeTypeMap = new HashMap();
	private final HashMap labelToFunctionMap = new HashMap();
	public static final int SOURCE_AND_TARGET = 0;
	public static final int SOURCE = 1;
	public static final int TARGET = 2;

	public void addFunctionMapping(String functionValueCol, String originalCol, String functionType) {
		this.metaColumnNameToFunctionMap.put(functionValueCol, functionType);
		this.functionColumnToOriginalColumnMap.put(functionValueCol, originalCol);
		this.functionColumnToAppliedNodeTypeMap.put(functionValueCol, new Integer(SOURCE_AND_TARGET));
	}

	public void addFunctionMapping(String functionValueCol, String originalCol,
			String functionType, int nodeType) {
		this.metaColumnNameToFunctionMap.put(functionValueCol, functionType);
		this.functionColumnToOriginalColumnMap.put(functionValueCol, originalCol);
		this.functionColumnToAppliedNodeTypeMap.put(functionValueCol, new Integer(nodeType));
	}

	public ValueAttributes addFunctionRow(Object id, ValueAttributes va) {
		this.labelToFunctionMap.put(id, va);
		
		return va;
	}

	public String getFunctionFromColumnName(String columnName) {
		return (String) this.metaColumnNameToFunctionMap.get(columnName);
	}

	public String getOriginalColumnFromFunctionColumn(String columnName) {
		return (String) this.functionColumnToOriginalColumnMap.get(columnName);
	}

	public ValueAttributes getFunctionRow(Object id) {
		return (ValueAttributes) this.labelToFunctionMap.get(id);
	}

	public int getAppliedNodeType(String columnName) {
		return ((Integer) this.functionColumnToAppliedNodeTypeMap.get(columnName)).intValue();
	}

	public static void parseProperties(Schema input, Schema nodes, Schema edges,
			Properties properties, AggregateFunctionMappings nodeFunctionMappings,
			AggregateFunctionMappings edgeFunctionMappings, LogService log) {

		if (properties != null) {
			HashSet functionNames = new HashSet(AssembleAggregateFunctions.defaultAssembly()
					.getFunctionNames());
			HashSet columnNames = new HashSet();

			for (int i = 0; i < input.getColumnCount(); i++) {
				columnNames.add(input.getColumnName(i));
			}

			for (final Iterator it = properties.keySet().iterator(); it.hasNext();) {
				final String key = (String) it.next();
				String[] functionDefinitionLHS = key.split("\\.");
				String[] functionDefinitionRHS = properties.getProperty(key).split("\\.");
				String applyToNodeType = null;
				int nodeType = -1;

				final String sourceColumnName = functionDefinitionRHS[0];
				final Class columnType = input.getColumnType(sourceColumnName);
				final String function = functionDefinitionRHS[functionDefinitionRHS.length - 1];
				if (functionDefinitionRHS.length == 3) {
					applyToNodeType = functionDefinitionRHS[1];
				}

				if (applyToNodeType == null) {
					nodeType = AggregateFunctionMappings.SOURCE_AND_TARGET;
				} else {
					if ("source".equalsIgnoreCase(applyToNodeType)) {
						nodeType = AggregateFunctionMappings.SOURCE;
					} else if ("target".equalsIgnoreCase(applyToNodeType)) {
						nodeType = AggregateFunctionMappings.TARGET;
					} else {
						nodeType = AggregateFunctionMappings.SOURCE_AND_TARGET;
					}
				}

				String newColumnName = functionDefinitionLHS[functionDefinitionLHS.length - 1];

				if (functionNames.contains(function) && columnNames.contains(sourceColumnName)
						&& !columnNames.contains(newColumnName)) {
					if (key.startsWith("edge.")) {
						createColumn(newColumnName, function, columnType, edges);
						edgeFunctionMappings.addFunctionMapping(newColumnName, sourceColumnName,
								function);
					}
					if (key.startsWith("node.")) {
						createColumn(newColumnName, function, columnType, nodes);
						nodeFunctionMappings.addFunctionMapping(newColumnName, sourceColumnName,
								function, nodeType);
					}
				}

				if (!functionNames.contains(function)) {
					log.log(LogService.LOG_WARNING, "Unrecognized function: " + function
							+ ".\nContinuing with "
							+ "extraction, but ignoring this specific analysis.");
				}
				if (!columnNames.contains(sourceColumnName)) {
					log.log(LogService.LOG_WARNING, "Unrecognized column: " + sourceColumnName
							+ ".\nContinuing with "
							+ "extraction, but ignoring this specific analysis.");
				}
				if (columnNames.contains(newColumnName)) {
					log.log(LogService.LOG_WARNING, "The column: " + newColumnName
							+ " already exists." + "\nContinuing with "
							+ "extraction, but ignoring this specific analysis.");
				}
			}

		}
	}

	public static final String DEFAULT_WEIGHT_NAME = "weight";

	public static void addDefaultEdgeWeightColumn(Schema inputGraphNodeSchema,
			Schema outputGraphEdgeSchema, AggregateFunctionMappings edgeFunctionMappings,
			String sourceColumnName) {
		/*
		 * Prepare to create a edge weight column, where each edge's weight is
		 * the number of times that relationship between nodes has occurred in
		 * the original dataset (for instance, in co-authorship the edge weight
		 * would be how many times two authors have co-authored together).
		 */
		String newColumnName = DEFAULT_WEIGHT_NAME;
		String function = AggregateFunctionNames.COUNT;
		Class columnType = inputGraphNodeSchema.getColumnType(sourceColumnName);

		createColumn(newColumnName, function, columnType, outputGraphEdgeSchema);
		edgeFunctionMappings.addFunctionMapping(newColumnName, sourceColumnName, function);
	}

	private static void createColumn(String newColumnName, String function, Class columnType,
			Schema newSchema) {
		Class finalType = AssembleAggregateFunctions.defaultAssembly().getAggregateFunction(
				function, columnType).getType();
		newSchema.addColumn(newColumnName, finalType);
	}
}
