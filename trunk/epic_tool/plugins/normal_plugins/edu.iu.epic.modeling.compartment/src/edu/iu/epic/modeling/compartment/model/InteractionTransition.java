package edu.iu.epic.modeling.compartment.model;

public class InteractionTransition implements Transition {
	public static final String SYNTAX = "--";

	private Compartment from;
	private Compartment interactor;
	private Compartment to;
	private String rate;
	private boolean isSecondary;

	protected InteractionTransition(Compartment from, Compartment interactsWith,
			Compartment to, String rate, boolean isSecondary) {
		this.from = from;
		this.interactor = interactsWith;
		this.to = to;
		this.rate = rate;
		this.isSecondary = isSecondary;
	}

	@Override
	public String toString() {
		String s = "";

		if (to == null) {
			/* When there is no explicit target "to" compartment,
			 * one assumes that the interactor is also the target.
			 */
			s += from + " " + SYNTAX + " " + interactor + " " + rate;
		} else {
			s += from + " " + SYNTAX + " " + interactor + " = " + to + " " + rate;
		}

		if (isSecondary) {
			s += " " + "secondary";
		}

		return s;
	}

	public boolean involves(Compartment compartment) {
		return from == compartment || to == compartment || interactor == compartment;
	}

	public Compartment getFrom() {
		return from;
	}

	public Compartment getInteractor() {
		return interactor;
	}

	public Compartment getTo() {
		return to;
	}

	public String getRate() {
		return rate;
	}
}
