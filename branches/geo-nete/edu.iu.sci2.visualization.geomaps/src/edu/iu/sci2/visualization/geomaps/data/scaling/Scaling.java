package edu.iu.sci2.visualization.geomaps.data.scaling;


public enum Scaling {
	Linear {
		@Override
		public double scaleUnchecked(double value) {
			return value;
		}		

		@Override
		public Scaling inverse() {
			return Scaling.Linear;
		}		
	},
	Logarithmic {
		@Override
		public double scaleUnchecked(double value) {
			return Math.log10(value);
		}

		@Override
		public Scaling inverse() {
			return Scaling.Exponential;
		}
	},
	Exponential {
		@Override
		public double scaleUnchecked(double value) {
			return Math.pow(10.0, value);
		}

		@Override
		public Scaling inverse() {
			return Scaling.Logarithmic;
		}		
	};
	
	public abstract double scaleUnchecked(double value);
	public abstract Scaling inverse();
	
	
	public double scale(double value) throws ScalingException {
		double unchecked = scaleUnchecked(value);
		
		if (Double.isInfinite(unchecked) || Double.isNaN(unchecked)) {
			throw new ScalingException(
					String.format("Scaling \"%s\" cannot scale the value \"%s\".",
							this, value));
		}
		
		return unchecked;
	}
	
	public double invert(double value) throws ScalingException {
		return inverse().scale(value);
	}
	
	public boolean isScalable(double value) {
		try {
			scale(value);
			return true;
		} catch (ScalingException e) {
			return false;
		}
	}
}
