package edu.iu.nwb.visualization.prefuse.beta.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.ps.PSGraphics2D;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.Action;
import prefuse.action.ActionList;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.FontAction;
import prefuse.action.assignment.ShapeAction;
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
import prefuse.util.ui.UILib;
import prefuse.visual.VisualItem;
import edu.iu.nwb.visualization.prefuse.beta.common.action.LegendAction;
import edu.iu.nwb.visualization.prefuse.beta.common.action.LegendDataColorAction;
import edu.iu.nwb.visualization.prefuse.beta.common.action.LegendDataShapeAction;
import edu.iu.nwb.visualization.prefuse.beta.common.action.LegendDataSizeAction;
import edu.iu.nwb.visualization.prefuse.beta.common.expression.SmartValueExpression;
import edu.iu.nwb.visualization.prefuse.beta.common.expression.ToDoubleExpression;
import edu.iu.nwb.visualization.prefuse.beta.common.renderer.ShapeLabelRenderer;

public abstract class AbstractVisualization implements PrefuseBetaVisualization {
	
	private static final JFileChooser chooser = new JFileChooser();
	
	/* static {
		chooser.addChoosableFileFilter(new FileFilter() {
			public boolean accept(File f) {
				if(f.isFile()) {
					if(f.getName().endsWith(".eps") || f.getName().endsWith(".ps")) {
						return true;
					}
					return false;
				}
				return true;
			}
			
			public String getDescription() {
				return "EPS file";
			}
		});
	} */
	
	protected static final String LAYOUT = "layout";
	protected static final String DRAW = "draw";
	private static final String all = "graph";
	private static final String nodes = "graph.nodes";
	private static final String edges = "graph.edges";

	private Visualization visualization;
	
	
	//this/these actions will be used at the beginning of the DRAW actionlist -- return an ActionList for multiple Actions
	//the parameters to this and the next allow for various magic -- right now nothing uses the parameters, though
	protected abstract Action getInitialDrawActions(String everythingGroup, Visualization visualization, Dictionary parameters);
	
	//all layout-related actions must be in this action(list), including redraw
	protected abstract Action getLayoutActions(String everythingGroup, Visualization visualization, Dictionary parameters);
	
	//	guaranteed to be called after getLayoutActions and getInitialDrawActions
	protected abstract Component arrangeComponents(Display display, JComponent legend);
	
	protected abstract void setTitle(JFrame frame);
	
	//right now everything should return null; the return parameter is a remainder of experiments in getting graphs out of visualizations
	public Graph create(Graph graph, Dictionary parameters) {
		UILib.setPlatformLookAndFeel();
		
		List legendActions = new ArrayList();
		
		//this is what the legend will go in
		Box legends = Box.createHorizontalBox();
		//a little padding on the left
		legends.add(Box.createHorizontalStrut(5));
		Box legendBox = null; //this variable will later hold individual parts of the legend as it is assembled


		visualization = new Visualization();
		
		Renderer nodeRenderer = new ShapeLabelRenderer();
		DefaultRendererFactory rendererFactory = new DefaultRendererFactory(nodeRenderer);
		
		EdgeRenderer edgeRenderer;
		
		if(graph.isDirected()) {
			edgeRenderer = new EdgeRenderer(prefuse.Constants.EDGE_TYPE_LINE, prefuse.Constants.EDGE_ARROW_FORWARD);
			edgeRenderer.setArrowHeadSize(6, 9);
		} else {
			edgeRenderer = new EdgeRenderer(prefuse.Constants.EDGE_TYPE_LINE, prefuse.Constants.EDGE_ARROW_NONE);
		}
		
		rendererFactory.setDefaultEdgeRenderer(edgeRenderer);
		
		
		visualization.setRendererFactory(rendererFactory);
		
		visualization.addGraph(all, graph);
		
		//stop the edges from even trying to respond to user interaction
		visualization.setValue(edges, null, VisualItem.INTERACTIVE, Boolean.FALSE);


		//String label = (String) parameters.get(Constants.label); //if we later let people set which field is the label
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
		
		draw.add(new ColorAction(edges, VisualItem.FILLCOLOR, black));
		
		if(!"".equals(nodeColorField)) { //a field is specified
			/* create an expression that, based on an example datapoint as a heuristic, determines the type and of the nodes and presents them
			 * converted as appropriate.
			 * This is necessary because almost all received values will be strings, but
			 * numbers, for instance, need to be handled as numbers in many cases.
			 */
			ColumnExpression nodeFillExpression = new SmartValueExpression(nodeColorField, Indirection.getExample(graph.getNodes(), nodeColorField));
			/* An indirection creates an additional column which will have the expression for its value */
			Indirection nodeFillIndirection = new Indirection(visualization, nodes, nodeFillExpression);
			//this action extends the DataColorAction to add legend production
			LegendDataColorAction nodeFillAction = new LegendDataColorAction(
										nodes, //what group we do this on
										nodeFillIndirection.getField(), //the appropriately typed field created by the indirection
										nodeFillIndirection.getDataType(), //the type from the indirection
										VisualItem.FILLCOLOR, //what we're coloring
										nodeFillIndirection.getPalette(), //this obtains the appropriate palette for coloring the data being indirected
										nodeFillExpression.getColumnName(), //the original column, needed for the legend
										"Node Color"); //the desired 'context' for the legend
			draw.add(nodeFillAction);
			legendActions.add(nodeFillAction);
		} else { //no field specified, so we make the nodes filled with white
			draw.add(new ColorAction(nodes, VisualItem.FILLCOLOR, white));
		}
		
		
		//same logic as above
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
			
		} else { //no field specified, we make the edges black
			draw.add(new ColorAction(edges, VisualItem.STROKECOLOR, black));
		}
		
