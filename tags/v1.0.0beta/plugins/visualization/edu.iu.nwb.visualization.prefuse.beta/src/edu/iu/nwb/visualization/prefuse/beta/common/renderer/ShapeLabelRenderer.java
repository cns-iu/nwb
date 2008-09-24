package edu.iu.nwb.visualization.prefuse.beta.common.renderer;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import prefuse.render.LabelRenderer;
import prefuse.render.Renderer;
import prefuse.render.ShapeRenderer;
import prefuse.visual.VisualItem;
import edu.iu.nwb.visualization.prefuse.beta.common.Constants;

public class ShapeLabelRenderer implements Renderer {
		
		private ShapeRenderer shapeRenderer = new ShapeRenderer();
		private LabelRenderer labelRenderer;
		private Map firstRun = new HashMap();
		
		public ShapeLabelRenderer() {
			this(Constants.label);
		}
		
		public ShapeLabelRenderer(String labelField) {
			labelRenderer = new LabelRenderer(labelField);
			labelRenderer.setHorizontalAlignment(prefuse.Constants.RIGHT);
			labelRenderer.setHorizontalPadding(shapeRenderer.getBaseSize());
			labelRenderer.setRenderType(LabelRenderer.RENDER_TYPE_NONE);
		}

		public boolean locatePoint(Point2D point, VisualItem item) {
			// TODO Auto-generated method stub
			return shapeRenderer.locatePoint(point, item) || labelRenderer.locatePoint(point, item);
		}

		public void render(Graphics2D graphics, VisualItem item) {
			//hack to make labels remain constant size despite item size changing
			boolean scaled = false;
			double scale = item.getVisualization().getDisplay(0).getScale();
			Font currentFont = item.getFont();
			String row = "" + item.getRow();
			if(!firstRun.containsKey(row)) {
				//scale font down to maintain consistent size
				item.setFont(currentFont.deriveFont((float) (currentFont.getSize() * 1.11/item.getSize())));
				firstRun.put(row, new Boolean(false));
			} else if(scale > .8) {
				scaled = true;
				
				item.setFont(currentFont.deriveFont((float) (currentFont.getSize() * 1/Math.pow(scale, .6))));
			}
			
			
			
			shapeRenderer.render(graphics, item);
			labelRenderer.render(graphics, item);
			
			if(scaled) {
				item.setFont(currentFont);
			}
			
			
		}

		public void setBounds(VisualItem item) {
			Shape shapeShape = shapeRenderer.getShape(item);
			Shape labelShape = labelRenderer.getShape(item);
			
			double x, y, h, w, lw, lw2;
			BasicStroke stroke = item.getStroke();
			
			Rectangle shapeBounds = shapeShape.getBounds();
			Rectangle labelBounds = labelShape.getBounds();
			x = Math.min(shapeBounds.x, labelBounds.x);
			y = Math.min(shapeBounds.y, labelBounds.y);
			//set an incorrect width so that the edge lines hit the center of the shape rather than somewhere in the label
			//this trick depends on the shape being roughly vertically centered on the left or right side of the rendered graphics
			w = (Math.max(shapeBounds.x + shapeBounds.width, labelBounds.x + labelBounds.width) - x - shapeBounds.width / 2) * 2;
			h = Math.max(shapeBounds.y + shapeBounds.height, labelBounds.y + labelBounds.height) - y;
			
			if ( stroke != null && (lw=stroke.getLineWidth()) > 1 ) {
				lw2 = lw/2.0;
				x -= lw2; y -= lw2; w += lw; h += lw;
			}
			
			item.setBounds(x, y, w, h);
		}
		
	}