package edu.iu.nwb.visualization.prefuse.beta.specified;

import java.awt.Color;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Dictionary;

import javax.swing.JFrame;
import javax.swing.JPanel;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.SizeAction;
import prefuse.action.layout.SpecifiedLayout;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.Tuple;
import prefuse.data.expression.AbstractPredicate;
import prefuse.data.expression.ColumnExpression;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.ui.UILib;
import prefuse.visual.VisualItem;
import edu.iu.nwb.visualization.prefuse.beta.common.Constants;
import edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaVisualization;

public class SpecifiedVisualization implements PrefuseBetaVisualization {
	private static final long serialVersionUID = 1L;
	
	public static final int LAYOUT_ANIMATION_DURATION_IN_MS = 1500;
	
	public static final int NUMBER_OF_CLICKS_FOR_FOCUS = 1;

	public static final Color DISPLAY_BACKGROUND_COLOR = Color.WHITE;
	public static final Color DISPLAY_FOREGROUND_COLOR = Color.GRAY;
	public static final int IU_CRIMSON = ColorLib.rgb(125, 17, 12);
	public static final int NODE_FILL_COLOR = IU_CRIMSON;	
	public static final int EDGE_STROKE_COLOR = ColorLib.gray(200);
	public static final int EDGE_FILL_COLOR = ColorLib.gray(200);
	public static final int NODE_TEXT_COLOR = ColorLib.gray(255);
	public static final int NODE_STROKE_COLOR = ColorLib.gray(0);
	public static final int HIGHLIGHT_COLOR = ColorLib.rgb(255,200,125);

	public static final String FRAME_TITLE =
		"Pre-defined Positions (prefuse beta)";

	// Prefuse action identifiers
	public static final String LAYOUT = "layout";
	public static final String DRAW = "draw";
	public static final String SIZE = "size";
	
	// Prefuse group identifiers
	public static final String GRAPH = "graph";
	public static final String NODES = "graph.nodes";
	public static final String EDGES = "graph.edges";

	protected static final double NODE_SIZE_MAXIMUM = 0.5;
	public static final int NODE_LABEL_ROUNDED_ARC_WIDTH = 8;
	public static final int NODE_LABEL_ROUNDED_ARC_HEIGHT = 8;
	public static final int DEFAULT_DISPLAY_HEIGHT = 700;
	public static final int DEFAULT_DISPLAY_WIDTH = 900;

	
	private JPanel panel;
    private Visualization visualization;
	
    
    public SpecifiedVisualization() {
		panel = new JPanel();
	}

	public SpecifiedVisualization(
			Graph graph, String label, Dictionary parameters) {    	
        visualization = createVisualization(graph, parameters);
        
        final Display display = createInteractiveDisplay(visualization);        
        panel = createPanel(display);
        panel.add(display);
        
        visualization.run(DRAW);
    }


	public Graph create(Graph graph, Dictionary parameters) {
	    UILib.setPlatformLookAndFeel();
	    
	    String label = (String) parameters.get(Constants.label);
		JFrame frame = createFrame(graph, label, parameters);
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    
		return null;
	}

	private Visualization createVisualization(
			Graph graph, Dictionary parameters) {
		Visualization visualization = new Visualization();
        
        LabelRenderer labelRenderer = new LabelRenderer();
        labelRenderer.setRoundedCorner(
        		NODE_LABEL_ROUNDED_ARC_WIDTH, NODE_LABEL_ROUNDED_ARC_HEIGHT);
        visualization.setRendererFactory(
        		new DefaultRendererFactory(labelRenderer));
		
        addScaledDoubleColumns(graph, parameters);

        visualization.removeGroup(GRAPH);
        visualization.addGraph(GRAPH, graph);        
        setGroupInteractivity(visualization, EDGES, false);
        
        visualization.putAction(SIZE, createSizeAction());
        
        ColorAction fill =
        	new ColorAction(NODES, VisualItem.FILLCOLOR, NODE_FILL_COLOR);
        fill.add(VisualItem.HIGHLIGHT, HIGHLIGHT_COLOR);
        
        visualization.putAction(DRAW, createDrawAction(fill));        
        visualization.putAction(LAYOUT, createLayoutAction(fill));
        
        // Schedule runs
        visualization.run(LAYOUT);
        visualization.runAfter(DRAW, SIZE);
        
        return visualization;
	}

	private static JFrame createFrame(
			Graph graph, String label, Dictionary parameters) {
	    final SpecifiedVisualization view =
	    	new SpecifiedVisualization(graph, label, parameters);
	    
	    // Launch window
	    JFrame frame = new JFrame(FRAME_TITLE);
	    frame.setContentPane(view.getPanel());
	    frame.pack();
	    frame.setVisible(true);
	    
	    frame.addWindowListener(new WindowAdapter() {
	        public void windowActivated(WindowEvent e) {
	            view.visualization.run(LAYOUT);
	        }
	        public void windowDeactivated(WindowEvent e) {
	            view.visualization.cancel(LAYOUT);
	        }
	    });
	    
	    return frame;
	}

