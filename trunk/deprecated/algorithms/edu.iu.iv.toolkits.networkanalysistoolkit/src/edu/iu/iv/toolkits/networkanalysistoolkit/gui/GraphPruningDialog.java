package edu.iu.iv.toolkits.networkanalysistoolkit.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Dictionary;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import edu.iu.iv.toolkits.networkanalysistoolkit.analysis.GraphThresholder;
import edu.iu.iv.toolkits.networkanalysistoolkit.analysis.Network;
import edu.iu.iv.toolkits.networkanalysistoolkit.util.Util;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
/**
 * This code was generated using CloudGarden's Jigloo SWT/Swing GUI Builder,
 * which is free for non-commercial use. If Jigloo is being used commercially
 * (ie, by a for-profit company or business) then you should purchase a license -
 * please visit www.cloudgarden.com for details.
 */
public class GraphPruningDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = -1781862805372446740L;

    private JPanel unDirVertexDegreePanel;

    private JLabel lblInstructionsDir;

    private JLabel lblIinstructions;

    private JPanel dirVertexDegreePanel;

    private JButton btnFilterUndirectedGraph;

    private JTextField tfMaxDegree;

    private JTextField tfMinDegree;

    private JLabel lblMaxDegree;

    private JLabel lblMinDegree;

    private JButton btnFilterDirectedGraph;

    private JTextField tfOutDegreeMax;

    private JTextField tfOutDegreeMin;

    private JTextField tfInDegreeMax;

    private JTextField tfInDegreeMin;

    private JLabel lblMaxOutDegree;

    private JLabel lblMinOutDegree;

    private JLabel lblMaxInDegree;

    private JLabel lblMinInDegree;

    private JPanel tbUndirectedGraph;

    private JPanel tbDirectedGraph;

    private JTabbedPane containerTabbedPane;

    public GraphPruningDialog(Frame frame) {
        super(frame);
        initGUI();
    }

	/**
	* Initializes the GUI.
	* Auto-generated code - any changes you make will disappear.
	*/
    public void initGUI(){
		try {
			preInitGUI();
	
			containerTabbedPane = new JTabbedPane();
			tbDirectedGraph = new JPanel();
			btnFilterDirectedGraph = new JButton();
			dirVertexDegreePanel = new JPanel();
			lblMinInDegree = new JLabel();
			lblMaxInDegree = new JLabel();
			lblMinOutDegree = new JLabel();
			lblMaxOutDegree = new JLabel();
			tfInDegreeMin = new JTextField();
			tfInDegreeMax = new JTextField();
			tfOutDegreeMin = new JTextField();
			tfOutDegreeMax = new JTextField();
			lblIinstructions = new JLabel();
			tbUndirectedGraph = new JPanel();
			unDirVertexDegreePanel = new JPanel();
			lblMinDegree = new JLabel();
			lblMaxDegree = new JLabel();
			tfMinDegree = new JTextField();
			tfMaxDegree = new JTextField();
			lblInstructionsDir = new JLabel();
			btnFilterUndirectedGraph = new JButton();
	
			BorderLayout thisLayout = new BorderLayout();
			this.getContentPane().setLayout(thisLayout);
			thisLayout.setHgap(0);
			thisLayout.setVgap(0);
			this.setTitle("Pruning");
			this.setSize(new java.awt.Dimension(316,267));
	
			containerTabbedPane.setPreferredSize(new java.awt.Dimension(309,265));
			this.getContentPane().add(containerTabbedPane, BorderLayout.CENTER);
	
			GridBagLayout tbDirectedGraphLayout = new GridBagLayout();
			tbDirectedGraph.setLayout(tbDirectedGraphLayout);
			tbDirectedGraphLayout.columnWidths = new int[] {1};
			tbDirectedGraphLayout.rowHeights = new int[] {1,1,1};
			tbDirectedGraphLayout.columnWeights = new double[] {0.1};
			tbDirectedGraphLayout.rowWeights = new double[] {0.1,0.1,0.1};
			tbDirectedGraph.setPreferredSize(new java.awt.Dimension(304,242));
			containerTabbedPane.add(tbDirectedGraph);
			containerTabbedPane.setTitleAt(0, "Directed Graph");
	
			btnFilterDirectedGraph.setText("Filter Graph");
			tbDirectedGraph.add(btnFilterDirectedGraph, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, 17, 0, new Insets(0, 5, 0, 5), 0, 0));
			btnFilterDirectedGraph.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnFilterDirectedGraphActionPerformed(evt);
				}
			});
	
			GridBagLayout dirVertexDegreePanelLayout = new GridBagLayout();
			dirVertexDegreePanel.setLayout(dirVertexDegreePanelLayout);
			dirVertexDegreePanelLayout.columnWidths = new int[] {1,1};
			dirVertexDegreePanelLayout.rowHeights = new int[] {1,1,1,1,1};
			dirVertexDegreePanelLayout.columnWeights = new double[] {0.1,0.1};
			dirVertexDegreePanelLayout.rowWeights = new double[] {0.1,0.1,0.1,0.1,0.1};
			dirVertexDegreePanel.setBorder(new TitledBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false), "Vertex Degree", TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font("Dialog",1,12), new java.awt.Color(0,0,0)));
			tbDirectedGraph.add(dirVertexDegreePanel, new GridBagConstraints(0, 0, 1, 3, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
	
			lblMinInDegree.setText("In-Degree Minimum");
			dirVertexDegreePanel.add(lblMinInDegree, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 17, 0, new Insets(0, 5, 0, 5), 0, 0));
	
			lblMaxInDegree.setText("In-Degree Maximum");
			dirVertexDegreePanel.add(lblMaxInDegree, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 17, 0, new Insets(0, 5, 0, 5), 0, 0));
	
			lblMinOutDegree.setText("Out-Degree Minimum");
			dirVertexDegreePanel.add(lblMinOutDegree, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, 17, 0, new Insets(0, 5, 0, 5), 0, 0));
	
			lblMaxOutDegree.setText("Out-Degree Maximum");
			dirVertexDegreePanel.add(lblMaxOutDegree, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, 17, 0, new Insets(0, 5, 0, 5), 0, 0));
	
			tfInDegreeMin.setMinimumSize(new java.awt.Dimension(40,20));
			dirVertexDegreePanel.add(tfInDegreeMin, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
	
			tfInDegreeMax.setMinimumSize(new java.awt.Dimension(40,20));
			dirVertexDegreePanel.add(tfInDegreeMax, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
	
			tfOutDegreeMin.setMinimumSize(new java.awt.Dimension(40,20));
			dirVertexDegreePanel.add(tfOutDegreeMin, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
	
			tfOutDegreeMax.setMinimumSize(new java.awt.Dimension(40,20));
			dirVertexDegreePanel.add(tfOutDegreeMax, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
	
			lblIinstructions.setText("<html>Enter degree values by which to filter graph. Leave blank to use minimum or maximum value.</html>");
			lblIinstructions.setHorizontalAlignment(SwingConstants.LEADING);
			lblIinstructions.setHorizontalTextPosition(SwingConstants.TRAILING);
			lblIinstructions.setVerticalAlignment(SwingConstants.TOP);
			lblIinstructions.setVerticalTextPosition(SwingConstants.CENTER);
			lblIinstructions.setPreferredSize(new java.awt.Dimension(60,40));
			lblIinstructions.setMinimumSize(new java.awt.Dimension(60,40));
			dirVertexDegreePanel.add(lblIinstructions, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
	
			GridBagLayout tbUndirectedGraphLayout = new GridBagLayout();
			tbUndirectedGraph.setLayout(tbUndirectedGraphLayout);
			tbUndirectedGraphLayout.columnWidths = new int[] {1};
			tbUndirectedGraphLayout.rowHeights = new int[] {1,1,1};
			tbUndirectedGraphLayout.columnWeights = new double[] {0.1};
			tbUndirectedGraphLayout.rowWeights = new double[] {0.1,0.1,0.1};
			tbUndirectedGraph.setPreferredSize(new java.awt.Dimension(304,212));
			containerTabbedPane.add(tbUndirectedGraph);
			containerTabbedPane.setTitleAt(1, "Undirected Graph");
	
			GridBagLayout unDirVertexDegreePanelLayout = new GridBagLayout();
			unDirVertexDegreePanel.setLayout(unDirVertexDegreePanelLayout);
			unDirVertexDegreePanelLayout.columnWidths = new int[] {1,1};
			unDirVertexDegreePanelLayout.rowHeights = new int[] {1,1,1,1};
			unDirVertexDegreePanelLayout.columnWeights = new double[] {0.1,0.1};
			unDirVertexDegreePanelLayout.rowWeights = new double[] {0.1,0.1,0.1,0.1};
			unDirVertexDegreePanel.setBorder(new TitledBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false), "Vertex Degree", TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font("Dialog",1,12), new java.awt.Color(0,0,0)));
			tbUndirectedGraph.add(unDirVertexDegreePanel, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
	
			lblMinDegree.setText("Minimum Degree");
			unDirVertexDegreePanel.add(lblMinDegree, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 17, 0, new Insets(0, 5, 0, 5), 0, 0));
	
			lblMaxDegree.setText("Maximum Degree");
			unDirVertexDegreePanel.add(lblMaxDegree, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 17, 0, new Insets(0, 5, 0, 5), 0, 0));
	
			tfMinDegree.setMinimumSize(new java.awt.Dimension(40,20));
			unDirVertexDegreePanel.add(tfMinDegree, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
	
			tfMaxDegree.setMinimumSize(new java.awt.Dimension(40,20));
			unDirVertexDegreePanel.add(tfMaxDegree, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
	
			lblInstructionsDir.setText("<html>Enter degree values by which to filter graph. Leave blank to use minimum or maximum value.</html>");
			unDirVertexDegreePanel.add(lblInstructionsDir, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
	
			btnFilterUndirectedGraph.setText("Filter Graph");
			tbUndirectedGraph.add(btnFilterUndirectedGraph, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 17, 0, new Insets(5, 5, 5, 5), 0, 0));
			btnFilterUndirectedGraph.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnFilterUndirectedGraphActionPerformed(evt);
				}
			});
	
			postInitGUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    /** Add your pre-init code in here */
    public void preInitGUI() {
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE) ;
    }

    /** Add your post-init code in here */
    public void postInitGUI() {
        //      not used
    }

    private Network network ;
    public void update(Network network) {
        this.network = network ;
        if (network.isDirected()) {
            btnFilterUndirectedGraph.setEnabled(false) ;
            btnFilterUndirectedGraph.setToolTipText("Graph is directed.") ;
        }
        else {
            btnFilterDirectedGraph.setEnabled(false) ;
            btnFilterDirectedGraph.setToolTipText("Graph is not directed.") ;
        }
    }
    
    /** Auto-generated event handler method */
    protected void btnFilterDirectedGraphActionPerformed(ActionEvent evt) {
        if (!isDirectedPanelInputValid())
            return;
        final JDialog thisDialog = this;
        Thread t = new Thread() {
            public void run() {
                // make a copy of graph cuz we dun wanna modify the original
                // thing.
                Graph graph = (Graph)network.getGraph().copy();
                GraphThresholder gt = GraphThresholder.getNewInstance();
                graph = gt.getVertexThresholdedGraph(graph, didmin, didmax,
                        dodmin, dodmax);
                
                    BasicData dm = new BasicData(graph,Graph.class.getName());
                    Dictionary map = dm.getMetadata();
                    map.put(DataProperty.LABEL,"Vertex Thresholded Graph");
                    map.put(DataProperty.TYPE,DataProperty.NETWORK_TYPE);
                
            }
        };
        t.start();
    }

    private int didmin, didmax, dodmin, dodmax;

    private boolean isDirectedPanelInputValid() {
        boolean valid = false;
        final boolean INCLUSIVE = true;
        try {
            Util util = Util.getInstance();
            String s = tfInDegreeMin.getText();
            if (s != null && s.length() > 0)
                didmin = Integer.parseInt(s);
            else
                didmin = 0;
            if (!util.checkInteger(didmin, 0, Integer.MAX_VALUE, INCLUSIVE))
                throw new IllegalArgumentException(
                        "Min In-Degree value unacceptable.");

            s = tfInDegreeMax.getText();
            if (s != null && s.length() > 0)
                didmax = Integer.parseInt(s);
            else
                didmax = Integer.MAX_VALUE;
            if (!util.checkInteger(didmax, 0, Integer.MAX_VALUE, INCLUSIVE))
                throw new IllegalArgumentException(
                        "Max In-Degree value unacceptable.");

            s = tfOutDegreeMin.getText();
            if (s != null && s.length() > 0)
                dodmin = Integer.parseInt(s);
            else
                dodmin = 0;
            if (!util.checkInteger(dodmin, 0, Integer.MAX_VALUE, INCLUSIVE))
                throw new IllegalArgumentException(
                        "Min Out-Degree value unacceptable.");

            s = tfOutDegreeMax.getText();
            if (s != null && s.length() > 0)
                dodmax = Integer.parseInt(s);
            else
                dodmax = Integer.MAX_VALUE;
            if (!util.checkInteger(dodmax, 0, Integer.MAX_VALUE, INCLUSIVE))
                throw new IllegalArgumentException(
                        "Min In-Degree value unacceptable.");
            valid = true ;
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this,
                    "One or more arguments is invalid. Please check values.",
                    "Error!", JOptionPane.ERROR_MESSAGE);
            valid = false;
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Error!",
                    JOptionPane.ERROR_MESSAGE);
            valid = false;
        }
        return valid;
    }

    /** Auto-generated event handler method */
    protected void btnFilterUndirectedGraphActionPerformed(ActionEvent evt) {
        if (!isUndirectedPanelInputValid())
            return;
        final JDialog thisDialog = this;
        Thread t = new Thread() {
            public void run() {
                // make a copy of graph cuz we dun wanna modify the original
                // thing.
                Graph graph = (Graph) network.getGraph().copy();
                GraphThresholder gt = GraphThresholder.getNewInstance();
                graph = gt.getVertexThresholdedGraph((UndirectedGraph) graph,
                        udmin, udmax);
                
                    BasicData dm = new BasicData(graph,Graph.class.getName());
                    Dictionary map = dm.getMetadata();
                    map.put(DataProperty.LABEL,"Vertex Thresholded Graph");
                    map.put(DataProperty.TYPE,DataProperty.NETWORK_TYPE);
                    
                
            }
        };
        t.start();
    }

    private int udmin, udmax;

    private boolean isUndirectedPanelInputValid() {
        boolean valid = false;
        try {
            Util util = Util.getInstance();
            String s = tfMinDegree.getText();
            if (s == null || s.length() == 0)
                udmin = 0;
            else
                udmin = Integer.parseInt(s);
            boolean INCLUSIVE = true;
            if (!util.checkInteger(udmin, 0, Integer.MAX_VALUE, INCLUSIVE))
                throw new IllegalArgumentException("Min Degree is invalid!");

            s = tfMaxDegree.getText();
            if (s == null || s.length() == 0)
                udmax = Integer.MAX_VALUE;
            else
                udmax = Integer.parseInt(s);
            if (!util.checkInteger(udmax, 0, Integer.MAX_VALUE, INCLUSIVE))
                throw new IllegalArgumentException("Max Degree is invalid!");
            valid = true ;
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this,
                    "One or more parameters is invalid. Please check values.",
                    "Error!", JOptionPane.ERROR_MESSAGE);
            valid = false ;
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Error!",
                    JOptionPane.ERROR_MESSAGE);
            valid = false ;
        }
        return valid;
    }
}