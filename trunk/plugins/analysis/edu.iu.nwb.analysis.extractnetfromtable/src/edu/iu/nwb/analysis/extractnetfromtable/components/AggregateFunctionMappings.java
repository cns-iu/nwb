package edu.iu.nwb.analysis.extractnetfromtable.components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.osgi.service.log.LogService;

import prefuse.data.Schema;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AbstractAggregateFunction;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AggregateFunctionName;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AssembleAggregateFunctions;

public class AggregateFunctionMappings {
	private final HashMap<String, AggregateFunctionName> metaColumnNameToFunctionMap = new HashMap<String, AggregateFunctionName>();
	private final HashMap<String, String> functionColumnToOriginalColumnMap = new HashMap<String, String>();
	private final HashMap<String, Integer> functionColumnToAppliedNodeTypeMap = new HashMap<String, Integer>();
	private final HashMap<Object, ValueAttributes> labelToFunctionMap = new HashMap<Object, ValueAttributes>();
	public static final int SOURCE_AND_TARGET = 0;
	public static final int SOURCE = 1;
	public static final int TARGET = 2;

	public void addFunctionMapping(String functionValueCol, String originalCol,
			AggregateFunctionName function) {
		this.metaColumnNameToFunctionMap.put(functionValueCol, function);
		this.functionColumnToOriginalColumnMap.put(functionValueCol,
				originalCol);
		this.functionColumnToAppliedNodeTypeMap.put(functionValueCol,
				new Integer(SOURCE_AND_TARGET));
	}

	public void addFunctionMapping(String functionValueCol, String originalCol,
			AggregateFunctionName functionType, int nodeType) {
		this.metaColumnNameToFunctionMap.put(functionValueCol, functionType);
		this.functionColumnToOriginalColumnMap.put(functionValueCol,
				originalCol);
		this.functionColumnToAppliedNodeTypeMap.put(functionValueCol,
				new Integer(nodeType));
	}

	public ValueAttributes addFunctionRow(Object id, ValueAttributes va) {
		this.labelToFunctionMap.put(id, va);

		return va;
	}

	public AggregateFunctionName getFunctionFromColumnName(String columnName) {
		return this.metaColumnNameToFunctionMap.get(columnName);
	}

	public String getOriginalColumnFromFunctionColumn(String columnName) {
		return this.functionColumnToOriginalColumnMap.get(columnName);
	}

	public ValueAttributes getFunctionRow(Object id) {
		return this.labelToFunctionMap.get(id);
	}

	public int getAppliedNodeType(String columnName) {
		return this.functionColumnToAppliedNodeTypeMap.get(columnName);
	}

	/**
	 * @throws CompatibleAggregationNotFoundException
	 *             if no appropriate {@link AbstractAggregateFunction} could be
	 *             found for the column type.
	 */
	public static void parseProperties(Schema input, Schema nodes,
			Schema edges, Properties properties,
			AggregateFunctionMappings nodeFunctionMappings,
			AggregateFunctionMappings edgeFunctionMappings, LogService log)
			throws CompatibleAggregationNotFoundException {

		if (properties != null) {
			HashSet<AggregateFunctionName> functionNames = new HashSet<AggregateFunctionName>(
					AssembleAggregateFunctions.defaultAssembly()
							.getFunctionNames());
			HashSet<String> columnNames = new HashSet<String>();

			for (int i = 0; i < input.getColumnCount(); i++) {
				columnNames.add(input.getColumnName(i));
			}

			for (final Iterator it = properties.keySet().iterator(); it
					.hasNext();) {
				final String key = (String) it.next();
				String[] functionDefinitionLHS = key.split("\\.");
				String[] functionDefinitionRHS = properties.getProperty(key)
						.split("\\.");
				String applyToNodeType = null;
				int nodeType = -1;

				final String sourceColumnName = functionDefinitionRHS[0];
				final Class columnType = input
						.getColumnType(sourceColumnName);
				final AggregateFunctionName function = AggregateFunctionName
						.fromString(functionDefinitionRHS[functionDefinitionRHS.length - 1]);
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

				if (functionNames.contains(function)
						&& columnNames.contains(sourceColumnName)
						&& !columnNames.contains(newColumnName)) {
					if (key.startsWith("edge.")) {
						if (!createColumn(newColumnName, function, columnType,
								edges)) {
							throw new CompatibleAggregationNotFoundException(
									String.format(
											"Trying to make column '%s', could not find an aggregation function %s that applies to column type %s",
											newColumnName, function,
											columnType.getName()));
						}
						edgeFunctionMappings.addFunctionMapping(newColumnName,
								sourceColumnName, function);
					}
					if (key.startsWith("node.")) {
						if (!createColumn(newColumnName, function, columnType,
								nodes)) {
							throw new CompatibleAggregationNotFoundException(
									String.format(
											"Trying to make column '%s', could not find an aggregation function %s that applies to column type %s",
											newColumnName, function,
											columnType.getName()));
						}
						nodeFunctionMappings.addFunctionMapping(newColumnName,
								sourceColumnName, function, nodeType);
					}
				}

				if (!functionNames.contains(function)) {
					log.log(LogService.LOG_WARNING,
							"Unrecognized function: "
									+ function
									+ ".\nContinuing with "
									+ "extraction, but ignoring this specific analysis.");
				}
				if (!columnNames.contains(sourceColumnName)) {
					log.log(LogService.LOG_WARNING,
							"Unrecognized column: "
									+ sourceColumnName
									+ ".\nContinuing with "
									+ "extraction, but ignoring this specific analysis.");
				}
				if (columnNames.contains(newColumnName)) {
					log.log(LogService.LOG_WARNING,
							"The column: "
									+ newColumnName
									+ " already exists."
									+ "\nContinuing with "
									+ "extraction, but ignoring this specific analysis.");
				}
			}

		}
	}

