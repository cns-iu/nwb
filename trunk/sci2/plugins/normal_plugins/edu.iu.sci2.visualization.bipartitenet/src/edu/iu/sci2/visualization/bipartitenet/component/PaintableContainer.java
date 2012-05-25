package edu.iu.sci2.visualization.bipartitenet.component;

import java.awt.Graphics2D;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * An implementation of {@link Paintable} that simply keeps track of other Paintables, and
 * calls their {@link Paintable#paint(Graphics2D)} methods, in order.
 *
 */
public class PaintableContainer implements Paintable {
	private List<Paintable> children = Lists.newArrayList();
	
	/**
	 * Add the given Paintable to the list of things to draw, at the end.  The new Paintable
	 * will be drawn <b>after</b> all the other Paintables that have been added so far.
	 */
	public void add(Paintable child) {
		children.add(child);
	}
	
	/**
	 * Add the given Paintable to the list of things to draw, at the start.  The new Paintable
	 * will be drawn <b>before</b> all the other Paintables that have been added so far.
	 */
	public void insert(Paintable child) {
		children.add(0, child);
	}
	
	/**
	 * Remove (one copy of) the given Paintable from the list of things to draw.
	 * @return true if the Paintable was removed, false if it could not be found.
	 */
	public boolean remove(Paintable child) {
		return children.remove(child);
	}

	@Override
	public void paint(Graphics2D g) {
		for (Paintable p : children) {
			Graphics2D newGraphics = (Graphics2D) g.create();
			p.paint(newGraphics);
			newGraphics.dispose();
		}
	}
	
	
}
