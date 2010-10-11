package edu.iu.epic.modeling.compartment.model;

import java.awt.geom.Point2D;

import edu.iu.epic.modeling.compartment.model.exception.CompartmentExistsException;
import edu.iu.epic.modeling.compartment.model.exception.InvalidCompartmentNameException;


public class Compartment {
	public static final Point2D DEFAULT_POSITION = new Point2D.Double(0, 0);

	private Model model;
	private String name;
	private boolean isSecondary;
	
	private Point2D position = DEFAULT_POSITION;
	
	protected Compartment(Model model, String name) {
		this(model, name, false);
	}
	
	protected Compartment(Model model, String name, boolean isSecondary) {
		this.model = model;
		this.name = name;		
		this.isSecondary = isSecondary;
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
	
	public boolean isSecondary() {
		return isSecondary;
	}
	public void setSecondary(boolean isSecondary) {
		this.isSecondary = isSecondary;
	}
	
	public Point2D getPosition() {
		return position;
	}
	public void setPosition(Point2D position) {
		this.position = position;
	}
}
