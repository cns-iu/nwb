package edu.iu.nwb.analysis.extractnetfromtable.aggregate;

import java.util.HashMap;
import java.util.Set;

public class AssembleAggregateFunctions {
	private HashMap<AggregateFunctionName, AggregateFunctionFactory> nameToFunctionFactory;

	public AssembleAggregateFunctions() {
		nameToFunctionFactory = new HashMap<AggregateFunctionName, AggregateFunctionFactory>();
	}

	public static AssembleAggregateFunctions defaultAssembly() {
		AssembleAggregateFunctions aaf = new AssembleAggregateFunctions();

		aaf.putAggregateFunctionFactory(AggregateFunctionName.ARITHMETICMEAN,
				new ArithmeticMeanFunctionFactory());
		aaf.putAggregateFunctionFactory(AggregateFunctionName.COUNT,
				new CountFunctionFactory());
		aaf.putAggregateFunctionFactory(AggregateFunctionName.GEOMETRICMEAN,
				new GeometricMeanFunctionFactory());
		aaf.putAggregateFunctionFactory(AggregateFunctionName.MAX,
				new MaxFunctionFactory());
		aaf.putAggregateFunctionFactory(AggregateFunctionName.MIN,
				new MinFunctionFactory());
		aaf.putAggregateFunctionFactory(AggregateFunctionName.SUM,
				new SumFunctionFactory());
		aaf.putAggregateFunctionFactory(AggregateFunctionName.MODE,
				new ModeFunctionFactory());

		return aaf;
	}

	public AbstractAggregateFunction getAggregateFunction(AggregateFunctionName function,
			Class type) {
		AbstractAggregateFunction af = null;

		try {
			af = nameToFunctionFactory.get(function)
					.getFunction(type);
		} catch (NullPointerException npe) {
			af = null;
		}

		return af;
	}

	public AggregateFunctionFactory putAggregateFunctionFactory(
			AggregateFunctionName name,
			AggregateFunctionFactory aggregationFunctionFactory) {
		if (this.nameToFunctionFactory.get(name) != null) {
			// redefine the function but
			// let the user know that they are
			// redefining a FunctionFactory,
			// throw FunctionFactoryDefinitionException?
			return null;
		}
		this.nameToFunctionFactory.put(name, aggregationFunctionFactory);
		return aggregationFunctionFactory;

	}

	public AggregateFunctionFactory removeAggregateFunctionFactory(
			String functionName) {
		return this.nameToFunctionFactory.get(functionName);
	}

	public Set<AggregateFunctionName> getFunctionNames() {
		return this.nameToFunctionFactory.keySet();
	}

}
