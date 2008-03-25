package edu.iu.nwb.visualization.prefuse.beta.radialgraph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.Dictionary;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.GroupAction;
import prefuse.action.ItemAction;
import prefuse.action.RepaintAction;
import prefuse.action.animate.ColorAnimator;
import prefuse.action.animate.PolarLocationAnimator;
import prefuse.action.animate.QualityControlAnimator;
import prefuse.action.animate.VisibilityAnimator;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.FontAction;
import prefuse.action.assignment.SizeAction;
import prefuse.action.layout.CollapsedSubtreeLayout;
import prefuse.action.layout.graph.RadialTreeLayout;
import prefuse.activity.SlowInSlowOutPacer;
import prefuse.controls.ControlAdapter;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.HoverActionControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.search.PrefixSearchTupleSet;
import prefuse.data.search.SearchTupleSet;
import prefuse.data.tuple.DefaultTupleSet;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.render.ShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.ui.JFastLabel;
import prefuse.util.ui.UILib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;
import prefuse.visual.sort.TreeDepthItemSorter;
import edu.iu.nwb.visualization.prefuse.beta.common.Constants;
import edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaVisualization;


/**
 * Demonstration of a node-link tree viewer
 *
 * @version 1.0
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class RadialGraphVisualization extends Display implements PrefuseBetaVisualization {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String DATA_FILE = "/socialnet.xml";
    
    private static final String tree = "tree";
    private static final String treeNodes = "tree.nodes";
    private static final String treeEdges = "tree.edges";
    private static final String linear = "linear";
    
    private LabelRenderer m_nodeRenderer;
    private EdgeRenderer m_edgeRenderer;
    
    private String m_label = "label";
    
    public RadialGraphVisualization(Graph g, String label) {
        super(new Visualization());
        m_label = label;

        // -- set up visualization --
        m_vis.add(tree, g);
        m_vis.setInteractive(treeEdges, null, false);
        
        // -- set up renderers --
        m_nodeRenderer = new LabelRenderer(m_label);
        m_nodeRenderer.setRenderType(ShapeRenderer.RENDER_TYPE_FILL);
        m_nodeRenderer.setHorizontalAlignment(prefuse.Constants.CENTER);
        m_nodeRenderer.setRoundedCorner(8,8);
        m_edgeRenderer = new EdgeRenderer() {
        	public double getLineWidth(VisualItem item) {
        		if(item.canGetString("weight")) {
        			return Double.parseDouble(item.getString("weight"));
        		}
        		
        		return getDefaultLineWidth();
        	}
        };
        
        DefaultRendererFactory rf = new DefaultRendererFactory(m_nodeRenderer);
        rf.add(new InGroupPredicate(treeEdges), m_edgeRenderer);
        m_vis.setRendererFactory(rf);
               
        // -- set up processing actions --
        
        // colors
        ItemAction nodeColor = new NodeColorAction(treeNodes);
        ItemAction textColor = new TextColorAction(treeNodes);
        m_vis.putAction("textColor", textColor);
        
        ItemAction edgeColor = new ColorAction(treeEdges,
                VisualItem.STROKECOLOR, ColorLib.rgb(200,200,200));
        
        FontAction fonts = new FontAction(treeNodes, 
                FontLib.getFont("Tahoma", 10));
        fonts.add("ingroup('_focus_')", FontLib.getFont("Tahoma", 11));
        
        // recolor
        ActionList recolor = new ActionList();
        recolor.add(nodeColor);
        recolor.add(textColor);
        m_vis.putAction("recolor", recolor);
        
        SizeAction sizeAction = new SizeAction() {
        	public double getSize(VisualItem item) {
        		double scale = RadialGraphVisualization.this.getScale();
        		if(scale <= 1) {
        			return 1;
        		} else {
        			return 1 / scale;
        		}
        	}
        };
        
        // repaint
        ActionList repaint = new ActionList();
        repaint.add(sizeAction);
        repaint.add(recolor);
        repaint.add(new RepaintAction());
        m_vis.putAction("repaint", repaint);
        
        // animate paint change
        ActionList animatePaint = new ActionList(400);
        animatePaint.add(new ColorAnimator(treeNodes));
        animatePaint.add(new RepaintAction());
        m_vis.putAction("animatePaint", animatePaint);
        
        // create the tree layout action
        RadialTreeLayout treeLayout = new RadialTreeLayout(tree);
        //treeLayout.setAngularBounds(-Math.PI/2, Math.PI);
        m_vis.putAction("treeLayout", treeLayout);
        
        CollapsedSubtreeLayout subLayout = new CollapsedSubtreeLayout(tree);
        m_vis.putAction("subLayout", subLayout);
        
        
        m_vis.putAction("size", sizeAction);
        
        // create the filtering and layout
        ActionList filter = new ActionList();
        filter.add(new TreeRootAction(tree));
        filter.add(fonts);
        filter.add(treeLayout);
        filter.add(subLayout);
		filter.add(sizeAction);
        filter.add(textColor);
        filter.add(nodeColor);
        filter.add(edgeColor);
        m_vis.putAction("filter", filter);
        
        // animated transition
        ActionList animate = new ActionList(1250);
        animate.add(sizeAction);
        animate.setPacingFunction(new SlowInSlowOutPacer());
        animate.add(new QualityControlAnimator());
        animate.add(new VisibilityAnimator(tree));
        animate.add(new PolarLocationAnimator(treeNodes, linear));
        animate.add(new ColorAnimator(treeNodes));
        animate.add(new RepaintAction());
        m_vis.putAction("animate", animate);
        m_vis.alwaysRunAfter("filter", "animate");
        
        // ------------------------------------------------
        
        // initialize the display
        setSize(600,600);
        setItemSorter(new TreeDepthItemSorter());
        addControlListener(new WheelZoomControl());
        addControlListener(new DragControl());
        addControlListener(new ZoomToFitControl() {
        	public void mouseClicked(MouseEvent event) {
        		m_vis.run("size");
        		super.mouseClicked(event);
        	}
        });
        addControlListener(new ZoomControl() {
        	public void mouseDragged(MouseEvent event) {
        		m_vis.run("size");
        		super.mouseDragged(event);
        	}
        });
        addControlListener(new PanControl());
        addControlListener(new FocusControl(1, "filter"));
        addControlListener(new HoverActionControl("repaint"));
        
        // ------------------------------------------------
        
        // filter graph and perform layout
        m_vis.run("filter");
        
        // maintain a set of items that should be interpolated linearly
        // this isn't absolutely necessary, but makes the animations nicer
        // the PolarLocationAnimator should read this set and act accordingly
        m_vis.addFocusGroup(linear, new DefaultTupleSet());
        m_vis.getGroup(Visualization.FOCUS_ITEMS).addTupleSetListener(
            new TupleSetListener() {
                public void tupleSetChanged(TupleSet t, Tuple[] add, Tuple[] rem) {
                    TupleSet linearInterp = m_vis.getGroup(linear);
                    if ( add.length < 1 ) return; linearInterp.clear();
                    for ( Node n = (Node)add[0]; n!=null; n=n.getParent() )
                        linearInterp.addTuple(n);
                }
            }
        );
        
        SearchTupleSet search = new PrefixSearchTupleSet();
        m_vis.addFocusGroup(Visualization.SEARCH_ITEMS, search);
        search.addTupleSetListener(new TupleSetListener() {
            public void tupleSetChanged(TupleSet t, Tuple[] add, Tuple[] rem) {
                m_vis.cancel("animatePaint");
                m_vis.run("recolor");
                m_vis.run("animatePaint");
            }
        });
    }
    
    // ------------------------------------------------------------------------
    
    public RadialGraphVisualization() {
		// TODO Auto-generated constructor stub
	}

	public Graph create(Graph g, Dictionary parameters) {
        
        UILib.setPlatformLookAndFeel();
        
        JFrame frame = new JFrame("p r e f u s e  |  r a d i a l t r e e v i e w");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        String label = (String) parameters.get(Constants.label);
		frame.setContentPane(demo(g, label));
        frame.pack();
        frame.setVisible(true);
		return null;
    }
    
    public JPanel demo(Graph g, final String label) {        
        // create a new radial tree view
        final RadialGraphVisualization gview = new RadialGraphVisualization(g, label);
        //Visualization vis = gview.getVisualization();
        
        
        final JFastLabel title = new JFastLabel("                 ");
        title.setPreferredSize(new Dimension(350, 20));
        title.setVerticalAlignment(SwingConstants.BOTTOM);
        title.setBorder(BorderFactory.createEmptyBorder(3,0,0,0));
        title.setFont(FontLib.getFont("Tahoma", Font.PLAIN, 16));
        
        gview.addControlListener(new ControlAdapter() {
            public void itemEntered(VisualItem item, MouseEvent e) {
                if ( item.canGetString(label) )
                    title.setText(item.getString(label));
            }
            public void itemExited(VisualItem item, MouseEvent e) {
                title.setText(null);
            }
        });
        
        Box box = new Box(BoxLayout.X_AXIS);
        box.add(Box.createHorizontalStrut(10));
        box.add(title);
        box.add(Box.createHorizontalGlue());
        box.add(Box.createHorizontalStrut(3));
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(gview, BorderLayout.CENTER);
        panel.add(box, BorderLayout.SOUTH);
        
        Color BACKGROUND = Color.WHITE;
        Color FOREGROUND = Color.DARK_GRAY;
        UILib.setColor(panel, BACKGROUND, FOREGROUND);
        
        return panel;
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * Switch the root of the tree by requesting a new spanning tree
     * at the desired root
     */
    public static class TreeRootAction extends GroupAction {
        public TreeRootAction(String graphGroup) {
            super(graphGroup);
        }
        public void run(double frac) {
            TupleSet focus = m_vis.getGroup(Visualization.FOCUS_ITEMS);
            if ( focus==null || focus.getTupleCount() == 0 ) return;
            
            Graph g = (Graph)m_vis.getGroup(m_group);
            Node f = null;
            while ( !g.containsTuple(f=(Node)focus.tuples().next()) ) {
                f = null;
            }
            if ( f == null ) return;
            g.getSpanningTree(f);
        }
    }
    
    /**
     * Set node fill colors
     */
    public static class NodeColorAction extends ColorAction {
        public NodeColorAction(String group) {
            super(group, VisualItem.FILLCOLOR, ColorLib.rgba(255,255,255,0));
            add("_hover", ColorLib.gray(220,230));
            add("ingroup('_search_')", ColorLib.rgb(255,190,190));
            add("ingroup('_focus_')", ColorLib.rgb(198,229,229));
        }
                
    } // end of inner class NodeColorAction
    
    /**
     * Set node text colors
     */
    public static class TextColorAction extends ColorAction {
        public TextColorAction(String group) {
            super(group, VisualItem.TEXTCOLOR, ColorLib.gray(0));
            add("_hover", ColorLib.rgb(255,0,0));
        }
    } // end of inner class TextColorAction
    
    
} // end of class RadialGraphView
