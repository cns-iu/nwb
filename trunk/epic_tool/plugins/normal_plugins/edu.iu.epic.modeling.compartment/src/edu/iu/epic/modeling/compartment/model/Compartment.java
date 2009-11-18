package edu.iu.epic.modeling.compartment.model;

import java.awt.Point;
import java.awt.geom.Point2D;

import edu.iu.epic.modeling.compartment.model.exception.CompartmentExistsException;
import edu.iu.epic.modeling.compartment.model.exception.InvalidCompartmentNameException;


public class Compartment {
	public static final Point2D.Double DEFAULT_POSITION = new Point.Double(0, 0);
	
	private String name;
	private Point2D position;
	private Model model;

	protected Compartment(Model model, String name) {
		this.name = name;
		this.model = model;
		
		this.position = DEFAULT_POSITION;
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
	
	public Point2D getPosition() {
		return position;
	}
	public void setPosition(Point2D position) {
		this.position = position;
	}
}
