package edu.iu.iv.toolkits.networkanalysistoolkit.gui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import edu.iu.iv.toolkits.networkanalysistoolkit.analysis.Network;
import edu.iu.iv.toolkits.networkanalysistoolkit.analysis.VertexDegreeDistributions;
import edu.iu.iv.toolkits.networkanalysistoolkit.util.Util;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.statistics.DegreeDistributions;
import edu.uci.ics.jung.statistics.Histogram;

/**
 * This code was generated using CloudGarden's Jigloo SWT/Swing GUI Builder,
 * which is free for non-commercial use. If Jigloo is being used commercially
 * (ie, by a for-profit company or business) then you should purchase a license -
 * please visit www.cloudgarden.com for details.
 */
public class DegreeDistributionsDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = 3298694966710619490L;

    private ButtonGroup btnGroupDirectedHistDist;

	private JTextField tfDirectedHistNumBins;

	private ButtonGroup btnGroupInOutDegree;

	private JRadioButton rbSelectOutDegree;

	private JRadioButton rbSelectInDegree;

	private JPanel selectInOutDegreePanel;

	private JTextField tfDirectedAvgDegree;

	private JCheckBox cbDirectedAvgDegree;

	private JCheckBox cbDirectedNormalize;

	private JButton btnDirectedPrintToConsole;

	private JButton btnDirectedCompute;

	private JCheckBox cbDirectedDistCharacteristicsSelectAll;

	private JTextField tfDirectedVariance;

	private JCheckBox cbDirectedVariance;

	private JTextField tfDirectedStdDev;

	private JCheckBox cbDirectedStdDev;

	private JTextField tfDirectedRSquare;

	private JCheckBox cbDirectedRSquare;

	private JTextField tfDirectedGamma;

	private JCheckBox cbDirectedGamma;

	private JPanel directedDistributionCharacteristicsPanel;

	private JLabel jLabel3;

	private JTextField tfDirectedHistMax;

	private JTextField tfDirectedHistMin;

	private JButton btnDirectedSaveDist;

	private JLabel jLabel2;

	private JLabel jLabel1;

	private JRadioButton rbDirectedHist;

	private JRadioButton rbDirectedDist;

	private JPanel directedDistributionsPanel;

	private JTextField tfUndirectedAvgDegree;

	private JCheckBox cbUndirectedAvgDegree;

	private JCheckBox cbUndirectedVarianceNormalized;

	private JButton btnUndirectedPrintToConsole;

	private JButton btnUndirectedCompute;

	private ButtonGroup btnGroupUndirectedHistDist;

	private JCheckBox cbUndirectedDistCharacteristicsSelectAll;

	private JTextField tfUndirectedVariance;

	private JLabel lblNumBins;

	private JCheckBox cbUndirectedVariance;

	private JTextField tfUndirectedStdDev;

	private JCheckBox cbUndirectedStdDev;

	private JTextField tfUndirectedRSquare;

	private JCheckBox cbUndirectedRSquare;

	private JTextField tfUndirectedGamma;

	private JCheckBox cbUndirectedGamma;

	private JPanel distributionCharacteristicsPanel;

	private JTextField tfUndirectedHistNumBins;

	private JTextField tfUndirectedHistMax;

	private JTextField tfUndirectedHistMin;

	private JPanel unDirectedDistributionsPanel;

	private JButton btnUndirectedSaveDist;

	private JLabel lblMaximum;

	private JLabel lblMinimum;

	private JRadioButton rbUndirectedHist;

	private JRadioButton rbUndirectedDist;

	private JPanel directedGraphPanel;

	private JPanel undirectedGraphPanel;

	private JTabbedPane tbGraphType;

	public DegreeDistributionsDialog(Frame frame) {
		super(frame);
		initGUI();
	}

	/**
	 * Initializes the GUI. Auto-generated code - any changes you make will
	 * disappear.
	 */
	public void initGUI() {
		try {
			preInitGUI();
			btnGroupUndirectedHistDist = new ButtonGroup();

			btnGroupInOutDegree = new ButtonGroup();

			btnGroupDirectedHistDist = new ButtonGroup();

			tbGraphType = new JTabbedPane();
			undirectedGraphPanel = new JPanel();
			unDirectedDistributionsPanel = new JPanel();
			rbUndirectedDist = new JRadioButton();
			rbUndirectedHist = new JRadioButton();
			lblMinimum = new JLabel();
			lblMaximum = new JLabel();
			btnUndirectedSaveDist = new JButton();
			tfUndirectedHistMin = new JTextField();
			tfUndirectedHistMax = new JTextField();
			tfUndirectedHistNumBins = new JTextField();
			lblNumBins = new JLabel();
			distributionCharacteristicsPanel = new JPanel();
			cbUndirectedGamma = new JCheckBox();
			tfUndirectedGamma = new JTextField();
			cbUndirectedRSquare = new JCheckBox();
			tfUndirectedRSquare = new JTextField();
			cbUndirectedStdDev = new JCheckBox();
			tfUndirectedStdDev = new JTextField();
			cbUndirectedVariance = new JCheckBox();
			tfUndirectedVariance = new JTextField();
			cbUndirectedDistCharacteristicsSelectAll = new JCheckBox();
			btnUndirectedCompute = new JButton();
			btnUndirectedPrintToConsole = new JButton();
			cbUndirectedVarianceNormalized = new JCheckBox();
			cbUndirectedAvgDegree = new JCheckBox();
			tfUndirectedAvgDegree = new JTextField();
			directedGraphPanel = new JPanel();
			directedDistributionsPanel = new JPanel();
			rbDirectedDist = new JRadioButton();
			rbDirectedHist = new JRadioButton();
			jLabel1 = new JLabel();
			jLabel2 = new JLabel();
			btnDirectedSaveDist = new JButton();
			tfDirectedHistMin = new JTextField();
			tfDirectedHistMax = new JTextField();
			tfDirectedHistNumBins = new JTextField();
			jLabel3 = new JLabel();
			directedDistributionCharacteristicsPanel = new JPanel();
			cbDirectedGamma = new JCheckBox();
			tfDirectedGamma = new JTextField();
			cbDirectedRSquare = new JCheckBox();
			tfDirectedRSquare = new JTextField();
			cbDirectedStdDev = new JCheckBox();
			tfDirectedStdDev = new JTextField();
			cbDirectedVariance = new JCheckBox();
			tfDirectedVariance = new JTextField();
			cbDirectedDistCharacteristicsSelectAll = new JCheckBox();
			btnDirectedCompute = new JButton();
			btnDirectedPrintToConsole = new JButton();
			cbDirectedNormalize = new JCheckBox();
			cbDirectedAvgDegree = new JCheckBox();
			tfDirectedAvgDegree = new JTextField();
			selectInOutDegreePanel = new JPanel();
			rbSelectInDegree = new JRadioButton();
			rbSelectOutDegree = new JRadioButton();

			GridBagLayout thisLayout = new GridBagLayout();
			this.getContentPane().setLayout(thisLayout);
			thisLayout.columnWidths = new int[] { 1, 1 };
			thisLayout.rowHeights = new int[] { 1, 1, 1 };
			thisLayout.columnWeights = new double[] { 0.1, 0.1 };
			thisLayout.rowWeights = new double[] { 0.1, 0.1, 0.1 };
			this.setTitle("Degree Distributions");
			this.setSize(new java.awt.Dimension(391, 461));

			this.getContentPane().add(
					tbGraphType,
					new GridBagConstraints(0, 0, 2, 2, 1.0, 0.8, 10, 1,
							new Insets(0, 0, 0, 0), 0, 0));

			GridBagLayout undirectedGraphPanelLayout = new GridBagLayout();
			undirectedGraphPanel.setLayout(undirectedGraphPanelLayout);
			undirectedGraphPanelLayout.columnWidths = new int[] { 1 };
			undirectedGraphPanelLayout.rowHeights = new int[] { 1, 1 };
			undirectedGraphPanelLayout.columnWeights = new double[] { 0.1 };
			undirectedGraphPanelLayout.rowWeights = new double[] { 0.1, 0.1 };
			undirectedGraphPanel.setPreferredSize(new java.awt.Dimension(379,
					410));
			tbGraphType.add(undirectedGraphPanel);
			tbGraphType.setTitleAt(0, "Undirected Graph");

			GridBagLayout unDirectedDistributionsPanelLayout = new GridBagLayout();
			unDirectedDistributionsPanel
					.setLayout(unDirectedDistributionsPanelLayout);
			unDirectedDistributionsPanelLayout.columnWidths = new int[] { 1, 1,
					1, 1 };
			unDirectedDistributionsPanelLayout.rowHeights = new int[] { 1, 1,
					1, 1 };
			unDirectedDistributionsPanelLayout.columnWeights = new double[] {
					0.1, 0.1, 0.1, 0.1 };
			unDirectedDistributionsPanelLayout.rowWeights = new double[] { 0.1,
					0.1, 0.1, 0.1 };
			unDirectedDistributionsPanel.setBorder(new TitledBorder(
					new LineBorder(new java.awt.Color(0, 0, 0), 1, false),
					"Distributions", TitledBorder.LEADING, TitledBorder.TOP,
					new java.awt.Font("Dialog", 1, 12), new java.awt.Color(0,
							0, 0)));
			undirectedGraphPanel.add(unDirectedDistributionsPanel,
					new GridBagConstraints(0, 0, 2, 1, 0.0, 0.3, 10, 1,
							new Insets(0, 0, 0, 0), 0, 0));

			rbUndirectedDist.setText("Distribution");
			rbUndirectedDist.setSelected(true);
			rbUndirectedDist.setPreferredSize(new java.awt.Dimension(90, 24));
			btnGroupUndirectedHistDist.add(rbUndirectedDist);
			unDirectedDistributionsPanel.add(rbUndirectedDist,
					new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 17, 0,
							new Insets(5, 5, 5, 5), 0, 0));
			rbUndirectedDist.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					rbUndirectedDistActionPerformed(evt);
				}
			});

			rbUndirectedHist.setText("Histogram");
			rbUndirectedHist.setPreferredSize(new java.awt.Dimension(84, 24));
			btnGroupUndirectedHistDist.add(rbUndirectedHist);
			unDirectedDistributionsPanel.add(rbUndirectedHist,
					new GridBagConstraints(0, 1, 1, 2, 0.0, 0.0, 17, 0,
							new Insets(5, 5, 5, 5), 0, 0));
			rbUndirectedHist.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					rbUndirectedHistActionPerformed(evt);
				}
			});

			lblMinimum.setText("Minimum");
			lblMinimum.setPreferredSize(new java.awt.Dimension(52, 16));
			unDirectedDistributionsPanel.add(lblMinimum,
					new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, 10, 0,
							new Insets(0, 5, 0, 5), 0, 0));

			lblMaximum.setText("Maximum");
			lblMaximum.setPreferredSize(new java.awt.Dimension(56, 16));
			unDirectedDistributionsPanel.add(lblMaximum,
					new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, 10, 0,
							new Insets(0, 0, 0, 0), 0, 0));

			btnUndirectedSaveDist.setText("Save Distribution");
			btnUndirectedSaveDist.setPreferredSize(new java.awt.Dimension(130,
					26));
			unDirectedDistributionsPanel.add(btnUndirectedSaveDist,
					new GridBagConstraints(0, 3, 1, 0, 0.0, 0.0, 17, 0,
							new Insets(5, 5, 5, 5), 0, 0));
			btnUndirectedSaveDist.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnUndirectedSaveDistActionPerformed(evt);
				}
			});

			tfUndirectedHistMin
					.setPreferredSize(new java.awt.Dimension(40, 20));
			tfUndirectedHistMin.setMinimumSize(new java.awt.Dimension(40, 20));
			tfUndirectedHistMin.setRequestFocusEnabled(true);
			unDirectedDistributionsPanel.add(tfUndirectedHistMin,
					new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, 10, 0,
							new Insets(0, 0, 0, 0), 0, 0));

			tfUndirectedHistMax
					.setPreferredSize(new java.awt.Dimension(40, 20));
			tfUndirectedHistMax.setMinimumSize(new java.awt.Dimension(40, 20));
			unDirectedDistributionsPanel.add(tfUndirectedHistMax,
					new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, 10, 0,
							new Insets(0, 0, 0, 0), 0, 0));

			tfUndirectedHistNumBins.setPreferredSize(new java.awt.Dimension(40,
					20));
			tfUndirectedHistNumBins.setMinimumSize(new java.awt.Dimension(40,
					20));
			unDirectedDistributionsPanel.add(tfUndirectedHistNumBins,
					new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, 10, 0,
							new Insets(0, 0, 0, 0), 0, 0));

			lblNumBins.setText("Number of Bins");
			unDirectedDistributionsPanel.add(lblNumBins,
					new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, 10, 0,
							new Insets(0, 0, 0, 0), 0, 0));

			GridBagLayout distributionCharacteristicsPanelLayout = new GridBagLayout();
			distributionCharacteristicsPanel
					.setLayout(distributionCharacteristicsPanelLayout);
			distributionCharacteristicsPanelLayout.columnWidths = new int[] {
					1, 1, 1, 1 };
			distributionCharacteristicsPanelLayout.rowHeights = new int[] { 1,
					1, 1, 1, 1, 1, 1, 1 };
			distributionCharacteristicsPanelLayout.columnWeights = new double[] {
					0.1, 0.1, 0.1, 0.1 };
			distributionCharacteristicsPanelLayout.rowWeights = new double[] {
					0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1 };
			distributionCharacteristicsPanel.setBorder(new TitledBorder(
					new LineBorder(new java.awt.Color(0, 0, 0), 1, false),
					"Distribution Characteristics", TitledBorder.LEADING,
					TitledBorder.TOP, new java.awt.Font("Dialog", 1, 12),
					new java.awt.Color(0, 0, 0)));
			undirectedGraphPanel.add(distributionCharacteristicsPanel,
					new GridBagConstraints(0, 1, 2, 1, 0.0, 0.3, 10, 1,
							new Insets(0, 0, 0, 0), 0, 0));

			cbUndirectedGamma.setText("Scale Free Exponent");
			distributionCharacteristicsPanel.add(cbUndirectedGamma,
					new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0, 17, 2,
							new Insets(0, 5, 0, 5), 0, 0));
			cbUndirectedGamma.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					cbUndirectedGammaActionPerformed(evt);
				}
			});

			tfUndirectedGamma.setEditable(false);
			tfUndirectedGamma.setEnabled(true);
			tfUndirectedGamma.setPreferredSize(new java.awt.Dimension(40, 20));
			tfUndirectedGamma.setMinimumSize(new java.awt.Dimension(40, 20));
			distributionCharacteristicsPanel.add(tfUndirectedGamma,
					new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, 10, 0,
							new Insets(0, 0, 0, 0), 0, 0));

			cbUndirectedRSquare.setText("<html>R<sup>2</sup></html>");
			distributionCharacteristicsPanel.add(cbUndirectedRSquare,
					new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, 17, 2,
							new Insets(0, 5, 0, 5), 0, 0));
			cbUndirectedRSquare.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					cbUndirectedRSquareActionPerformed(evt);
				}
			});

			tfUndirectedRSquare.setEditable(false);
			tfUndirectedRSquare.setEnabled(true);
			tfUndirectedRSquare
					.setPreferredSize(new java.awt.Dimension(40, 20));
			tfUndirectedRSquare.setMinimumSize(new java.awt.Dimension(40, 20));
			distributionCharacteristicsPanel.add(tfUndirectedRSquare,
					new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, 10, 0,
							new Insets(0, 0, 0, 0), 0, 0));

			cbUndirectedStdDev.setEnabled(true);
			cbUndirectedStdDev.setText("Standard Deviation");
			distributionCharacteristicsPanel.add(cbUndirectedStdDev,
					new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, 17, 2,
							new Insets(0, 5, 0, 5), 0, 0));

			tfUndirectedStdDev.setEditable(false);
			tfUndirectedStdDev.setEnabled(true);
			tfUndirectedStdDev.setPreferredSize(new java.awt.Dimension(40, 20));
			tfUndirectedStdDev.setMinimumSize(new java.awt.Dimension(40, 20));
			distributionCharacteristicsPanel.add(tfUndirectedStdDev,
					new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, 10, 0,
							new Insets(0, 0, 0, 0), 0, 0));

			cbUndirectedVariance.setEnabled(true);
			cbUndirectedVariance.setText("Variance");
			distributionCharacteristicsPanel.add(cbUndirectedVariance,
					new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 17, 2,
							new Insets(0, 5, 0, 5), 0, 0));

			tfUndirectedVariance.setEditable(false);
			tfUndirectedVariance.setEnabled(true);
			tfUndirectedVariance
					.setPreferredSize(new java.awt.Dimension(40, 20));
			tfUndirectedVariance.setMinimumSize(new java.awt.Dimension(40, 20));
			distributionCharacteristicsPanel.add(tfUndirectedVariance,
					new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, 10, 0,
							new Insets(0, 0, 0, 0), 0, 0));

			cbUndirectedDistCharacteristicsSelectAll.setText("Select All");
			distributionCharacteristicsPanel.add(
					cbUndirectedDistCharacteristicsSelectAll,
					new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, 17, 0,
							new Insets(0, 5, 0, 5), 0, 0));
			cbUndirectedDistCharacteristicsSelectAll
					.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							cbUndirectedDistCharacteristicsSelectAllActionPerformed(evt);
						}
					});

			btnUndirectedCompute.setText("Compute");
			distributionCharacteristicsPanel.add(btnUndirectedCompute,
					new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, 17, 0,
							new Insets(5, 5, 5, 5), 5, 0));
			btnUndirectedCompute.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnUndirectedComputeActionPerformed(evt);
				}
			});

			btnUndirectedPrintToConsole.setText("Print To Console");
			distributionCharacteristicsPanel.add(btnUndirectedPrintToConsole,
					new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, 17, 0,
							new Insets(5, 5, 5, 5), 0, 0));

			cbUndirectedVarianceNormalized.setEnabled(false);
			cbUndirectedVarianceNormalized.setText("Normalize");
			cbUndirectedVarianceNormalized.setSelected(true);
			distributionCharacteristicsPanel.add(
					cbUndirectedVarianceNormalized, new GridBagConstraints(1,
							2, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0),
							0, 0));

			cbUndirectedAvgDegree.setText("Average Degree");
			distributionCharacteristicsPanel.add(cbUndirectedAvgDegree,
					new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, 17, 2,
							new Insets(0, 5, 0, 5), 0, 0));

			tfUndirectedAvgDegree.setEditable(false);
			tfUndirectedAvgDegree.setEnabled(true);
			tfUndirectedAvgDegree.setPreferredSize(new java.awt.Dimension(40,
					20));
			tfUndirectedAvgDegree
					.setMinimumSize(new java.awt.Dimension(40, 20));
			distributionCharacteristicsPanel.add(tfUndirectedAvgDegree,
					new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, 10, 0,
							new Insets(0, 0, 0, 0), 0, 0));

			GridBagLayout directedGraphPanelLayout = new GridBagLayout();
			directedGraphPanel.setLayout(directedGraphPanelLayout);
			directedGraphPanelLayout.columnWidths = new int[] { 1 };
			directedGraphPanelLayout.rowHeights = new int[] { 1, 1, 1 };
			directedGraphPanelLayout.columnWeights = new double[] { 0.1 };
			directedGraphPanelLayout.rowWeights = new double[] { 0.1, 0.1, 0.1 };
			directedGraphPanel
					.setPreferredSize(new java.awt.Dimension(379, 410));
			tbGraphType.add(directedGraphPanel);
			tbGraphType.setTitleAt(1, "Directed Graph");

			GridBagLayout directedDistributionsPanelLayout = new GridBagLayout();
			directedDistributionsPanel
					.setLayout(directedDistributionsPanelLayout);
			directedDistributionsPanelLayout.columnWidths = new int[] { 1, 1,
					1, 1 };
			directedDistributionsPanelLayout.rowHeights = new int[] { 1, 1, 1,
					1 };
			directedDistributionsPanelLayout.columnWeights = new double[] {
					0.1, 0.1, 0.1, 0.1 };
			directedDistributionsPanelLayout.rowWeights = new double[] { 0.1,
					0.1, 0.1, 0.1 };
			directedDistributionsPanel.setBorder(new TitledBorder(null,
					"Distributions", TitledBorder.LEADING, TitledBorder.TOP,
					new java.awt.Font("Dialog", 1, 12), new java.awt.Color(0,
							0, 0)));
			directedGraphPanel.add(directedDistributionsPanel,
					new GridBagConstraints(0, 1, 2, 1, 0.0, 0.3, 10, 1,
							new Insets(0, 0, 0, 0), 0, 0));

			rbDirectedDist.setText("Distribution");
			rbDirectedDist.setSelected(true);
			btnGroupDirectedHistDist.add(rbDirectedDist);
			directedDistributionsPanel.add(rbDirectedDist,
					new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 17, 0,
							new Insets(5, 5, 5, 5), 0, 0));
			rbDirectedDist.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					rbDirectedDistActionPerformed(evt);
				}
			});

			rbDirectedHist.setText("Histogram");
			btnGroupDirectedHistDist.add(rbDirectedHist);
			directedDistributionsPanel.add(rbDirectedHist,
					new GridBagConstraints(0, 1, 1, 2, 0.0, 0.0, 17, 0,
							new Insets(5, 5, 5, 5), 0, 0));
			rbDirectedHist.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					rbDirectedHistActionPerformed(evt);
				}
			});

			jLabel1.setText("Minimum");
			directedDistributionsPanel.add(jLabel1, new GridBagConstraints(1,
					1, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 5, 0, 5), 0, 0));

			jLabel2.setText("Maximum");
			directedDistributionsPanel.add(jLabel2, new GridBagConstraints(2,
					1, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));

			btnDirectedSaveDist.setText("Save Distribution");
			directedDistributionsPanel.add(btnDirectedSaveDist,
					new GridBagConstraints(0, 3, 1, 0, 0.0, 0.0, 17, 0,
							new Insets(5, 5, 5, 5), 0, 0));
			btnDirectedSaveDist.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnDirectedSaveDistActionPerformed(evt);
				}
			});

			tfDirectedHistMin.setMinimumSize(new java.awt.Dimension(40, 20));
			tfDirectedHistMin.setRequestFocusEnabled(true);
			directedDistributionsPanel.add(tfDirectedHistMin,
					new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, 10, 0,
							new Insets(0, 0, 0, 0), 0, 0));

			tfDirectedHistMax.setMinimumSize(new java.awt.Dimension(40, 20));
			directedDistributionsPanel.add(tfDirectedHistMax,
					new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, 10, 0,
							new Insets(0, 0, 0, 0), 0, 0));

			tfDirectedHistNumBins
					.setMinimumSize(new java.awt.Dimension(40, 20));
			directedDistributionsPanel.add(tfDirectedHistNumBins,
					new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, 10, 0,
							new Insets(0, 0, 0, 0), 0, 0));

			jLabel3.setText("Number of Bins");
			directedDistributionsPanel.add(jLabel3, new GridBagConstraints(3,
					1, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));

			GridBagLayout directedDistributionCharacteristicsPanelLayout = new GridBagLayout();
			directedDistributionCharacteristicsPanel
					.setLayout(directedDistributionCharacteristicsPanelLayout);
			directedDistributionCharacteristicsPanelLayout.columnWidths = new int[] {
					1, 1, 1, 1 };
			directedDistributionCharacteristicsPanelLayout.rowHeights = new int[] {
					1, 1, 1, 1, 1, 1, 1, 1 };
			directedDistributionCharacteristicsPanelLayout.columnWeights = new double[] {
					0.1, 0.1, 0.1, 0.1 };
			directedDistributionCharacteristicsPanelLayout.rowWeights = new double[] {
					0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1 };
			directedDistributionCharacteristicsPanel
					.setBorder(new TitledBorder(null,
							"Distribution Characteristics",
							TitledBorder.LEADING, TitledBorder.TOP,
							new java.awt.Font("Dialog", 1, 12),
							new java.awt.Color(0, 0, 0)));
			directedGraphPanel.add(directedDistributionCharacteristicsPanel,
					new GridBagConstraints(0, 2, 2, 1, 0.0, 0.3, 10, 1,
							new Insets(0, 0, 0, 0), 0, 0));

			cbDirectedGamma.setText("Scale Free Exponent");
			directedDistributionCharacteristicsPanel.add(cbDirectedGamma,
					new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0, 17, 2,
							new Insets(0, 5, 0, 5), 0, 0));
			cbDirectedGamma.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					cbDirectedGammaActionPerformed(evt);
				}
			});

			tfDirectedGamma.setEditable(false);
			tfDirectedGamma.setEnabled(true);
			tfDirectedGamma.setMinimumSize(new java.awt.Dimension(40, 20));
			directedDistributionCharacteristicsPanel.add(tfDirectedGamma,
					new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, 10, 0,
							new Insets(0, 0, 0, 0), 0, 0));

			cbDirectedRSquare.setText("<html>R<sup>2</sup></html>");
			directedDistributionCharacteristicsPanel.add(cbDirectedRSquare,
					new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, 17, 2,
							new Insets(0, 5, 0, 5), 0, 0));
			cbDirectedRSquare.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					cbDirectedRSquareActionPerformed(evt);
				}
			});

			tfDirectedRSquare.setEditable(false);
			tfDirectedRSquare.setEnabled(true);
			tfDirectedRSquare.setMinimumSize(new java.awt.Dimension(40, 20));
			directedDistributionCharacteristicsPanel.add(tfDirectedRSquare,
					new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, 10, 0,
							new Insets(0, 0, 0, 0), 0, 0));

			cbDirectedStdDev.setEnabled(true);
			cbDirectedStdDev.setText("Standard Deviation");
			directedDistributionCharacteristicsPanel.add(cbDirectedStdDev,
					new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, 17, 2,
							new Insets(0, 5, 0, 5), 0, 0));

			tfDirectedStdDev.setEditable(false);
			tfDirectedStdDev.setEnabled(true);
			tfDirectedStdDev.setMinimumSize(new java.awt.Dimension(40, 20));
			tfDirectedStdDev.setOpaque(true);
			directedDistributionCharacteristicsPanel.add(tfDirectedStdDev,
					new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, 10, 0,
							new Insets(0, 0, 0, 0), 0, 0));

			cbDirectedVariance.setEnabled(true);
			cbDirectedVariance.setText("Variance");
			directedDistributionCharacteristicsPanel.add(cbDirectedVariance,
					new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 17, 2,
							new Insets(0, 5, 0, 5), 0, 0));

			tfDirectedVariance.setEditable(false);
			tfDirectedVariance.setEnabled(true);
			tfDirectedVariance.setMinimumSize(new java.awt.Dimension(40, 20));
			directedDistributionCharacteristicsPanel.add(tfDirectedVariance,
					new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, 10, 0,
							new Insets(0, 0, 0, 0), 0, 0));

			cbDirectedDistCharacteristicsSelectAll.setText("Select All");
			directedDistributionCharacteristicsPanel.add(
					cbDirectedDistCharacteristicsSelectAll,
					new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, 17, 0,
							new Insets(0, 5, 0, 5), 0, 0));
			cbDirectedDistCharacteristicsSelectAll
					.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							cbDirectedDistCharacteristicsSelectAllActionPerformed(evt);
						}
					});

			btnDirectedCompute.setText("Compute");
			directedDistributionCharacteristicsPanel.add(btnDirectedCompute,
					new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, 17, 0,
							new Insets(5, 5, 5, 5), 5, 0));
			btnDirectedCompute.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnDirectedComputeActionPerformed(evt);
				}
			});

			btnDirectedPrintToConsole.setText("Print To Console");
			directedDistributionCharacteristicsPanel.add(
					btnDirectedPrintToConsole, new GridBagConstraints(0, 7, 1,
							1, 0.0, 0.0, 17, 0, new Insets(5, 5, 5, 5), 0, 0));

			cbDirectedNormalize.setEnabled(false);
			cbDirectedNormalize.setText("Normalize");
			cbDirectedNormalize.setSelected(true);
			directedDistributionCharacteristicsPanel.add(cbDirectedNormalize,
					new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, 10, 0,
							new Insets(0, 0, 0, 0), 0, 0));

			cbDirectedAvgDegree.setText("Average Degree");
			directedDistributionCharacteristicsPanel.add(cbDirectedAvgDegree,
					new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, 17, 2,
							new Insets(0, 5, 0, 5), 0, 0));

			tfDirectedAvgDegree.setEditable(false);
			tfDirectedAvgDegree.setEnabled(true);
			tfDirectedAvgDegree
					.setPreferredSize(new java.awt.Dimension(40, 20));
			tfDirectedAvgDegree.setMinimumSize(new java.awt.Dimension(40, 20));
			directedDistributionCharacteristicsPanel.add(tfDirectedAvgDegree,
					new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, 10, 0,
							new Insets(0, 0, 0, 0), 0, 0));

			GridBagLayout selectInOutDegreePanelLayout = new GridBagLayout();
			selectInOutDegreePanel.setLayout(selectInOutDegreePanelLayout);
			selectInOutDegreePanelLayout.columnWidths = new int[] { 1, 1 };
			selectInOutDegreePanelLayout.rowHeights = new int[] { 1 };
			selectInOutDegreePanelLayout.columnWeights = new double[] { 0.1,
					0.1 };
			selectInOutDegreePanelLayout.rowWeights = new double[] { 0.1 };
			selectInOutDegreePanel.setBorder(new TitledBorder(new LineBorder(
					new java.awt.Color(0, 0, 0), 1, false), "Select",
					TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font(
							"Dialog", 1, 12), new java.awt.Color(0, 0, 0)));
			directedGraphPanel.add(selectInOutDegreePanel,
					new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 1,
							new Insets(0, 0, 0, 0), 0, 0));

			rbSelectInDegree.setText("In Degree");
			rbSelectInDegree.setSelected(true);
			btnGroupInOutDegree.add(rbSelectInDegree);
			selectInOutDegreePanel.add(rbSelectInDegree,
					new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 0,
							new Insets(0, 0, 0, 0), 0, 0));

			rbSelectOutDegree.setText("Out Degree");
			btnGroupInOutDegree.add(rbSelectOutDegree);
			selectInOutDegreePanel.add(rbSelectOutDegree,
					new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, 10, 0,
							new Insets(0, 0, 0, 0), 0, 0));

			postInitGUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Add your pre-init code in here */
	public void preInitGUI() {
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	}

	/** Add your post-init code in here */
	public void postInitGUI() {
		// nothing
	}

	private Network network;

	public void update(Network network) {
		this.network = network;
		if (network.isDirected()) {
			btnUndirectedSaveDist.setEnabled(false);
			btnUndirectedSaveDist.setToolTipText("Graph is directed.");
			btnUndirectedCompute.setToolTipText("Graph is directed.");
			btnUndirectedCompute.setEnabled(false);
		} else {
			btnDirectedSaveDist.setEnabled(false);
			btnDirectedSaveDist.setToolTipText("Graph is not directed.");
			btnDirectedCompute.setToolTipText("Graph is not directed.");
			btnDirectedCompute.setEnabled(false);
		}
		setUndirectedHistDefaultValues();
		setDirectedHistDefaultValues();
	}

	/** Auto-generated event handler method */
	protected void rbDirectedDistActionPerformed(ActionEvent evt) {
		setDistributionParametersEnabled(false);
		btnUndirectedSaveDist.setText("Get Distribution");
	}

	/** Auto-generated event handler method */
	protected void rbDirectedHistActionPerformed(ActionEvent evt) {
		setDistributionParametersEnabled(true);
		btnUndirectedSaveDist.setText("Save Histogram");
	}

	private void setDistributionParametersEnabled(boolean enabled) {
		tfUndirectedHistMin.setEnabled(enabled);
		tfUndirectedHistMax.setEnabled(enabled);
		tfUndirectedHistNumBins.setEnabled(enabled);
	}

	/** Auto-generated event handler method */
	protected void cbDirectedDistCharacteristicsSelectAllActionPerformed(
			ActionEvent evt) {
		boolean sel = cbDirectedDistCharacteristicsSelectAll.isSelected();
		cbDirectedAvgDegree.setSelected(sel);
		cbDirectedGamma.setSelected(sel);
		cbDirectedRSquare.setSelected(sel);
		cbDirectedStdDev.setSelected(sel);
		cbDirectedVariance.setSelected(sel);
	}

	/** Auto-generated event handler method */
	protected void rbUndirectedDistActionPerformed(ActionEvent evt) {
		btnUndirectedSaveDist.setText("Save Distribution");
	}

	/** Auto-generated event handler method */
	protected void rbUndirectedHistActionPerformed(ActionEvent evt) {
		btnUndirectedSaveDist.setText("Save Histogram");
	}

	private void setUndirectedHistDefaultValues() {
		tfUndirectedHistMin.setText("0");
		tfUndirectedHistMax.setText(Integer.toString(this
				.getMaxValue(this.network.getGraph())));
		tfUndirectedHistNumBins.setText(Integer.toString(this.network
				.getNumVertices()));
	}

	private void setDirectedHistDefaultValues() {
		tfDirectedHistMin.setText("0");
		tfDirectedHistMax.setText(Integer.toString(this
				.getMaxValue(this.network.getGraph())));
		tfDirectedHistNumBins.setText(Integer.toString(this.network
				.getNumVertices()));
	}

	/** Auto-generated event handler method */
	protected void cbUndirectedDistCharacteristicsSelectAllActionPerformed(
			ActionEvent evt) {
		boolean sel = cbUndirectedDistCharacteristicsSelectAll.isSelected();
		cbUndirectedAvgDegree.setSelected(sel);
		cbUndirectedRSquare.setSelected(sel);
		cbUndirectedGamma.setSelected(sel);
		cbUndirectedStdDev.setSelected(sel);
		cbUndirectedVariance.setSelected(sel);
	}

	/** Auto-generated event handler method */
	protected void btnUndirectedComputeActionPerformed(ActionEvent evt) {
		Thread t = new Thread() {
			public void run() {
				btnUndirectedCompute.setEnabled(false);
				VertexDegreeDistributions vdd = VertexDegreeDistributions
						.getInstance();
				Util util = Util.getInstance();
				if (cbUndirectedAvgDegree.isSelected())
					tfUndirectedAvgDegree.setText(util.formatDoubleToString(vdd
							.getAverageDegree(network)));
				if (cbUndirectedStdDev.isSelected()) {
					if (undirectedHistogram == null)
						undirectedHistogram = generateUndirectedHistogram();
					if (undirectedHistogram != null)
						tfUndirectedStdDev.setText(util
								.formatDoubleToString(undirectedHistogram
										.standardDeviation()));
				}
				if (cbUndirectedVariance.isSelected()) {
					if (undirectedHistogram == null)
						undirectedHistogram = generateUndirectedHistogram();
					if (undirectedHistogram != null) {
						if (cbUndirectedVarianceNormalized.isSelected())
							tfUndirectedVariance.setText(util
									.formatDoubleToString(undirectedHistogram
											.variance()));
					}
				}
				if (cbUndirectedGamma.isSelected()) {
					tfUndirectedGamma.setText(util.formatDoubleToString(vdd
							.getScaleFreeExponent(network,
									VertexDegreeDistributions.DEGREE)));
				}
				if (cbUndirectedRSquare.isSelected()) {
					tfUndirectedRSquare.setText(util.formatDoubleToString(vdd
							.getRSquare(network,
									VertexDegreeDistributions.DEGREE)));
				}
				btnUndirectedCompute.setEnabled(true);
			}
		};
		t.start();
	}

	/** Auto-generated event handler method */
	protected void btnUndirectedSaveDistActionPerformed(ActionEvent evt) {
		if (rbUndirectedDist.isSelected()) {
			int vals[] = VertexDegreeDistributions.getInstance()
					.getDistribution(this.network.getGraph().getVertices(),
							VertexDegreeDistributions.DEGREE);
			if (vals != null)
				saveDistribution(vals);
		} else
			undirectedHistogram = generateUndirectedHistogram();
		if (undirectedHistogram != null)
			saveHistogram(undirectedHistogram);
	}

	private void saveDistribution(int[] vals) {
		File file = getFileFromUser();
		if (file == null)
			return;
		writeDistributionToDisk(vals, file);
	}

	private void writeDistributionToDisk(int[] vals, File file) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < vals.length; ++i) {
				writer.write(i + " " + vals[i]);
				writer.newLine();
			}
			writer.flush();
			writer.close();
			Util.println("Distribution saved to: " + file.getPath());
		} catch (IOException e) {
			JOptionPane
					.showMessageDialog(
							this,
							"Error",
							"An I/O error occurred. The file may not have been saved correctly.",
							JOptionPane.ERROR_MESSAGE);
		} finally {
			writer = null;
		}
	}

	/** Auto-generated event handler method */
	protected void btnDirectedSaveDistActionPerformed(ActionEvent evt) {
		if (rbDirectedDist.isSelected()) {
			int[] vals = VertexDegreeDistributions
					.getInstance()
					.getDistribution(
							this.network.getGraph().getVertices(),
							this.rbSelectInDegree.isSelected() ? VertexDegreeDistributions.INDEGREE
									: VertexDegreeDistributions.OUTDEGREE);
			if (vals != null)
				saveDistribution(vals);
		} else
			directedHistogram = generateDirectedHistogram();
		if (directedHistogram != null)
			saveHistogram(directedHistogram);
	}

	private int getMaxValue(Graph g) {
		Set s = g.getVertices();
		Iterator iter = s.iterator();
		int max = 0;
		while (iter.hasNext()) {
			int val = ((Vertex) iter.next()).degree();
			if (val > max)
				max = val;
		}
		return max;
	}

	private double directedHistMin, directedHistMax;

	private int directedHistNumBins;

	private Histogram generateDirectedHistogram() {
		Histogram histogram = null;
		if (!areDirectedHistogramParametersValid())
			histogram = null;
		else {
			histogram = VertexDegreeDistributions
					.getInstance()
					.getHistogram(
							this.network.getGraph().getVertices(),
							directedHistMin,
							directedHistMax,
							directedHistNumBins,
							rbSelectInDegree.isSelected() ? VertexDegreeDistributions.INDEGREE
									: VertexDegreeDistributions.OUTDEGREE);
		}
		return histogram;
	}

	private boolean areDirectedHistogramParametersValid() {
		boolean valid = false;
		try {
			String s = tfDirectedHistMin.getText();
			if (s != null && s.length() != 0)
				directedHistMin = Double.parseDouble(s);
			else {
				directedHistMin = 0;
				tfDirectedHistMin.setText("0");
			}
			s = tfDirectedHistMax.getText();
			if (s != null && s.length() != 0)
				directedHistMax = Double.parseDouble(s);
			else {
				directedHistMax = 0;
				tfDirectedHistMax.setText(Integer.toString(Integer.MAX_VALUE));
			}
			s = tfDirectedHistNumBins.getText();
			if (s != null && s.length() != 0)
				directedHistNumBins = Integer.parseInt(s);
			else
				throw new IllegalArgumentException("Number of bins invalid!");
			if (!Util.getInstance().checkInteger(directedHistNumBins, 1,
					this.network.getNumVertices(), true)) {
				throw new IllegalArgumentException(
						"Number of bins must be between 1 and "
								+ this.network.getNumVertices());
			}
			valid = true;
		} catch (NumberFormatException nfe) {
			JOptionPane
					.showMessageDialog(
							this,
							"One or more input parameters invalid. Please check values.",
							"Error!", JOptionPane.ERROR_MESSAGE);
			valid = false;
		} catch (IllegalArgumentException iae) {
			JOptionPane.showMessageDialog(this, iae.getMessage(), "Error!",
					JOptionPane.ERROR_MESSAGE);
			valid = false;
		}

		return valid;
	}

	private boolean areUndirectedHistogramParametersValid() {
		boolean valid = false;
		try {
			String s = tfUndirectedHistMin.getText();
			if (s != null && s.length() != 0)
				directedHistMin = Double.parseDouble(s);
			else {
				undirectedHistMin = 0;
				tfUndirectedHistMin.setText("0");
			}
			s = tfUndirectedHistMax.getText();
			if (s != null && s.length() != 0)
				undirectedHistMax = Double.parseDouble(s);
			else {
				undirectedHistMax = 0;
				tfUndirectedHistMax
						.setText(Integer.toString(Integer.MAX_VALUE));
			}
			s = tfUndirectedHistNumBins.getText();
			if (s != null && s.length() != 0)
				undirectedHistNumBins = Integer.parseInt(s);
			else
				throw new IllegalArgumentException("Number of bins invalid!");
			if (!Util.getInstance().checkDouble(undirectedHistNumBins, 1,
					this.network.getNumVertices(), true)) {
				throw new IllegalArgumentException(
						"Number of bins must be between 1 and "
								+ this.network.getNumVertices());
			}
			valid = true;
		} catch (NumberFormatException nfe) {
			JOptionPane
					.showMessageDialog(
							this,
							"One or more input parameters invalid. Please check values.",
							"Error!", JOptionPane.ERROR_MESSAGE);
			valid = false;
		} catch (IllegalArgumentException iae) {
			JOptionPane.showMessageDialog(this, iae.getMessage(), "Error!",
					JOptionPane.ERROR_MESSAGE);
			valid = false;
		}

		return valid;
	}

	private double undirectedHistMin, undirectedHistMax;

	private int undirectedHistNumBins;

	private Histogram generateUndirectedHistogram() {
		Histogram histogram = null;
		if (!areUndirectedHistogramParametersValid())
			histogram = null;
		else {
			histogram = VertexDegreeDistributions.getInstance().getHistogram(
					this.network.getGraph().getVertices(), undirectedHistMin,
					undirectedHistMax, undirectedHistNumBins,
					VertexDegreeDistributions.DEGREE);
		}
		return histogram;
	}

	private void saveHistogram(Histogram histogram) {
		File file = getFileFromUser();
		if (file == null)
			return;
		else
			writeHistogramToDisk(histogram, file);
	}

	private File getFileFromUser() {
		JFileChooser chooser = new JFileChooser("Save Histogram");
		boolean done = false;
		while (!done) {
			int val = chooser.showSaveDialog(this);
			if (val == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				if (file.exists()) {
					int opt = JOptionPane
							.showConfirmDialog(
									this,
									"File:\n"
											+ file.getPath()
											+ "\nalready exists. Are you sure you want to overwrite it?",
									"Confirm File Overwrite",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE);
					if (opt == JOptionPane.YES_OPTION) {
						return file;
					}
				} else {
					return file;
				}
			} else
				return null;
		}
		return null;
	}

	public void writeHistogramToDisk(Histogram histogram, File file) {
		DegreeDistributions.saveDistribution(histogram, file.getPath());
		Util.println("Histogram saved to: " + file.getPath());
	}

	private Histogram undirectedHistogram;

	private Histogram directedHistogram;

	/** Auto-generated event handler method */
	protected void cbUndirectedRSquareActionPerformed(ActionEvent evt) {
		if (cbUndirectedRSquare.isSelected())
			cbUndirectedGamma.setSelected(true);
	}

	/** Auto-generated event handler method */
	protected void cbUndirectedGammaActionPerformed(ActionEvent evt) {
		if (!cbUndirectedGamma.isSelected())
			cbUndirectedRSquare.setSelected(false);
	}

	/** Auto-generated event handler method */
	protected void cbDirectedRSquareActionPerformed(ActionEvent evt) {
		if (cbDirectedRSquare.isSelected())
			cbDirectedGamma.setSelected(true);
	}

	/** Auto-generated event handler method */
	protected void cbDirectedGammaActionPerformed(ActionEvent evt) {
		if (cbDirectedGamma.isSelected())
			cbDirectedGamma.setSelected(true);
	}

	/** Auto-generated event handler method */
	protected void btnDirectedComputeActionPerformed(ActionEvent evt) {
		Thread t = new Thread() {
			public void run() {
				btnDirectedCompute.setEnabled(false);
				VertexDegreeDistributions vdd = VertexDegreeDistributions
						.getInstance();
				Util util = Util.getInstance();
				if (cbDirectedAvgDegree.isSelected())
					tfDirectedAvgDegree.setText(util.formatDoubleToString(vdd
							.getAverageDegree(network)));
				if (cbDirectedStdDev.isSelected()) {
					if (directedHistogram == null)
						directedHistogram = generateDirectedHistogram();
					if (directedHistogram != null)
						tfDirectedStdDev.setText(util
								.formatDoubleToString(directedHistogram
										.standardDeviation()));
				}
				if (cbDirectedVariance.isSelected()) {
					if (directedHistogram == null)
						directedHistogram = generateDirectedHistogram();
					if (directedHistogram != null) {
						if (cbDirectedVariance.isSelected())
							tfDirectedVariance.setText(util
									.formatDoubleToString(directedHistogram
											.variance()));
					}
					if (cbDirectedGamma.isSelected()) {
						tfDirectedGamma
								.setText(util
										.formatDoubleToString(vdd
												.getScaleFreeExponent(
														network,
														rbSelectInDegree
																.isSelected() ? VertexDegreeDistributions.INDEGREE
																: VertexDegreeDistributions.OUTDEGREE)));
					}
					if (cbDirectedRSquare.isSelected()) {
						tfDirectedRSquare
								.setText(util
										.formatDoubleToString(vdd
												.getRSquare(
														network,
														rbSelectInDegree
																.isSelected() ? VertexDegreeDistributions.INDEGREE
																: VertexDegreeDistributions.OUTDEGREE)));
					}
				}
				btnDirectedCompute.setEnabled(true);
			}
		};
		t.start();
	}
}