package edu.iu.sci2.visualization.bipartitenet.component;

import java.awt.Graphics2D;
import java.util.List;

import com.google.common.collect.Lists;

public class PaintableContainer implements Paintable {
	private List<Paintable> children = Lists.newArrayList();
	
	public void add(Paintable child) {
		children.add(child);
	}
	
	public boolean remove(Paintable child) {
		return children.remove(child);
	}

	@Override
	public void paint(Graphics2D g) {
		for (Paintable p : children) {
			p.paint((Graphics2D) g.create());
		}
	}
	
	
}
