package edu.iu.epic.modeling.compartment.model;

public class RatioTransition implements Transition {
	public static final String SYNTAX = "->";

	private Compartment source;
	private Compartment target;
	private String ratio;
	private boolean isSecondary;

	protected RatioTransition(Compartment source, Compartment target, String ratio,
			boolean isSecondary) {
		this.source = source;
		this.target = target;
		this.ratio = ratio;
		this.isSecondary = isSecondary;
	}

	@Override
	public String toString() {
		String s = source + " " + SYNTAX + " " + target + " " + ratio;
		if (isSecondary) {
			s += " " + "secondary";
		}

		return s;
	}

	public boolean involves(Compartment compartment) {
		return source == compartment || target == compartment;
	}

	public String getRatio() {
		return ratio;
	}

	public boolean setRatio(String newRatio) {
		if (Model.isValidParameterExpression(newRatio)) {
			this.ratio = newRatio;
			return true;
		} else {
			return false;
		}
	}
}
