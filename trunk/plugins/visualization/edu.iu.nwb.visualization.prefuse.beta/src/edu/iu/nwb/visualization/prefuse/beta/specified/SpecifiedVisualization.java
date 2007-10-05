package edu.iu.nwb.visualization.prefuse.beta.specified;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.util.Dictionary;
import java.util.Iterator;

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
import edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaVisualization;
import edu.iu.nwb.visualization.prefuse.beta.radialgraph.RadialGraphVisualization;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.SizeAction;
import prefuse.action.filter.GraphDistanceFilter;
import prefuse.action.layout.SpecifiedLayout;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.expression.ColumnExpression;
import prefuse.data.io.GraphMLReader;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
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

/**
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class SpecifiedVisualization extends JPanel implements PrefuseBetaVisualization {

    private static final String graph = "graph";
    private static final String nodes = "graph.nodes";
    private static final String edges = "graph.edges";

    private Visualization m_vis;
	private Dictionary parameters;
    
    public SpecifiedVisualization(Graph g, String label, Dictionary parameters) {
        this.parameters = parameters;
        // create a new, empty visualization for our data
        m_vis = new Visualization();
        
        // --------------------------------------------------------------------
        // set up the renderers
        
        LabelRenderer tr = new LabelRenderer();
        tr.setRoundedCorner(8, 8);
        m_vis.setRendererFactory(new DefaultRendererFactory(tr));
        
        String xLabel = (String) parameters.get(Constants.x);
		String yLabel = (String) parameters.get(Constants.y);
		
		//moderately bad, but necessary, hack
		
		class ToDoubleExpression extends ColumnExpression {
			ToDoubleExpression(String column) {
				super(column);
			}
			
			public double getDouble(Tuple t) {
				if(super.getType(t.getSchema()) == double.class) {
					return super.getDouble(t);
				}
				double value = Double.parseDouble((String) super.get(t));
				return value;
			}
			public Class getType(Schema s) {
				return double.class;
			}
		}
		
		g.addColumn(Constants._x, new ToDoubleExpression(xLabel));
		g.addColumn(Constants._y, new ToDoubleExpression(yLabel));
		
		
		
        
        
        // --------------------------------------------------------------------
        // register the data with a visualization
        
        // adds graph to visualization and sets renderer label field
        setGraph(g, label);
        
        
        
        
        SizeAction sizeAction = new SizeAction() {
        	public double getSize(VisualItem item) {
        		double scale = m_vis.getDisplay(0).getScale();
        		if(scale <= 1) {
        			return 1;
        		} else {
        			return 1 / scale;
        		}
        	}
        };
        m_vis.putAction("size", sizeAction);
        
        
        // --------------------------------------------------------------------
        // create actions to process the visual data

        

        ColorAction fill = new ColorAction(nodes, 
                VisualItem.FILLCOLOR, ColorLib.rgb(200,200,255));
        //fill.add(VisualItem.FIXED, ColorLib.rgb(255,100,100));
        fill.add(VisualItem.HIGHLIGHT, ColorLib.rgb(255,200,125));
        
        ActionList draw = new ActionList();
        draw.add(fill);
        draw.add(new ColorAction(nodes, VisualItem.STROKECOLOR, 0));
        draw.add(new ColorAction(nodes, VisualItem.TEXTCOLOR, ColorLib.rgb(0,0,0)));
        draw.add(new ColorAction(edges, VisualItem.FILLCOLOR, ColorLib.gray(200)));
        draw.add(new ColorAction(edges, VisualItem.STROKECOLOR, ColorLib.gray(200)));
        
        ActionList animate = new ActionList(1500);
        
        
		
		animate.add(new SpecifiedLayout(graph, Constants._x, Constants._y));
        //animate.add(new ForceDirectedLayout(graph));
		animate.add(fill);
        animate.add(new RepaintAction());
        
        // finally, we register our ActionList with the Visualization.
        // we can later execute our Actions by invoking a method on our
        // Visualization, using the name we've chosen below.
        m_vis.putAction("draw", draw);
        m_vis.putAction("layout", animate);

        m_vis.runAfter("draw", "layout");
        
        
        // --------------------------------------------------------------------
        // set up a display to show the visualization
        
        Display display = new Display(m_vis);
        display.setSize(700,700);
        //display.pan(350, 350);
        display.setForeground(Color.GRAY);
        display.setBackground(Color.WHITE);
        
        // main display controls
        display.addControlListener(new FocusControl(1));
        display.addControlListener(new DragControl());
        display.addControlListener(new PanControl());
        display.addControlListener(new ZoomControl() {
        	public void mouseDragged(MouseEvent event) {
        		m_vis.run("size");
        		super.mouseDragged(event);
        	}
        });
        display.addControlListener(new WheelZoomControl());
        display.addControlListener(new ZoomToFitControl() {
        	public void mouseClicked(MouseEvent event) {
        		m_vis.run("size");
        		super.mouseClicked(event);
        	}
        });
        display.addControlListener(new NeighborHighlightControl());

        // overview display
//        Display overview = new Display(vis);
//        overview.setSize(290,290);
//        overview.addItemBoundsListener(new FitOverviewListener());
        
        display.setForeground(Color.GRAY);
        display.setBackground(Color.WHITE);
        
        // --------------------------------------------------------------------        
        // launch the visualization
        
        
        
        
        

       
        
        // now we run our action list
        m_vis.run("draw");
        
        add(display);
    }
    
    public SpecifiedVisualization() {
		// TODO Auto-generated constructor stub
	}

	public void setGraph(Graph g, String label) {
        // update labeling
        DefaultRendererFactory drf = (DefaultRendererFactory)
                                                m_vis.getRendererFactory();
        ((LabelRenderer)drf.getDefaultRenderer()).setTextField(label);
        
        // update graph
        m_vis.removeGroup(graph);
        VisualGraph vg = m_vis.addGraph(graph, g);
        m_vis.setValue(edges, null, VisualItem.INTERACTIVE, Boolean.FALSE);
        VisualItem f = (VisualItem)vg.getNode(0);
        m_vis.getGroup(Visualization.FOCUS_ITEMS).setTuple(f);
        f.setFixed(false);
    }
    
    // ------------------------------------------------------------------------
    // Main and demo methods
    
    public Graph create(Graph graph, Dictionary parameters) {
        UILib.setPlatformLookAndFeel();
        
        // create graphview
        
        String label = (String) parameters.get(Constants.label);
		JFrame frame = demo(graph, label, parameters);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		return null;
    }
    
    
    
    public JFrame demo(Graph g, String label, Dictionary parameters) {
        final SpecifiedVisualization view = new SpecifiedVisualization(g, label, parameters);
        
        // set up menu
        /* JMenu dataMenu = new JMenu("Data");
        dataMenu.add(new OpenGraphAction(view));
        dataMenu.add(new GraphMenuAction("Grid","ctrl 1",view) {
            protected Graph getGraph() {
                return GraphLib.getGrid(15,15);
            }
        });
        dataMenu.add(new GraphMenuAction("Clique","ctrl 2",view) {
            protected Graph getGraph() {
                return GraphLib.getClique(10);
            }
        });
        dataMenu.add(new GraphMenuAction("Honeycomb","ctrl 3",view) {
            protected Graph getGraph() {
                return GraphLib.getHoneycomb(5);
            }
        });
        dataMenu.add(new GraphMenuAction("Balanced Tree","ctrl 4",view) {
            protected Graph getGraph() {
                return GraphLib.getBalancedTree(3,5);
            }
        });
        dataMenu.add(new GraphMenuAction("Diamond Tree","ctrl 5",view) {
            protected Graph getGraph() {
                return GraphLib.getDiamondTree(3,3,3);
            }
        });
        JMenuBar menubar = new JMenuBar();
        menubar.add(dataMenu);
        */
        // launch window
        JFrame frame = new JFrame("Pre-defined Positions (prefuse beta)");
        //frame.setJMenuBar(menubar);
        frame.setContentPane(view);
        frame.pack();
        frame.setVisible(true);
        
        frame.addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                view.m_vis.run("layout");
            }
            public void windowDeactivated(WindowEvent e) {
                view.m_vis.cancel("layout");
            }
        });
        
        return frame;
    }
    
    
    // ------------------------------------------------------------------------
    
    /**
     * Swing menu action that loads a graph into the graph viewer.
     */
    public abstract static class GraphMenuAction extends AbstractAction {
        private SpecifiedVisualization m_view;
        public GraphMenuAction(String name, String accel, SpecifiedVisualization view) {
            m_view = view;
            this.putValue(AbstractAction.NAME, name);
            this.putValue(AbstractAction.ACCELERATOR_KEY,
                          KeyStroke.getKeyStroke(accel));
        }
        public void actionPerformed(ActionEvent e) {
            m_view.setGraph(getGraph(), "label");
        }
        protected abstract Graph getGraph();
    }
    
    public static class OpenGraphAction extends AbstractAction {
        private SpecifiedVisualization m_view;

        public OpenGraphAction(SpecifiedVisualization view) {
            m_view = view;
            this.putValue(AbstractAction.NAME, "Open File...");
            this.putValue(AbstractAction.ACCELERATOR_KEY,
                          KeyStroke.getKeyStroke("ctrl O"));
        }
        public void actionPerformed(ActionEvent e) {
            Graph g = IOLib.getGraphFile(m_view);
            if ( g == null ) return;
            String label = getLabel(m_view, g);
            if ( label != null ) {
                m_view.setGraph(g, label);
            }
        }
        public static String getLabel(Component c, Graph g) {
            // get the column names
            Table t = g.getNodeTable();
            int  cc = t.getColumnCount();
            String[] names = new String[cc];
            for ( int i=0; i<cc; ++i )
                names[i] = t.getColumnName(i);
            
            // where to store the result
            final String[] label = new String[1];

            // -- build the dialog -----
            // we need to get the enclosing frame first
            while ( c != null && !(c instanceof JFrame) ) {
                c = c.getParent();
            }
            final JDialog dialog = new JDialog(
                    (JFrame)c, "Choose Label Field", true);
            
            // create the ok/cancel buttons
            final JButton ok = new JButton("OK");
            ok.setEnabled(false);
            ok.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                   dialog.setVisible(false);
               }
            });
            JButton cancel = new JButton("Cancel");
            cancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    label[0] = null;
                    dialog.setVisible(false);
                }
            });
            
            // build the selection list
            final JList list = new JList(names);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.getSelectionModel().addListSelectionListener(
            new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    int sel = list.getSelectedIndex(); 
                    if ( sel >= 0 ) {
                        ok.setEnabled(true);
                        label[0] = (String)list.getModel().getElementAt(sel);
                    } else {
                        ok.setEnabled(false);
                        label[0] = null;
                    }
                }
            });
            JScrollPane scrollList = new JScrollPane(list);
            
            JLabel title = new JLabel("Choose a field to use for node labels:");
            
            // layout the buttons
            Box bbox = new Box(BoxLayout.X_AXIS);
            bbox.add(Box.createHorizontalStrut(5));
            bbox.add(Box.createHorizontalGlue());
            bbox.add(ok);
            bbox.add(Box.createHorizontalStrut(5));
            bbox.add(cancel);
            bbox.add(Box.createHorizontalStrut(5));
            
            // put everything into a panel
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(title, BorderLayout.NORTH);
            panel.add(scrollList, BorderLayout.CENTER);
            panel.add(bbox, BorderLayout.SOUTH);
            panel.setBorder(BorderFactory.createEmptyBorder(5,2,2,2));
            
            // show the dialog
            dialog.setContentPane(panel);
            dialog.pack();
            dialog.setLocationRelativeTo(c);
            dialog.setVisible(true);
            dialog.dispose();
            
            // return the label field selection
            return label[0];
        }
    }
    
    public static class FitOverviewListener implements ItemBoundsListener {
        private Rectangle2D m_bounds = new Rectangle2D.Double();
        private Rectangle2D m_temp = new Rectangle2D.Double();
        private double m_d = 15;
        public void itemBoundsChanged(Display d) {
            d.getItemBounds(m_temp);
            GraphicsLib.expand(m_temp, 25/d.getScale());
            
            double dd = m_d/d.getScale();
            double xd = Math.abs(m_temp.getMinX()-m_bounds.getMinX());
            double yd = Math.abs(m_temp.getMinY()-m_bounds.getMinY());
            double wd = Math.abs(m_temp.getWidth()-m_bounds.getWidth());
            double hd = Math.abs(m_temp.getHeight()-m_bounds.getHeight());
            if ( xd>dd || yd>dd || wd>dd || hd>dd ) {
                m_bounds.setFrame(m_temp);
                DisplayLib.fitViewToBounds(d, m_bounds, 0);
            }
        }
    }
    
} // end of class GraphView
