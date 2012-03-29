package edu.iu.iv.toolkits.networkanalysistoolkit.gui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import edu.iu.iv.toolkits.networkanalysistoolkit.analysis.Network;
import edu.iu.iv.toolkits.networkanalysistoolkit.analysis.NetworkPaths;
import edu.iu.iv.toolkits.networkanalysistoolkit.util.Util;

/**
 * This code was generated using CloudGarden's Jigloo SWT/Swing GUI Builder,
 * which is free for non-commercial use. If Jigloo is being used commercially
 * (ie, by a for-profit company or business) then you should purchase a license -
 * please visit www.cloudgarden.com for details.
 */
public class GraphPathsDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = -4094267596184931333L;

    private JButton btnPrintToConsole;

    private JCheckBox cbSelectAll;

    private JButton btnCompute;

    private JTextField tfNumPathsOfLengthK;

    private JTextField tfLength;

    private JTextField tfCharacteristicPathLength;

    private JTextField tfDiameter;

    private JCheckBox cbNumPathsLengthK;

    private JCheckBox cbCharacteristicPathLength;

    private JCheckBox cbDiameter;

    public GraphPathsDialog(Frame frame) throws HeadlessException {
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

            cbDiameter = new JCheckBox();
            cbCharacteristicPathLength = new JCheckBox();
            cbNumPathsLengthK = new JCheckBox();
            tfDiameter = new JTextField();
            tfCharacteristicPathLength = new JTextField();
            tfLength = new JTextField();
            tfNumPathsOfLengthK = new JTextField();
            btnCompute = new JButton();
            cbSelectAll = new JCheckBox();
            btnPrintToConsole = new JButton();

            GridBagLayout thisLayout = new GridBagLayout();
            this.getContentPane().setLayout(thisLayout);
            thisLayout.columnWidths = new int[] { 1, 1, 1, 1 };
            thisLayout.rowHeights = new int[] { 1, 1, 1, 1, 1, 1 };
            thisLayout.columnWeights = new double[] { 0.1, 0.1, 0.1, 0.1 };
            thisLayout.rowWeights = new double[] { 0.1, 0.1, 0.1, 0.1, 0.1, 0.1 };
            this.setTitle("Path Characteristics");
            this.setSize(new java.awt.Dimension(312, 226));

            cbDiameter.setText("Diameter");
            this.getContentPane().add(
                    cbDiameter,
                    new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, 17, 0,
                            new Insets(5, 5, 5, 5), 0, 0));

            cbCharacteristicPathLength.setText("Characteristic Path Length");
            this.getContentPane().add(
                    cbCharacteristicPathLength,
                    new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, 17, 2,
                            new Insets(5, 5, 5, 5), 0, 0));

            cbNumPathsLengthK.setEnabled(false);
            cbNumPathsLengthK.setText("Number of Paths of Length");
            cbNumPathsLengthK.setToolTipText("Not yet implemented :)");
            this.getContentPane().add(
                    cbNumPathsLengthK,
                    new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, 17, 0,
                            new Insets(5, 5, 5, 5), 0, 0));

            tfDiameter.setEditable(false);
            tfDiameter.setEnabled(true);
            tfDiameter.setMinimumSize(new java.awt.Dimension(60, 20));
            this.getContentPane().add(
                    tfDiameter,
                    new GridBagConstraints(3, 0, 1, 1, 0.6, 0.0, 10, 0,
                            new Insets(0, 0, 0, 0), 0, 0));

            tfCharacteristicPathLength.setEditable(false);
            tfCharacteristicPathLength.setEnabled(true);
            tfCharacteristicPathLength.setMinimumSize(new java.awt.Dimension(
                    60, 20));
            this.getContentPane().add(
                    tfCharacteristicPathLength,
                    new GridBagConstraints(3, 1, 1, 1, 0.6, 0.0, 10, 0,
                            new Insets(0, 0, 0, 0), 0, 0));

            tfLength.setText("2");
            tfLength.setMinimumSize(new java.awt.Dimension(40, 20));
            this.getContentPane().add(
                    tfLength,
                    new GridBagConstraints(2, 2, 1, 1, 0.2, 0.0, 10, 0,
                            new Insets(0, 5, 5, 5), 0, 0));

            tfNumPathsOfLengthK.setEditable(false);
            tfNumPathsOfLengthK.setEnabled(true);
            tfNumPathsOfLengthK.setMinimumSize(new java.awt.Dimension(60, 20));
            this.getContentPane().add(
                    tfNumPathsOfLengthK,
                    new GridBagConstraints(3, 2, 1, 1, 0.6, 0.0, 10, 0,
                            new Insets(0, 0, 0, 0), 0, 0));

            btnCompute.setText("Compute");
            this.getContentPane().add(
                    btnCompute,
                    new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, 17, 0,
                            new Insets(5, 5, 5, 5), 0, 0));
            btnCompute.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    btnComputeActionPerformed(evt);
                }
            });

            cbSelectAll.setText("Select All");
            this.getContentPane().add(
                    cbSelectAll,
                    new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, 17, 0,
                            new Insets(5, 5, 5, 5), 0, 0));
            cbSelectAll.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    cbSelectAllActionPerformed(evt);
                }
            });

            btnPrintToConsole.setEnabled(true);
            btnPrintToConsole.setText("Print To Console");
            this.getContentPane().add(
                    btnPrintToConsole,
                    new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0, 17, 0,
                            new Insets(5, 5, 5, 5), 0, 0));
            btnPrintToConsole.addActionListener(new ActionListener() {
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
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }

    /** Add your post-init code in here */
    public void postInitGUI() {
        // not used
    }

    private Network network;

    public void update(Network network) {
        this.network = network;
    }

    /** Auto-generated event handler method */
    protected void cbSelectAllActionPerformed(ActionEvent evt) {
        boolean sel = cbSelectAll.isSelected();
        if (cbDiameter.isEnabled())
            cbDiameter.setSelected(sel);
        if (cbCharacteristicPathLength.isEnabled())
            cbCharacteristicPathLength.setSelected(sel);
        if (cbNumPathsLengthK.isEnabled())
            cbNumPathsLengthK.setSelected(sel);
    }

    /** Auto-generated event handler method */
    protected void btnComputeActionPerformed(ActionEvent evt) {
        if (!isInputValid())
            return;
        final JDialog parentGUI = this ;
        Thread t = new Thread() {
            public void run() {
                Util util = Util.getInstance();
                NetworkPaths np = NetworkPaths.getNewInstance();
                btnCompute.setEnabled(false);
                if (cbDiameter.isSelected())
                    tfDiameter.setText(Integer
                            .toString(np.getDiameter(network)));
                if (cbCharacteristicPathLength.isSelected()) {
                    double val = np.getCharacteristicPathLength(network);
                    if (val == NetworkPaths.BAD_VALUE)
                        JOptionPane
                                .showMessageDialog(
                                        parentGUI,
                                        "Cannot calculate characteristic path length. Graph is not connected enough!",
                                        "Graph Connectivity Low",
                                        JOptionPane.INFORMATION_MESSAGE);
                    else
                        tfCharacteristicPathLength.setText(util
                            .formatDoubleToString(val));
                }
                if (cbNumPathsLengthK.isSelected()) {
                    // FINISH num paths length k implementation
                }
                btnCompute.setEnabled(true);
            }
        };
        t.start();
    }

    private int length = 0;

    private boolean isInputValid() {
        boolean valid = false;
        try {
            if (cbNumPathsLengthK.isSelected()) {
                String s = tfLength.getText();
                if (s != null && s.length() != 0) {
                    length = Integer.parseInt(s);
                    if (length < 0)
                        throw new IllegalArgumentException("Length must be > 0");
                }
                valid = true;
            } else
                valid = true;
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Length must be > 0", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            tfLength.requestFocus();
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Error!",
                    JOptionPane.ERROR_MESSAGE);
            tfLength.requestFocus();
        }
        return valid;
    }

    /** Auto-generated event handler method */
    protected void btnPrintToConsoleActionPerformed(ActionEvent evt) {
        Util.println(cbDiameter.getText() + " : " + tfDiameter.getText());
        Util.println(cbCharacteristicPathLength.getText() + " : "
                + tfCharacteristicPathLength.getText());
        //	    System.out.println(cbNumPathsLengthK.getText() + " : " +
        // tfNumPathsOfLengthK.getText()) ;
        // TODO: uncomment when done.
    }
}