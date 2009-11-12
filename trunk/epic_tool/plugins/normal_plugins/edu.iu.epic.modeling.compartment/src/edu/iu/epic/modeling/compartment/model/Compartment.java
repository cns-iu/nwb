package edu.iu.epic.modeling.compartment.model;

import edu.iu.epic.modeling.compartment.model.exception.CompartmentExistsException;
import edu.iu.epic.modeling.compartment.model.exception.InvalidCompartmentNameException;


public class Compartment {
	public static enum Type {
		SUSCEPTIBLE, INFECTED, LATENT, RECOVERED;

		@Override
		public String toString() {
			return name().toLowerCase();
		}
	}

	private String name;
	private Type type;
	private Model model;

	protected Compartment(Model model, String name, Type type) {
		this.name = name;
		this.type = type;
		this.model = model;
	}

	@Override
	public String toString() {
		return name;
	}

	public synchronized void setName(String name)
			throws CompartmentExistsException, InvalidCompartmentNameException {
		this.model.renameCompartment(this, name);
		/* This line must be second!
		 * Only rename if the rename above succeeds (and that one is synchronized).
		 */
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}
}
