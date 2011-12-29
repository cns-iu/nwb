/*
 * Created on Jan 27, 2005
 */
package edu.iu.iv.visualization.treeviz;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;

/**
 * A Simple Tree Visualization Using A Swing JTree
 * 
 * @author Shashikant
 */
public class TreeVizGUI extends JFrame {

    private static final long serialVersionUID = 3440269425929210695L;
    private JTree jtree;
    private JScrollPane jtreeScrollPane ;
    
    public TreeVizGUI(TreeModel treeModel, String title) {
        super(title);
        this.jtree = new JTree(treeModel);
        this.jtreeScrollPane = new JScrollPane(this.jtree) ;
        
        layoutGUI();
    }

    private void layoutGUI() {
        this.getContentPane().setLayout(new GridBagLayout());
        this.getContentPane().add(
                this.jtreeScrollPane,
                new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(2, 2, 2, 2), 0, 0));
        this.pack();
    }
}