package edu.iu.epic.modeling.compartment.model;

public class SpontaneousTransition implements Transition {
	public static final String SYNTAX = "->";

	private Compartment from;
	private Compartment to;
	private String rate;
	private boolean isSecondary;

	protected SpontaneousTransition(Compartment from, Compartment to, String parameter,
			boolean isSecondary) {
		this.from = from;
		this.to = to;
		this.rate = parameter;
		this.isSecondary = isSecondary;
	}

	@Override
	public String toString() {
		String s = from + " " + SYNTAX + " " + to + " " + rate;
		if (isSecondary) {
			s += " " + "secondary";
		}

		return s;
	}

	public boolean involves(Compartment compartment) {
		return from == compartment || to == compartment;
	}

	public String getRate() {
		return rate;
	}
}
