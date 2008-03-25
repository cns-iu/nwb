package edu.iu.iv.toolkits.networkanalysistoolkit.gui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import edu.iu.iv.toolkits.networkanalysistoolkit.analysis.Network;
import edu.iu.iv.toolkits.networkanalysistoolkit.util.Util;

/**
 * This code was generated using CloudGarden's Jigloo SWT/Swing GUI Builder,
 * which is free for non-commercial use. If Jigloo is being used commercially
 * (ie, by a for-profit company or business) then you should purchase a license -
 * please visit www.cloudgarden.com for details.
 */
public class GraphTypeDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = 5769716278543315923L;

    public GraphTypeDialog(Frame arg0) throws HeadlessException {
        super(arg0);
        initGUI();
    }

    private JButton btnPrintToConsole;

    private JTextField tfNumLoops;

    private JTextField tfNumEdges;

    private JTextField tfNumNodes;

    private JLabel lblNumSelfLoops;

    private JLabel lblNumEdges;

    private JLabel lblNumNodes;

    private JPanel countsPanel;

    private JPanel predicatesPanel;

    private JTextField tfHasParallelEdges;

    private JLabel lblHasParallelEdges;

    private JTextField tfIsConnected;

    private JLabel lblIsConnected;

    private JTextField tfIsDirected;

    private JLabel lblIsDirected;

	/**
	* Initializes the GUI.
	* Auto-generated code - any changes you make will disappear.
	*/
    public void initGUI(){
		try {
			preInitGUI();
	
			predicatesPanel = new JPanel();
			lblIsDirected = new JLabel();
			tfIsDirected = new JTextField();
			lblIsConnected = new JLabel();
			tfIsConnected = new JTextField();
			lblHasParallelEdges = new JLabel();
			tfHasParallelEdges = new JTextField();
			countsPanel = new JPanel();
			lblNumNodes = new JLabel();
			lblNumEdges = new JLabel();
			lblNumSelfLoops = new JLabel();
			tfNumNodes = new JTextField();
			tfNumEdges = new JTextField();
			tfNumLoops = new JTextField();
			btnPrintToConsole = new JButton();
	
			GridBagLayout thisLayout = new GridBagLayout();
			this.getContentPane().setLayout(thisLayout);
			thisLayout.columnWidths = new int[] {1};
			thisLayout.rowHeights = new int[] {1,1,1};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.rowWeights = new double[] {0.1,0.1,0.1};
			this.setTitle("Graph Type");
			this.setSize(new java.awt.Dimension(201,357));
	
			GridBagLayout predicatesPanelLayout = new GridBagLayout();
			predicatesPanel.setLayout(predicatesPanelLayout);
			predicatesPanelLayout.columnWidths = new int[] {1,1};
			predicatesPanelLayout.rowHeights = new int[] {1,1,1};
			predicatesPanelLayout.columnWeights = new double[] {0.1,0.1};
			predicatesPanelLayout.rowWeights = new double[] {0.1,0.1,0.1};
			predicatesPanel.setAutoscrolls(true);
			predicatesPanel.setBorder(new TitledBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false), "Graph Type", TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font("Dialog",1,12), new java.awt.Color(0,0,0)));
			this.getContentPane().add(predicatesPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.4, 10, 1, new Insets(5, 5, 5, 5), 0, 0));
	
			lblIsDirected.setText("Directed");
			predicatesPanel.add(lblIsDirected, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 13, 0, new Insets(5, 5, 5, 5), 0, 0));
	
			tfIsDirected.setEnabled(false);
			predicatesPanel.add(tfIsDirected, new GridBagConstraints(1, 0, 1, 1, 0.6, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
	
			lblIsConnected.setText("Connected");
			predicatesPanel.add(lblIsConnected, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 13, 0, new Insets(5, 5, 5, 5), 0, 0));
	
			tfIsConnected.setEnabled(false);
			predicatesPanel.add(tfIsConnected, new GridBagConstraints(1, 1, 1, 1, 0.6, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
	
			lblHasParallelEdges.setText("Parallel Edges");
			predicatesPanel.add(lblHasParallelEdges, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 13, 0, new Insets(5, 5, 5, 5), 0, 0));
	
			tfHasParallelEdges.setEnabled(false);
			predicatesPanel.add(tfHasParallelEdges, new GridBagConstraints(1, 2, 1, 1, 0.6, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
	
			GridBagLayout countsPanelLayout = new GridBagLayout();
			countsPanel.setLayout(countsPanelLayout);
			countsPanelLayout.columnWidths = new int[] {1,1};
			countsPanelLayout.rowHeights = new int[] {1,1,1};
			countsPanelLayout.columnWeights = new double[] {0.1,0.1};
			countsPanelLayout.rowWeights = new double[] {0.1,0.1,0.1};
			countsPanel.setBorder(new TitledBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false), "Counts", TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font("Dialog",1,12), new java.awt.Color(0,0,0)));
			this.getContentPane().add(countsPanel, new GridBagConstraints(0, 1, 1, 1, 0.5, 0.4, 10, 1, new Insets(5, 5, 5, 5), 0, 0));
	
			lblNumNodes.setText("Number of Nodes");
			countsPanel.add(lblNumNodes, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
	
			lblNumEdges.setText("Number of Edges");
			countsPanel.add(lblNumEdges, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
	
			lblNumSelfLoops.setText("Number of Loops");
			countsPanel.add(lblNumSelfLoops, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
	
			tfNumNodes.setEnabled(false);
			countsPanel.add(tfNumNodes, new GridBagConstraints(1, 0, 1, 1, 0.6, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
	
			tfNumEdges.setEnabled(false);
			countsPanel.add(tfNumEdges, new GridBagConstraints(1, 1, 1, 1, 0.6, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
	
			tfNumLoops.setEnabled(false);
			countsPanel.add(tfNumLoops, new GridBagConstraints(1, 2, 1, 1, 0.6, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
	
			btnPrintToConsole.setText("Print To Console");
			this.getContentPane().add(btnPrintToConsole, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.2, 10, 2, new Insets(5, 5, 5, 5), 0, 0));
			btnPrintToConsole.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnPrintToConsoleActionPerformed(evt);
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
        // not used
    }

    private Network network;

    public void update(Network network) {
        this.network = network ;
        setGraphTypeValues();
        setGraphCounts();
    }

    private void setGraphTypeValues() {
        tfIsDirected.setText(Boolean.toString(network.isDirected()));
        tfIsConnected.setText(Boolean.toString(network.isConnected()));
        tfHasParallelEdges.setText(Boolean
                .toString(network.hasParallelEdges()));
    }

    private void setGraphCounts() {
        tfNumNodes.setText(Integer.toString(network.getNumVertices()));
        tfNumEdges.setText(Integer.toString(network.getNumEdges()));
        tfNumLoops.setText(Integer.toString(network.getNumLoops())) ;
    }

    /** Auto-generated event handler method */
    protected void btnPrintToConsoleActionPerformed(ActionEvent evt) {
        Util.println("Directed: " + tfIsDirected.getText());
        Util.println("Connected: " + tfIsConnected.getText());
        Util.println("Parallel Edges: " + tfHasParallelEdges.getText());
        Util.println("Number of Nodes: " + tfNumNodes.getText());
        Util.println("Number of Edges: " + tfNumEdges.getText());
        Util.println("Number of Loops: " + tfNumLoops.getText());
    }
}