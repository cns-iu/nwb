package edu.iu.iv.toolkits.networkanalysistoolkit.gui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import edu.iu.iv.toolkits.networkanalysistoolkit.analysis.Network;

/**
 * This code was generated using CloudGarden's Jigloo SWT/Swing GUI Builder,
 * which is free for non-commercial use. If Jigloo is being used commercially
 * (ie, by a for-profit company or business) then you should purchase a license -
 * please visit www.cloudgarden.com for details.
 */
public class NetworkAnalysisToolkitGUI extends javax.swing.JFrame {

    private static final long serialVersionUID = -5019356217726206740L;

    private JButton btnDegreeDistributions;

    private JButton btnPruning;

    private JButton btnClusteringCoefficients;

    private JButton btnPathCharacteristics;

    private JButton btnGraphType;

    public NetworkAnalysisToolkitGUI() {
        initGUI();
    }

	/**
	* Initializes the GUI.
	* Auto-generated code - any changes you make will disappear.
	*/
    public void initGUI(){
		try {
			preInitGUI();
	
			btnGraphType = new JButton();
			btnPathCharacteristics = new JButton();
			btnClusteringCoefficients = new JButton();
			btnPruning = new JButton();
			btnDegreeDistributions = new JButton();
	
			GridBagLayout thisLayout = new GridBagLayout();
			this.getContentPane().setLayout(thisLayout);
			thisLayout.columnWidths = new int[] {1,1,1,1};
			thisLayout.rowHeights = new int[] {1,1,1,1,1};
			thisLayout.columnWeights = new double[] {0.1,0.1,0.1,0.1};
			thisLayout.rowWeights = new double[] {0.1,0.1,0.1,0.1,0.1};
			this.setTitle("Network Analysis Toolkit");
			this.setSize(new java.awt.Dimension(189,208));
	
			btnGraphType.setText("Graph Type");
			this.getContentPane().add(btnGraphType, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
			btnGraphType.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnGraphTypeActionPerformed(evt);
				}
			});
	
			btnPathCharacteristics.setText("Path Characteristics");
			this.getContentPane().add(btnPathCharacteristics, new GridBagConstraints(0, 1, 4, 1, 0.0, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
			btnPathCharacteristics.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnPathCharacteristicsActionPerformed(evt);
				}
			});
	
			btnClusteringCoefficients.setText("Clustering Coefficients");
			this.getContentPane().add(btnClusteringCoefficients, new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
			btnClusteringCoefficients.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnClusteringCoefficientsActionPerformed(evt);
				}
			});
	
			btnPruning.setText("Pruning");
			this.getContentPane().add(btnPruning, new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
			btnPruning.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnPruningActionPerformed(evt);
				}
			});
	
			btnDegreeDistributions.setText("Degree Distributions");
			this.getContentPane().add(btnDegreeDistributions, new GridBagConstraints(0, 4, 4, 1, 0.0, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
			btnDegreeDistributions.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnDegreeDistributionsActionPerformed(evt);
				}
			});
	
			postInitGUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    /** Add your pre-init code in here */
    public void preInitGUI() {
        // not used
    }

    /** Add your post-init code in here */
    public void postInitGUI() {
        // not used
    }

    /** Auto-generated main method */
    public static void main(String[] args) {
        showGUI();
    }

	/**
	* This static method creates a new instance of this class and shows
	* it inside a new JFrame, (unless it is already a JFrame).
	*
	* It is a convenience method for showing the GUI, but it can be
	* copied and used as a basis for your own code.	*
	* It is auto-generated code - the body of this method will be
	* re-generated after any changes are made to the GUI.
	* However, if you delete this method it will not be re-created.	*/
    public static void showGUI(){
		try {
			NetworkAnalysisToolkitGUI inst = new NetworkAnalysisToolkitGUI();
			inst.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    private Network network ;
    public void setNetwork(Network network) {
        this.network = network ;
        if (graphTypeDialog != null)
            graphTypeDialog.update(this.network) ;
    }
    
    private GraphTypeDialog graphTypeDialog = null;
    /** Auto-generated event handler method */
    protected void btnGraphTypeActionPerformed(ActionEvent evt) {
        if (graphTypeDialog == null) {
            graphTypeDialog = new GraphTypeDialog((Frame) this);
            graphTypeDialog.setVisible(true);
            graphTypeDialog.update(this.network) ;
        }
        else
            graphTypeDialog.setVisible(!graphTypeDialog.isVisible()) ;
    }

    private GraphPathsDialog graphPathsDialog;

    /** Auto-generated event handler method */
    protected void btnPathCharacteristicsActionPerformed(ActionEvent evt) {
        if (graphPathsDialog == null) {
            graphPathsDialog = new GraphPathsDialog((Frame) this);
            graphPathsDialog.setVisible(true);
            graphPathsDialog.update(this.network) ;
        } else
            graphPathsDialog.setVisible(!graphPathsDialog.isVisible());
    }

    private ClusteringCoefficientsDialog clusteringCoefficientsDialog = null;

    /** Auto-generated event handler method */
    protected void btnClusteringCoefficientsActionPerformed(ActionEvent evt) {
        if (clusteringCoefficientsDialog == null) {
            clusteringCoefficientsDialog = new ClusteringCoefficientsDialog(
                    (Frame) this);
            clusteringCoefficientsDialog.setVisible(true);
            clusteringCoefficientsDialog.update(this.network) ;
        } else
            clusteringCoefficientsDialog
                    .setVisible(!clusteringCoefficientsDialog.isVisible());
    }

    private GraphPruningDialog graphPruningDialog = null;

    /** Auto-generated event handler method */
    protected void btnPruningActionPerformed(ActionEvent evt) {
        if (graphPruningDialog == null) {
            graphPruningDialog = new GraphPruningDialog((Frame) this);
            graphPruningDialog.setVisible(true);
            graphPruningDialog.update(this.network) ;
        } else
            graphPruningDialog.setVisible(!graphPruningDialog.isVisible());
    }

    private DegreeDistributionsDialog degreeDistributionsDialog = null ;
    /** Auto-generated event handler method */
    protected void btnDegreeDistributionsActionPerformed(ActionEvent evt) {
        if (degreeDistributionsDialog == null) {
            degreeDistributionsDialog = new DegreeDistributionsDialog((Frame)this) ;
            degreeDistributionsDialog.setVisible(true) ;
            degreeDistributionsDialog.update(this.network) ;
        }
        else
            degreeDistributionsDialog.setVisible(!degreeDistributionsDialog.isVisible()) ;
    }
}