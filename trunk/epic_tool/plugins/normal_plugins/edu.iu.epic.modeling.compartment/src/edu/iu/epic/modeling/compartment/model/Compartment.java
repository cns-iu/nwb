package edu.iu.epic.modeling.compartment.model;

import java.awt.geom.Point2D;

import org.cishell.utilities.OrderingUtilities;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

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
	
	
	public static class CompartmentNameOrdering {
		public static final ImmutableList<String> PREFERRED_ORDER_OF_NAME_FIRST_CHARACTERS =
			ImmutableList.of("S", "E", "L", "I", "R");
		
		public static final Ordering<String> BY_FIRST_CHARACTER_OF_NAME =
			OrderingUtilities.explicitWithUnknownsLast(PREFERRED_ORDER_OF_NAME_FIRST_CHARACTERS);
		
		/**
		 * Ties on the first character resolved by comparing full names.
		 * Need this for consistency with equals().
		 */
		public static final Ordering<String> BY_NAME =
			BY_FIRST_CHARACTER_OF_NAME
				.onResultOf(new Function<String, String>() {
					public String apply(String compartmentName) {
						return compartmentName.substring(0, 1).toUpperCase();
					}
				})
				.compound(Ordering.natural());
		
		public static final Ordering<Compartment> BY_COMPARTMENT =
			BY_NAME.onResultOf(new Function<Compartment, String>() {
				public String apply(Compartment compartment) {
					return compartment.getName();
				}
			});
	}
}