	private JPanel createPanel(final Display display) {
		final JPanel panel = new JPanel();
        
        // When the panel is resized, resize the inner display accordingly.
        panel.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                display.setBounds(new Rectangle(e.getComponent().getSize()));
                panel.invalidate();
            }
        });
        
        return panel;
	}
    
	private static Display createInteractiveDisplay(
			Visualization visualization) {
		final Display display = new Display(visualization);
		
        display.setSize(DEFAULT_DISPLAY_WIDTH, DEFAULT_DISPLAY_HEIGHT);
        // Pan to center
        display.pan(DEFAULT_DISPLAY_WIDTH / 2.0, DEFAULT_DISPLAY_HEIGHT / 2.0);
        
        display.setForeground(DISPLAY_FOREGROUND_COLOR);
        display.setBackground(DISPLAY_BACKGROUND_COLOR);
        
        display.addControlListener(new FocusControl(NUMBER_OF_CLICKS_FOR_FOCUS));
        display.addControlListener(new DragControl());
        display.addControlListener(new PanControl());
        display.addControlListener(new ZoomControl());
        display.addControlListener(new WheelZoomControl());
        display.addControlListener(new ZoomToFitControl(NODES));
        display.addControlListener(new NeighborHighlightControl());
        
		return display;
	}

	private ActionList createLayoutAction(ColorAction fill) {
		ActionList layoutActions =
        	new ActionList(LAYOUT_ANIMATION_DURATION_IN_MS);
        layoutActions.add(
        		new SpecifiedLayout(GRAPH, Constants._x, Constants._y));
		layoutActions.add(fill);
        layoutActions.add(new RepaintAction());
		return layoutActions;
	}

	private ActionList createDrawAction(ColorAction fill) {
		ActionList drawActions = new ActionList();        
        drawActions.add(fill);
        drawActions.add(new ColorAction(
        		NODES, VisualItem.STROKECOLOR, NODE_STROKE_COLOR));
        drawActions.add(new ColorAction(
        		NODES, VisualItem.TEXTCOLOR, NODE_TEXT_COLOR));
        drawActions.add(new ColorAction(
        		EDGES, VisualItem.FILLCOLOR, EDGE_FILL_COLOR));
        drawActions.add(new ColorAction(
        		EDGES, VisualItem.STROKECOLOR, EDGE_STROKE_COLOR));
		return drawActions;
	}

	private SizeAction createSizeAction() {
		SizeAction sizeAction = new SizeAction() {
        	public double getSize(VisualItem item) {
        		double scale = m_vis.getDisplay(0).getScale();
        		
        		if (scale <= 1) {
        			return NODE_SIZE_MAXIMUM;
        		} else {
        			return NODE_SIZE_MAXIMUM / scale;
        		}
        	}
        };
		return sizeAction;
	}

	/* Enable Prefuse to both read doubles and parse them from Strings.
	 * Also scales from the DrL values (which tend to be small) to
	 * an arbitrary bigger scale so that there we will get better dispersion
	 * of nodes on the Visualization.
	 */
	private void addScaledDoubleColumns(Graph graph, Dictionary parameters) {
		String xLabel = (String) parameters.get(Constants.X_ID);
		String yLabel = (String) parameters.get(Constants.Y_ID);
		
		class DoubleParsingExpression extends ColumnExpression {
			public static final int DRL_TO_VISUAL_SCALE_FACTOR = 20;
			
			DoubleParsingExpression(String column) {
				super(column);
			}
			
			public double getDouble(Tuple t) {				
				double drlValue = Double.NaN;
				if(super.getType(t.getSchema()) == double.class) {
					drlValue = super.getDouble(t);
				} else {
					drlValue = Double.parseDouble((String) super.get(t));
				}
				
				double visualValue = DRL_TO_VISUAL_SCALE_FACTOR * drlValue;
				return visualValue;
			}
			public Class getType(Schema s) {
				return double.class;
			}
		}		
		graph.addColumn(Constants._x, new DoubleParsingExpression(xLabel));
		graph.addColumn(Constants._y, new DoubleParsingExpression(yLabel));
	}
    
    private void setGroupInteractivity(
			Visualization visualization,
			String groupIdentifier,
			boolean isInteractive) {
		visualization.setValue(
				groupIdentifier,
				new ConstantTruePredicate(),
				VisualItem.INTERACTIVE,
				new Boolean(isInteractive));
	}

	private Container getPanel() {
		return panel;
	}
	
	
	public static final class ConstantTruePredicate extends AbstractPredicate {
		public Object get(Tuple tuple) {
			return Boolean.TRUE;
		}

		public boolean getBoolean(Tuple tuple) {
			return true;
		}
	}
}