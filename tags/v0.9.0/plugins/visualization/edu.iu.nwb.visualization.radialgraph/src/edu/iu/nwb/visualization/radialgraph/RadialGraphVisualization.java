package edu.iu.nwb.visualization.radialgraph;

import edu.berkeley.guir.prefuse.AggregateItem;
import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.RepaintAction;
import edu.berkeley.guir.prefuse.action.animate.ColorAnimator;
import edu.berkeley.guir.prefuse.action.animate.PolarLocationAnimator;
import edu.berkeley.guir.prefuse.action.assignment.ColorFunction;
import edu.berkeley.guir.prefuse.action.filter.TreeFilter;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.activity.SlowInSlowOutPacer;
import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.GraphLib;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefuse.graph.Tree;
import edu.berkeley.guir.prefuse.render.DefaultEdgeRenderer;
import edu.berkeley.guir.prefuse.render.DefaultRendererFactory;
import edu.berkeley.guir.prefuse.render.Renderer;
import edu.berkeley.guir.prefuse.render.TextItemRenderer;
import edu.berkeley.guir.prefuse.util.ColorLib;
import edu.berkeley.guir.prefuse.util.StringAbbreviator;
import edu.berkeley.guir.prefusex.controls.DragControl;
import edu.berkeley.guir.prefusex.controls.FocusControl;
import edu.berkeley.guir.prefusex.controls.NeighborHighlightControl;
import edu.berkeley.guir.prefusex.controls.PanControl;
import edu.berkeley.guir.prefusex.controls.ZoomControl;
import edu.berkeley.guir.prefusex.layout.RadialTreeLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Paint;

import javax.swing.JFrame;


/**
 * visualization showcasing the use of an animated radial tree layout to
 * visualize a graph.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */

// modified Shashikant Penumarthy for IVC
// modified James Ellis for IVC-RCP
public class RadialGraphVisualization {
    public static final String nameField = "label";
    private ItemRegistry registry;
    private Graph graph;
    private Display display;
    private ActionList layout;
    private ActionList update;
    private ActionList animate;
    private ActionList filter;
    private JFrame frame;

    /**
     * Creates a new RadialGraphVisualization object.
     */
    public RadialGraphVisualization() {
    }

    /**
     * Sets the input Graph to be used by this Visualization
     *
     * @param graph the Graph to visualize with the RadialGraphVisualization
     */
    public void setInput(Graph graph) {
        this.graph = graph;
    }

