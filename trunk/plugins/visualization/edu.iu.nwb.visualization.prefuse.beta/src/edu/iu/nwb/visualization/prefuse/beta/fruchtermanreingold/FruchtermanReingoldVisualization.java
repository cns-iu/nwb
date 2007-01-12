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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
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
import edu.iu.nwb.visualization.prefuse.beta.common.action.LegendAction;
import edu.iu.nwb.visualization.prefuse.beta.common.action.LegendDataColorAction;
import edu.iu.nwb.visualization.prefuse.beta.common.action.LegendDataShapeAction;
import edu.iu.nwb.visualization.prefuse.beta.common.action.LegendDataSizeAction;
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
		
		//use this later, once a workaround with the expressions has been decided on
		List legendActions = new ArrayList();
		
		Box box = Box.createHorizontalBox();
		box.add(Box.createHorizontalStrut(5));
		Box legendsBox = null;
		//box.add(Box.createHorizontalStrut(5));


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
		
		
		if(!"".equals(nodeColorField)) {
			ColumnExpression nodeFillExpression = new SmartValueExpression(nodeColorField, Indirection.getExample(graph.getNodes(), nodeColorField));
			Indirection nodeFillIndirection = new Indirection(visualization, nodes, nodeFillExpression);
			LegendDataColorAction nodeFillAction = new LegendDataColorAction(
										nodes,
										nodeFillIndirection.getField(),
										nodeFillIndirection.getDataType(),
										VisualItem.FILLCOLOR,
										nodeFillIndirection.getPalette(),
										nodeFillExpression.getColumnName(),
										"Node Color");
			draw.add(nodeFillAction);
			legendActions.add(nodeFillAction);
		} else {
			draw.add(new ColorAction(nodes, VisualItem.FILLCOLOR, white));
		}
		
		
		if(!"".equals(edgeColorField)) {
			ColumnExpression edgeColorExpression = new SmartValueExpression(edgeColorField, Indirection.getExample(graph.getEdges(), edgeColorField));
			Indirection edgeColorIndirection = new Indirection(visualization, edges, edgeColorExpression);
			LegendDataColorAction edgeColorAction = new LegendDataColorAction(
										edges,
										edgeColorIndirection.getField(),
										edgeColorIndirection.getDataType(),
										VisualItem.STROKECOLOR,
										edgeColorIndirection.getPalette(),
										edgeColorExpression.getColumnName(),
										"Edge Color");
			draw.add(edgeColorAction);
			legendActions.add(edgeColorAction);
			
		} else {
			draw.add(new ColorAction(edges, VisualItem.STROKECOLOR, black));
		}
		
		if(!"".equals(ringColorField)) {
			ColumnExpression ringColorExpression = new SmartValueExpression(ringColorField, Indirection.getExample(graph.getNodes(), ringColorField));
			Indirection ringColorIndirection = new Indirection(visualization, nodes, ringColorExpression);
			LegendDataColorAction ringColorAction = new LegendDataColorAction(
										nodes,
										ringColorIndirection.getField(),
										ringColorIndirection.getDataType(),
										VisualItem.STROKECOLOR,
										ringColorIndirection.getPalette(),
										ringColorExpression.getColumnName(),
										"Ring Color");
			draw.add(ringColorAction);
			legendActions.add(ringColorAction);
		}  else {
			draw.add(new ColorAction(nodes, VisualItem.STROKECOLOR, black));
		}

		if(!"".equals(edgeSizeField)) {
			ColumnExpression edgeSizeExpression = new ToDoubleExpression(edgeSizeField);
			Indirection edgeSizeIndirection = new Indirection(visualization, edges, edgeSizeExpression);
			LegendDataSizeAction edgeSizeAction = new LegendDataSizeAction(
					edges,
					edgeSizeIndirection.getField(),
					edgeSizeExpression.getColumnName(),
					"Edge Width");
			draw.add(edgeSizeAction);
			legendActions.add(edgeSizeAction);
		} //no need for edge size if not specified

		if(!"".equals(nodeSizeField)) {
			ToDoubleExpression nodeSizeExpression = new ToDoubleExpression(nodeSizeField);
			Indirection nodeSizeIndirection = new Indirection(visualization, nodes, nodeSizeExpression);
			LegendDataSizeAction nodeSizeAction = new LegendDataSizeAction(
					nodes,
					nodeSizeIndirection.getField(),
					nodeSizeExpression.getColumnName(),
					"Node Size");
			draw.add(nodeSizeAction);
			legendActions.add(nodeSizeAction);
		}

		if(!"".equals(nodeShapeField)) {
			LegendDataShapeAction nodeShapeAction = new LegendDataShapeAction(nodes, nodeShapeField, new int[] { prefuse.Constants.SHAPE_ELLIPSE,
																						prefuse.Constants.SHAPE_RECTANGLE,
																						prefuse.Constants.SHAPE_TRIANGLE_UP,
																						prefuse.Constants.SHAPE_DIAMOND,
																						prefuse.Constants.SHAPE_TRIANGLE_DOWN},
																						"Node Shape");
			draw.add(nodeShapeAction);
			legendActions.add(nodeShapeAction);
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
		
		class LegendComparator implements Comparator {

			public int compare(Object one, Object two) {
				LegendAction first = (LegendAction) one;
				LegendAction second = (LegendAction) two;
				Integer firstSize = new Integer(first.getLegendSize());
				Integer secondSize = new Integer(second.getLegendSize());
				
				return firstSize.compareTo(secondSize);
			}
			
		}
		Collections.sort(legendActions, new LegendComparator());
		Collections.reverse(legendActions);
		int overflow = 5; //trick to make it initialize legendsBox first go around
		int current = 6;
		
		Iterator legendActionsIter = legendActions.iterator();
		while(legendActionsIter.hasNext()) {
			if(current >= overflow) {
				legendsBox = new Box(BoxLayout.PAGE_AXIS);
				box.add(legendsBox);
				current = 0;
			} else {
				legendsBox.add(Box.createVerticalStrut(Constants.VERTICAL_STRUT_DISTANCE));
				legendsBox.add(new JSeparator());
				legendsBox.add(Box.createVerticalStrut(Constants.VERTICAL_STRUT_DISTANCE));
			}
			LegendAction action = (LegendAction) legendActionsIter.next();
			JComponent legend = action.getLegend();
			legendsBox.add(legend);
			System.out.println(action.getLegendSize());
			current += action.getLegendSize();
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
