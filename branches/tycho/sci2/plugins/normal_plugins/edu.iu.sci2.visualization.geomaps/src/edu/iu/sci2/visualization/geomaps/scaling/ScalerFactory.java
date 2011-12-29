package edu.iu.sci2.visualization.geomaps.scaling;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

public class ScalerFactory {
	private enum ScalerType { LINEAR_SCALER, LOG_SCALER }
	public static final Map<String, ScalerType> SCALER_TYPES;
	static {
		Map<String, ScalerType> t = new HashMap<String, ScalerType>();
		t.put("Linear", ScalerType.LINEAR_SCALER);
		t.put("Logarithmic", ScalerType.LOG_SCALER);
		SCALER_TYPES = Collections.unmodifiableMap(t);
	}
	
	public static Scaler createScaler(String scaling)
			throws AlgorithmExecutionException {
		if (SCALER_TYPES.containsKey(scaling)) {
			return createScaler(SCALER_TYPES.get(scaling));
		}
		else {
			throw new AlgorithmExecutionException(
					"Unrecognized scaling \""  + scaling + "\"");
		}
	}
	
	public static Scaler createScaler(ScalerType scalerType)
			throws AlgorithmExecutionException {
		switch (scalerType) {
			case LINEAR_SCALER:
				return new LinearScaler();
			case LOG_SCALER:
				return new LogScaler();
		}
		
		throw new AlgorithmExecutionException(
				"Unrecognized scaler type \"" + scalerType + "\".");
	}
}
