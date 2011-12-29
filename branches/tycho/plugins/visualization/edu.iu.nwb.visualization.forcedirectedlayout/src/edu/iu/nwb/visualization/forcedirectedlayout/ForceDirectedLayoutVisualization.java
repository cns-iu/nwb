package edu.iu.nwb.visualization.forcedirectedlayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.RepaintAction;
import edu.berkeley.guir.prefuse.action.assignment.ColorFunction;
import edu.berkeley.guir.prefuse.action.filter.GraphFilter;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.activity.Activity;
import edu.berkeley.guir.prefuse.event.ControlAdapter;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.render.DefaultEdgeRenderer;
import edu.berkeley.guir.prefuse.render.DefaultRendererFactory;
import edu.berkeley.guir.prefuse.render.TextItemRenderer;
import edu.berkeley.guir.prefusex.controls.DragControl;
import edu.berkeley.guir.prefusex.controls.NeighborHighlightControl;
import edu.berkeley.guir.prefusex.controls.PanControl;
import edu.berkeley.guir.prefusex.controls.ZoomControl;
import edu.berkeley.guir.prefusex.force.ForcePanel;
import edu.berkeley.guir.prefusex.force.ForceSimulator;
import edu.berkeley.guir.prefusex.layout.ForceDirectedLayout;

/**
 * graph visualization using an interactive force-based layout.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
// modified Shashikant Penumarthy for IVC
public class ForceDirectedLayoutVisualization extends Display {

    private static final long serialVersionUID = 4753295708022893746L;
    private JFrame     frame;
    private ForcePanel fpanel;
    
    private ForceSimulator m_fsim;
    private String         m_textField;
    private ItemRegistry   m_registry;
    private Activity       m_actionList;
    
    /**
     * Constructor that takes a prefuse graph and a Force Simulator with
     * values for the coefficients already set and initializes the demo.
     * Assumes that the label field would be displayed.
     * 
     * @param g The graph to apply the force directed layout to.
     * @param fsim The ForceSimulator.
     */
    public ForceDirectedLayoutVisualization(Graph g, ForceSimulator fsim) {
        this(g, fsim, "label");
    } //
    
    /**
     * Another constructor that needs to be given the textfield to
     * display.
     *
     * @param g The graph to apply this force directed layout to.
     * @param fsim The ForceSimulator
     * @param textField The field to display.
     */
    public ForceDirectedLayoutVisualization(Graph g, ForceSimulator fsim, String textField) {
        // set up component first
        m_fsim = fsim;
        m_textField = textField;
        m_registry = new ItemRegistry(g);
        this.setItemRegistry(m_registry);
        initRenderers();
        m_actionList = initActionList();
        setSize(700,700);
        pan(350,350);
        this.addControlListener(new NeighborHighlightControl());
        this.addControlListener(new DragControl(false));
        this.addControlListener(new MouseOverControl());
        this.addControlListener(new PanControl(false));
        this.addControlListener(new ZoomControl(false));
    } //
    
    public void show() {
        // now set up application window
        fpanel = new ForcePanel(m_fsim);        
        frame = new JFrame("Force Simulator");
        Container c = frame.getContentPane();
        c.setLayout(new BorderLayout());
        c.add(this, BorderLayout.CENTER);
        c.add(fpanel, BorderLayout.EAST);
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE) ;
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                Dimension d = frame.getSize();
                Dimension p = fpanel.getSize();
                Insets in = frame.getInsets();
                ForceDirectedLayoutVisualization.this.setSize(d.width-in.left-in.right-p.width,
                        d.height-in.top-in.bottom);
            } //
            
        });
        frame.pack();
        frame.setVisible(true);
        
        // start force simulation
        m_actionList.runNow();
    } //
    
    private void initRenderers() {
        TextItemRenderer    nodeRenderer = new TextItemRenderer();
        nodeRenderer.setRenderType(TextItemRenderer.RENDER_TYPE_FILL);
        nodeRenderer.setRoundedCorner(8,8);
        nodeRenderer.setTextAttributeName(m_textField);
//        DefaultNodeRenderer nRenderer = new DefaultNodeRenderer();
        DefaultEdgeRenderer edgeRenderer = new DefaultEdgeRenderer();    
        m_registry.setRendererFactory(new DefaultRendererFactory(
                nodeRenderer, edgeRenderer, null));
    } //
    
    private ActionList initActionList() {
        ActionList actionList = new ActionList(m_registry,-1,20);
        actionList.add(new GraphFilter());
        actionList.add(new ForceDirectedLayout(m_fsim, false, false));
        actionList.add(new DemoColorFunction());
        actionList.add(new RepaintAction());
        return actionList;
    } //

    public class DemoColorFunction extends ColorFunction {
        private Color pastelRed = new Color(255,125,125);
        private Color pastelOrange = new Color(255,200,125);
        private Color lightGray = new Color(220,220,255);
        public Paint getColor(VisualItem item) {
            if ( item instanceof EdgeItem ) {
                if ( item.isHighlighted() )
                    return pastelOrange;
                else
                    return Color.LIGHT_GRAY;
            } else {
                return Color.BLACK;
            }
        } //
        public Paint getFillColor(VisualItem item) {
            if ( item.isHighlighted() )
                return pastelOrange;
            else if ( item instanceof NodeItem ) {
                if ( item.isFixed() )
                    return pastelRed;
                else
                    return lightGray;
            } else {
                return Color.BLACK;
            }
        } //        
    } //
    
    /**
     * Tags and fixes the node under the mouse pointer.
     */
    public class MouseOverControl extends ControlAdapter {
        
        public void itemEntered(VisualItem item, MouseEvent e) {
            ((Display)e.getSource()).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            item.setFixed(true);
        } //
        
        public void itemExited(VisualItem item, MouseEvent e) {
            ((Display)e.getSource()).setCursor(Cursor.getDefaultCursor());
            item.setFixed(false);
        } //
        
        public void itemReleased(VisualItem item, MouseEvent e) {
            item.setFixed(false);
        } //
        
    } // end of inner class FocusControl
    
} // end of class ForceDirectedLayoutVisualization
