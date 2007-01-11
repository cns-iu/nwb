package edu.iu.nwb.visualization.prefuse.beta.fruchtermanreingold;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Date;
import java.util.Dictionary;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.iu.nwb.visualization.prefuse.beta.common.Constants;
import edu.iu.nwb.visualization.prefuse.beta.common.Indirection;
import edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaVisualization;
import edu.iu.nwb.visualization.prefuse.beta.common.control.SmartMarginZoomToFitControl;
import edu.iu.nwb.visualization.prefuse.beta.common.expression.SmartValueExpression;
import edu.iu.nwb.visualization.prefuse.beta.common.expression.ToDoubleExpression;
import edu.iu.nwb.visualization.prefuse.beta.common.renderer.ShapeLabelRenderer;
import edu.iu.nwb.visualization.prefuse.beta.radialgraph.RadialGraphVisualization;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.assignment.DataShapeAction;
import prefuse.action.assignment.DataSizeAction;
import prefuse.action.assignment.FontAction;
import prefuse.action.assignment.ShapeAction;
import prefuse.action.assignment.SizeAction;
import prefuse.action.filter.GraphDistanceFilter;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.action.layout.graph.FruchtermanReingoldLayout;
import prefuse.activity.Activity;
import prefuse.controls.Control;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.expression.ColumnExpression;
import prefuse.data.expression.Expression;
import prefuse.data.io.GraphMLReader;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.render.Renderer;
import prefuse.render.ShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.GraphLib;
import prefuse.util.GraphicsLib;
import prefuse.util.display.DisplayLib;
import prefuse.util.display.ItemBoundsListener;
import prefuse.util.force.ForceSimulator;
import prefuse.util.io.IOLib;
import prefuse.util.ui.JForcePanel;
import prefuse.util.ui.JValueSlider;
import prefuse.util.ui.UILib;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;