	public static final String DEFAULT_WEIGHT_NAME = "weight";

	/**
	 * @throws CompatibleAggregationNotFoundException
	 *             if no appropriate {@link AbstractAggregateFunction} could be
	 *             found for the column type.
	 * @throws IllegalArgumentException
	 *             if the column for weight could not be created
	 */
	public static void addDefaultEdgeWeightColumn(Schema inputGraphNodeSchema,
			Schema outputGraphEdgeSchema,
			AggregateFunctionMappings edgeFunctionMappings,
			String sourceColumnName) throws CompatibleAggregationNotFoundException {
		/*
		 * Prepare to create a edge weight column, where each edge's weight is
		 * the number of times that relationship between nodes has occurred in
		 * the original dataset (for instance, in co-authorship the edge weight
		 * would be how many times two authors have co-authored together).
		 */
		String newColumnName = DEFAULT_WEIGHT_NAME;
		AggregateFunctionName function = AggregateFunctionName.COUNT;
		Class columnType = inputGraphNodeSchema
				.getColumnType(sourceColumnName);

		if (!createColumn(newColumnName, function, columnType, outputGraphEdgeSchema)) {
			throw new CompatibleAggregationNotFoundException(
					String.format(
							"Trying to make column '%s', could not find an aggregation function %s that applies to column type %s",
							newColumnName, function, columnType.getName()));
		}
		edgeFunctionMappings.addFunctionMapping(newColumnName,
				sourceColumnName, function);
	}

	/**
	 * Returns true if the column could be created; false if it was not able to
	 * find an appropriate aggregation function (e.g. if you try to sum a string
	 * column).
	 * 
	 * @param newColumnName
	 * @param function
	 * @param columnType
	 * @param newSchema
	 * @return <code>false</code> If no aggregation function could be found for
	 *         the <code>columnType</code>.
	 * @throws IllegalArgumentException
	 *             if either name or type are null or the name already exists in
	 *             this schema.
	 */
	private static boolean createColumn(String newColumnName,
			AggregateFunctionName function, Class columnType,
			Schema newSchema) {
		AbstractAggregateFunction aggFunc = AssembleAggregateFunctions
				.defaultAssembly().getAggregateFunction(function, columnType);
		if (aggFunc == null) {
			return false;
		}
		Class finalType = aggFunc.getType();
		newSchema.addColumn(newColumnName, finalType);
		return true;
	}
	
	/**
	 * This exception represents the case where no compatible
	 * {@link AbstractAggregateFunction} could be found for the type, (e.g. if
	 * you try to sum a string column).
	 */
	 public static class CompatibleAggregationNotFoundException extends Exception {
		private static final long serialVersionUID = 7588826870075190310L;

		/**
		  * @see Exception#Exception()
		  */
		 public CompatibleAggregationNotFoundException() {
			 super();
		 }
		 
		 /**
		  * @see Exception#Exception(String) 
		  */
		 public CompatibleAggregationNotFoundException(String message) {
			 super(message);			
		 }
		 
		 /**
		  * @see Exception#Exception(Throwable)
		  */
		 public CompatibleAggregationNotFoundException(Throwable cause) {
			 super(cause);			
		 }
		 
		 /**
		  * @see Exception#Exception(String, Throwable)
		  */
		 public CompatibleAggregationNotFoundException(String message, Throwable cause) {
			 super(message, cause);
		 }
	 }
}
