package edu.iu.epic.modeling.compartment.model;

public class InfectionTransition implements Transition {
	public static final String SYNTAX = "--";

	private Compartment source;
	private Compartment infector;
	private Compartment target;
	private String ratio;
	private boolean isSecondary;

	protected InfectionTransition(Compartment source, Compartment infector,
			Compartment target, String ratio, boolean isSecondary) {
		this.source = source;
		this.infector = infector;
		this.target = target;
		this.ratio = ratio;
		this.isSecondary = isSecondary;
	}

	@Override
	public String toString() {
		String s = "";

		if (target == null) {
			/* When there is no explicit target "target" compartment,
			 * one assumes that the infector is also the target.
			 */
			s += source + " " + SYNTAX + " " + infector + " " + ratio;
		} else {
			s += source + " " + SYNTAX + " " + infector + " = " + target + " " + ratio;
		}

		if (isSecondary) {
			s += " " + "secondary";
		}

		return s;
	}

	public boolean involves(Compartment compartment) {
		return source == compartment || target == compartment || infector == compartment;
	}

	public Compartment getSource() {
		return source;
	}

	public Compartment getInfector() {
		return infector;
	}

	public Compartment getTarget() {
		return target;
	}

	public String getRatio() {
		return ratio;
	}
}
