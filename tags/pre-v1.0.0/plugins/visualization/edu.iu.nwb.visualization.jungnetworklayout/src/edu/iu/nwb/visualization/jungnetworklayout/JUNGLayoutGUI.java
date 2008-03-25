package edu.iu.nwb.visualization.jungnetworklayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.VertexShapeFunction;
import edu.uci.ics.jung.graph.decorators.VertexStringer;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.LayoutScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;

public class JUNGLayoutGUI extends JFrame {
	private final class RightMouseScalingGraphMousePlugin extends AbstractGraphMousePlugin implements MouseMotionListener, MouseListener {
		private ScalingControl scaler;
		private float in;
		private float out;
		private Point2D pressedLocation = null;

		private RightMouseScalingGraphMousePlugin(ScalingControl scaler, int modifiers) {
			this(scaler, modifiers, 1.03f, 1/1.03f);
		}

		private RightMouseScalingGraphMousePlugin(ScalingControl scaler, int modifiers, float in, float out) {
			super(modifiers);
			this.scaler = scaler;
			this.in = in;
			this.out = out;
		}
		
		public RightMouseScalingGraphMousePlugin(CrossoverScalingControl scaler) {
			this(scaler, 0);
		}

		public boolean checkModifiers(MouseEvent e) {
			return e.getModifiers() == this.modifiers || (e.getModifiers() & this.modifiers) != 0;
		}

		public void mouseDragged(MouseEvent e) {
			
			boolean accepted = pressedLocation != null;
			if(accepted) {
				
				VisualizationViewer vv = (VisualizationViewer) e.getSource();
				Point2D movedLocation = e.getPoint();
				float dx = (float) (movedLocation.getX() - pressedLocation.getX()) / vv.getWidth();
				float dy = (float) (movedLocation.getY() - pressedLocation.getY()) / vv.getHeight();
				float amount = (dx + dy) / 2;
				System.out.println(amount);
				
				
				if(amount > 0) {
					scaler.scale(vv, in, pressedLocation);
				} else if(amount < 0) {
					scaler.scale(vv, out, pressedLocation);
				}
				e.consume();
				vv.repaint();
				this.pressedLocation = movedLocation;
			}

		}

		public void mouseMoved(MouseEvent e) {}

		public void mouseClicked(MouseEvent e) {}

		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON3) {
				this.pressedLocation = e.getPoint();
			}
			
		}

		public void mouseReleased(MouseEvent e) {
			this.pressedLocation = null;
			
		}
	}

	private static final int ADDITIONAL_SIZE = 100;

	private static final long serialVersionUID = -1066069022881159726L;

	private Layout layout ;

	public JUNGLayoutGUI(String title, Layout layout) {
		super(title);
		this.layout = layout ;
		initialize();
	}

	private PluggableRenderer pr;

	private VisualizationViewer vv;

	public void initialize() {
		pr = new PluggableRenderer();
		pr.setVertexShapeFunction(new VertexShapeFunction() {
			public Shape getShape(Vertex arg0) {
				//changed from new Ellipse2D.Float(0, 0, 5, 5);
				//not sure what's goin on with jung, but by doing this
				//it put the center of the circle at the origin so 
				//when lines are drawn they draw to the center of the circle
				return new Ellipse2D.Float(-5, -5, 10, 10);
			}
		});
		pr.setVertexLabelCentering(false);
		pr.setVertexStringer(new VertexStringer() {
			public String getLabel(ArchetypeVertex v) {

				Object label = v.getUserDatum("label");

				if (label != null) {
					label = label.toString();
				}

				return (String)label;
			}});

		vv = new VisualizationViewer(layout, pr);
		vv.setBackground(Color.WHITE);


		this.getContentPane().add(vv);
		this.pack();
		Dimension size = vv.getGraphLayout().getCurrentSize();
		this.setSize(new Dimension(size.width + ADDITIONAL_SIZE, size.height + ADDITIONAL_SIZE));
		DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
		graphMouse.add(new RightMouseScalingGraphMousePlugin(new CrossoverScalingControl()));
		vv.setGraphMouse(graphMouse);
		/* this.vv.removeMouseListener(this.vv.getMouseListeners()[0]);
        this.vv.removeMouseMotionListener(this.vv.getMouseMotionListeners()[0]);
        ZoomPanMouseBehaviour zpmb = new ZoomPanMouseBehaviour() ;
        zpmb.setListeningSource(this.vv) ; */
	}
}

class ZoomPanMouseBehaviour {

	private VisualizationViewer vv;

	public ZoomPanMouseBehaviour() {
		// do nothing ;
	}
	public void setListeningSource(VisualizationViewer vv) {
		this.vv = vv;
		configure();
		enableListeners() ;
	}
	private boolean enable = false ;
	public void disableListeners() {
		enable = false ;
	}
	public void enableListeners() {
		enable = true ;
	}
	public void destroyListeners() {
		this.vv.removeMouseListener(vvMouseAdapter) ;
		this.vv.removeMouseMotionListener(vvMouseMotionAdapter) ;
	}
	private MouseAdapter vvMouseAdapter ;
	private MouseMotionAdapter vvMouseMotionAdapter ;
	private void configure() {
		vvMouseAdapter = new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				mousePress(event);
			}

			public void mouseReleased(MouseEvent event) {
				mouseRelease(event);
			}
		} ;
		vvMouseMotionAdapter = new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent event) {
				if (SwingUtilities.isLeftMouseButton(event))
					leftMouseButtonDragged(event);
				else if (SwingUtilities.isRightMouseButton(event))
					rightMouseButtonDragged(event);
			}
		} ;
		this.vv.addMouseListener(vvMouseAdapter);
		this.vv.addMouseMotionListener(vvMouseMotionAdapter);
	}

	protected void mouseRelease(MouseEvent event) {
		ok = true;
	}

	protected void mousePress(MouseEvent event) {
		if (!ok)
			return;
		this.mouseX = event.getX();
		this.mouseY = event.getY();
		ok = false;
	}

	private boolean ok = true;

	private int mouseX, mouseY;

	protected void leftMouseButtonDragged(MouseEvent event) {
		if (!enable) return ;
		double dx = (double) (event.getX() - this.mouseX) / this.vv.getWidth();
		double dy = (double) (event.getY() - this.mouseY) / this.vv.getHeight();
		double ox = -dx / this.vv.getScaleX();
		double oy = -dy / this.vv.getScaleY();
		this.vv.setOffset(ox, oy);
		this.vv.repaint();
	}

	protected void rightMouseButtonDragged(MouseEvent event) {
		if (!enable) return ;
		double dx = (double) (event.getX() - this.mouseX) / this.vv.getWidth();
		double dy = (double) (event.getY() - this.mouseY) / this.vv.getHeight();
		double avgd = (dx + dy) / 2;
		this.vv
		.setScale(this.vv.getScaleX() + avgd, this.vv.getScaleY()
				+ avgd);
		this.vv.repaint();
	}
}