		//same logic as above
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
		}  else { //no field specified, make the rings black
			draw.add(new ColorAction(nodes, VisualItem.STROKECOLOR, black));
		}

		if(!"".equals(edgeSizeField)) {
			//it should always be a number (but might be in the form of a string; this handles that)
			ColumnExpression edgeSizeExpression = new ToDoubleExpression(edgeSizeField);
			Indirection edgeSizeIndirection = new Indirection(visualization, edges, edgeSizeExpression);
			LegendDataSizeAction edgeSizeAction = new LegendDataSizeAction(
					edges, //group
					edgeSizeIndirection.getField(), //field indirection creates
					edgeSizeExpression.getColumnName(), //original column name
					"Edge Width"); //context for legend
			draw.add(edgeSizeAction);
			legendActions.add(edgeSizeAction);
		} //no need for edge size if not specified

		if(!"".equals(nodeSizeField)) {
			//same logic as above
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
			//no indirection or expression is needed because this can work with anything . . . except it can't
			SmartValueExpression nodeShapeExpression = new SmartValueExpression(nodeShapeField, Indirection.getExample(graph.getNodes(), nodeShapeField));
			Indirection nodeShapeIndirection = new Indirection(visualization, nodes, nodeShapeExpression);
			LegendDataShapeAction nodeShapeAction = new LegendDataShapeAction(	nodes, //the group
																				nodeShapeIndirection.getField(), //the field used to determine shape
																				new int[] { prefuse.Constants.SHAPE_ELLIPSE, //the shapes we will cycle through
																						prefuse.Constants.SHAPE_RECTANGLE,
																						prefuse.Constants.SHAPE_TRIANGLE_UP,
																						prefuse.Constants.SHAPE_DIAMOND,
																						prefuse.Constants.SHAPE_TRIANGLE_DOWN},
																				nodeShapeExpression.getColumnName(),
																				"Node Shape"); //context for legend
			draw.add(nodeShapeAction);
			legendActions.add(nodeShapeAction);
		} else { //no field specified, so everything's an ellipse
			draw.add(new ShapeAction(nodes, prefuse.Constants.SHAPE_ELLIPSE));
		}
		
		//make fonts consistent
		draw.add(new FontAction(nodes, Constants.LABEL_FONT));
		//give text a dark color
		draw.add(new ColorAction(nodes, VisualItem.TEXTCOLOR, ColorLib.gray(0)));
		
		
		
		Action layout = this.getLayoutActions(all, visualization, parameters);

		visualization.putAction(DRAW, draw);
		visualization.putAction(LAYOUT, layout);
		visualization.runAfter(DRAW, LAYOUT);
		
		Indirection.resetPalette();
		
		//sort legend parts by the number of 'lines' in the part -- reported as size
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
		
		// but we want the longest legends first
		Collections.reverse(legendActions);
		int overflow = 5; //max # of 'lines' in the box -- but overflow happens when the last added element is longer than needed to make this number.
		int current = 6; //trick to make it initialize legendsBox first go around (resetting current to 0)
		int maxHeight = 0;
		
		Iterator legendActionsIter = legendActions.iterator();
		while(legendActionsIter.hasNext()) {
			if(current >= overflow) { //if overflowed, start a new box
				if(legendBox != null) {
					legendBox.add(Box.createVerticalGlue());
					legendBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, maxHeight * 2)); //horrible kludge
					//System.err.println(maxHeight);
				}
				legendBox = new Box(BoxLayout.PAGE_AXIS);
				legendBox.setAlignmentY(Component.TOP_ALIGNMENT);
				legends.add(legendBox);
				current = 0;
				maxHeight = 0;
			} else { //add to the current box as appropriate, including separator
				legendBox.add(Box.createVerticalStrut(Constants.VERTICAL_STRUT_DISTANCE));
				legendBox.add(new JSeparator());
				legendBox.add(Box.createVerticalStrut(Constants.VERTICAL_STRUT_DISTANCE));
				maxHeight += Constants.VERTICAL_STRUT_DISTANCE * 3;
			}
			LegendAction action = (LegendAction) legendActionsIter.next();
			JComponent legend = action.getLegend();
			int legendSize = action.getLegendSize();
			legend.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) Constants.LEGEND_CANVAS_HEIGHT * legendSize));
			maxHeight += legend.getMaximumSize().height;
			legend.setAlignmentX(Component.LEFT_ALIGNMENT);
			//legend.setMaximumSize(maximumSize)
			legendBox.add(legend); //add the next legend part to the box
			
			current += legendSize;
		}
		if(legendBox != null) {
			legendBox.add(Box.createVerticalGlue());
			legendBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, maxHeight * 2)); //horrible kludge
			//System.err.println(maxHeight);
		}


		final Display display = new Display(visualization);
		display.setSize(700, 700); //seems a good default; visualizations will generally scale themselves to this
		
		display.setBackground(Color.WHITE);

		/* Prefuse Beta Magic: Controls */
		display.addControlListener(new FocusControl(1)); //focus on a node when clicked once. For some visualizations, this does nothing
		display.addControlListener(new DragControl()); //we can drag nodes
		display.addControlListener(new PanControl()); //we can pan by left-dragging the background
		display.addControlListener(new ZoomControl()); //we can zoom by right-dragging the background
		ZoomToFitControl zoomToFitControl = new ZoomToFitControl(edges, Control.MIDDLE_MOUSE_BUTTON); //we can zoom to fit by middle clicking (does not respect limits on zoom controls)
		zoomToFitControl.setMargin(25); //margin around the boundary -- this might need to be fiddled with. The default is too big.
		display.addControlListener(zoomToFitControl);
		display.addControlListener(new WheelZoomControl()); //we can zoom with the mouse wheel
		display.addControlListener(new NeighborHighlightControl()); //sets neighbors as highlighted. Currently has no effect on any visualizations

		
		//start things going
		visualization.run(DRAW);	
		
		//create a frame to stick everything in
        final JFrame frame = new JFrame();
        JPanel contentPane = new JPanel(new BorderLayout());
        
        KeyAdapter keyAdapter =
        	new KeyAdapter() {
	        	public void keyTyped(KeyEvent e) {
	        		if(e.getKeyChar() == 'e') {	        			
	        			exportGraphToFile(frame, display);		        			
	        		}
	        	}
	        };

		frame.addKeyListener(keyAdapter);
		display.addKeyListener(keyAdapter);

        JButton exportButton = new JButton ("Export Image to .eps File");
    	exportButton.setMnemonic(KeyEvent.VK_E);
        exportButton.setActionCommand("export");

    	exportButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if ("export".equals(e.getActionCommand())) {
    				exportGraphToFile(frame, display);
        		}
    		}  		
    	});

        JScrollPane scrollPane = new JScrollPane(legends);
        scrollPane.setPreferredSize(new Dimension(100, Math.min(180, maxHeight * 2)));
        display.setOpaque(true);
        
        Component thePanel = this.arrangeComponents(display, scrollPane);
        contentPane.add(exportButton, BorderLayout.NORTH);
        contentPane.add(thePanel, BorderLayout.CENTER);
        
        this.setTitle(frame);
        frame.setContentPane(contentPane);
		
		//standard boilerplate
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		

		return null;
	}
	
	private void exportGraphToFile(JFrame frame, Display display) {
		int option = chooser.showSaveDialog(frame);
		if(option == JFileChooser.APPROVE_OPTION) {
			Properties properties = new Properties();
			properties.setProperty("PageSize", "A5");
			
			try {				
				//display.saveImage(new FileOutputStream(chooser.getSelectedFile()), "eps", 1.0);				
				//Rectangle2D bounds = visualization.getBounds(all);
				
				VectorGraphics graphics =
					new PSGraphics2D(
							chooser.getSelectedFile(), display.getSize());
				graphics.setProperties(properties);
				graphics.startExport();
				display.printAll(graphics);
				graphics.endExport();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//ExportDialog export = new ExportDialog();
			//export.showExportDialog(frame, "Export visualization as . . .", display, "export");
		}
	}
}
