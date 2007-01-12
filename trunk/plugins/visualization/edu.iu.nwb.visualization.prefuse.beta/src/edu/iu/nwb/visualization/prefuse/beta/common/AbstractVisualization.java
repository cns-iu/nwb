package edu.iu.nwb.visualization.prefuse.beta.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import edu.iu.nwb.visualization.prefuse.beta.common.action.LegendAction;
import edu.iu.nwb.visualization.prefuse.beta.common.action.LegendDataColorAction;
import edu.iu.nwb.visualization.prefuse.beta.common.action.LegendDataShapeAction;
import edu.iu.nwb.visualization.prefuse.beta.common.action.LegendDataSizeAction;
import edu.iu.nwb.visualization.prefuse.beta.common.expression.SmartValueExpression;
import edu.iu.nwb.visualization.prefuse.beta.common.expression.ToDoubleExpression;
import edu.iu.nwb.visualization.prefuse.beta.common.renderer.ShapeLabelRenderer;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.Action;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.FontAction;
import prefuse.action.assignment.ShapeAction;
import prefuse.action.layout.Layout;
import prefuse.action.layout.graph.FruchtermanReingoldLayout;
import prefuse.controls.Control;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.data.expression.ColumnExpression;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.Renderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.ui.UILib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;

public abstract class AbstractVisualization implements PrefuseBetaVisualization {
	
	protected static final String LAYOUT = "layout";
	protected static final String DRAW = "draw";
	private static final String all = "graph";
	private static final String nodes = "graph.nodes";
	private static final String edges = "graph.edges";

	private Visualization visualization;
	
	protected abstract Action getInitialDrawActions(String everythingGroup, Visualization visualization, Dictionary parameters);
	
	protected abstract Action getLayoutActions(String everythingGroup, Visualization visualization, Dictionary parameters);
	
	//	guaranteed to be called after getLayoutActions
	protected abstract void arrangeComponents(JFrame frame, Display display, JComponent legend);
	
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


		//String label = (String) parameters.get(Constants.label);
		String nodeColorField = (String) parameters.get(Constants.nodeColorField);
		String ringColorField = (String) parameters.get(Constants.ringColorField);
		String edgeSizeField = (String) parameters.get(Constants.edgeSizeField);
		String edgeColorField = (String) parameters.get(Constants.edgeColorField);
		String nodeShapeField = (String) parameters.get(Constants.nodeShapeField);
		String nodeSizeField = (String) parameters.get(Constants.nodeSizeField);
		
		int white = ColorLib.rgb(255, 255, 255);
		int black = ColorLib.rgb(0, 0, 0);
		
		
		ActionList draw = new ActionList();
		Action drawActions = this.getInitialDrawActions(all, visualization, parameters);
		if(drawActions != null) {
			draw.add(drawActions);
		}
		
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
		
		
		
		Action layout = this.getLayoutActions(all, visualization, parameters);

		visualization.putAction(DRAW, draw);
		visualization.putAction(LAYOUT, layout);
		visualization.runAfter(DRAW, LAYOUT);
		
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

		
		
		visualization.run(DRAW);
		
		
		
		
		
        JFrame frame = new JFrame();
		this.arrangeComponents(frame, display, box);
		
		
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
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
}
