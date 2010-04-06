package edu.iu.scipolicy.visualization.horizontalbargraph;

public enum ScalingFunction {
	LINEAR {
		public double scale(double input) {
			return input;
		}

		public String getDisplayName() {
			return "Linear";
		}
	},
	LOGARITHMIC {
		public double scale(double input) {
			return Math.log(input + 1);
		}

		public String getDisplayName() {
			return "Logarithmic";
		}
	};

	public static final String LINEAR_SCALING_FUNCTION_NAME = "Linear Scaling";
	public static final String LOGARITHMIC_SCALING_FUNCTION_NAME = "Logarithmic Scaling";

	public abstract double scale(double input);
	public abstract String getDisplayName();
}