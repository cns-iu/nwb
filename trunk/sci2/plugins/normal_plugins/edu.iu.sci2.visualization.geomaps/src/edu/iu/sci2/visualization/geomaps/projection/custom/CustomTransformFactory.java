package edu.iu.sci2.visualization.geomaps.projection.custom;

import java.util.HashMap;
import java.util.Map;

import org.geotools.parameter.ParameterGroup;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.MathTransform;

import edu.iu.sci2.visualization.geomaps.utility.Constants;

public class CustomTransformFactory {
	public static final double DEFAULT_CENTRAL_MERIDIAN = 0.0;
	
	public static final Map<String, MathTransform> customTransforms =
		new HashMap<String, MathTransform>();

	static {
		// EckertIV default parameters
		ParameterValueGroup parameterValues =
			new ParameterGroup(EckertIV.Provider.PARAMETERS);
		parameterValues.parameter("semi_major").setValue(6378137);
		parameterValues.parameter("semi_minor").setValue(6378137);
		parameterValues.parameter("central_meridian").setValue(DEFAULT_CENTRAL_MERIDIAN);
		parameterValues.parameter("scale_factor").setValue(1.0);
		parameterValues.parameter("false_easting").setValue(0.0);
		parameterValues.parameter("false_northing").setValue(0.0);
		
		MathTransform transform = 
			(new EckertIV.Provider()).createMathTransform(parameterValues);
		registerAs(Constants.ECKERT_IV_DISPLAY_NAME, transform);
	}
	
	static {
		// Winkel Tripel default parameters
		ParameterValueGroup parameterValues =
			new ParameterGroup(WinkelTripel.Provider.PARAMETERS);
		parameterValues.parameter("semi_major").setValue(6378137);
		parameterValues.parameter("semi_minor").setValue(6378137);
		parameterValues.parameter("central_meridian").setValue(DEFAULT_CENTRAL_MERIDIAN);
		parameterValues.parameter("scale_factor").setValue(1.0);
		parameterValues.parameter("false_easting").setValue(0.0);
		parameterValues.parameter("false_northing").setValue(0.0);
		
		MathTransform transform = 
			(new WinkelTripel.Provider()).createMathTransform(parameterValues);
		registerAs(Constants.WINKEL_TRIPEL_DISPLAY_NAME, transform);
	}
	
	private static void registerAs(String projectionName, MathTransform transform) {
		customTransforms.put(projectionName, transform);
	}
	
	public static MathTransform getTransform(String projectionName)
			throws TransformNotFoundException {
		if (customTransforms.containsKey(projectionName)) {
			MathTransform transform = customTransforms.get(projectionName);
			return transform;
		} else {
			throw new TransformNotFoundException(projectionName);
		}
	}
	
	
	public static class TransformNotFoundException extends Exception {
		private static final long serialVersionUID = 1L;

		public TransformNotFoundException(String projectionName) {
			super("Couldn't find the default transform for projection named "
					+ projectionName);
		}	
	}
}
