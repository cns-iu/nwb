package edu.iu.iv.toolkits.networkanalysistoolkit.gui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.DebugGraphics;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import edu.iu.iv.toolkits.networkanalysistoolkit.analysis.ClusteringCoefficients;
import edu.iu.iv.toolkits.networkanalysistoolkit.analysis.Network;
import edu.iu.iv.toolkits.networkanalysistoolkit.util.Util;

/**
* This code was generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a
* for-profit company or business) then you should purchase
* a license - please visit www.cloudgarden.com for details.
*/
public class ClusteringCoefficientsDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = 1683191202452233429L;
    private JCheckBox cbDirectedSelectAll;
	private JButton btnGeneralCoeffPrintToConsole;
	private JButton btnDirectedCoeffPrintToConsole;
	private JButton btnComputeDirectedCoeffs;
	private JTextField tfTriangleCoeff;
	private JTextField tfMutualConnectionCoeff;
	private JTextField tfOutgoingCC;
	private JTextField tfIncomingCC;
	private JCheckBox cbTriangleCoeff;
	private JCheckBox cbMutualConnectionCoeff;
	private JCheckBox cbOutgoingCC;
	private JCheckBox cbIncomingCC;
	private JPanel directedPanel;
	private JButton btnComputeGeneralCC;
	private ButtonGroup btnGroupPendantValue;
	private JRadioButton rbPendantValueOne;
	private JRadioButton rbPendantValueZero;
	private JLabel lblPendantValue;
	private JTextField tfClustCoeffMN;
	private JTextField tfClustCoeffWS;
	private JCheckBox cbClustCoeffMN;
	private JCheckBox cbClustCoeffWS;
	private JPanel generalClustCoeffPanel;
	public ClusteringCoefficientsDialog(Frame frame) {
		initGUI();
	}

	/**
	* Initializes the GUI.
	* Auto-generated code - any changes you make will disappear.
	*/
	public void initGUI(){
		try {
			preInitGUI();
			btnGroupPendantValue = new ButtonGroup();
	
	
			generalClustCoeffPanel = new JPanel();
			cbClustCoeffWS = new JCheckBox();
			cbClustCoeffMN = new JCheckBox();
			tfClustCoeffWS = new JTextField();
			tfClustCoeffMN = new JTextField();
			lblPendantValue = new JLabel();
			rbPendantValueZero = new JRadioButton();
			rbPendantValueOne = new JRadioButton();
			btnComputeGeneralCC = new JButton();
			btnGeneralCoeffPrintToConsole = new JButton();
			directedPanel = new JPanel();
			cbIncomingCC = new JCheckBox();
			cbOutgoingCC = new JCheckBox();
			cbMutualConnectionCoeff = new JCheckBox();
			cbTriangleCoeff = new JCheckBox();
			tfIncomingCC = new JTextField();
			tfOutgoingCC = new JTextField();
			tfMutualConnectionCoeff = new JTextField();
			tfTriangleCoeff = new JTextField();
			btnComputeDirectedCoeffs = new JButton();
			btnDirectedCoeffPrintToConsole = new JButton();
			cbDirectedSelectAll = new JCheckBox();
	
			GridBagLayout thisLayout = new GridBagLayout();
			this.getContentPane().setLayout(thisLayout);
			thisLayout.columnWidths = new int[] {1};
			thisLayout.rowHeights = new int[] {1,1};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.rowWeights = new double[] {0.1,0.1};
			this.setTitle("Clustering Coefficients");
			this.setSize(new java.awt.Dimension(382,362));
	
			GridBagLayout generalClustCoeffPanelLayout = new GridBagLayout();
			generalClustCoeffPanel.setLayout(generalClustCoeffPanelLayout);
			generalClustCoeffPanelLayout.columnWidths = new int[] {1,1,1,1};
			generalClustCoeffPanelLayout.rowHeights = new int[] {1,1,1,1,1};
			generalClustCoeffPanelLayout.columnWeights = new double[] {0.1,0.1,0.1,0.1};
			generalClustCoeffPanelLayout.rowWeights = new double[] {0.1,0.1,0.1,0.1,0.1};
			generalClustCoeffPanel.setBorder(new TitledBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false), "General", TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font("Dialog",1,12), new java.awt.Color(0,0,0)));
			this.getContentPane().add(generalClustCoeffPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.3, 10, 1, new Insets(5, 5, 5, 5), 0, 0));
	
			cbClustCoeffWS.setText("Clustering Coefficient (WS)");
            cbClustCoeffWS.setToolTipText("After Watts-Strogatz See:\n http://ivc.sourceforge.net/clustering/clustering-coefficient.html");
			generalClustCoeffPanel.add(cbClustCoeffWS, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 17, 1, new Insets(0, 10, 0, 0), 0, 0));
	
			cbClustCoeffMN.setText("Clustering Coefficient (N)");
            cbClustCoeffMN.setToolTipText("After Newman. See:\n http://ivc.sourceforge.net/clustering/clustering-coefficient.html");
			generalClustCoeffPanel.add(cbClustCoeffMN, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 17, 1, new Insets(0, 10, 0, 0), 0, 0));
	
			tfClustCoeffWS.setEditable(false);
			tfClustCoeffWS.setEnabled(true);
			tfClustCoeffWS.setPreferredSize(new java.awt.Dimension(40,20));
			tfClustCoeffWS.setMinimumSize(new java.awt.Dimension(40,20));
			generalClustCoeffPanel.add(tfClustCoeffWS, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
	
			tfClustCoeffMN.setEditable(false);
			tfClustCoeffMN.setEnabled(true);
			tfClustCoeffMN.setPreferredSize(new java.awt.Dimension(40,20));
			tfClustCoeffMN.setMinimumSize(new java.awt.Dimension(40,20));
			tfClustCoeffMN.setDebugGraphicsOptions(DebugGraphics.BUFFERED_OPTION);
			generalClustCoeffPanel.add(tfClustCoeffMN, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
	
			lblPendantValue.setText("For Pendants Use Value");
			generalClustCoeffPanel.add(lblPendantValue, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 17, 0, new Insets(0, 10, 0, 0), 0, 0));
	
			rbPendantValueZero.setText("0");
			rbPendantValueZero.setSelected(true);
			btnGroupPendantValue.add(rbPendantValueZero);
			generalClustCoeffPanel.add(rbPendantValueZero, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
	
			rbPendantValueOne.setText("1");
			btnGroupPendantValue.add(rbPendantValueOne);
			generalClustCoeffPanel.add(rbPendantValueOne, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
	
			btnComputeGeneralCC.setText("Compute");
			generalClustCoeffPanel.add(btnComputeGeneralCC, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, 17, 0, new Insets(0, 10, 0, 0), 0, 0));
			btnComputeGeneralCC.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnComputeGeneralCCActionPerformed(evt);
				}
			});
	
			btnGeneralCoeffPrintToConsole.setText("Print To Console");
			generalClustCoeffPanel.add(btnGeneralCoeffPrintToConsole, new GridBagConstraints(1, 4, 2, 1, 0.0, 0.0, 17, 0, new Insets(0, 10, 0, 0), 0, 0));
			btnGeneralCoeffPrintToConsole.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnGeneralCoeffPrintToConsoleActionPerformed(evt);
				}
			});
	
			GridBagLayout directedPanelLayout = new GridBagLayout();
			directedPanel.setLayout(directedPanelLayout);
			directedPanelLayout.columnWidths = new int[] {1,1,1,1};
			directedPanelLayout.rowHeights = new int[] {1,1,1,1,1,1};
			directedPanelLayout.columnWeights = new double[] {0.1,0.1,0.1,0.1};
			directedPanelLayout.rowWeights = new double[] {0.1,0.1,0.1,0.1,0.1,0.1};
			directedPanel.setBorder(new TitledBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false), "Directed Graphs", TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font("Dialog",1,12), new java.awt.Color(0,0,0)));
			this.getContentPane().add(directedPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.7, 10, 1, new Insets(0, 5, 0, 5), 0, 0));
	
			cbIncomingCC.setText("Incoming Clustering Coefficient");
			directedPanel.add(cbIncomingCC, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 17, 0, new Insets(0, 10, 0, 0), 0, 0));
	
			cbOutgoingCC.setText("Outgoing Clustering Coefficient");
			directedPanel.add(cbOutgoingCC, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 17, 0, new Insets(0, 10, 0, 0), 0, 0));
	
			cbMutualConnectionCoeff.setText("Mutual Connection Coefficient");
			directedPanel.add(cbMutualConnectionCoeff, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 17, 0, new Insets(0, 10, 0, 0), 0, 0));
	
			cbTriangleCoeff.setText("Triangle Coefficient");
			directedPanel.add(cbTriangleCoeff, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, 17, 0, new Insets(0, 10, 0, 0), 0, 0));
	
			tfIncomingCC.setEditable(false);
			tfIncomingCC.setEnabled(true);
			tfIncomingCC.setPreferredSize(new java.awt.Dimension(40,20));
			tfIncomingCC.setMinimumSize(new java.awt.Dimension(40,20));
			directedPanel.add(tfIncomingCC, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
	
			tfOutgoingCC.setEditable(false);
			tfOutgoingCC.setEnabled(true);
			tfOutgoingCC.setPreferredSize(new java.awt.Dimension(40,20));
			tfOutgoingCC.setMinimumSize(new java.awt.Dimension(40,20));
			directedPanel.add(tfOutgoingCC, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
	
			tfMutualConnectionCoeff.setEditable(false);
			tfMutualConnectionCoeff.setEnabled(true);
			tfMutualConnectionCoeff.setPreferredSize(new java.awt.Dimension(40,20));
			tfMutualConnectionCoeff.setMinimumSize(new java.awt.Dimension(40,20));
			directedPanel.add(tfMutualConnectionCoeff, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
	
			tfTriangleCoeff.setEditable(false);
			tfTriangleCoeff.setEnabled(true);
			tfTriangleCoeff.setPreferredSize(new java.awt.Dimension(40,20));
			tfTriangleCoeff.setMinimumSize(new java.awt.Dimension(40,20));
			directedPanel.add(tfTriangleCoeff, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
	
			btnComputeDirectedCoeffs.setText("Compute");
			directedPanel.add(btnComputeDirectedCoeffs, new GridBagConstraints(0, 5, 1, 0, 0.0, 0.0, 17, 0, new Insets(0, 10, 0, 0), 0, 0));
			btnComputeDirectedCoeffs.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnComputeDirectedCoeffsActionPerformed(evt);
				}
			});
	
			btnDirectedCoeffPrintToConsole.setText("Print To Console");
			directedPanel.add(btnDirectedCoeffPrintToConsole, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, 17, 0, new Insets(0, 10, 0, 0), 0, 0));
			btnDirectedCoeffPrintToConsole.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnDirectedCoeffPrintToConsoleActionPerformed(evt);
				}
			});
	
			cbDirectedSelectAll.setText("Select All");
			directedPanel.add(cbDirectedSelectAll, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, 17, 0, new Insets(0, 10, 0, 0), 0, 0));
			cbDirectedSelectAll.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					cbDirectedSelectAllActionPerformed(evt);
				}
			});
	
			postInitGUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/** Add your pre-init code in here 	*/
	public void preInitGUI(){
	    this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE) ;
	}

	/** Add your post-init code in here 	*/
	public void postInitGUI(){
//	  not used
	}
	
	private Network network ;
	public void update(Network network) {
	    this.network = network ;
	    if (!this.network.isDirected()) {
	        btnComputeDirectedCoeffs.setEnabled(false) ;
	        btnComputeDirectedCoeffs.setToolTipText("Graph is not directed.") ;
	    }
	}

	/** Auto-generated event handler method */
	protected void cbDirectedSelectAllActionPerformed(ActionEvent evt){
		boolean sel = cbDirectedSelectAll.isSelected() ;
		cbIncomingCC.setSelected(sel) ;
		cbOutgoingCC.setSelected(sel) ;
		cbMutualConnectionCoeff.setSelected(sel) ;
		cbTriangleCoeff.setSelected(sel) ;
	}

	/** Auto-generated event handler method */
	protected void btnComputeGeneralCCActionPerformed(ActionEvent evt){
		Thread t = new Thread() {
		    public void run() {
		        btnComputeGeneralCC.setEnabled(false) ;
		        Util util = Util.getInstance() ;
		        ClusteringCoefficients cc = ClusteringCoefficients.getNewInstance() ;
		        if (cbClustCoeffWS.isSelected()) {
		            int pendantValue = rbPendantValueOne.isSelected()? 0:1 ;
		            tfClustCoeffWS.setText(util.formatDoubleToString(cc.getClusteringCoefficientWS(network, pendantValue))) ;
		        }
		        if (cbClustCoeffMN.isSelected())
		            tfClustCoeffMN.setText(util.formatDoubleToString(cc.getClusteringCoefficientMN(network))) ;
		        btnComputeGeneralCC.setEnabled(true) ;
		    }
		} ;
		t.start() ;
	}

	/** Auto-generated event handler method */
	protected void btnComputeDirectedCoeffsActionPerformed(ActionEvent evt){
	    Thread t = new Thread() {
		    public void run() {
		     btnComputeDirectedCoeffs.setEnabled(false) ;
		        Util util = Util.getInstance() ;
		        ClusteringCoefficients cc = ClusteringCoefficients.getNewInstance() ;
		        tfIncomingCC.setText(util.formatDoubleToString(cc.getIncomingClusteringCoefficient(network))) ;
		        tfOutgoingCC.setText(util.formatDoubleToString(cc.getOutgoingClusteringCoefficient(network))) ;
		        tfMutualConnectionCoeff.setText(util.formatDoubleToString(cc.getMutualConnectionCoefficient(network))) ;
		        tfTriangleCoeff.setText(util.formatDoubleToString(cc.getTriangleCoefficient(network))) ;
		      btnComputeDirectedCoeffs.setEnabled(true) ;
		    }
	    } ;
	    t.start() ;
	}

	/** Auto-generated event handler method */
	protected void btnGeneralCoeffPrintToConsoleActionPerformed(ActionEvent evt){
	    Util.println("Pendant Value : " + (rbPendantValueOne.isSelected()?1:0)) ;
	    Util.println(cbClustCoeffWS.getText() + " : "  + tfClustCoeffWS.getText()) ;
		Util.println(cbClustCoeffMN.getText() + " : " + tfClustCoeffMN.getText()) ;
	}

	/** Auto-generated event handler method */
	protected void btnDirectedCoeffPrintToConsoleActionPerformed(ActionEvent evt){
	    Util.println(cbIncomingCC.getText() + " : " + tfIncomingCC.getText()) ;
	    Util.println(cbOutgoingCC.getText() + " : " + tfOutgoingCC.getText()) ;
	    Util.println(cbMutualConnectionCoeff.getText() + ":" + tfMutualConnectionCoeff.getText()) ;
	    Util.println(cbTriangleCoeff.getText() + " : " + tfTriangleCoeff.getText()) ;
	}
	
}