    /**
     * Display the visualization of the graph that was passed into
     * the setInput(Graph) method.
     */
    public void show() {
        try {
            // create display and filter
            registry = new ItemRegistry(graph);
            display = new Display();

            // initialize renderers
            initializeRenderers();

            // initialize action pipelines
            initializeActionList();

            // initialize display 
            initializeDisplay();

            // set up initial focus and focus listener
            registry.getDefaultFocusSet().addFocusListener(new FocusListener() {
                    public void focusChanged(FocusEvent e) {
                        if (update.isScheduled()) {
                            update.cancel();
                        }

                        Node node = (Node) e.getFirstAdded();

                        if (node != null) {
                            NodeItem item = (NodeItem) registry.getNodeItem(node);
                            Tree t = GraphLib.breadthFirstTree(item);
                            registry.setFilteredGraph(t);
                            layout.runNow();
                            animate.runNow();
                        }
                    } //
                });

            // create and display application window
            frame = new JFrame("Radial Tree/Graph (prefuse alpha)");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(display, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);

            // run filter, layout, and perform initial animation
            filter.runNow();
            layout.runNow();
            animate.runNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    } //

    /*
     * initialize the renderers for this RadialGraphVisualization
     */
    private void initializeRenderers() {
        Renderer nodeRenderer = new TextItemRenderer() {
                private int maxWidth = 175;
                private StringAbbreviator abbrev = new StringAbbreviator(null,
                        null);

                protected String getText(VisualItem item) {
                    String s = item.getAttribute(m_labelName);
                    Font font = item.getFont();

                    if (font == null) {
                        font = m_font;
                    }

                    FontMetrics fm = DEFAULT_GRAPHICS.getFontMetrics(font);
                    
                    if (s != null && fm.stringWidth(s) > maxWidth) {
                        s = abbrev.abbreviate(s, StringAbbreviator.NAME, fm,
                                maxWidth);
                    }

                    return s;
                } //
            };

        Renderer edgeRenderer = new DefaultEdgeRenderer() {
                protected int getLineWidth(VisualItem item) {
                    String w = item.getAttribute("weight");

                    if (w != null) {
                        try {
                            return Integer.parseInt(w);
                        } catch (Exception e) {
                        }
                    }

                    return m_width;
                } //
            };

        Renderer aggrRenderer = null;
        ((TextItemRenderer) nodeRenderer).setRoundedCorner(8, 8);

        // initialize item registry
        registry.setRendererFactory(new DefaultRendererFactory(nodeRenderer,
                edgeRenderer, aggrRenderer));
    }

    /*
     * Initialize the ActionList for this RadialGraphVisualization
     */
    private void initializeActionList() {
        filter = new ActionList(registry);
        filter.add(new TreeFilter());

        layout = new ActionList(registry);
        layout.add(new RadialTreeLayout());
        layout.add(new DemoColorFunction(3));

        update = new ActionList(registry);
        update.add(new DemoColorFunction(3));
        update.add(new RepaintAction());

        animate = new ActionList(registry, 1500, 20);
        animate.setPacingFunction(new SlowInSlowOutPacer());
        animate.add(new PolarLocationAnimator());
        animate.add(new ColorAnimator());
        animate.add(new RepaintAction());
    }
    
    /*
     * initialize the display from this RadialGraphVisualization
     */
    private void initializeDisplay() {
        display.setItemRegistry(registry);
        display.setSize(700, 700);
        display.setBackground(Color.WHITE);
        display.addControlListener(new FocusControl());
        display.addControlListener(new DragControl());
        display.addControlListener(new PanControl());
        display.addControlListener(new ZoomControl());
        display.addControlListener(new NeighborHighlightControl(update));
    }

    public class DemoColorFunction extends ColorFunction {
        private Color graphEdgeColor = Color.LIGHT_GRAY;
        private Color highlightColor = new Color(50, 50, 255);
        private Color[] nodeColors;
        private Color[] edgeColors;

        public DemoColorFunction(int thresh) {
            nodeColors = new Color[thresh];
            edgeColors = new Color[thresh];

            for (int i = 0; i < thresh; i++) {
                double frac = i / ((double) thresh);
                nodeColors[i] = ColorLib.getIntermediateColor(Color.RED,
                        Color.BLACK, frac);
                edgeColors[i] = ColorLib.getIntermediateColor(Color.RED,
                        Color.BLACK, frac);
            }
        } //

        public Paint getFillColor(VisualItem item) {
            if (item instanceof NodeItem) {
                return Color.WHITE;
            } else if (item instanceof AggregateItem) {
                return Color.LIGHT_GRAY;
            } else if (item instanceof EdgeItem) {
                return getColor(item);
            } else {
                return Color.BLACK;
            }
        } //

        public Paint getColor(VisualItem item) {
            if (item.isHighlighted()) {
                return highlightColor;
            } else if (item instanceof NodeItem) {
                int d = ((NodeItem) item).getDepth();

                return nodeColors[Math.min(d, nodeColors.length - 1)];
            } else if (item instanceof EdgeItem) {
                EdgeItem e = (EdgeItem) item;

                if (e.isTreeEdge()) {
                    int d;
                    int d1;
                    int d2;
                    d1 = ((NodeItem) e.getFirstNode()).getDepth();
                    d2 = ((NodeItem) e.getSecondNode()).getDepth();
                    d = Math.max(d1, d2);

                    return edgeColors[Math.min(d, edgeColors.length - 1)];
                } else {
                    return graphEdgeColor;
                }
            } else {
                return Color.BLACK;
            }
        } //
    } // end of inner class DemoColorFunction
} // end of classs RadialGraphVisualization