/**
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class FruchtermanReingoldVisualization implements PrefuseBetaVisualization {

	private static final String all = "graph";
	private static final String nodes = "graph.nodes";
	private static final String edges = "graph.edges";

	private Visualization visualization;

	public FruchtermanReingoldVisualization() {

	}

	public Graph create(Graph graph, Dictionary parameters) {
		UILib.setPlatformLookAndFeel();
		
		Box box = Box.createHorizontalBox();
		box.add(Box.createHorizontalGlue());
		Box legendsBox = new Box(BoxLayout.PAGE_AXIS);
		box.add(legendsBox);
		box.add(Box.createHorizontalStrut(5));


		visualization = new Visualization();
		
		Renderer nodeRenderer = new ShapeLabelRenderer();
		DefaultRendererFactory rendererFactory = new DefaultRendererFactory(nodeRenderer);
		if(graph.isDirected()) {
			rendererFactory.add(new InGroupPredicate(edges), new EdgeRenderer(prefuse.Constants.EDGE_TYPE_LINE, prefuse.Constants.EDGE_ARROW_FORWARD));
		}
		visualization.setRendererFactory(rendererFactory);
		
		visualization.addGraph(all, graph);
		
		visualization.setValue(edges, null, VisualItem.INTERACTIVE, Boolean.FALSE);


		String label = (String) parameters.get(Constants.label);
		String nodeColorField = (String) parameters.get(Constants.nodeColorField);
		String ringColorField = (String) parameters.get(Constants.ringColorField);
		String edgeSizeField = (String) parameters.get(Constants.edgeSizeField);
		String edgeColorField = (String) parameters.get(Constants.edgeColorField);
		String nodeShapeField = (String) parameters.get(Constants.nodeShapeField);
		String nodeSizeField = (String) parameters.get(Constants.nodeSizeField);
		
		int[] greenPalette = ColorLib.getInterpolatedPalette(ColorLib.rgb(0, 0, 0), ColorLib.rgb(0, 255, 0));
		int[] redPalette = ColorLib.getInterpolatedPalette(ColorLib.rgb(0, 0, 0), ColorLib.rgb(255,0,0));
		int green = ColorLib.rgb(0, 200, 0);
		int white = ColorLib.rgb(255, 255, 255);
		int black = ColorLib.rgb(0, 0, 0);
		
		
		ActionList draw = new ActionList();
		
		
		LegendDataColorAction nodeFillAction = null;
		ColumnExpression nodeFillExpression = null;
		if(!"".equals(nodeColorField)) {
			nodeFillExpression = new SmartValueExpression(nodeColorField, Indirection.getExample(graph.getNodes(), nodeColorField));
			Indirection nodeFillIndirection = new Indirection(visualization, nodes, nodeFillExpression);
			nodeFillAction = new LegendDataColorAction(
										nodes,
										nodeFillIndirection.getField(),
										nodeFillIndirection.getDataType(),
										VisualItem.FILLCOLOR,
										greenPalette);
			draw.add(nodeFillAction);
		} else {
			draw.add(new ColorAction(nodes, VisualItem.FILLCOLOR, white));
		}
		
		
		LegendDataColorAction edgeColorAction = null;
		ColumnExpression edgeColorExpression = null;
		if(!"".equals(edgeColorField)) {
			edgeColorExpression = new SmartValueExpression(edgeColorField, Indirection.getExample(graph.getEdges(), edgeColorField));
			Indirection edgeColorIndirection = new Indirection(visualization, edges, edgeColorExpression);
			edgeColorAction = new LegendDataColorAction(
										edges,
										edgeColorIndirection.getField(),
										edgeColorIndirection.getDataType(),
										VisualItem.STROKECOLOR,
										greenPalette);
			draw.add(edgeColorAction);
			
			
		} else {
			draw.add(new ColorAction(edges, VisualItem.STROKECOLOR, black));
		}

		if(!"".equals(ringColorField)) {
			Expression ringColorExpression = new SmartValueExpression(ringColorField, Indirection.getExample(graph.getNodes(), ringColorField));
			Indirection ringColorIndirection = new Indirection(visualization, nodes, ringColorExpression);
			draw.add(new DataColorAction(
							nodes,
							ringColorIndirection.getField(),
							ringColorIndirection.getDataType(),
							VisualItem.STROKECOLOR,
							redPalette));
		}  else {
			draw.add(new ColorAction(nodes, VisualItem.STROKECOLOR, black));
		}

		if(!"".equals(edgeSizeField)) {
			draw.add(new DataSizeAction(
							edges,
							new Indirection(visualization, edges, new ToDoubleExpression(edgeSizeField)).getField()));
		} //no need for edge size if not specified

		if(!"".equals(nodeSizeField)) {
			draw.add(new DataSizeAction(
							nodes,
							new Indirection(visualization, nodes, new ToDoubleExpression(nodeSizeField)).getField()));
		}

		if(!"".equals(nodeShapeField)) {
			draw.add(new DataShapeAction(nodes, nodeShapeField, new int[] { prefuse.Constants.SHAPE_ELLIPSE,
																			prefuse.Constants.SHAPE_RECTANGLE,
																			prefuse.Constants.SHAPE_TRIANGLE_UP,
																			prefuse.Constants.SHAPE_DIAMOND,
																			prefuse.Constants.SHAPE_TRIANGLE_DOWN}));
		} else {
			draw.add(new ShapeAction(nodes, prefuse.Constants.SHAPE_ELLIPSE));
		}
		
		draw.add(new FontAction(nodes, FontLib.getFont("Tahoma", 10)));
		draw.add(new ColorAction(nodes, VisualItem.TEXTCOLOR, ColorLib.gray(0)));

		ActionList layout = new ActionList();
		layout.add(new FruchtermanReingoldLayout(all));
		layout.add(new RepaintAction());

		visualization.putAction("draw", draw);
		visualization.putAction("layout", layout);
		visualization.runAfter("draw", "layout");
		
		if(edgeColorAction != null) {
			legendsBox.add(edgeColorAction.getLegend("Edge Color", edgeColorExpression.getColumnName()));
			//legendsBox.add(Box.createVerticalStrut(5));
		}
		if(nodeFillAction != null) {
			legendsBox.add(nodeFillAction.getLegend("Node Color", nodeFillExpression.getColumnName()));
		}



		Display display = new Display(visualization);
		display.setSize(700, 700);
		
		display.setBackground(Color.WHITE);

		display.addControlListener(new FocusControl(1));
		display.addControlListener(new DragControl());
		display.addControlListener(new PanControl());
		display.addControlListener(new ZoomControl());
		ZoomToFitControl zoomToFitControl = new ZoomToFitControl(edges, Control.MIDDLE_MOUSE_BUTTON);
		zoomToFitControl.setMargin(25);
		display.addControlListener(zoomToFitControl);
		display.addControlListener(new WheelZoomControl());
		display.addControlListener(new NeighborHighlightControl());

		
		
		visualization.run("draw");
		
		
		
		JFrame frame = new JFrame("p r e f u s e  |  g r a p h v i e w");
		
		JPanel panel = new JPanel(new BorderLayout());
        panel.add(display, BorderLayout.CENTER);
        panel.add(box, BorderLayout.SOUTH);
		
		
		
		
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		
		
		/*
		MouseEvent fakeEvent = new MouseEvent(display, 42, 42L, Control.MIDDLE_MOUSE_BUTTON, 0, 0, 1, false);
		zoomToFitControl.mouseClicked(fakeEvent);
		*/
		
		
		
		frame.setVisible(true);
		

		/* frame.addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent e) {
				visualization.run("layout");
			}
			public void windowDeactivated(WindowEvent e) {
				visualization.cancel("layout");
			}
		}); */

		

		return null;
	}

} // end of class GraphView
